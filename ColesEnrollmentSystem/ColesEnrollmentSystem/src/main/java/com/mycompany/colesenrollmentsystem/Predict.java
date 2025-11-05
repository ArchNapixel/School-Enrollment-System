/*
 * AI Model Training and Prediction Class
 * Responsible for training models and making predictions using Weka
 * Also handles subject recommendations for student enrollment
 */
package com.mycompany.colesenrollmentsystem;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LogisticRegression;
import weka.core.Instances;
import weka.core.SerializationHelper;
import javax.swing.JOptionPane;
import java.io.File;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Predict class - Trains AI models and makes predictions
 * Uses multiple classifiers: Decision Tree (J48), Naive Bayes, Logistic Regression
 * @author Arch Coles
 */
public class Predict {
    private Classifier decisionTreeModel;
    private Classifier naiveBayesModel;
    private Classifier logisticRegressionModel;
    private Instances trainingData;
    private String modelDirectory = "models/";
    private String selectedDatabase;
    private Classifier trainedModel;
    
    /**
     * Initialize predictor with training data
     */
    public Predict(Instances data, String database) {
        this.trainingData = data;
        this.selectedDatabase = database;
        this.decisionTreeModel = null;
        this.naiveBayesModel = null;
        this.logisticRegressionModel = null;
        this.trainedModel = null;
        
        // Create models directory if it doesn't exist
        File dir = new File(modelDirectory);
        if (!dir.exists()) {
            dir.mkdir();
        }
    }
    
    /**
     * Empty constructor for loading pre-trained models
     */
    public Predict() {
        this(null, "");
    }
    
