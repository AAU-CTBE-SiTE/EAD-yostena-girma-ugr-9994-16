package com.itsc;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Basic JDBC connection demo from the lab manual.
 */
public class JDBCDemo {
    public static void main(String[] args) {
        try {
            String driver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/StudentsDB";
            String username = System.getenv("MYSQL_USER") != null ? System.getenv("MYSQL_USER") : "root";
            String password = System.getenv("MYSQL_PASSWORD") != null ? System.getenv("MYSQL_PASSWORD") : "root";

            Class.forName(driver); // optional with JDBC 4.0+
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("Established Connection");
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
