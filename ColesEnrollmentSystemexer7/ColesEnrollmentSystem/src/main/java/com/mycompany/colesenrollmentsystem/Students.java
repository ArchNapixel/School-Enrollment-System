package com.mycompany.colesenrollmentsystem;

import java.sql.ResultSet;

public class Students extends ColesEnrollmentSystem {
    int studentid;
    String Name;
    String Address;
    String Contact;
    String email;
    String Course;
    String Gender;
    String YearLevel;
    
    Students() {
        connectDB();
    }
    public void connectDB(){
        DBConnect();
        StudentsForm a = new StudentsForm();
    }
    
    public void SaveRecord(String Name, String Address, String Contact, String email, String Course, String Gender, String Yrlevel) {
        String username = null;
        String password = null;
        int newStudID = 1001;

        try {
            String maxQuery = "SELECT MAX(studentid) AS newId FROM StudentsTable";
            rs = st.executeQuery(maxQuery);
            if (rs.next()) {
                int maxID = rs.getInt("newId");
                if (maxID >= 1001) {
                    newStudID = maxID + 1;
                }
            }
        } catch (Exception ex) {
            System.out.println("Error getting next ID: " + ex.getMessage());
        }

        try {
            String insertQuery = "INSERT INTO StudentsTable (studentid, Name, Address, Contact, email, Course, Gender, YearLevel) VALUES ("
                    + newStudID + ", '" + Name + "', '" + Address + "', '" + Contact + "', '" + email + "', '"
                    + Course + "', '" + Gender + "', '" + Yrlevel + "')";
            st.executeUpdate(insertQuery);
            System.out.println("Insert Success");
        } catch (Exception ex) {
            System.out.println("Failed to Insert: " + ex.getMessage());
        }

        try {
            username = newStudID + Name;
            password = "AdDU" + Name;

            String createUserQuery = "CREATE USER '" + username + "'@'%' IDENTIFIED BY '" + password + "';";
            st.executeUpdate(createUserQuery);
            System.out.println("User Creation Success");
        } catch (Exception ex) {
            System.out.println("Failed to create user / user already exists: " + ex.getMessage());
        }

        try {
            String updatePrivilageQuery = "GRANT SELECT ON " + db + ".* TO '" + username + "'@'%';";
            st.executeUpdate(updatePrivilageQuery);

            String flushQuery = "FLUSH PRIVILEGES;";
            st.executeUpdate(flushQuery);
            System.out.println("Grant Privileges Success");
        } catch (Exception ex) {
            System.out.println("Failed to grant privileges: " + ex.getMessage());
        }
    }

    public void DeleteRecord(int studentid, String Name) {
        String deleteQuery = "DELETE FROM StudentsTable WHERE studentid = " + studentid;
        
        try {
            st.executeUpdate(deleteQuery);
            System.out.println("Delete Success");
        } catch (Exception ex) {
            System.out.println("Failed to Delete: " + ex.getMessage());
        }

        String username = studentid + Name;
        boolean exists = checkStudentExists(studentid, Name);
        
        System.out.println(exists);
        
        String revokeQuery = "REVOKE ALL PRIVILEGES ON " + db + ".* FROM '" + username + "'@'%'";
        String dropQuery = "DROP USER '" + username + "'@'%';";
        if (exists){
            try {
                st.executeUpdate(revokeQuery);
                System.out.println("Revoke Success");
            } catch (Exception ex){
                System.out.println("Failed to Revoke: " + ex);
            }
        } else {
            try {
                st.executeUpdate(dropQuery);
                System.out.println("delete user Success");
            } catch (Exception ex){
                System.out.println("Failed to delete user: " + ex);
            }
        }
    }

    public boolean checkStudentExists(int studentid, String Name) {
        boolean exists = false;
        try {
            // db1
            String q1 = "SELECT * FROM 1stSemSY2025_2026.StudentsTable WHERE studentid = " + studentid + " AND Name = '" + Name + "'";
            rs = st.executeQuery(q1);
            if (rs.next()) {
                exists = true;
            }

            // db2
            String q2 = "SELECT * FROM 2ndSemSY2025_2026.StudentsTable WHERE studentid = " + studentid + " AND Name = '" + Name + "'";
            rs = st.executeQuery(q2);
            if (rs.next()) {
                exists = true;
            }

            // db3
            String q3 = "SELECT * FROM SummerSY2025_2026.StudentsTable WHERE studentid = " + studentid + " AND Name = '" + Name + "'";
            rs = st.executeQuery(q3);
            if (rs.next()) {
                exists = true;
            }

        } catch (Exception e) {
            System.out.println("failed to check if user exists");
        }
        return exists;
    }

    public void UpdateRecord(int studentid, String Name, String Address, String Contact, String email, String Course, String Gender, String Yrlevel) {
        String query = "UPDATE StudentsTable SET "
                + "Name = '" + Name + "', "
                + "Address = '" + Address + "', "
                + "Contact = '" + Contact + "', "
                + "email = '" + email + "', "
                + "Course = '" + Course + "', "
                + "Gender = '" + Gender + "', "
                + "YearLevel = '" + Yrlevel + "' "
                + "WHERE studentid = " + studentid;

        try {
            st.executeUpdate(query);
            System.out.println("Update Success");
        } catch (Exception ex) {
            System.out.println("Failed to Update: " + ex.getMessage());
        }
    }
}
