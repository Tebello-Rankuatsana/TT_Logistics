package ranks.one;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.sql.*;
import java.time.LocalDate;

public class DriverManagementController {

    @FXML private TextField fullNameField, addressField, phoneField;
    @FXML private DatePicker dobPicker;
    @FXML private Button addPersonBtn;

    @FXML private ComboBox<String> fullTimePersonCombo, contractPersonCombo;
    @FXML private TextField employeeNumberField, salaryField, contractNumberField, hourlyRateField;
    @FXML private DatePicker hireDatePicker;
    @FXML private Button addFullTimeBtn, addContractBtn;

    @FXML private TableView<FullTimeDriver> fullTimeTable;
    @FXML private TableView<ContractDriver> contractTable;

    @FXML private Label statusLabel, userInfoLabel;

    private String username;
    private String userPrivilege;

    @FXML
    public void initialize() {
        setupTables();
        loadPersonComboBoxes();
        loadDrivers();
    }

    public void setUserInfo(String username, String privilege) {
        this.username = username;
        this.userPrivilege = privilege;
        userInfoLabel.setText("User: " + username + " | Role: " + privilege);
        applyPrivilegeRestrictions();
    }

    private void applyPrivilegeRestrictions() {
        switch (userPrivilege) {
            case "VIEW_ONLY":
                addPersonBtn.setDisable(true);
                addFullTimeBtn.setDisable(true);
                addContractBtn.setDisable(true);
                break;
            case "INSERT_ONLY":
                // Keep add buttons enabled
                break;
        }
    }

    @SuppressWarnings("unchecked")
    private void setupTables() {
        // Setup full-time table columns
        TableColumn<FullTimeDriver, Integer> colPersonId = new TableColumn<>("Person ID");
        colPersonId.setCellValueFactory(new PropertyValueFactory<>("personId"));

        TableColumn<FullTimeDriver, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<FullTimeDriver, String> colEmpNum = new TableColumn<>("Employee #");
        colEmpNum.setCellValueFactory(new PropertyValueFactory<>("employeeNumber"));

        TableColumn<FullTimeDriver, Double> colSalary = new TableColumn<>("Salary");
        colSalary.setCellValueFactory(new PropertyValueFactory<>("salary"));

        TableColumn<FullTimeDriver, Date> colHireDate = new TableColumn<>("Hire Date");
        colHireDate.setCellValueFactory(new PropertyValueFactory<>("hireDate"));

        fullTimeTable.getColumns().setAll(colPersonId, colName, colEmpNum, colSalary, colHireDate);

        // Setup contract table columns
        TableColumn<ContractDriver, Integer> colCrtPersonId = new TableColumn<>("Person ID");
        colCrtPersonId.setCellValueFactory(new PropertyValueFactory<>("personId"));

        TableColumn<ContractDriver, String> colCrtName = new TableColumn<>("Name");
        colCrtName.setCellValueFactory(new PropertyValueFactory<>("fullName"));

        TableColumn<ContractDriver, String> colContractNum = new TableColumn<>("Contract #");
        colContractNum.setCellValueFactory(new PropertyValueFactory<>("contractNumber"));

        TableColumn<ContractDriver, Double> colHourlyRate = new TableColumn<>("Hourly Rate");
        colHourlyRate.setCellValueFactory(new PropertyValueFactory<>("hourlyRate"));

        contractTable.getColumns().setAll(colCrtPersonId, colCrtName, colContractNum, colHourlyRate);
    }

