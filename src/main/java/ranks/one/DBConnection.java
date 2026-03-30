package ranks.one;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    public Connection databaseLink;

    public Connection getConnection(){
        String databaseName = "tt_logistics";
        String databaseUser = "ranks_2";
        String databasePassword = "anime";
        String url = "jdbc:mysql://localhost/"+databaseName;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(url,databaseUser,databasePassword);
        } catch (Exception s) {
            s.printStackTrace();
        }
        return databaseLink;
    }
}
