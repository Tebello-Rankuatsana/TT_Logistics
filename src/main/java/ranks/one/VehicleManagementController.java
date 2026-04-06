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

public class VehicleManagementController {

    @FXML private TextField regNumberField, capacityField;
    @FXML private ComboBox<String> vehicleTypeCombo, depotCombo;
    @FXML private DatePicker purchaseDatePicker;
    @FXML private TableView<Vehicle> vehicleTable;
    @FXML private TableColumn<Vehicle, Integer> colId;
    @FXML private TableColumn<Vehicle, String> colRegistration, colType, colDepot;
    @FXML private TableColumn<Vehicle, Integer> colCapacity;
    @FXML private TableColumn<Vehicle, Date> colPurchaseDate;
    @FXML private Button addBtn, updateBtn, deleteBtn;
    @FXML private Label statusLabel, userInfoLabel;

    private ObservableList<Vehicle> vehicleList = FXCollections.observableArrayList();
    private Vehicle selectedVehicle;
    private String userPrivilege;
    private String username;

    @FXML
    public void initialize() {
        setupTableColumns();
        loadDepots();
        loadVehicles();
        setupTableSelection();
        loadUserInfo();
    }

    public void setUserInfo(String username, String privilege) {
        this.username = username;
        this.userPrivilege = privilege;
        applyPrivilegeRestrictions();
    }

    private void loadUserInfo() {
        userInfoLabel.setText("User: " + username + " | Role: " + userPrivilege);
    }

    private void applyPrivilegeRestrictions() {
        switch (userPrivilege) {
            case "VIEW_ONLY":
                addBtn.setDisable(true);
                updateBtn.setDisable(true);
                deleteBtn.setDisable(true);
                break;
            case "INSERT_ONLY":
                updateBtn.setDisable(true);
                deleteBtn.setDisable(true);
                break;
            case "ALL_PRIVILEGES":
                // All enabled
                break;
        }
    }

    private void setupTableColumns() {
        colId.setCellValueFactory(new PropertyValueFactory<>("vehicleId"));
        colRegistration.setCellValueFactory(new PropertyValueFactory<>("registrationNumber"));
        colType.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
        colCapacity.setCellValueFactory(new PropertyValueFactory<>("capacity"));
        colPurchaseDate.setCellValueFactory(new PropertyValueFactory<>("purchaseDate"));
        colDepot.setCellValueFactory(new PropertyValueFactory<>("depotName"));
    }

    private void setupTableSelection() {
        vehicleTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedVehicle = newSelection;
                populateForm(selectedVehicle);
            }
        });
    }

    private void loadDepots() {
        String query = "SELECT depot_id, depot_name FROM Depot";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            depotCombo.getItems().clear();
            while (rs.next()) {
                depotCombo.getItems().add(rs.getInt("depot_id") + " - " + rs.getString("depot_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadVehicles() {
        String query = "SELECT v.*, d.depot_name FROM Vehicle v LEFT JOIN Depot d ON v.depot_id = d.depot_id";
        vehicleList.clear();

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                        rs.getInt("vehicle_id"),
                        rs.getString("registration_number"),
                        rs.getString("vehicle_type"),
                        rs.getInt("capacity"),
                        rs.getDate("purchase_date"),
                        rs.getString("depot_name")
                );
                vehicleList.add(vehicle);
            }
            vehicleTable.setItems(vehicleList);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void addVehicle() {
        if (!validateForm()) return;

        String query = "INSERT INTO Vehicle (registration_number, vehicle_type, capacity, purchase_date, depot_id) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, regNumberField.getText());
            pstmt.setString(2, vehicleTypeCombo.getValue());
            pstmt.setInt(3, Integer.parseInt(capacityField.getText()));
            pstmt.setDate(4, Date.valueOf(purchaseDatePicker.getValue()));

            String depotId = depotCombo.getValue().split(" - ")[0];
            pstmt.setInt(5, Integer.parseInt(depotId));

            pstmt.executeUpdate();
            statusLabel.setText("Vehicle added successfully!");
            clearForm();
            loadVehicles();

        } catch (SQLException e) {
            statusLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void updateVehicle() {
        if (selectedVehicle == null) {
            statusLabel.setText("Please select a vehicle to update");
            return;
        }
        if (!validateForm()) return;

        String query = "UPDATE Vehicle SET registration_number=?, vehicle_type=?, capacity=?, purchase_date=?, depot_id=? WHERE vehicle_id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, regNumberField.getText());
            pstmt.setString(2, vehicleTypeCombo.getValue());
            pstmt.setInt(3, Integer.parseInt(capacityField.getText()));
            pstmt.setDate(4, Date.valueOf(purchaseDatePicker.getValue()));

            String depotId = depotCombo.getValue().split(" - ")[0];
            pstmt.setInt(5, Integer.parseInt(depotId));
            pstmt.setInt(6, selectedVehicle.getVehicleId());

            pstmt.executeUpdate();
            statusLabel.setText("Vehicle updated successfully!");
            clearForm();
            loadVehicles();

        } catch (SQLException e) {
            statusLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteVehicle() {
        if (selectedVehicle == null) {
            statusLabel.setText("Please select a vehicle to delete");
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Delete");
        alert.setContentText("Are you sure you want to delete vehicle " + selectedVehicle.getRegistrationNumber() + "?");

        if (alert.showAndWait().get() == ButtonType.OK) {
            String query = "DELETE FROM Vehicle WHERE vehicle_id = ?";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setInt(1, selectedVehicle.getVehicleId());
                pstmt.executeUpdate();
                statusLabel.setText("Vehicle deleted successfully!");
                clearForm();
                loadVehicles();

            } catch (SQLException e) {
                statusLabel.setText("Error: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void clearForm() {
        regNumberField.clear();
        capacityField.clear();
        vehicleTypeCombo.setValue(null);
        purchaseDatePicker.setValue(null);
        depotCombo.setValue(null);
        selectedVehicle = null;
        vehicleTable.getSelectionModel().clearSelection();
    }

    private boolean validateForm() {
        if (regNumberField.getText().isEmpty()) {
            statusLabel.setText("Registration number is required");
            return false;
        }
        if (vehicleTypeCombo.getValue() == null) {
            statusLabel.setText("Please select vehicle type");
            return false;
        }
        if (capacityField.getText().isEmpty()) {
            statusLabel.setText("Capacity is required");
            return false;
        }
        if (purchaseDatePicker.getValue() == null) {
            statusLabel.setText("Purchase date is required");
            return false;
        }
        if (depotCombo.getValue() == null) {
            statusLabel.setText("Please select a depot");
            return false;
        }
        return true;
    }

    private void populateForm(Vehicle vehicle) {
        regNumberField.setText(vehicle.getRegistrationNumber());
        vehicleTypeCombo.setValue(vehicle.getVehicleType());
        capacityField.setText(String.valueOf(vehicle.getCapacity()));
        purchaseDatePicker.setValue(vehicle.getPurchaseDate().toLocalDate());
        depotCombo.setValue(vehicle.getDepotName());
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

            Stage stage = (Stage) addBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}