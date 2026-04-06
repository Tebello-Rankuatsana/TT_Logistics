package ranks.one;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class TtLogisticsMenu {

    @FXML
    private Button btnVehicle;

    @FXML
    private Button btnDriver;

    @FXML
    private Button btnTrip;

    @FXML
    private Button btnReports;

    @FXML
    private Label userInfoLabel;

    private String currentUsername;
    private String currentPrivilege;

    @FXML
    public void initialize() {
        System.out.println("Main Menu Initialized");
    }

    public void setUserInfo(String username, String privilege) {
        this.currentUsername = username;
        this.currentPrivilege = privilege;

        // Display user info if label exists
        if (userInfoLabel != null) {
            userInfoLabel.setText("Logged in as: " + username + " | Role: " + privilege);
        }

        System.out.println("User Info Set - Username: " + username + ", Privilege: " + privilege);

        // Apply button restrictions based on privilege
        applyPrivilegeRestrictions(privilege);
    }

    private void applyPrivilegeRestrictions(String privilege) {
        switch (privilege) {
            case "ALL_PRIVILEGES":
                // Enable all buttons
                btnVehicle.setDisable(false);
                btnDriver.setDisable(false);
                btnTrip.setDisable(false);
                btnReports.setDisable(false);
                System.out.println("ALL_PRIVILEGES - All buttons enabled");
                break;

            case "INSERT_ONLY":
                // Enable Vehicle, Driver, Trip only
                btnVehicle.setDisable(false);
                btnDriver.setDisable(false);
                btnTrip.setDisable(false);
                btnReports.setDisable(true);
                System.out.println("INSERT_ONLY - Vehicle, Driver, Trip enabled, Reports disabled");
                break;

            case "VIEW_ONLY":
                // Enable Reports only
                btnVehicle.setDisable(true);
                btnDriver.setDisable(true);
                btnTrip.setDisable(true);
                btnReports.setDisable(false);
                System.out.println("VIEW_ONLY - Only Reports enabled");
                break;

            default:
                // Default: enable all buttons
                btnVehicle.setDisable(false);
                btnDriver.setDisable(false);
                btnTrip.setDisable(false);
                btnReports.setDisable(false);
                System.out.println("Unknown privilege - All buttons enabled by default");
                break;
        }
    }

    @FXML
    private void vehicleManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/VehicleManagement.fxml"));
            Scene scene = new Scene(loader.load());

            VehicleManagementController controller = loader.getController();
            controller.setUserInfo(currentUsername, currentPrivilege);

            Stage stage = (Stage) btnVehicle.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("TT Logistics - Vehicle Management");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading Vehicle Management: " + e.getMessage());
        }
    }

    @FXML
    private void driverManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/DriverManagement.fxml"));
            Scene scene = new Scene(loader.load());

            DriverManagementController controller = loader.getController();
            controller.setUserInfo(currentUsername, currentPrivilege);

            Stage stage = (Stage) btnDriver.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("TT Logistics - Driver Management");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading Driver Management: " + e.getMessage());
        }
    }

    @FXML
    private void tripManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TripManagement.fxml"));
            Scene scene = new Scene(loader.load());

            TripManagementController controller = loader.getController();
            controller.setUserInfo(currentUsername, currentPrivilege);

            Stage stage = (Stage) btnTrip.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("TT Logistics - Trip Management");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading Trip Management: " + e.getMessage());
        }
    }

    @FXML
    private void reportManagement() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ReportsManagement.fxml"));
            Scene scene = new Scene(loader.load());

            ReportsManagementController controller = loader.getController();
            controller.setUserInfo(currentUsername, currentPrivilege);

            Stage stage = (Stage) btnReports.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("TT Logistics - Reports");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading Reports: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) btnVehicle.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("TT Logistics - Login");
            stage.setMaximized(false);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Error logging out: " + e.getMessage());
        }
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}