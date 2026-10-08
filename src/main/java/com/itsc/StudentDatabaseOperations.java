package com.itsc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * JDBC Lab: StudentsDB CRUD operations and average grade.
 *
 * Tasks:
 * 1. Connect, create StudentsDB and students table
 * 2. Insert sample data
 * 3. Retrieve five rows
 * 4. Update a student's firstname by id
 * 5. Delete a student by id
 * 6. Calculate average grade
 */
public class StudentDatabaseOperations {

    // Override with env vars MYSQL_USER / MYSQL_PASSWORD if needed
    private static final String HOST = "jdbc:mysql://localhost:3306/";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/StudentsDB";
    private static final String USERNAME = envOr("MYSQL_USER", "root");
    private static final String PASSWORD = envOr("MYSQL_PASSWORD", "root");

    private static String envOr(String key, String defaultValue) {
        String value = System.getenv(key);
        return (value == null || value.isBlank()) ? defaultValue : value;
    }

    public static void main(String[] args) {
        try {
            // Optional with JDBC 4.0+ (driver auto-loads from classpath)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Task 1: create database, connect, create table
            createDatabaseIfNeeded();
            try (Connection connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
                 Statement statement = connection.createStatement()) {

                System.out.println("Established Connection to StudentsDB");

                statement.execute(
                        "CREATE TABLE IF NOT EXISTS students ("
                                + "id INT PRIMARY KEY, "
                                + "firstname VARCHAR(255), "
                                + "lastname VARCHAR(255), "
                                + "grade INT)"
                );
                System.out.println("Table 'students' ready.");

                // Clear previous run so inserts stay idempotent for lab demos
                statement.executeUpdate("DELETE FROM students");

                // Task 2: Insert Data
                insertSampleData(connection);

                // Task 3: Retrieve Data
                System.out.println("\n--- Task 3: Retrieve five rows ---");
                retrieveData(connection);

                // Task 4: Update Data
                System.out.println("\n--- Task 4: Update student id=1 firstname ---");
                updateStudentName(connection, 1, "UpdatedFirstName");
                retrieveData(connection);

                // Task 5: Delete Data
                System.out.println("\n--- Task 5: Delete student id=2 ---");
                deleteStudent(connection, 2);
                retrieveData(connection);

                // Task 6: Calculate Average Grade
                System.out.println("\n--- Task 6: Average grade ---");
                calculateAverageGrade(connection);
            }
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Check pom.xml / classpath.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /** Connects to MySQL server (no DB) and creates StudentsDB if missing. */
    private static void createDatabaseIfNeeded() throws SQLException {
        try (Connection connection = DriverManager.getConnection(HOST, USERNAME, PASSWORD);
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS StudentsDB");
            System.out.println("Database 'StudentsDB' ready.");
        }
    }

    /** Task 2: one sample row + ten more rows. */
    private static void insertSampleData(Connection connection) throws SQLException {
        String sql = "INSERT INTO students (id, firstname, lastname, grade) VALUES (?, ?, ?, ?)";

        // Single example row
        try (PreparedStatement insertSingle = connection.prepareStatement(sql)) {
            insertSingle.setInt(1, 1);
            insertSingle.setString(2, "John");
            insertSingle.setString(3, "Doe");
            insertSingle.setInt(4, 90);
            insertSingle.executeUpdate();
        }

        // Ten more rows
        Object[][] moreStudents = {
                {2, "Aster", "Nega", 85},
                {3, "Jemal", "Edris", 92},
                {4, "Haile", "Anaol", 78},
                {5, "Teddy", "Anbesaw", 88},
                {6, "Sara", "Bekele", 95},
                {7, "Daniel", "Kebede", 70},
                {8, "Hanna", "Tesfaye", 82},
                {9, "Abel", "Mulugeta", 91},
                {10, "Liya", "Getachew", 87},
                {11, "Yonas", "Assefa", 76}
        };

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            for (Object[] row : moreStudents) {
                ps.setInt(1, (Integer) row[0]);
                ps.setString(2, (String) row[1]);
                ps.setString(3, (String) row[2]);
                ps.setInt(4, (Integer) row[3]);
                ps.executeUpdate();
            }
        }

        System.out.println("Inserted 11 student rows.");
    }

    /** Task 3: retrieve and display five rows. */
    private static void retrieveData(Connection connection) throws SQLException {
        String query = "SELECT * FROM students LIMIT 5";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstname = resultSet.getString("firstname");
                String lastname = resultSet.getString("lastname");
                int grade = resultSet.getInt("grade");
                System.out.println("ID: " + id + ", Name: " + firstname + " " + lastname + ", Grade: " + grade);
            }
        }
    }

    /** Task 4: update firstname for a given id. */
    private static void updateStudentName(Connection connection, int id, String newFirstName)
            throws SQLException {
        String sql = "UPDATE students SET firstname = ? WHERE id = ?";
        try (PreparedStatement updateStatement = connection.prepareStatement(sql)) {
            updateStatement.setString(1, newFirstName);
            updateStatement.setInt(2, id);
            int rows = updateStatement.executeUpdate();
            System.out.println("Updated " + rows + " row(s).");
        }
    }

    /** Task 5: delete a row by id. */
    private static void deleteStudent(Connection connection, int id) throws SQLException {
        String sql = "DELETE FROM students WHERE id = ?";
        try (PreparedStatement deleteStatement = connection.prepareStatement(sql)) {
            deleteStatement.setInt(1, id);
            int rows = deleteStatement.executeUpdate();
            System.out.println("Deleted " + rows + " row(s).");
        }
    }

    /** Task 6: average grade of all students. */
    private static void calculateAverageGrade(Connection connection) throws SQLException {
        String query = "SELECT AVG(grade) AS average_grade FROM students";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            if (resultSet.next()) {
                double averageGrade = resultSet.getDouble("average_grade");
                System.out.println("Average Grade: " + averageGrade);
            }
        }
    }
}
