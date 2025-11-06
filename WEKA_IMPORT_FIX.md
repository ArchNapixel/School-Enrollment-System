# Weka Import Issues - Solution Guide

## Problem
Getting compilation errors with Weka imports in Predict.java:
```java
import weka.classifiers.functions.LogisticRegression;
import weka.classifiers.trees.J48;
import weka.classifiers.bayes.NaiveBayes;
```

## Root Cause
Missing or incomplete Weka dependencies in `pom.xml`

## Solution

### Step 1: Update pom.xml Dependencies
Your pom.xml now includes:

```xml
<!-- Weka for Machine Learning -->
<dependency>
    <groupId>nz.ac.waikato.cms.weka</groupId>
    <artifactId>weka-stable</artifactId>
    <version>3.8.6</version>
</dependency>

<!-- Weka core classifiers (explicit) -->
<dependency>
    <groupId>nz.ac.waikato.cms.weka</groupId>
    <artifactId>weka-src</artifactId>
    <version>3.8.6</version>
</dependency>

<!-- Java cup for Weka -->
<dependency>
    <groupId>net.sf.java-cup</groupId>
    <artifactId>java-cup</artifactId>
    <version>11a</version>
</dependency>

<!-- JAMA for linear algebra (used by Weka) -->
<dependency>
    <groupId>gov.nist.math</groupId>
    <artifactId>jama</artifactId>
    <version>1.0.3</version>
</dependency>
```

### Step 2: Clean and Rebuild
In your IDE or terminal:

**For NetBeans:**
1. Right-click Project → Clean and Build

**For Maven via Terminal:**
```bash
cd "c:\Users\Arch Coles\Desktop\github\School-Enrollment-System\ColesEnrollmentSystemexer8\ColesEnrollmentSystem"
mvn clean install
```

**For IntelliJ IDEA:**
1. File → Invalidate Caches / Restart
2. Build → Rebuild Project

### Step 3: Verify Imports Work
The following imports should now be recognized:

```java
import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.LogisticRegression;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.classifiers.Evaluation;
```

## What Each Dependency Does

| Dependency | Purpose |
|-----------|---------|
| `weka-stable` | Main Weka library with all classifiers |
| `weka-src` | Additional source utilities and tools |
| `java-cup` | Parser generator (required by Weka) |
| `jama` | Linear algebra library (used by Weka classifiers) |

## Common Issues After Fix

### Issue 1: Still Not Recognizing Imports
**Solution:** 
- Delete the `models/` directory in your project folder
- Rebuild project: `mvn clean install`
- Refresh IDE project: F5 (NetBeans) or Ctrl+F5 (IntelliJ)

### Issue 2: Runtime Error - "NoClassDefFoundError"
**Solution:**
```bash
mvn dependency:resolve
mvn dependency:tree
```
Ensure all dependencies download successfully.

### Issue 3: Different IDE Than NetBeans
**For Eclipse:**
1. Right-click Project → Maven → Update Project (Alt+F5)

**For VS Code:**
1. Run: `Java: Configure Classpath`
2. Ensure pom.xml is correctly formatted

### Issue 4: Maven Not Finding Repository
**Solution:** Add to pom.xml:
```xml
<repositories>
    <repository>
        <id>central</id>
        <url>https://repo.maven.apache.org/maven2</url>
    </repository>
</repositories>
```

## Verify Installation
Create a test file to verify Weka works:

```java
// Test if Weka imports work
import weka.classifiers.trees.J48;
import weka.core.Instances;

public class WekaTest {
    public static void main(String[] args) {
        try {
            J48 tree = new J48();
            System.out.println("✓ Weka imports working correctly!");
        } catch (Exception e) {
            System.out.println("✗ Error: " + e.getMessage());
        }
    }
}
```

## Alternative: Use Pre-compiled JAR
If Maven issues persist:

1. Download `weka-stable-3.8.6.jar` from [Weka's website](https://www.cs.waikato.ac.nz/ml/weka/)
2. Add to your project's `lib/` folder
3. Add to classpath in your IDE

## IDE-Specific Quick Fix

**NetBeans:**
```
Tools → Plugins → Search "Weka" → Install any available plugins
```

**IntelliJ:**
```
File → Project Structure → Libraries → + → Maven
Search: nz.ac.waikato.cms.weka:weka-stable:3.8.6
```

## After Fixing
Run Maven build again:
```bash
mvn clean package
```

All Weka classifiers should now be available:
- ✓ `J48` (Decision Trees)
- ✓ `NaiveBayes` 
- ✓ `LogisticRegression`
- ✓ `Evaluation`
- ✓ `SerializationHelper`
