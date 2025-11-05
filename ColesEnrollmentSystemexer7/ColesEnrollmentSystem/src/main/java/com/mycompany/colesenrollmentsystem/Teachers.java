/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.colesenrollmentsystem;

import static com.mycompany.colesenrollmentsystem.ColesEnrollmentSystem.DBConnect;
import static com.mycompany.colesenrollmentsystem.ColesEnrollmentSystem.st;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Teachers extends ColesEnrollmentSystem {
    int teachid;
    String teachname;
    String teachaddress;
    String teachcontact;
    String teachemail;
    String teachdept;
    
    Teachers() {
        connectDB();
    }
    public void connectDB(){
        DBConnect();
        StudentsForm a = new StudentsForm();
    }
    
    public void SaveRecord(String teachname, String teachaddress, String teachcontact, String teachemail, String teachdept) {
        try {
            ColesEnrollmentSystem.DBConnect();

            // Insert teacher record
            String query = "INSERT INTO TeachersTable (teachname, teachaddress, teachcontact, teachemail, teachdept) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = ColesEnrollmentSystem.con.prepareStatement(query);
            ps.setString(1, teachname);
            ps.setString(2, teachaddress);
            ps.setString(3, teachcontact);
            ps.setString(4, teachemail);
            ps.setString(5, teachdept);
            ps.executeUpdate();
            ps.close();
            System.out.println("Teacher saved successfully.");

            // Get the latest teachId
            String username = null;
            String password = null;
            try {
                String selectIdQuery = "SELECT MAX(teachId) AS newId FROM " + db + ".TeachersTable;";
                PreparedStatement idPs = ColesEnrollmentSystem.con.prepareStatement(selectIdQuery);
                ResultSet rs = idPs.executeQuery();

                String newId = "";
                if (rs.next()) {
                    newId = rs.getString("newId");
                }
                rs.close();
                idPs.close();

                username = newId + teachname;
                password = "AdDU" + teachname;

                // Create database user
                String createUserQuery = "CREATE USER ?@'%' IDENTIFIED BY ?;";
                PreparedStatement userPs = ColesEnrollmentSystem.con.prepareStatement(createUserQuery);
                userPs.setString(1, username);
                userPs.setString(2, password);
                userPs.executeUpdate();
                userPs.close();
                System.out.println("User created successfully: " + username);
            } catch (Exception ex) {
                System.out.println("Failed to create user / user already exists: " + ex.getMessage());
                ex.printStackTrace();
            }

            // Grant privileges
            try {
                String updatePrivilegeQuery = "GRANT SELECT, INSERT, UPDATE ON " + db + ".* TO ?@'%';";
                PreparedStatement privPs = ColesEnrollmentSystem.con.prepareStatement(updatePrivilegeQuery);
                privPs.setString(1, username);
                privPs.executeUpdate();
                privPs.close();

                String flushQuery = "FLUSH PRIVILEGES;";
                PreparedStatement flushPs = ColesEnrollmentSystem.con.prepareStatement(flushQuery);
                flushPs.executeUpdate();
                flushPs.close();
                System.out.println("Privileges granted successfully for user: " + username);
            } catch (Exception ex) {
                System.out.println("Failed to grant privileges: " + ex.getMessage());
                ex.printStackTrace();
            }

        } catch (Exception e) {
            System.out.println("SAVE FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }


    
    public void DeleteRecord(int teachid, String teachname) {
        String db = "your_database_name"; // Replace with actual database name

        // Delete teacher record
        String deletequery = "DELETE FROM TeachersTable WHERE teachid = " + teachid;
        System.out.println("DELETE QUERY: " + deletequery);
        try {
            st.executeUpdate(deletequery);
            System.out.println("DELETE SUCCESSFULLY MADE");
        } catch (Exception ex) {
            System.out.println("DELETE FAILED: " + ex.getMessage());
            ex.printStackTrace();
        }

        // Delete associated assignments
        String assignQuery = "DELETE FROM Assign WHERE tid = " + teachid;
        System.out.println("ASSIGN DELETE QUERY: " + assignQuery);
        try {
            st.executeUpdate(assignQuery);
            System.out.println("Assign Delete Success");
        } catch (Exception ex) {
            System.out.println("Failed to Delete Assign: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        // Manage database user
        String username = teachid + teachname;
        String revokeQuery = "REVOKE ALL PRIVILEGES ON " + db + ".* FROM '" + username + "'@'%'";
        String dropUserQuery = "DROP USER '" + username + "'@'%'";
        boolean exists = checkStudentExists(teachid, teachname);

        if (exists) {
            try {
                st.executeUpdate(revokeQuery);
                System.out.println("Revoke Success");
            } catch (Exception ex) {
                System.out.println("Failed to Revoke: " + ex.getMessage());
                ex.printStackTrace();
            }
        } else {
            try {
                st.executeUpdate(dropUserQuery);
                System.out.println("Delete User Success");
            } catch (Exception ex) {
                System.out.println("Failed to Delete User: " + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }
    public boolean checkStudentExists(int id, String name) {
        boolean exists = false;
        try {
            String q1 = "SELECT * FROM 1stSemSY2025_2026.TeachersTable WHERE teachid = " + id + " AND teachname = '" + name + "'";
            rs = st.executeQuery(q1);
            if (rs.next()) {
                exists = true;
            }

            String q2 = "SELECT * FROM 2ndSemSY2025_2026.TeachersTable WHERE teachid = " + id + " AND teachname = '" + name + "'";
            rs = st.executeQuery(q2);
            if (rs.next()) {
                exists = true;
            }

            String q3 = "SELECT * FROM SummerSY2025_2026.TeachersTable WHERE teachid = " + id + " AND teachname = '" + name + "'";
            rs = st.executeQuery(q3);
            if (rs.next()) {
                exists = true;
            }
        } catch (Exception e) {
            System.out.println("failed to check if user exists");
        }
        return exists;
    }
    
    public void UpdateRecord(int teachid, String teachname, String teachaddress, String teachcontact, String teachemail, String teachdept) {
        String query = "UPDATE TeachersTable SET teachname='" + teachname + "', teachaddress='" + teachaddress + "', teachcontact='" + teachcontact + "', teachemail='" + teachemail + "', teachdept='" + teachdept + "' WHERE teachid='" + teachid + "'";
        System.out.println("DELETE QUERY: " + query);
        try {
            st.executeUpdate(query);
            System.out.println("UPDATE SUCCESSFULLY MADE");
        } catch (Exception ex) {
            System.out.println("UPDATE FAILED: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    
}

