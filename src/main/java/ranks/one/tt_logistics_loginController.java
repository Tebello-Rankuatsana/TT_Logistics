package ranks.one;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class tt_logistics_loginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField PsdPassword;

    @FXML
    private Button btnLogin;

    @FXML
    private Label feedbackLabel;

    private String loggedInUsername;
    private String loggedInPrivilege;

    @FXML
    public void initialize() {
        btnLogin.setOnAction(event -> handleLogin());
        txtUsername.setOnAction(event -> handleLogin());
        PsdPassword.setOnAction(event -> handleLogin());
    }

    @FXML
    private void handleLogin() {
        String username = txtUsername.getText().trim();
        String password = PsdPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            feedbackLabel.setText("Please enter username and password");
            return;
        }

        if (authenticateUser(username, password)) {
            feedbackLabel.setText("Login successful! Loading...");
            feedbackLabel.setStyle("-fx-text-fill: green;");
            loadMainMenu();
        } else {
            feedbackLabel.setText("Invalid username or password");
            feedbackLabel.setStyle("-fx-text-fill: #fa2323;");
            clearFields();
        }
    }

    private boolean authenticateUser(String username, String password) {
        String query = "SELECT name, privilege_type FROM users WHERE name = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    loggedInUsername = rs.getString("name");
                    loggedInPrivilege = rs.getString("privilege_type");

                    System.out.println("Login Successful - User: " + loggedInUsername + ", Privilege: " + loggedInPrivilege);
                    return true;
                }
            }
        } catch (SQLException e) {
            feedbackLabel.setText("Database error: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private void clearFields() {
        txtUsername.clear();
        PsdPassword.clear();
        txtUsername.requestFocus();
    }

    private void loadMainMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("tt_logistics_menu.fxml"));
            Scene scene = new Scene(loader.load());

            TtLogisticsMenu mainController = loader.getController();
            mainController.setUserInfo(loggedInUsername, loggedInPrivilege);

            Stage stage = (Stage) btnLogin.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("TT Logistics - Fleet Management System");
            stage.setMaximized(false);
            stage.setResizable(true);
            stage.show();

        } catch (Exception e) {
            feedbackLabel.setText("Error loading main menu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public String getLoggedInUsername() {
        return loggedInUsername;
    }

    public String getLoggedInPrivilege() {
        return loggedInPrivilege;
    }
}