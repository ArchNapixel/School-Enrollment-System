/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.colesenrollmentsystem;

import weka.classifiers.Classifier;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import java.io.File;

public class Predict extends ColesEnrollmentSystem {
    private Classifier classifier;
    private Instances dataset;
    private String modelPath;
    private static final String MODELS_DIR = "weka_models";
    
    public Predict() {
        connectDB();
    }
    
    public void connectDB() {
        DBConnect();
    }
    
    public boolean loadModel(String databaseName) {
        try {
            modelPath = MODELS_DIR + File.separator + "model_" + databaseName + ".model";
            
            if (!new File(modelPath).exists()) {
                System.out.println("ERROR: Model file not found at " + modelPath);
                System.out.println("Please train a model first for database: " + databaseName);
                return false;
            }
            
            classifier = (Classifier) SerializationHelper.read(modelPath);
            System.out.println("Model loaded successfully from: " + modelPath);
            return true;
            
        } catch (Exception ex) {
            System.out.println("ERROR loading model: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
    
    public String predictRecommendedSubject(String yearLevel, String course, double numSubjectsEnrolled) {
        try {
            if (classifier == null) {
                System.out.println("WARNING: Classifier not loaded, using default recommendation");
                return getDefaultSubjectRecommendation(yearLevel, course);
            }
            
            // Query database for available subjects
            String subjectQuery = "SELECT DISTINCT subjcode FROM SubjectsTable WHERE subjcode IS NOT NULL LIMIT 10";
            java.sql.ResultSet subjRs = ColesEnrollmentSystem.st.executeQuery(subjectQuery);
            
            java.util.ArrayList<String> subjectValues = new java.util.ArrayList<>();
            while (subjRs.next()) {
                String code = subjRs.getString("subjcode");
                if (code != null && !code.trim().isEmpty()) {
                    subjectValues.add(code);
                }
            }
            
            // Add default subjects if none found
            if (subjectValues.isEmpty()) {
                subjectValues.add("CS101");
                subjectValues.add("MATH101");
                subjectValues.add("ENG101");
            }
            
            // Create attributes (must match training data)
            Attribute yearLevelAttr = new Attribute("YearLevel");
            Attribute courseAttr = new Attribute("Course");
            Attribute numSubjectsAttr = new Attribute("NumSubjectsEnrolled");
            Attribute recommendedSubjectAttr = new Attribute("RecommendedSubject", subjectValues);
            
            // Create attribute list
            java.util.ArrayList<Attribute> attributes = new java.util.ArrayList<>();
            attributes.add(yearLevelAttr);
            attributes.add(courseAttr);
            attributes.add(numSubjectsAttr);
            attributes.add(recommendedSubjectAttr);
            
            // Create instances
            Instances predictionInstances = new Instances("SubjectPrediction", attributes, 0);
            predictionInstances.setClassIndex(predictionInstances.numAttributes() - 1);
            
            // Create instance for prediction
            double[] values = new double[4];
            values[0] = predictionInstances.attribute(0).addStringValue(yearLevel);
            values[1] = predictionInstances.attribute(1).addStringValue(course);
            values[2] = numSubjectsEnrolled;
            values[3] = 0; // placeholder for class
            
            DenseInstance predInstance = new DenseInstance(1.0, values);
            predictionInstances.add(predInstance);
            
            // Get prediction
            double prediction = classifier.classifyInstance(predictionInstances.firstInstance());
            int predictionIndex = (int) prediction;
            
            if (predictionIndex >= 0 && predictionIndex < subjectValues.size()) {
                String result = subjectValues.get(predictionIndex);
                System.out.println("Subject prediction for " + yearLevel + " - " + course + ": " + result);
                return result;
            } else {
                return getDefaultSubjectRecommendation(yearLevel, course);
            }
            
        } catch (Exception ex) {
            System.out.println("ERROR making prediction: " + ex.getMessage());
            ex.printStackTrace();
            return getDefaultSubjectRecommendation(yearLevel, course);
        }
    }
    
    private String getDefaultSubjectRecommendation(String yearLevel, String course) {
        // Simple logic: recommend subjects based on year level
        if (yearLevel.contains("1") || yearLevel.contains("First")) {
            return "GEN101"; // General Education for first year
        } else if (yearLevel.contains("2") || yearLevel.contains("Second")) {
            return "CS201"; // Core subjects for second year
        } else if (yearLevel.contains("3") || yearLevel.contains("Third")) {
            return "CS301"; // Advanced subjects for third year
        } else {
            return "CS401"; // Specialized subjects for fourth year
        }
    }
    
    public double[] getPredictionProbabilities(String yearLevel, String course, double numSubjectsEnrolled) {
        try {
            if (classifier == null) {
                return new double[]{};
            }
            
            // Create attributes
            Attribute yearLevelAttr = new Attribute("YearLevel");
            Attribute courseAttr = new Attribute("Course");
            Attribute numSubjectsAttr = new Attribute("NumSubjectsEnrolled");
            
            java.util.ArrayList<String> subjectValues = new java.util.ArrayList<>();
            subjectValues.add("CS101");
            subjectValues.add("CS102");
            subjectValues.add("CS103");
            subjectValues.add("GEN101");
            Attribute recommendedSubjectAttr = new Attribute("RecommendedSubject", subjectValues);
            
            java.util.ArrayList<Attribute> attributes = new java.util.ArrayList<>();
            attributes.add(yearLevelAttr);
            attributes.add(courseAttr);
            attributes.add(numSubjectsAttr);
            attributes.add(recommendedSubjectAttr);
            
            Instances predictionInstances = new Instances("SubjectPrediction", attributes, 0);
            predictionInstances.setClassIndex(predictionInstances.numAttributes() - 1);
            
            double[] values = new double[4];
            values[0] = predictionInstances.attribute(0).addStringValue(yearLevel);
            values[1] = predictionInstances.attribute(1).addStringValue(course);
            values[2] = numSubjectsEnrolled;
            values[3] = 0;
            
            DenseInstance predInstance = new DenseInstance(1.0, values);
            predictionInstances.add(predInstance);
            
            return classifier.distributionForInstance(predictionInstances.firstInstance());
            
        } catch (Exception ex) {
            System.out.println("ERROR getting probabilities: " + ex.getMessage());
            return new double[]{};
        }
    }
    
    public String getModelPath() {
        return modelPath;
    }
}
