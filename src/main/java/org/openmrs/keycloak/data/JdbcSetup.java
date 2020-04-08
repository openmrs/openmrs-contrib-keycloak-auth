package org.openmrs.keycloak.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcSetup {
    public static void main(String[] args) {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/server";
        String username = "root";
        String password = "root";

        try {
            Class.forName(driver);

            Connection connection=DriverManager.getConnection(url,username,password);
            System.out.println("Connection Success");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
