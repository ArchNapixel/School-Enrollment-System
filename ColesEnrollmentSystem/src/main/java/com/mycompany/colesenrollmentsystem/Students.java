package com.mycompany.colesenrollmentsystem;

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
        int newStudID = 1001;
        try {
            String maxQuery = "SELECT MAX(studentid) FROM StudentsTable";
            rs = st.executeQuery(maxQuery);
            if (rs.next()) {
                int maxID = rs.getInt(1);
                if (maxID >= 1001) {
                    newStudID = maxID + 1;
                }
            }
        } catch (Exception ex) {
            System.out.println("Error getting next ID: " + ex.getMessage());
        }

        String query = "INSERT INTO StudentsTable(studentid, Name, Address, Contact, email, Course, Gender, YearLevel) VALUES("
                + newStudID + ", '" + Name + "', '" + Address + "', '" + Contact + "', '" + email + "', '"
                + Course + "', '" + Gender + "', '" + Yrlevel + "')";
        try {
            st.executeUpdate(query);
            System.out.println("Data inserted successfully");
        } catch (Exception ex) {
            System.out.println("Failed to insert: " + ex.getMessage());
        }
    }


    public void DeleteRecord(int studid) {
        try {
            String deleteQuery = "DELETE FROM StudentsTable WHERE studentid = " + studid;
            st.executeUpdate(deleteQuery);
            System.out.println("Student with ID " + studid + " deleted successfully");
        } catch (Exception ex) {
            System.out.println("Failed to delete: " + ex.getMessage());
        }
    }

    public void UpdateRecord(int studid, String Name, String Address, String Contact, String email, String Course, String Gender, String Yrlevel) {
        String query = "UPDATE StudentsTable SET "
                + "Name = '" + Name + "', "
                + "Address = '" + Address + "', "
                + "Contact = '" + Contact + "', "
                + "email = '" + email + "', "
                + "Course = '" + Course + "', "
                + "Gender = '" + Gender + "', "
                + "YearLevel = '" + Yrlevel + "' "
                + "WHERE studentid = " + studid;
        try {
            st.executeUpdate(query);
            System.out.println("Update Successful");
        } catch (Exception ex) {
            System.out.println("Failed to Update: " + ex.getMessage());
        }
    }
}
