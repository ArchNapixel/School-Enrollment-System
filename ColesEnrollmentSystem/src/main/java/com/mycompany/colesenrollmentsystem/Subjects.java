/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.colesenrollmentsystem;

import static com.mycompany.colesenrollmentsystem.ColesEnrollmentSystem.DBConnect;
import static com.mycompany.colesenrollmentsystem.ColesEnrollmentSystem.st;

/**
 *
 * @author Arch Coles
 */
public class Subjects extends ColesEnrollmentSystem {
    int subjid;
    String subjcode;
    String subjdescription;
    String subjschedule;
    int subjunit;
    
    Subjects() {
        connectDB();
    }

    public void connectDB() {
        DBConnect();
        StudentsForm a = new StudentsForm();
    }

    public void SaveRecord (int subjid, String subjcode, String subjdescription, String subjschedule, int subjunit) {
        String query = "INSERT INTO SubjectsTable (subjid, subjcode, subjdescription, subjschedule, subjunit) VALUES ('" + subjid + "','" + subjcode + "','" + subjdescription + "','" + subjschedule + "','" + subjunit + "')";
        try {
            st.executeUpdate(query);
            System.out.println("SAVE SUCCESSFULLY MADE");
        } catch (Exception ex) {
            System.out.println("SAVE FAILED: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void DeleteRecord(int subjid) {
        String query = "DELETE FROM SubjectsTable WHERE subjid = " + subjid;
        System.out.println("DELETE QUERY: " + query);
        try {
            st.executeUpdate(query);
            System.out.println("DELETE SUCCESSFULLY MADE");
        } catch (Exception ex) {
            System.out.println("DELETE FAILED: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void UpdateRecord(int subjid, String subjcode, String subjdescription, String subjschedule, int subjunit) {
        String query = "UPDATE SubjectsTable SET subjcode='" + subjcode + "', subjdescription='" + subjdescription + "', subjschedule='" + subjschedule + "', subjunit='" + subjunit + "' WHERE subjid='" + subjid + "'";
        System.out.println("UPDATE QUERY: " + query);
        try {
            st.executeUpdate(query);
            System.out.println("UPDATE SUCCESSFULLY MADE");
        } catch (Exception ex) {
            System.out.println("UPDATE FAILED: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
}
