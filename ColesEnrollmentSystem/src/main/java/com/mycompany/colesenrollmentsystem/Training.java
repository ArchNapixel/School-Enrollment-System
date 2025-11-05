/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.colesenrollmentsystem;

import java.io.File;
import java.sql.SQLException;
import java.util.Scanner;
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.SerializationHelper;

public class Training extends ColesEnrollmentSystem {
    private Classifier classifier;
    private Instances dataset;
    private static final String MODELS_DIR = "weka_models";
    
    public Training() {
        connectDB();
        new File(MODELS_DIR).mkdirs();
    }
    
    public void connectDB() {
        DBConnect();
    }
    
    public boolean trainModel(String databaseName) {
        try {
            // Switch to selected database
            st.executeUpdate("USE " + databaseName);
            System.out.println("Connected to database: " + databaseName);
            
            // Create dataset from enrollment data
            dataset = createDataset();
            
            if (dataset == null || dataset.numInstances() == 0) {
                System.out.println("ERROR: No data available for training");
                return false;
            }
            
            // Set class attribute (last attribute)
            dataset.setClassIndex(dataset.numAttributes() - 1);
            
            // Initialize and train classifier (Decision Tree - J48)
            classifier = new J48();
            classifier.buildClassifier(dataset);
            
            // Save the trained model
            String modelPath = MODELS_DIR + File.separator + "model_" + databaseName + ".model";
            SerializationHelper.write(modelPath, classifier);
            
            System.out.println("Model trained and saved successfully at: " + modelPath);
            System.out.println("This model predicts recommended subjects for students in the semester");
            return true;
            
        } catch (Exception ex) {
            System.out.println("ERROR training model: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
    
    private Instances createDataset() {
        try {
            // Query to get student enrollment and subject information
            String query = "SELECT e.studid, s.YearLevel, s.Course, e.subjid, sub.subjcode, sub.subjdescription, COUNT(e.subjid) OVER (PARTITION BY e.studid) as StudentSubjectCount " +
                          "FROM Enroll e " +
                          "LEFT JOIN StudentsTable s ON e.studid = s.studentid " +
                          "LEFT JOIN SubjectsTable sub ON e.subjid = sub.subjid " +
                          "WHERE sub.subjcode IS NOT NULL " +
                          "ORDER BY e.studid, e.subjid";
            
            rs = st.executeQuery(query);
            
            // Collect all unique subjects first
            java.util.Set<String> uniqueSubjects = new java.util.LinkedHashSet<>();
            java.util.Map<String, Integer> yearCourseSubjectCount = new java.util.HashMap<>();
            
            // First pass: collect subjects and patterns
            rs = st.executeQuery(query);
            while (rs.next()) {
                String subjCode = rs.getString("subjcode");
                String yearLevel = rs.getString("YearLevel");
                String course = rs.getString("Course");
                
                if (subjCode != null && !subjCode.trim().isEmpty()) {
                    uniqueSubjects.add(subjCode);
                    
                    // Track which subjects are taken by which year/course combination
                    String key = yearLevel + "_" + course + "_" + subjCode;
                    yearCourseSubjectCount.put(key, yearCourseSubjectCount.getOrDefault(key, 0) + 1);
                }
            }
            
            // If no subjects found, create a default dataset
            if (uniqueSubjects.isEmpty()) {
                uniqueSubjects.add("CS101");
                uniqueSubjects.add("MATH101");
                uniqueSubjects.add("ENG101");
            }
            
            java.util.ArrayList<String> subjectList = new java.util.ArrayList<>(uniqueSubjects);
            
            // Create attributes for student profile
            Attribute yearLevel = new Attribute("YearLevel");
            Attribute course = new Attribute("Course");
            Attribute numSubjectsEnrolled = new Attribute("NumSubjectsEnrolled");
            
            // Create class attribute - the recommended subject
            Attribute recommendedSubject = new Attribute("RecommendedSubject", subjectList);
            
            // Create attribute list
            java.util.ArrayList<Attribute> attributes = new java.util.ArrayList<>();
            attributes.add(yearLevel);
            attributes.add(course);
            attributes.add(numSubjectsEnrolled);
            attributes.add(recommendedSubject);
            
            // Create instances
            Instances instances = new Instances("SubjectRecommendationData", attributes, 0);
            
            // Populate instances from query results
            java.util.Map<String, Integer> studentSubjectCount = new java.util.HashMap<>();
            rs = st.executeQuery(query);
            
            while (rs.next()) {
                String studentId = rs.getString("studid");
                String yearLvl = rs.getString("YearLevel");
                String studentCourse = rs.getString("Course");
                String subjCode = rs.getString("subjcode");
                
                if (yearLvl != null && studentCourse != null && subjCode != null && !subjCode.trim().isEmpty()) {
                    // Count subjects per student
                    studentSubjectCount.put(studentId, studentSubjectCount.getOrDefault(studentId, 0) + 1);
                    
                    double[] values = new double[4];
                    values[0] = instances.attribute(0).addStringValue(yearLvl);
                    values[1] = instances.attribute(1).addStringValue(studentCourse);
                    values[2] = studentSubjectCount.getOrDefault(studentId, 0);
                    values[3] = instances.attribute(3).indexOfValue(subjCode);
                    
                    instances.add(new DenseInstance(1.0, values));
                }
            }
            
            System.out.println("Dataset created with " + instances.numInstances() + " instances");
            System.out.println("Predicting recommended subjects from: " + subjectList.size() + " available subjects");
            System.out.println("Available subjects: " + subjectList);
            return instances;
            
        } catch (SQLException ex) {
            System.out.println("ERROR creating dataset: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
    
    public Classifier getClassifier() {
        return classifier;
    }
    
    public Instances getDataset() {
        return dataset;
    }
    
    public String getModelInfo() {
        if (classifier == null) {
            return "No model trained yet";
        }
        return classifier.toString();
    }
}
