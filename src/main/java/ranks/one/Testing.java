package ranks.one;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.net.ConnectException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Testing {

    @FXML
    private Button btnPress;

    @FXML
    private TextArea txtDisplay;

    @FXML
    void handlePress(ActionEvent event) {
        DBConnection db_conn = new DBConnection();
        Connection conn = db_conn.getConnection();

        String connectQuery = "select * from client";

        try{
            Statement statement  = conn.createStatement();
            ResultSet queryOutput = statement.executeQuery(connectQuery);

            while(queryOutput.next()){
                String id = queryOutput.getString("client_id");
                String name = queryOutput.getString("client_name");
                String contact = queryOutput.getString("contact_phone");
                String address = queryOutput.getString("address");
                txtDisplay.appendText(
                        "ID: "+id + "\n" +
                        "Name: "+name + "\n" +
                        "Contact: "+contact + "\n" +
                        "Address: "+address+"\n"
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
