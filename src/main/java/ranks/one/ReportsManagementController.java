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

public class ReportsManagementController {

    // Active Deliveries
    @FXML private TableView<ActiveDelivery> activeDeliveriesTable;
    @FXML private TableColumn<ActiveDelivery, Integer> colActiveId;
    @FXML private TableColumn<ActiveDelivery, Date> colActiveDate;
    @FXML private TableColumn<ActiveDelivery, String> colActiveClient, colActiveVehicle, colActiveStatus;

    // Driver Workload
    @FXML private TableView<DriverWorkload> driverWorkloadTable;
    @FXML private TableColumn<DriverWorkload, Integer> colWorkloadId;
    @FXML private TableColumn<DriverWorkload, String> colWorkloadName;
    @FXML private TableColumn<DriverWorkload, Integer> colWorkloadDeliveries;
    @FXML private TableColumn<DriverWorkload, Double> colWorkloadHours;

    // Driver Statistics
    @FXML private ComboBox<String> driverStatsCombo, vehicleStatsCombo;
    @FXML private Label driverTotalLabel, vehicleTotalLabel;

    // Maintenance Summary
    @FXML private TableView<VehicleMaintenance> allMaintenanceTable;
    @FXML private TableColumn<VehicleMaintenance, Integer> colSummaryId;
    @FXML private TableColumn<VehicleMaintenance, String> colSummaryVehicle, colSummaryType;
    @FXML private TableColumn<VehicleMaintenance, Date> colSummaryDate;
    @FXML private TableColumn<VehicleMaintenance, Double> colSummaryCost;
    @FXML private Label totalMaintenanceCostLabel;

    @FXML private Label userInfoLabel;

    private String username;
    private String userPrivilege;

    @FXML
    public void initialize() {
        setupTables();
        loadComboBoxes();
        loadActiveDeliveries();
        loadDriverWorkload();
        loadMaintenanceSummary();
    }

    public void setUserInfo(String username, String privilege) {
        this.username = username;
        this.userPrivilege = privilege;
        userInfoLabel.setText("User: " + username + " | Role: " + privilege);
    }

