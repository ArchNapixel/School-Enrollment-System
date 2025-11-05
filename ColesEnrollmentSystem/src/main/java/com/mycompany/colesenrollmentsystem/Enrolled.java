/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.colesenrollmentsystem;

/**
 *
 * @author Arch Coles
 */
public class Enrolled extends ColesEnrollmentSystem {
    private int subjid;

    public void setsubjid(int a) {
        this.subjid = a;
    }

    public int getsubjid() {
        return this.subjid;
    }

    public String enrollStud(int studid) {
        try {
            if (st == null) {
                DBConnect();
            }

            String checkQuery = "SELECT COUNT(*) FROM Enroll WHERE studid = " + studid + " AND subjid = " + this.subjid;
            rs = st.executeQuery(checkQuery);
            if (rs.next() && rs.getInt(1) > 0) {
                return "Student is already enrolled in this subject.";
            }

            String maxIdQuery = "SELECT COALESCE(MAX(eid), 0) + 1 FROM Enroll";
            rs = st.executeQuery(maxIdQuery);
            int nextEid = 1;
            if (rs.next()) {
                nextEid = rs.getInt(1);
            }
            String query = "INSERT INTO Enroll (eid, studid, subjid) VALUES (" + nextEid + ", " + studid + ", " + this.subjid + ")";

            st.executeUpdate(query);
            return "Student enrolled successfully.";
        } catch (Exception ex) {
            System.out.println("Error enrolling student: " + ex);
            return "Failed to enroll student: " + ex.getMessage();
        }
    }

    public String DropSubj(int studid) {
        try {
            if (st == null) {
                DBConnect();
            }

            String query = "DELETE FROM Enroll WHERE studid = " + studid + " AND subjid = " + this.subjid;
            int rowsAffected = st.executeUpdate(query);
            if (rowsAffected > 0) {
                return "Subject dropped successfully.";
            } else {
                return "Student is not enrolled in this subject.";
            }
        } catch (Exception ex) {
            System.out.println("Error dropping subject: " + ex);
            return "Failed to drop subject: " + ex.getMessage();
        }
    }
}
