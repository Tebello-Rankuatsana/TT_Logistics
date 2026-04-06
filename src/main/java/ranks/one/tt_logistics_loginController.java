package ranks.one;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class tt_logistics_loginController {

    @FXML
    private PasswordField PsdPassword;

    @FXML
    private TextField txtUsername;

    @FXML
    private Label feedbackLabel;

    @FXML
    void handleLogin(ActionEvent event) {
        String username = txtUsername.getText();
        String password = PsdPassword.getText();

        if (authenticateUser(username, password)) {
            loadMainMenu();
        } else {
            feedbackLabel.setText("incorrect credentials");
        }
    }

    private boolean authenticateUser(String username, String password) {
        // Check if user exists in MySQL user table
        String query = "SELECT User, Host FROM mysql.user WHERE User = ?";
        try (PreparedStatement statement = DatabaseConnection.getConnection().prepareStatement(query)) {
            statement.setString(1, username);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                // Validate privileges from your custom users table
                return validatePrivileges(username, password);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
