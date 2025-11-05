/*
 * AI Model Training and Prediction Class
 * Responsible for training models and making predictions using Weka
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
    
    /**
     * Initialize predictor with training data
     */
    public Predict(Instances data, String database) {
        this.trainingData = data;
        this.selectedDatabase = database;
        this.decisionTreeModel = null;
        this.naiveBayesModel = null;
        this.logisticRegressionModel = null;
        
        // Create models directory if it doesn't exist
        File dir = new File(modelDirectory);
        if (!dir.exists()) {
            dir.mkdir();
        }
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
}
