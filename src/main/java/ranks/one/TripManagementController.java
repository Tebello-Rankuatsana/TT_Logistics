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

public class TripManagementController {

    // Delivery creation fields
    @FXML private ComboBox<String> clientCombo, vehicleCombo;
    @FXML private DatePicker deliveryDatePicker;
    @FXML private TextField originField, destinationField;
    @FXML private ComboBox<String> statusCombo;
    @FXML private Button createDeliveryBtn;

    // Driver assignment fields
    @FXML private ComboBox<String> deliveryCombo, driverCombo, roleCombo;
    @FXML private TextField hoursWorkedField;
    @FXML private Button assignDriverBtn;
    @FXML private TableView<DriverAssignment> assignmentTable;
    @FXML private TableColumn<DriverAssignment, Integer> colAssignmentId;
    @FXML private TableColumn<DriverAssignment, String> colDriverName, colDeliveryInfo, colRole;
    @FXML private TableColumn<DriverAssignment, Double> colHours;

    // Maintenance fields
    @FXML private ComboBox<String> maintenanceVehicleCombo, maintenanceTypeCombo;
    @FXML private DatePicker maintenanceDatePicker;
    @FXML private TextField maintenanceCostField;
    @FXML private Button recordMaintenanceBtn;
    @FXML private TableView<VehicleMaintenance> maintenanceTable;
    @FXML private TableColumn<VehicleMaintenance, Integer> colMaintenanceId;
    @FXML private TableColumn<VehicleMaintenance, String> colMaintenanceVehicle, colMaintenanceType;
    @FXML private TableColumn<VehicleMaintenance, Date> colMaintenanceDate;
    @FXML private TableColumn<VehicleMaintenance, Double> colMaintenanceCost;

    // View deliveries fields
    @FXML private TableView<Delivery> deliveryTable;
    @FXML private TableColumn<Delivery, Integer> colDeliveryId;
    @FXML private TableColumn<Delivery, Date> colDeliveryDate;
    @FXML private TableColumn<Delivery, String> colClient, colVehicleReg, colOrigin, colDestination, colDeliveryStatus;
    @FXML private ComboBox<String> filterStatusCombo;

    @FXML private Label statusLabel, userInfoLabel;

    private String username;
    private String userPrivilege;