    private void loadPersonComboBoxes() {
        String query = "SELECT person_id, full_name FROM Person";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            fullTimePersonCombo.getItems().clear();
            contractPersonCombo.getItems().clear();

            while (rs.next()) {
                String item = rs.getInt("person_id") + " - " + rs.getString("full_name");
                fullTimePersonCombo.getItems().add(item);
                contractPersonCombo.getItems().add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDrivers() {
        loadFullTimeDrivers();
        loadContractDrivers();
    }

    private void loadFullTimeDrivers() {
        String query = "SELECT p.person_id, p.full_name, f.employee_number, f.salary, f.hire_date " +
                "FROM Person p JOIN Full_Time_Driver f ON p.person_id = f.person_id";

        ObservableList<FullTimeDriver> list = FXCollections.observableArrayList();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                FullTimeDriver driver = new FullTimeDriver(
                        rs.getInt("person_id"),
                        rs.getString("full_name"),
                        rs.getString("employee_number"),
                        rs.getDouble("salary"),
                        rs.getDate("hire_date")
                );
                list.add(driver);
            }
            fullTimeTable.setItems(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadContractDrivers() {
        String query = "SELECT p.person_id, p.full_name, c.contract_number, c.hourly_rate " +
                "FROM Person p JOIN Contract_Driver c ON p.person_id = c.person_id";

        ObservableList<ContractDriver> list = FXCollections.observableArrayList();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                ContractDriver driver = new ContractDriver(
                        rs.getInt("person_id"),
                        rs.getString("full_name"),
                        rs.getString("contract_number"),
                        rs.getDouble("hourly_rate")
                );
                list.add(driver);
            }
            contractTable.setItems(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addPerson() {
        if (fullNameField.getText().isEmpty() || addressField.getText().isEmpty() ||
                phoneField.getText().isEmpty() || dobPicker.getValue() == null) {
            statusLabel.setText("Please fill all person fields");
            return;
        }

        String query = "INSERT INTO Person (full_name, address, phone, date_of_birth) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, fullNameField.getText());
            pstmt.setString(2, addressField.getText());
            pstmt.setString(3, phoneField.getText());
            pstmt.setDate(4, Date.valueOf(dobPicker.getValue()));

            pstmt.executeUpdate();
            statusLabel.setText("Person added successfully!");
            clearPersonForm();
            loadPersonComboBoxes();

        } catch (SQLException e) {
            statusLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void addFullTimeDriver() {
        if (fullTimePersonCombo.getValue() == null) {
            statusLabel.setText("Please select a person");
            return;
        }

        if (employeeNumberField.getText().isEmpty() || salaryField.getText().isEmpty() ||
                hireDatePicker.getValue() == null) {
            statusLabel.setText("Please fill all driver fields");
            return;
        }

        int personId = Integer.parseInt(fullTimePersonCombo.getValue().split(" - ")[0]);
        String query = "INSERT INTO Full_Time_Driver (person_id, employee_number, salary, hire_date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, personId);
            pstmt.setString(2, employeeNumberField.getText());
            pstmt.setDouble(3, Double.parseDouble(salaryField.getText()));
            pstmt.setDate(4, Date.valueOf(hireDatePicker.getValue()));

            pstmt.executeUpdate();
            statusLabel.setText("Full-time driver added successfully!");
            clearFullTimeForm();
            loadFullTimeDrivers();

        } catch (SQLException e) {
            statusLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void addContractDriver() {
        if (contractPersonCombo.getValue() == null) {
            statusLabel.setText("Please select a person");
            return;
        }

        if (contractNumberField.getText().isEmpty() || hourlyRateField.getText().isEmpty()) {
            statusLabel.setText("Please fill all contract fields");
            return;
        }

        int personId = Integer.parseInt(contractPersonCombo.getValue().split(" - ")[0]);
        String query = "INSERT INTO Contract_Driver (person_id, contract_number, hourly_rate) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, personId);
            pstmt.setString(2, contractNumberField.getText());
            pstmt.setDouble(3, Double.parseDouble(hourlyRateField.getText()));

            pstmt.executeUpdate();
            statusLabel.setText("Contract driver added successfully!");
            clearContractForm();
            loadContractDrivers();

        } catch (SQLException e) {
            statusLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void clearPersonForm() {
        fullNameField.clear();
        addressField.clear();
        phoneField.clear();
        dobPicker.setValue(null);
    }

    @FXML
    private void clearFullTimeForm() {
        fullTimePersonCombo.setValue(null);
        employeeNumberField.clear();
        salaryField.clear();
        hireDatePicker.setValue(null);
    }

    @FXML
    private void clearContractForm() {
        contractPersonCombo.setValue(null);
        contractNumberField.clear();
        hourlyRateField.clear();
    }

    @FXML
    private void goBack() {
        navigateTo("/fxml/TtLogisticsMenu.fxml", "TT Logistics - Main Menu");
    }

    private void navigateTo(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());

            TtLogisticsMenu controller = loader.getController();
            controller.setUserInfo(username, userPrivilege);

            Stage stage = (Stage) addPersonBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}