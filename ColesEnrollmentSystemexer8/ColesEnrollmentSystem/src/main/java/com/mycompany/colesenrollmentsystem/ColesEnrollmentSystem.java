package com.mycompany.colesenrollmentsystem;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;


public class ColesEnrollmentSystem {
    public static String createDatabase
            = "CREATE TABLE StudentsTable (studentid INT PRIMARY KEY AUTO_INCREMENT, Name TEXT, Address TEXT, Contact TEXT, email TEXT, Course TEXT, Gender TEXT, YearLevel TEXT)AUTO_INCREMENT = 1001;\n"
            + "CREATE TABLE SubjectsTable (subjid INT PRIMARY KEY AUTO_INCREMENT, subjcode TEXT, subjdescription TEXT, subjschedule TEXT, subjunit INT) AUTO_INCREMENT = 2001;\n"
            + "CREATE TABLE TeachersTable (teachid INT PRIMARY KEY AUTO_INCREMENT, teachname TEXT, teachaddress TEXT, teachcontact TEXT, teachemail TEXT, teachdept TEXT) AUTO_INCREMENT = 3001;\n"
            + "CREATE TABLE Enroll (eid INT PRIMARY KEY AUTO_INCREMENT, studid INT, subjid INT, UNIQUE KEY unique_stud_subj (studid, subjid), FOREIGN KEY (studid) REFERENCES StudentsTable(studentid) ON DELETE CASCADE, FOREIGN KEY (subjid) REFERENCES SubjectsTable(subjid) ON DELETE CASCADE);\n"
            + "CREATE TABLE Assign (subid INT UNIQUE, tid INT, FOREIGN KEY (subid) REFERENCES SubjectsTable(subjid) ON DELETE CASCADE, FOREIGN KEY (tid) REFERENCES TeachersTable(teachid) ON DELETE CASCADE);\n"
            + "CREATE TABLE Grades (gradesid INT PRIMARY KEY AUTO_INCREMENT, eid INT UNIQUE, prelim TEXT, midterm TEXT, prefinal TEXT, final TEXT, FOREIGN KEY (eid) REFERENCES Enroll(eid) ON DELETE CASCADE);\n"
            + "CREATE TABLE TransactionCharges (transac INT PRIMARY KEY AUTO_INCREMENT, department TEXT, subjunit DECIMAL(10,2), insurance DECIMAL(10,2), computer DECIMAL(10,2), laboratory DECIMAL(10,2), cultural DECIMAL(10,2), library DECIMAL(10,2), facility DECIMAL(10,2));\n"
            + "CREATE TABLE Invoice (Transacid INT PRIMARY KEY AUTO_INCREMENT, invoicenum INT UNIQUE, studid INT, FOREIGN KEY (studid) REFERENCES StudentsTable(studentid) ON DELETE CASCADE);\n";
    
    static Connection con;
    static Statement st;
    static ResultSet rs;

    static String db = "";
    static String uname = null;
    static String pswd = null;
    
    public static void main(String[] args) {
        LoginForm login = new LoginForm();
        login.setVisible(true);
        login.setLocationRelativeTo(null);
    }
    
    public static void DBConnect() {
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://10.4.44.40:3306/" + db + "?zeroDateTimeBehavior=CONVERT_TO_NULL",uname,pswd);
            st = con.createStatement();
            System.out.println("CONNECTED: " + db);
        } catch (Exception ex) {
            System.out.println("Error to connect: " + ex.getMessage());
        }
    }   
    public static void switchDB() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://10.4.44.40:3306/" + db + "?zeroDateTimeBehavior=CONVERT_TO_NULL", uname, pswd);
            st = con.createStatement();
            JOptionPane.showMessageDialog(null, "Database switched:" + db, "New Database", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            System.out.println("Error to connect: " + ex.getMessage());
        }
    }
}

