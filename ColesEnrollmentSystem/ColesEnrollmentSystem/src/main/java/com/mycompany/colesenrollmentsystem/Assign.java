/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.colesenrollmentsystem;

/**
 *
 * @author Arch Coles
 */
public class Assign extends ColesEnrollmentSystem {
    private int subjid;

    public void setsubjid(int a) {
        this.subjid = a;
    }

    public int getsubjid() {
        return this.subjid;
    }

    public String AssignSubj(int tid) {
        try {
            if (st == null) {
                DBConnect();
            }

            String checkQuery = "SELECT COUNT(*) FROM Assign WHERE subid = " + this.subjid;
            rs = st.executeQuery(checkQuery);
            if (rs.next() && rs.getInt(1) > 0) {
                return "A teacher is already assigned to this subject. Only one teacher can be assigned per subject.";
            }

            String query = "INSERT INTO Assign (subid, tid) VALUES (" + this.subjid + ", " + tid + ")";
            st.executeUpdate(query);
            return "Subject assigned successfully.";
        } catch (Exception ex) {
            System.out.println("Error assigning subject: " + ex);
            return "Failed to assign subject: " + ex.getMessage();
        }
    }

    public String DeleteSubj(int tid) {
        try {
            if (st == null) {
                DBConnect();
            }

            String query = "DELETE FROM Assign WHERE tid = " + tid + " AND subid = " + this.subjid;
            int rowsAffected = st.executeUpdate(query);
            if (rowsAffected > 0) {
                return "Subject assignment removed successfully.";
            } else {
                return "Teacher is not assigned to this subject.";
            }
        } catch (Exception ex) {
            System.out.println("Error removing assignment: " + ex);
            return "Failed to remove assignment: " + ex.getMessage();
        }
    }
}