    @FXML
    public void initialize() {
        setupTables();
        loadComboBoxes();
        loadDeliveriesTable();
        loadAssignments();
        loadMaintenanceHistory();

        filterStatusCombo.setValue("All");
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
                createDeliveryBtn.setDisable(true);
                assignDriverBtn.setDisable(true);
                recordMaintenanceBtn.setDisable(true);
                break;
            case "INSERT_ONLY":
                // All enabled
                break;
            case "ALL_PRIVILEGES":
                break;
        }
    }

    @SuppressWarnings("unchecked")
    private void setupTables() {
        // Assignment table
        colAssignmentId.setCellValueFactory(new PropertyValueFactory<>("assignmentId"));
        colDriverName.setCellValueFactory(new PropertyValueFactory<>("driverName"));
        colDeliveryInfo.setCellValueFactory(new PropertyValueFactory<>("deliveryInfo"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("role"));
        colHours.setCellValueFactory(new PropertyValueFactory<>("hoursWorked"));

        // Maintenance table
        colMaintenanceId.setCellValueFactory(new PropertyValueFactory<>("maintenanceId"));
        colMaintenanceVehicle.setCellValueFactory(new PropertyValueFactory<>("vehicleInfo"));
        colMaintenanceDate.setCellValueFactory(new PropertyValueFactory<>("maintenanceDate"));
        colMaintenanceType.setCellValueFactory(new PropertyValueFactory<>("maintenanceType"));
        colMaintenanceCost.setCellValueFactory(new PropertyValueFactory<>("cost"));

        // Delivery table
        colDeliveryId.setCellValueFactory(new PropertyValueFactory<>("deliveryId"));
        colDeliveryDate.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));
        colClient.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        colVehicleReg.setCellValueFactory(new PropertyValueFactory<>("vehicleRegistration"));
        colOrigin.setCellValueFactory(new PropertyValueFactory<>("origin"));
        colDestination.setCellValueFactory(new PropertyValueFactory<>("destination"));
        colDeliveryStatus.setCellValueFactory(new PropertyValueFactory<>("deliveryStatus"));
    }

    private void loadComboBoxes() {
        // Load clients
        String clientQuery = "SELECT client_id, client_name FROM Client";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(clientQuery)) {
            while (rs.next()) {
                clientCombo.getItems().add(rs.getInt("client_id") + " - " + rs.getString("client_name"));
            }
        } catch (SQLException e) { e.printStackTrace(); }

        // Load vehicles
        String vehicleQuery = "SELECT vehicle_id, registration_number FROM Vehicle";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(vehicleQuery)) {
            while (rs.next()) {
                String vehicleItem = rs.getInt("vehicle_id") + " - " + rs.getString("registration_number");
                vehicleCombo.getItems().add(vehicleItem);
                maintenanceVehicleCombo.getItems().add(vehicleItem);
            }
        } catch (SQLException e) { e.printStackTrace(); }

        // Load deliveries for assignment
        loadDeliveryCombo();

        // Load drivers
        String driverQuery = "SELECT p.person_id, p.full_name FROM Person p " +
                "JOIN Full_Time_Driver f ON p.person_id = f.person_id " +
                "UNION " +
                "SELECT p.person_id, p.full_name FROM Person p " +
                "JOIN Contract_Driver c ON p.person_id = c.person_id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(driverQuery)) {
            while (rs.next()) {
                driverCombo.getItems().add(rs.getInt("person_id") + " - " + rs.getString("full_name"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void loadDeliveryCombo() {
        deliveryCombo.getItems().clear();
        String query = "SELECT delivery_id, origin, destination FROM Delivery WHERE delivery_status != 'Completed'";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                deliveryCombo.getItems().add(rs.getInt("delivery_id") + " - " +
                        rs.getString("origin") + " to " + rs.getString("destination"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void loadDeliveriesTable() {
        loadDeliveriesWithFilter(filterStatusCombo.getValue());
    }

    private void loadDeliveriesWithFilter(String status) {
        String query = "SELECT d.*, c.client_name, v.registration_number FROM Delivery d " +
                "JOIN Client c ON d.client_id = c.client_id " +
                "JOIN Vehicle v ON d.vehicle_id = v.vehicle_id";
        if (!"All".equals(status)) {
            query += " WHERE d.delivery_status = '" + status + "'";
        }

        ObservableList<Delivery> list = FXCollections.observableArrayList();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new Delivery(
                        rs.getInt("delivery_id"),
                        rs.getDate("delivery_date"),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getString("delivery_status"),
                        rs.getString("client_name"),
                        rs.getString("registration_number")
                ));
            }
            deliveryTable.setItems(list);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void loadAssignments() {
        String query = "SELECT da.assignment_id, p.full_name as driver_name, " +
                "CONCAT(d.origin, ' to ', d.destination) as delivery_info, " +
                "da.role, da.hours_worked " +
                "FROM Driver_Assignment da " +
                "JOIN Person p ON da.person_id = p.person_id " +
                "JOIN Delivery d ON da.delivery_id = d.delivery_id";

        ObservableList<DriverAssignment> list = FXCollections.observableArrayList();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new DriverAssignment(
                        rs.getInt("assignment_id"),
                        rs.getString("driver_name"),
                        rs.getString("delivery_info"),
                        rs.getString("role"),
                        rs.getDouble("hours_worked")
                ));
            }
            assignmentTable.setItems(list);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void loadMaintenanceHistory() {
        String query = "SELECT vm.*, v.registration_number FROM Vehicle_Maintenance vm " +
                "JOIN Vehicle v ON vm.vehicle_id = v.vehicle_id ORDER BY vm.maintenance_date DESC";

        ObservableList<VehicleMaintenance> list = FXCollections.observableArrayList();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                list.add(new VehicleMaintenance(
                        rs.getInt("maintenance_id"),
                        rs.getString("registration_number"),
                        rs.getDate("maintenance_date"),
                        rs.getString("maintenance_type"),
                        rs.getDouble("cost")
                ));
            }
            maintenanceTable.setItems(list);
        } catch (SQLException e) { e.printStackTrace(); }
    }

    @FXML
    private void createDelivery() {
        if (clientCombo.getValue() == null || vehicleCombo.getValue() == null ||
                deliveryDatePicker.getValue() == null || originField.getText().isEmpty() ||
                destinationField.getText().isEmpty() || statusCombo.getValue() == null) {
            statusLabel.setText("Please fill all fields");
            return;
        }

        int clientId = Integer.parseInt(clientCombo.getValue().split(" - ")[0]);
        int vehicleId = Integer.parseInt(vehicleCombo.getValue().split(" - ")[0]);

        String query = "INSERT INTO Delivery (delivery_date, origin, destination, delivery_status, client_id, vehicle_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setDate(1, Date.valueOf(deliveryDatePicker.getValue()));
            pstmt.setString(2, originField.getText());
            pstmt.setString(3, destinationField.getText());
            pstmt.setString(4, statusCombo.getValue());
            pstmt.setInt(5, clientId);
            pstmt.setInt(6, vehicleId);

            pstmt.executeUpdate();
            statusLabel.setText("Delivery created successfully!");
            clearDeliveryForm();
            loadDeliveriesTable();
            loadDeliveryCombo();

        } catch (SQLException e) {
            statusLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void assignDriverToDelivery() {
        if (deliveryCombo.getValue() == null || driverCombo.getValue() == null ||
                roleCombo.getValue() == null || hoursWorkedField.getText().isEmpty()) {
            statusLabel.setText("Please fill all fields");
            return;
        }

        int personId = Integer.parseInt(driverCombo.getValue().split(" - ")[0]);
        int deliveryId = Integer.parseInt(deliveryCombo.getValue().split(" - ")[0]);
        String role = roleCombo.getValue();
        double hoursWorked = Double.parseDouble(hoursWorkedField.getText());

        // Call stored procedure for assigning driver
        String call = "{CALL assign_driver_to_delivery(?, ?, ?, ?)}";

        try (Connection conn = DBConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(call)) {

            cstmt.setInt(1, personId);
            cstmt.setInt(2, deliveryId);
            cstmt.setString(3, role);
            cstmt.setDouble(4, hoursWorked);

            cstmt.execute();
            statusLabel.setText("Driver assigned successfully!");
            clearAssignmentForm();
            loadAssignments();
            loadDeliveryCombo();

        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate") || e.getMessage().contains("assigned")) {
                statusLabel.setText("Error: Driver already assigned to this delivery!");
            } else if (e.getMessage().contains("zero")) {
                statusLabel.setText("Error: Hours worked must be greater than 0!");
            } else {
                statusLabel.setText("Error: " + e.getMessage());
            }
            e.printStackTrace();
        }
    }

    @FXML
    private void recordMaintenance() {
        if (maintenanceVehicleCombo.getValue() == null || maintenanceDatePicker.getValue() == null ||
                maintenanceTypeCombo.getValue() == null || maintenanceCostField.getText().isEmpty()) {
            statusLabel.setText("Please fill all fields");
            return;
        }

        int vehicleId = Integer.parseInt(maintenanceVehicleCombo.getValue().split(" - ")[0]);
        double cost = Double.parseDouble(maintenanceCostField.getText());

//        calling procedure to record vehicle maintenace
        String call = "{CALL record_vehicle_maintenance(?, ?, ?, ?)}";

        try (Connection conn = DBConnection.getConnection();
             CallableStatement cstmt = conn.prepareCall(call)) {

            cstmt.setInt(1, vehicleId);
            cstmt.setDate(2, Date.valueOf(maintenanceDatePicker.getValue()));
            cstmt.setString(3, maintenanceTypeCombo.getValue());
            cstmt.setDouble(4, cost);

            cstmt.execute();
            statusLabel.setText("Maintenance recorded successfully!");
            clearMaintenanceForm();
            loadMaintenanceHistory();

        } catch (SQLException e) {
            if (e.getMessage().contains("negative")) {
                statusLabel.setText("Error: Cost cannot be negative!");
            } else {
                statusLabel.setText("Error: " + e.getMessage());
            }
            e.printStackTrace();
        }
    }

    @FXML
    private void filterDeliveries() {
        loadDeliveriesWithFilter(filterStatusCombo.getValue());
    }

    @FXML
    private void refreshDeliveries() {
        loadDeliveriesWithFilter(filterStatusCombo.getValue());
    }

    @FXML
    private void clearDeliveryForm() {
        clientCombo.setValue(null);
        vehicleCombo.setValue(null);
        deliveryDatePicker.setValue(null);
        originField.clear();
        destinationField.clear();
        statusCombo.setValue(null);
    }

    @FXML
    private void clearAssignmentForm() {
        deliveryCombo.setValue(null);
        driverCombo.setValue(null);
        roleCombo.setValue(null);
        hoursWorkedField.clear();
    }

    @FXML
    private void clearMaintenanceForm() {
        maintenanceVehicleCombo.setValue(null);
        maintenanceDatePicker.setValue(null);
        maintenanceTypeCombo.setValue(null);
        maintenanceCostField.clear();
    }

    @FXML
    private void goBack() {
        navigateTo("tt_logistics_menu.fxml", "TT Logistics - Main Menu");
    }

    private void navigateTo(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());

            TtLogisticsMenu controller = loader.getController();
            controller.setUserInfo(username, userPrivilege);

            Stage stage = (Stage) createDeliveryBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}