    @SuppressWarnings("unchecked")
    private void setupTables() {
        // Active Deliveries Table
        colActiveId.setCellValueFactory(new PropertyValueFactory<>("deliveryId"));
        colActiveDate.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));
        colActiveClient.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        colActiveVehicle.setCellValueFactory(new PropertyValueFactory<>("vehicleRegistration"));
        colActiveStatus.setCellValueFactory(new PropertyValueFactory<>("deliveryStatus"));

        // Driver Workload Table
        colWorkloadId.setCellValueFactory(new PropertyValueFactory<>("personId"));
        colWorkloadName.setCellValueFactory(new PropertyValueFactory<>("driverName"));
        colWorkloadDeliveries.setCellValueFactory(new PropertyValueFactory<>("numberOfDeliveries"));
        colWorkloadHours.setCellValueFactory(new PropertyValueFactory<>("totalHoursWorked"));

        // Maintenance Summary Table
        colSummaryId.setCellValueFactory(new PropertyValueFactory<>("maintenanceId"));
        colSummaryVehicle.setCellValueFactory(new PropertyValueFactory<>("vehicleInfo"));
        colSummaryDate.setCellValueFactory(new PropertyValueFactory<>("maintenanceDate"));
        colSummaryType.setCellValueFactory(new PropertyValueFactory<>("maintenanceType"));
        colSummaryCost.setCellValueFactory(new PropertyValueFactory<>("cost"));
    }

    private void loadComboBoxes() {
        // Load drivers for stats
        String driverQuery = "SELECT p.person_id, p.full_name FROM Person p " +
                "JOIN Full_Time_Driver f ON p.person_id = f.person_id " +
                "UNION " +
                "SELECT p.person_id, p.full_name FROM Person p " +
                "JOIN Contract_Driver c ON p.person_id = c.person_id";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(driverQuery)) {
            while (rs.next()) {
                driverStatsCombo.getItems().add(rs.getInt("person_id") + " - " + rs.getString("full_name"));
            }
        } catch (SQLException e) { e.printStackTrace(); }

        // Load vehicles for stats
        String vehicleQuery = "SELECT vehicle_id, registration_number FROM Vehicle";
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(vehicleQuery)) {
            while (rs.next()) {
                vehicleStatsCombo.getItems().add(rs.getInt("vehicle_id") + " - " + rs.getString("registration_number"));
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }

    private void loadActiveDeliveries() {
        // Using the Active_Deliveries_View
        String query = "SELECT * FROM Active_Deliveries_View";

        ObservableList<ActiveDelivery> list = FXCollections.observableArrayList();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                list.add(new ActiveDelivery(
                        rs.getInt("delivery_id"),
                        rs.getDate("delivery_date"),
                        rs.getString("client_name"),
                        rs.getString("registration_number"),
                        rs.getString("delivery_status")
                ));
            }
            activeDeliveriesTable.setItems(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadDriverWorkload() {
        // Using the Driver_Workload_View
        String query = "SELECT * FROM Driver_Workload_View";

        ObservableList<DriverWorkload> list = FXCollections.observableArrayList();
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                list.add(new DriverWorkload(
                        rs.getInt("person_id"),
                        rs.getString("full_name"),
                        rs.getInt("number_of_deliveries"),
                        rs.getDouble("total_hours_worked")
                ));
            }
            driverWorkloadTable.setItems(list);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadMaintenanceSummary() {
        String query = "SELECT vm.*, v.registration_number FROM Vehicle_Maintenance vm " +
                "JOIN Vehicle v ON vm.vehicle_id = v.vehicle_id ORDER BY vm.maintenance_date DESC";

        ObservableList<VehicleMaintenance> list = FXCollections.observableArrayList();
        double totalCost = 0;

        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                VehicleMaintenance vm = new VehicleMaintenance(
                        rs.getInt("maintenance_id"),
                        rs.getString("registration_number"),
                        rs.getDate("maintenance_date"),
                        rs.getString("maintenance_type"),
                        rs.getDouble("cost")
                );
                list.add(vm);
                totalCost += rs.getDouble("cost");
            }
            allMaintenanceTable.setItems(list);
            totalMaintenanceCostLabel.setText("Total Maintenance Cost Across All Vehicles: M" + String.format("%.2f", totalCost));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void getDriverTotalDeliveries() {
        if (driverStatsCombo.getValue() == null) {
            driverTotalLabel.setText("Please select a driver");
            return;
        }

        int personId = Integer.parseInt(driverStatsCombo.getValue().split(" - ")[0]);

        // Call the function
        String query = "SELECT total_deliveries_by_driver(?) as total";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, personId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int total = rs.getInt("total");
                driverTotalLabel.setText("Total Deliveries for " + driverStatsCombo.getValue().split(" - ")[1] + ": " + total);
            }
        } catch (SQLException e) {
            driverTotalLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void getVehicleMaintenanceCost() {
        if (vehicleStatsCombo.getValue() == null) {
            vehicleTotalLabel.setText("Please select a vehicle");
            return;
        }

        int vehicleId = Integer.parseInt(vehicleStatsCombo.getValue().split(" - ")[0]);

        // Call the function
        String query = "SELECT vehicle_total_maintenance(?) as total";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, vehicleId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                double total = rs.getDouble("total");
                vehicleTotalLabel.setText("Total Maintenance Cost for " + vehicleStatsCombo.getValue().split(" - ")[1] + ": M" + String.format("%.2f", total));
            }
        } catch (SQLException e) {
            vehicleTotalLabel.setText("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void refreshActiveDeliveries() {
        loadActiveDeliveries();
    }

    @FXML
    private void refreshDriverWorkload() {
        loadDriverWorkload();
    }

    @FXML
    private void refreshMaintenanceSummary() {
        loadMaintenanceSummary();
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

            Stage stage = (Stage) activeDeliveriesTable.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}