package com.mycompany.colesenrollmentsystem;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class LoginForm extends javax.swing.JFrame {
    String username;
    String password;
    private boolean isLoggedIn = false;
    int id = 0;
    
    public LoginForm() {
        initComponents();
    }

    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        usernameinput = new javax.swing.JTextField();
        passwordinput = new javax.swing.JPasswordField();
        loginbutton = new javax.swing.JButton();
        sycombobox = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        submitbutton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel1.setText("USERNAME");

        jLabel2.setText("PASSWORD");

        usernameinput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameinputActionPerformed(evt);
            }
        });

        passwordinput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordinputActionPerformed(evt);
            }
        });

        loginbutton.setText("Login");
        loginbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loginbuttonActionPerformed(evt);
            }
        });

        sycombobox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "School Year" }));

        jLabel3.setText("SCHOOL YEAR");

        submitbutton.setText("Submit");
        submitbutton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitbuttonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(147, 147, 147)
                        .addComponent(loginbutton))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(83, 83, 83)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(usernameinput)
                            .addComponent(passwordinput, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(64, 64, 64)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sycombobox, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(144, 144, 144)
                        .addComponent(submitbutton)))
                .addContainerGap(44, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(usernameinput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(passwordinput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(loginbutton)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sycombobox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addComponent(submitbutton)
                .addContainerGap(70, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void usernameinputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameinputActionPerformed
        
    }//GEN-LAST:event_usernameinputActionPerformed

    private void loginbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loginbuttonActionPerformed
        ColesEnrollmentSystem system = new ColesEnrollmentSystem();

        username = usernameinput.getText().trim();
        password = new String(passwordinput.getPassword()).trim();

        try {
            system.uname = username;
            system.pswd = password;
            system.db = "mysql";

            Class.forName("com.mysql.cj.jdbc.Driver");
            system.DBConnect();
            System.out.println("CONNECTED: " + system.db);

            // If DBConnect doesn't throw, assume login is successful
            isLoggedIn = true;
            submitbutton.setEnabled(true);
            sycombobox.setEnabled(true);

            system.rs = system.st.executeQuery("SHOW DATABASES;");
            sycombobox.removeAllItems();

            ArrayList<String> databases = new ArrayList<>();
            while (system.rs.next()) {
                String db = system.rs.getString(1);
                if (!(db.equalsIgnoreCase("mysql")
                        || db.equalsIgnoreCase("performance_schema")
                        || db.equalsIgnoreCase("information_schema")
                        || db.equalsIgnoreCase("studyBase")
                        || db.equalsIgnoreCase("sys"))) {
                    databases.add(db);
                }
            }

            // Fallback if no valid databases found
            if (databases.isEmpty()) {
                databases.add("information_schema");
            }

            for (String db : databases) {
                sycombobox.addItem(db);
            }

        } catch (Exception ex) {
            isLoggedIn = false;
            username = null;
            password = null;
            JOptionPane.showMessageDialog(null, "Invalid User");
            System.out.println("Invalid Login: " + ex);
            sycombobox.removeAllItems();
            submitbutton.setEnabled(false);
            sycombobox.setEnabled(false);
        }
    }//GEN-LAST:event_loginbuttonActionPerformed

    private void submitbuttonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitbuttonActionPerformed
        ColesEnrollmentSystem system = new ColesEnrollmentSystem();

        // Check login status before proceeding
        if (!isLoggedIn || username == null || password == null) {
            JOptionPane.showMessageDialog(null, "Please Login First");
            return;
        }

        // Get selected database
        String database = (String) sycombobox.getSelectedItem();
        if (database == null || database.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Please select a database");
            return;
        }

        system.uname = username;
        system.pswd = password;
        system.db = database;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            system.DBConnect();

            ResultSet rs = system.st.executeQuery("SHOW GRANTS FOR CURRENT_USER");

            boolean canSelect = false;
            boolean canInsert = false;
            boolean canUpdate = false;
            boolean canDelete = false;

            while (rs.next()) {
                String grant = rs.getString(1).toUpperCase();

                if (grant.contains("ALL PRIVILEGES")) {
                    canSelect = canInsert = canUpdate = canDelete = true;
                } else {
                    if (grant.contains("SELECT")) {
                        canSelect = true;
                    }
                    if (grant.contains("INSERT")) {
                        canInsert = true;
                    }
                    if (grant.contains("UPDATE")) {
                        canUpdate = true;
                    }
                    if (grant.contains("DELETE")) {
                        canDelete = true;
                    }
                }
            }
            if (canSelect && canInsert && canUpdate && canDelete) {
                // Admin / root UI
                ColesEnrollmentSystem.db = database;
                ColesEnrollmentSystem.uname = username;
                ColesEnrollmentSystem.pswd = password;
                
                StudentsForm studForm = new StudentsForm();
                studForm.setVisible(true);
                this.setVisible(false);

            } else if (canSelect && canInsert && canUpdate && !canDelete) {
                // Teacher UI → pass teacherId (userId)
                ColesEnrollmentSystem.db = database;
                ColesEnrollmentSystem.uname = username;
                ColesEnrollmentSystem.pswd = password;
                
                GradesForm form = new GradesForm();
                form.teachid = Integer.parseInt(username.substring(0, 4));
                form.setLocationRelativeTo(null);
                form.setVisible(true);
                this.setVisible(false);

            } else if (canSelect && !canInsert && !canUpdate && !canDelete) {
                // Student UI → pass studentId (userId)
                this.setVisible(false);
                
                Reports report = new Reports();
                try {
                    id = Integer.parseInt(username.substring(0, 4));
                    report.createReportPDF(id);
                } catch (Exception e) {
                    System.out.println("Failed to Create PDF: " + e);
                }
            } else {
                JOptionPane.showMessageDialog(this,
                        "User does not have a recognized privilege set.",
                        "Access Denied",
                        JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Failed to connect or launch form");
            System.out.println("Submit error: " + ex);
        }                 
    }//GEN-LAST:event_submitbuttonActionPerformed

    private void passwordinputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordinputActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passwordinputActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(LoginForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new LoginForm().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JButton loginbutton;
    private javax.swing.JPasswordField passwordinput;
    private javax.swing.JButton submitbutton;
    private javax.swing.JComboBox<String> sycombobox;
    private javax.swing.JTextField usernameinput;
    // End of variables declaration//GEN-END:variables
}

