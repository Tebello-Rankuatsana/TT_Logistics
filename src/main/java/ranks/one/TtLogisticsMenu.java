package ranks.one;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

    // Store user info as instance variables
    private String currentUsername;
    private String currentPrivilege;

    @FXML
    public void initialize() {
        System.out.println("Main Menu Loaded");
    }

    // This method receives user info from login controller
    public void setUserInfo(String username, String privilege) {
        this.currentUsername = username;
        this.currentPrivilege = privilege;

        System.out.println("Welcome: " + username + " - Privilege: " + privilege);

        // Apply privilege restrictions to buttons
        applyPrivilegeRestrictions(privilege);
    }

    private void applyPrivilegeRestrictions(String privilege) {
        switch (privilege) {
            case "VIEW_ONLY":
                btnVehicle.setDisable(true);
                btnDriver.setDisable(true);
                btnTrip.setDisable(true);
                btnReports.setDisable(false);
                break;

            case "INSERT_ONLY":
                btnVehicle.setDisable(false);
                btnDriver.setDisable(false);
                btnTrip.setDisable(false);
                btnReports.setDisable(true);
                break;

            case "ALL_PRIVILEGES":
                btnVehicle.setDisable(false);
                btnDriver.setDisable(false);
                btnTrip.setDisable(false);
                btnReports.setDisable(false);
                break;
        }
    }

    @FXML
    private void vehicleManagement() {
        navigateTo("/fxml/VehicleManagement.fxml", "TT Logistics - Vehicle Management");
    }

    @FXML
    private void driverManagement() {
        navigateTo("/fxml/DriverManagement.fxml", "TT Logistics - Driver Management");
    }

    @FXML
    private void tripManagement() {
        navigateTo("/fxml/TripManagement.fxml", "TT Logistics - Trip/Delivery Management");
    }

    @FXML
    private void reportManagement() {
        navigateTo("/fxml/ReportsManagement.fxml", "TT Logistics - Reports");
    }

    @FXML
    private void handleLogout() {
        navigateTo("/fxml/login.fxml", "TT Logistics - Login");
    }

    private void navigateTo(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene scene = new Scene(loader.load());

            Stage stage = (Stage) btnVehicle.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            showError("Error loading page: " + e.getMessage());
        }
    }

    private void showError(String message) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}