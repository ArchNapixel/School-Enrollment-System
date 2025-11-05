package com.mycompany.colesenrollmentsystem;

import static com.mycompany.colesenrollmentsystem.ColesEnrollmentSystem.con;
import static com.mycompany.colesenrollmentsystem.ColesEnrollmentSystem.db;
import static com.mycompany.colesenrollmentsystem.ColesEnrollmentSystem.pswd;
import static com.mycompany.colesenrollmentsystem.ColesEnrollmentSystem.st;
import static com.mycompany.colesenrollmentsystem.ColesEnrollmentSystem.uname;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Login extends javax.swing.JFrame {
    String username;
    String password;
    private boolean isLoggedIn = false;
    int id = 0;
    
    public Login() {
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

    private void usernameinputActionPerformed(java.awt.event.ActionEvent evt) {
        
    }

    private void loginbuttonActionPerformed(java.awt.event.ActionEvent evt) {
        username = usernameinput.getText().trim();
        password = new String(passwordinput.getPassword()).trim();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/?zeroDateTimeBehavior=CONVERT_TO_NULL", "root", "1234");
            st = con.createStatement();
            System.out.println("CONNECTED SUCCESSFULLY");

            isLoggedIn = true;
            submitbutton.setEnabled(true);
            sycombobox.setEnabled(true);

            ResultSet rs = st.executeQuery("SHOW DATABASES;");
            sycombobox.removeAllItems();

            ArrayList<String> databases = new ArrayList<>();
            while (rs.next()) {
                String dbName = rs.getString(1);
                if (!(dbName.equalsIgnoreCase("mysql")
                        || dbName.equalsIgnoreCase("performance_schema")
                        || dbName.equalsIgnoreCase("information_schema")
                        || dbName.equalsIgnoreCase("sys"))) {
                    databases.add(dbName);
                }
            }

            if (databases.isEmpty()) {
                JOptionPane.showMessageDialog(null, "No databases found. Please create a school year database first.");
            } else {
                for (String dbName : databases) {
                    sycombobox.addItem(dbName);
                }
            }

        } catch (Exception ex) {
            isLoggedIn = false;
            username = null;
            password = null;
            JOptionPane.showMessageDialog(null, "Invalid Username or Password: " + ex.getMessage());
            System.out.println("Invalid Login: " + ex);
            sycombobox.removeAllItems();
            submitbutton.setEnabled(false);
            sycombobox.setEnabled(false);
        }
    }

    private void submitbuttonActionPerformed(java.awt.event.ActionEvent evt) {
        if (!isLoggedIn) {
            JOptionPane.showMessageDialog(null, "Please Login First");
            return;
        }

        String database = (String) sycombobox.getSelectedItem();
        if (database == null || database.trim().isEmpty() || database.equals("School Year")) {
            JOptionPane.showMessageDialog(null, "Please select a valid database");
            return;
        }

        try {
            ColesEnrollmentSystem.db = database;
            ColesEnrollmentSystem.uname = "root";
            ColesEnrollmentSystem.pswd = "1234";
            ColesEnrollmentSystem.DBConnect();

            StudentsForm studForm = new StudentsForm();
            studForm.setVisible(true);
            this.setVisible(false);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Failed to launch StudentsForm: " + ex.getMessage());
            System.out.println("Submit error: " + ex);
        }                 
    }

    private void passwordinputActionPerformed(java.awt.event.ActionEvent evt) {
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }
    
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JButton loginbutton;
    private javax.swing.JPasswordField passwordinput;
    private javax.swing.JButton submitbutton;
    private javax.swing.JComboBox<String> sycombobox;
    private javax.swing.JTextField usernameinput;
}
