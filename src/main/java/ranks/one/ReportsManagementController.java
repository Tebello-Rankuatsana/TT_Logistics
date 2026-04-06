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
    @FXML private Label totalMaintenanceCost;