    /**
     * Train all models using the provided training data
     */
    public void trainModels() {
        if (trainingData == null || trainingData.numInstances() == 0) {
            JOptionPane.showMessageDialog(null, "No training data available!");
            return;
        }
        
        try {
            // Set class attribute (last attribute - enrollment status)
            trainingData.setClassIndex(trainingData.numAttributes() - 1);
            
            System.out.println("Training models with " + trainingData.numInstances() + " instances...");
            
            // Train Decision Tree (J48)
            System.out.println("Training Decision Tree (J48)...");
            decisionTreeModel = new J48();
            decisionTreeModel.buildClassifier(trainingData);
            System.out.println("Decision Tree trained successfully");
            
            // Train Naive Bayes
            System.out.println("Training Naive Bayes...");
            naiveBayesModel = new NaiveBayes();
            naiveBayesModel.buildClassifier(trainingData);
            System.out.println("Naive Bayes trained successfully");
            
            // Train Logistic Regression
            System.out.println("Training Logistic Regression...");
            logisticRegressionModel = new LogisticRegression();
            logisticRegressionModel.buildClassifier(trainingData);
            System.out.println("Logistic Regression trained successfully");
            
            // Save models
            saveModels();
            
            JOptionPane.showMessageDialog(null, 
                "All models trained successfully!\n" +
                "Database: " + selectedDatabase + "\n" +
                "Training instances: " + trainingData.numInstances(),
                "Training Complete", JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception ex) {
            System.out.println("Error training models: " + ex.getMessage());
            ex.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error during training: " + ex.getMessage(), 
                "Training Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Save trained models to disk
     */
    private void saveModels() {
        try {
            SerializationHelper.write(modelDirectory + "decisionTree_" + selectedDatabase + ".model", decisionTreeModel);
            SerializationHelper.write(modelDirectory + "naiveBayes_" + selectedDatabase + ".model", naiveBayesModel);
            SerializationHelper.write(modelDirectory + "logisticRegression_" + selectedDatabase + ".model", logisticRegressionModel);
            System.out.println("Models saved successfully");
        } catch (Exception ex) {
            System.out.println("Error saving models: " + ex.getMessage());
        }
    }
    
    /**
     * Load previously trained models from disk
     */
    public boolean loadModels() {
        try {
            File dt = new File(modelDirectory + "decisionTree_" + selectedDatabase + ".model");
            File nb = new File(modelDirectory + "naiveBayes_" + selectedDatabase + ".model");
            File lr = new File(modelDirectory + "logisticRegression_" + selectedDatabase + ".model");
            
            if (dt.exists() && nb.exists() && lr.exists()) {
                decisionTreeModel = (Classifier) SerializationHelper.read(dt.getAbsolutePath());
                naiveBayesModel = (Classifier) SerializationHelper.read(nb.getAbsolutePath());
                logisticRegressionModel = (Classifier) SerializationHelper.read(lr.getAbsolutePath());
                System.out.println("Models loaded successfully");
                return true;
            }
        } catch (Exception ex) {
            System.out.println("Error loading models: " + ex.getMessage());
        }
        return false;
    }
    
    /**
     * Load trained model from a specific database
     */
    public boolean loadTrainedModel(String database) {
        try {
            String modelPath = modelDirectory + database + "_enrollment_model.model";
            File modelFile = new File(modelPath);
            
            if (modelFile.exists()) {
                trainedModel = (Classifier) SerializationHelper.read(modelPath);
                System.out.println("Trained model loaded from: " + modelPath);
                return true;
            } else {
                System.out.println("Model file not found: " + modelPath);
                return false;
            }
        } catch (Exception ex) {
            System.out.println("Error loading trained model: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        }
    }
    
    /**
     * Load trained model (uses last trained database)
     */
    public boolean loadTrainedModel() {
        if (selectedDatabase == null || selectedDatabase.isEmpty()) {
            System.out.println("No database selected for model loading");
            return false;
        }
        return loadTrainedModel(selectedDatabase);
    }
    
    /**
     * Make prediction using Decision Tree
     */
    public String predictWithDecisionTree(double[] instance) throws Exception {
        if (decisionTreeModel == null) throw new Exception("Decision Tree model not trained");
        double prediction = decisionTreeModel.classifyInstance(new weka.core.DenseInstance(1.0, instance));
        return trainingData.classAttribute().value((int) prediction);
    }
    
    /**
     * Make prediction using Naive Bayes
     */
    public String predictWithNaiveBayes(double[] instance) throws Exception {
        if (naiveBayesModel == null) throw new Exception("Naive Bayes model not trained");
        double prediction = naiveBayesModel.classifyInstance(new weka.core.DenseInstance(1.0, instance));
        return trainingData.classAttribute().value((int) prediction);
    }
    
    /**
     * Make prediction using Logistic Regression
     */
    public String predictWithLogisticRegression(double[] instance) throws Exception {
        if (logisticRegressionModel == null) throw new Exception("Logistic Regression model not trained");
        double prediction = logisticRegressionModel.classifyInstance(new weka.core.DenseInstance(1.0, instance));
        return trainingData.classAttribute().value((int) prediction);
    }
    
    /**
     * Get model accuracy on training data
     */
    public double getModelAccuracy(Classifier model) throws Exception {
        if (model == null) return 0.0;
        
        weka.classifiers.Evaluation eval = new weka.classifiers.Evaluation(trainingData);
        eval.evaluateModel(model, trainingData);
        return eval.pctCorrect();
    }
    
    /**
     * Get all model accuracies
     */
    public String getModelAccuracies() {
        try {
            double dtAccuracy = getModelAccuracy(decisionTreeModel);
            double nbAccuracy = getModelAccuracy(naiveBayesModel);
            double lrAccuracy = getModelAccuracy(logisticRegressionModel);
            
            return "Decision Tree: " + String.format("%.2f", dtAccuracy) + "%\n" +
                   "Naive Bayes: " + String.format("%.2f", nbAccuracy) + "%\n" +
                   "Logistic Regression: " + String.format("%.2f", lrAccuracy) + "%";
        } catch (Exception ex) {
            return "Error calculating accuracies: " + ex.getMessage();
        }
    }
    
    /**
     * Predict recommended subjects for a student based on their profile and enrollment patterns
     * @param studentID the student ID
     * @return List of recommended subject IDs
     */
    public List<Integer> predictSubjectsForStudent(int studentID) {
        List<Integer> recommendedSubjects = new ArrayList<>();
        
        try {
            ColesEnrollmentSystem system = new ColesEnrollmentSystem();
            system.DBConnect();
            
            // Get student info
            String studentQuery = "SELECT YearLevel, Course FROM StudentsTable WHERE studentid = ?";
            PreparedStatement ps = system.con.prepareStatement(studentQuery);
            ps.setInt(1, studentID);
            ResultSet rs = ps.executeQuery();
            
            if (!rs.next()) {
                System.out.println("Student not found");
                return recommendedSubjects;
            }
            
            String yearLevel = rs.getString("YearLevel");
            String course = rs.getString("Course");
            ps.close();
            
            // Get subjects already enrolled by this student
            String enrolledQuery = "SELECT subjid FROM Enroll WHERE studid = ?";
            ps = system.con.prepareStatement(enrolledQuery);
            ps.setInt(1, studentID);
            rs = ps.executeQuery();
            
            List<Integer> alreadyEnrolled = new ArrayList<>();
            while (rs.next()) {
                alreadyEnrolled.add(rs.getInt("subjid"));
            }
            ps.close();
            
            // Find subjects taken by students with same year level and course
            // who have higher grades (suggesting appropriate difficulty)
            String inClause = buildInClause(alreadyEnrolled);
            String recommendQuery = 
                "SELECT DISTINCT s.subjid, COUNT(*) as popularity " +
                "FROM SubjectsTable s " +
                "INNER JOIN Enroll e ON s.subjid = e.subjid " +
                "INNER JOIN Grades g ON e.eid = g.eid " +
                "INNER JOIN StudentsTable st ON e.studid = st.studentid " +
                "WHERE st.YearLevel = ? " +
                "AND st.Course = ? " +
                "AND CAST(g.final AS DECIMAL(5,2)) >= 75 " +
                "AND s.subjid NOT IN (" + inClause + ") " +
                "GROUP BY s.subjid " +
                "ORDER BY popularity DESC " +
                "LIMIT 5";
            
            ps = system.con.prepareStatement(recommendQuery);
            ps.setString(1, yearLevel);
            ps.setString(2, course);
            rs = ps.executeQuery();
            
            while (rs.next()) {
                recommendedSubjects.add(rs.getInt("subjid"));
            }
            ps.close();
            
            // If no recommendations found based on grades, get all available subjects
            // not yet enrolled by the student
            if (recommendedSubjects.isEmpty()) {
                String fallbackQuery = 
                    "SELECT subjid FROM SubjectsTable " +
                    "WHERE subjid NOT IN (" + inClause + ") " +
                    "LIMIT 5";
                
                ps = system.con.prepareStatement(fallbackQuery);
                rs = ps.executeQuery();
                
                while (rs.next()) {
                    recommendedSubjects.add(rs.getInt("subjid"));
                }
                ps.close();
            }
            
        } catch (Exception ex) {
            System.out.println("Error predicting subjects: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        return recommendedSubjects;
    }
    
    /**
     * Get detailed subject information for recommended subjects
     * @param subjectIDs list of subject IDs
     * @return Map of subject ID to subject details
     */
    public Map<Integer, SubjectDetail> getSubjectDetails(List<Integer> subjectIDs) {
        Map<Integer, SubjectDetail> subjectMap = new HashMap<>();
        
        if (subjectIDs.isEmpty()) {
            return subjectMap;
        }
        
        try {
            ColesEnrollmentSystem system = new ColesEnrollmentSystem();
            system.DBConnect();
            
            String inClause = buildInClause(subjectIDs);
            String query = "SELECT subjid, subjcode, subjdescription, subjschedule, subjunit " +
                          "FROM SubjectsTable WHERE subjid IN (" + inClause + ")";
            
            PreparedStatement ps = system.con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                int subjid = rs.getInt("subjid");
                SubjectDetail info = new SubjectDetail(
                    subjid,
                    rs.getString("subjcode"),
                    rs.getString("subjdescription"),
                    rs.getString("subjschedule"),
                    rs.getInt("subjunit")
                );
                subjectMap.put(subjid, info);
            }
            ps.close();
            
        } catch (Exception ex) {
            System.out.println("Error getting subject details: " + ex.getMessage());
        }
        
        return subjectMap;
    }
    
    /**
     * Helper method to build IN clause for SQL queries
     */
    private String buildInClause(List<Integer> ids) {
        if (ids.isEmpty()) {
            return "NULL";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ids.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(ids.get(i));
        }
        return sb.toString();
    }
    
    /**
     * Inner class to hold subject information
     */
    public static class SubjectDetail {
        public int subjid;
        public String subjcode;
        public String subjdescription;
        public String subjschedule;
        public int subjunit;
        
        public SubjectDetail(int subjid, String subjcode, String subjdescription, 
                            String subjschedule, int subjunit) {
            this.subjid = subjid;
            this.subjcode = subjcode;
            this.subjdescription = subjdescription;
            this.subjschedule = subjschedule;
            this.subjunit = subjunit;
        }
        
        @Override
        public String toString() {
            return subjcode + " - " + subjdescription + " (" + subjunit + " units)";
        }
    }
    
    /**
     * Set reference dataset for predictions
     */
    public void setReferenceDataset(Instances dataset) {
        this.trainingData = dataset;
    }
    
    /**
     * Predict subjects for a student using the trained model
     */
    public ArrayList<String> predictStudentSubjects(int studentID) {
        ArrayList<String> predictedSubjects = new ArrayList<>();
        
        try {
            if (trainedModel == null) {
                System.out.println("Error: Trained model not loaded");
                return predictedSubjects;
            }
            
            // Use the predictSubjectsForStudent method that uses database queries
            List<Integer> subjectIDs = predictSubjectsForStudent(studentID);
            Map<Integer, SubjectDetail> details = getSubjectDetails(subjectIDs);
            
            for (Integer subjid : subjectIDs) {
                SubjectDetail detail = details.get(subjid);
                if (detail != null) {
                    predictedSubjects.add(detail.toString());
                }
            }
        } catch (Exception ex) {
            System.out.println("Error predicting subjects: " + ex.getMessage());
            ex.printStackTrace();
        }
        
        return predictedSubjects;
    }
}
