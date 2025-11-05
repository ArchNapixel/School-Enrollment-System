@echo off
REM Build and run the application with Maven
cd /d "%~dp0"
echo Building and running ColesEnrollmentSystem...
mvn clean compile exec:java -Dexec.mainClass=com.mycompany.colesenrollmentsystem.ColesEnrollmentSystem
pause
