package ranks.one;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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

}
