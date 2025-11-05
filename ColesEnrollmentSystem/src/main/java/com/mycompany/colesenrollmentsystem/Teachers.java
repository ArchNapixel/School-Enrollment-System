/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.colesenrollmentsystem;

import static com.mycompany.colesenrollmentsystem.ColesEnrollmentSystem.DBConnect;
import static com.mycompany.colesenrollmentsystem.ColesEnrollmentSystem.st;
import java.sql.PreparedStatement;

/**
 *
 * @author Arch Coles
 */
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
    
    public void SaveRecord(int teachid, String teachname, String teachaddress, String teachcontact, String teachemail, String teachdept) {
        try {
            ColesEnrollmentSystem.DBConnect(); 

            String query = "INSERT INTO TeachersTable (teachid, teachname, teachaddress, teachcontact, teachemail, teachdept) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = ColesEnrollmentSystem.con.prepareStatement(query);
            ps.setInt(1, teachid);
            ps.setString(2, teachname);
            ps.setString(3, teachaddress);
            ps.setString(4, teachcontact);
            ps.setString(5, teachemail);
            ps.setString(6, teachdept);

            ps.executeUpdate();
            ps.close(); 

            System.out.println("Teacher saved successfully.");
        } catch (Exception e) {
            System.out.println("SAVE FAILED: " + e.getMessage());
            e.printStackTrace();
        }
    }


    
    public void DeleteRecord(int teachid) {
        String deletequery = "DELETE FROM TeachersTable WHERE teachid = " + teachid;
        System.out.println("DELETE QUERY: " + deletequery);
        try {
            st.executeUpdate(deletequery);
            System.out.println("DELETE SUCCESSFULLY MADE");
        } catch (Exception ex) {
            System.out.println("DELETE FAILED: " + ex.getMessage());
            ex.printStackTrace();
        }
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

