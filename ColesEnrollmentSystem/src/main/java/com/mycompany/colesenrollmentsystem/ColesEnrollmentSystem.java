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

//     public static void createTables(){
//        
//        try {
//            String createData = "CREATE DATABASE IF NOT EXISTS " + db + "";
//            st.executeUpdate(createData);
//            String useDatabase = "USE " + db;
//            st.executeUpdate(useDatabase);
//            System.out.println("Switched to database: " + db);
//
//            String createStudentsTable = 
//                """
//                CREATE TABLE IF NOT EXISTS StudentsTable (
//                studentid INT PRIMARY KEY AUTO_INCREMENT,
//                Name TEXT,
//                Address TEXT,
//                Contact TEXT,
//                email TEXT,
//                Course TEXT,
//                Gender TEXT,
//                YearLevel TEXT
//                ) AUTO_INCREMENT = 1001;
//                """;
//                st.executeUpdate(createStudentsTable);
//
//            String createSubjectsTable = "CREATE TABLE IF NOT EXISTS subjects (" +
//                "subId INT PRIMARY KEY AUTO_INCREMENT, " +
//                "subUnits INT, " +
//                "subCode TEXT, " +
//                "subDescription TEXT, " +
//                "subSchedule TEXT" +
//                ") AUTO_INCREMENT = 2001";
//            st.executeUpdate(createSubjectsTable);
//
//            String createTeachersTable = "CREATE TABLE IF NOT EXISTS teachers (" +
//                "teachId INT PRIMARY KEY AUTO_INCREMENT, " +
//                "teachName TEXT, " +
//                "teachAddress TEXT, " +
//                "teachContact TEXT, " +
//                "teachEmail TEXT, " +
//                "teachDepartment TEXT" +
//                ") AUTO_INCREMENT = 3001";
//            st.executeUpdate(createTeachersTable);
//
//            String createTransactionChargesTable = "CREATE TABLE IF NOT EXISTS TransactionCharges (" +
//                "TransID INT PRIMARY KEY AUTO_INCREMENT, " +
//                "Department TEXT, " +
//                "SubjUnits DECIMAL(10,2), " +
//                "Insurance DECIMAL(10,2), " +
//                "Computer DECIMAL(10,2), " +
//                "Laboratory DECIMAL(10,2), " +
//                "Cultural DECIMAL(10,2), " +
//                "Library DECIMAL(10,2), " +
//                "Facility DECIMAL(10,2)" +
//                ")";
//            st.executeUpdate(createTransactionChargesTable);
//
//            String createEnrollTable = "CREATE TABLE IF NOT EXISTS Enroll (" +
//                "eid INT PRIMARY KEY AUTO_INCREMENT, " +
//                "studid INT, " +
//                "subjid INT," +
//                "FOREIGN KEY (studid) REFERENCES students(studId) ON DELETE CASCADE, " +
//                "FOREIGN KEY (subjid) REFERENCES subjects(subId) ON DELETE CASCADE," +
//                "UNIQUE (studid, subjid)" +
//                ")";
//            st.executeUpdate(createEnrollTable);
//
//            String createAssignTable = "CREATE TABLE IF NOT EXISTS Assign (" +
//                "subid INT UNIQUE," +
//                "tid INT," +
//                "FOREIGN KEY (subid) REFERENCES subjects(subId) ON DELETE CASCADE, " +
//                "FOREIGN KEY (tid) REFERENCES teachers(teachId) ON DELETE CASCADE" +
//                ")";
//            st.executeUpdate(createAssignTable);
//
//            String createGradesTable = "CREATE TABLE IF NOT EXISTS Grades (" +
//                "GradeID INT PRIMARY KEY AUTO_INCREMENT, " +
//                "eid INT UNIQUE, " +
//                "Prelim TEXT, " +
//                "Midterm TEXT, " +
//                "Prefinal TEXT, " +
//                "Final TEXT, " +
//                "FOREIGN KEY (eid) REFERENCES Enroll(eid) ON DELETE CASCADE" +
//                ")";
//            st.executeUpdate(createGradesTable);
//
//            String createInvoiceTable = "CREATE TABLE IF NOT EXISTS Invoice (" +
//                "Invoicenum INT PRIMARY KEY AUTO_INCREMENT, " +
//                "studid INT, " +
//                "TransID INT" +
//                ")";
//            st.executeUpdate(createInvoiceTable);
//
//            System.out.println("All database structures created successfully!");
//
//            JOptionPane.showMessageDialog(null,"Database Created: " + db, "New Database", JOptionPane.INFORMATION_MESSAGE);
//
//        } catch (Exception ex){
//            System.out.println("Failed to Create Database: " + ex);
//        }
//
//        DBConnect();
//
//        }
}

