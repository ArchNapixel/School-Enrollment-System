/*
 * Training Data Collection Class
 * Responsible for extracting data from selected database for AI training
 */
package com.mycompany.colesenrollmentsystem;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import java.sql.ResultSet;
import java.util.ArrayList;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.core.SerializationHelper;

/**
 * Training class - Collects and prepares data from database for machine learning
 * @author Arch Coles
 */
public class Training extends ColesEnrollmentSystem {
    private Instances trainingData;
    private String selectedDatabase;
    private Classifier classifier;
    private String modelFilePath;
    
    /**
     * Initialize training with selected database
     */
    public Training(String database) {
        this.selectedDatabase = database;
        this.trainingData = null;
        this.classifier = new NaiveBayes();
        this.modelFilePath = "models/" + database + "_enrollment_model.model";
        DBConnect();
    }
    
    /**
     * Collect student enrollment data from database
     * Features: YearLevel, Course, GPA (estimated from grades), Enrollment Status
     */
    public Instances collectEnrollmentData() {
        try {
            // Create attributes for the dataset
            ArrayList<Attribute> attributes = new ArrayList<>();
            
            // Numeric attributes
            attributes.add(new Attribute("studentid"));
            attributes.add(new Attribute("yearLevel"));
            attributes.add(new Attribute("subjectsEnrolled"));
            attributes.add(new Attribute("averageGrade"));
            
            // Nominal (categorical) attributes
            ArrayList<String> courseValues = new ArrayList<>();
            courseValues.add("Computer Science");
            courseValues.add("Information Technology");
            courseValues.add("Software Engineering");
            courseValues.add("Other");
            attributes.add(new Attribute("course", courseValues));
            
            ArrayList<String> enrollmentStatus = new ArrayList<>();
            enrollmentStatus.add("Active");
            enrollmentStatus.add("Inactive");
            enrollmentStatus.add("Graduated");
            attributes.add(new Attribute("enrollmentStatus", enrollmentStatus));
            
            // Create dataset
            Instances dataset = new Instances("EnrollmentData", attributes, 0);
            
            // Query to get student data with grades
            String query = "SELECT s.studentid, s.YearLevel, s.Course, " +
                    "COUNT(e.eid) as enrolledSubjects, " +
                    "AVG(CAST(g.final AS DECIMAL(5,2))) as avgGrade " +
                    "FROM StudentsTable s " +
                    "LEFT JOIN Enroll e ON s.studentid = e.studid " +
                    "LEFT JOIN Grades g ON e.eid = g.eid " +
                    "GROUP BY s.studentid, s.YearLevel, s.Course";
            
            rs = st.executeQuery(query);
            
            while (rs.next()) {
                double[] values = new double[dataset.numAttributes()];
                
                values[0] = rs.getDouble("studentid");
                
                // Convert year level to numeric (1st=1, 2nd=2, 3rd=3)
                String yearLevel = rs.getString("YearLevel");
                values[1] = yearLevel != null && yearLevel.contains("1") ? 1 : 
                           yearLevel != null && yearLevel.contains("2") ? 2 : 3;
                
                values[2] = rs.getDouble("enrolledSubjects");
                
                double avgGrade = rs.getDouble("avgGrade");
                values[3] = (rs.wasNull() ? 0 : avgGrade);
                
                // Course attribute
                String course = rs.getString("Course");
                values[4] = dataset.attribute("course").indexOfValue(
                    course != null && courseValues.contains(course) ? course : "Other"
                );
                
                // Enrollment status based on average grade
                double grade = values[3];
                String status = grade >= 75 ? "Active" : grade > 0 ? "Inactive" : "Graduated";
                values[5] = dataset.attribute("enrollmentStatus").indexOfValue(status);
                
                dataset.add(new DenseInstance(1.0, values));
            }
            
            this.trainingData = dataset;
            System.out.println("Training data collected: " + dataset.numInstances() + " records");
            return dataset;
            
        } catch (Exception ex) {
            System.out.println("Error collecting training data: " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }
    }
    
    /**
     * Get the collected training data
     */
    public Instances getTrainingData() {
        return this.trainingData;
    }
    
    /**
     * Get the selected database name
     */
    public String getSelectedDatabase() {
        return this.selectedDatabase;
    }
    
    /**
     * Train the AI classifier on the collected data
     */
    public void trainClassifier() {
        try {
            if (trainingData == null || trainingData.numInstances() == 0) {
                System.out.println("Error: No training data available");
                return;
            }
            
            // Set class attribute (last attribute)
            trainingData.setClassIndex(trainingData.numAttributes() - 1);
            
            System.out.println("Training classifier on " + trainingData.numInstances() + " instances...");
            classifier.buildClassifier(trainingData);
            System.out.println("Classifier training completed successfully!");
            
        } catch (Exception ex) {
            System.out.println("Error training classifier: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    /**
     * Save the trained model to disk
     */
    public void saveModel() {
        try {
            // Create models directory if it doesn't exist
            java.io.File dir = new java.io.File("models");
            if (!dir.exists()) {
                dir.mkdir();
            }
            
            SerializationHelper.write(modelFilePath, classifier);
            System.out.println("Model saved to: " + modelFilePath);
            
            // Also save the training data as ARFF file
            saveTrainingDataAsARFF();
        } catch (Exception ex) {
            System.out.println("Error saving model: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    /**
     * Save training data as ARFF (Attribute-Relation File Format) for Weka
     */
    public void saveTrainingDataAsARFF() {
        try {
            String arffFilePath = "models/" + selectedDatabase + "_enrollment.arff";
            java.io.BufferedWriter writer = new java.io.BufferedWriter(
                new java.io.FileWriter(arffFilePath));
            
            writer.write(trainingData.toString());
            writer.close();
            
            System.out.println("Training data saved as ARFF to: " + arffFilePath);
        } catch (Exception ex) {
            System.out.println("Error saving ARFF file: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    /**
     * Load a previously trained model from disk
     */
    public void loadModel() {
        try {
            classifier = (Classifier) SerializationHelper.read(modelFilePath);
            System.out.println("Model loaded from: " + modelFilePath);
        } catch (Exception ex) {
            System.out.println("Error loading model: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public Classifier getClassifier() {
        return this.classifier;
    }
}

