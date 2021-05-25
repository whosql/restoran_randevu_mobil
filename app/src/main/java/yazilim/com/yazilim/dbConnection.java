package yazilim.com.yazilim;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class dbConnection {
    String connectClass = "com.mysql.jdbc.Driver";
    String connectionURL = "jdbc:mysql://sql7.freemysqlhosting.net/sql7269367";
    String connectionUser = "sql7269367";
    String connectionPassword = "RP5grmETRZ";


    public Connection getConnection(){
        Connection connect = null;

        try {
            Class.forName(connectClass);
            connect = DriverManager.getConnection(connectionURL,connectionUser,connectionPassword);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return connect;
    }
}
