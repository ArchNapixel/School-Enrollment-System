package com.mycompany.colesenrollmentsystem;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import com.itextpdf.layout.properties.VerticalAlignment;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import static com.itextpdf.kernel.pdf.PdfName.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.properties.HorizontalAlignment;
import java.awt.Desktop;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Reports {

    public static void createReportPDF(int studId) throws Exception {
        PdfWriter writer = new PdfWriter("report.pdf");
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);

        PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont regularFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        addHeader(document, boldFont, regularFont);

        Paragraph sheetTitle = new Paragraph("Student Grade Sheet")
                .setFont(boldFont)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(30);
        document.add(sheetTitle);

        addReportStudentInfo(document, regularFont, studId);

        addGradesTable(document, boldFont, regularFont, studId);

        addSignature(document, regularFont);

        document.close();

        try {
            File pdfFile = new File("report.pdf");
            if (pdfFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    System.out.println("AWT Desktop is not supported!");
                }
            } else {
                System.out.println("File does not exist.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private static void addHeader(Document document, PdfFont boldFont, PdfFont regularFont) {

        Table headerTable = new Table(UnitValue.createPercentArray(new float[]{35f, 30f, 35f}));
        headerTable.setWidth(UnitValue.createPercentValue(100));

        Cell logoCell = new Cell();
        logoCell.setBorder(null);
        logoCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        logoCell.setTextAlignment(TextAlignment.RIGHT);

        Cell textCell = new Cell();
        textCell.setBorder(null);
        textCell.setVerticalAlignment(VerticalAlignment.TOP);
        textCell.setTextAlignment(TextAlignment.CENTER);

        Paragraph collegeTitle = new Paragraph("Nailong University")
                .setFont(regularFont)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(0);

        Paragraph registrarsOffice = new Paragraph("Registrars Office")
                .setFont(regularFont)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(10);

        Image logo = null;
        try {
            ImageData imageData = ImageDataFactory.create("logo.png");
            logo = new Image(imageData).scaleToFit(100, 100);
            logo.setHorizontalAlignment(HorizontalAlignment.RIGHT);
            logoCell.add(logo);
        } catch (Exception e) {
            System.out.println("failed to load image: " + e);
        }

        textCell.add(collegeTitle);
        textCell.add(registrarsOffice);

        headerTable.addCell(logoCell);
        headerTable.addCell(textCell);
        headerTable.addCell(new Cell().setBorder(null));

        document.add(headerTable);
    }

    private static void addReportStudentInfo(Document document, PdfFont font, int studId) {
        ColesEnrollmentSystem system = new ColesEnrollmentSystem();

        String id = "" + studId;
        String studName = "";
        String studYr = "";
        String schoolYr = system.db;
        String studCourse = "";

        try {
            system.rs = system.st.executeQuery("Select * from StudentsTable where studentid=" + studId);
            if (system.rs.next()) {
                studName = system.rs.getString("Name");
                studYr = system.rs.getString("Yearlevel");
                studCourse = system.rs.getString("Course");
            }
        } catch (Exception e) {
            System.out.println("Failed to load student info: " + e);
        }

        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{50, 50}));
        infoTable.setWidth(UnitValue.createPercentValue(100));
        infoTable.setMarginBottom(20);

        Cell leftCell = new Cell()
                .setBorder(null)
                .setVerticalAlignment(VerticalAlignment.TOP);

        leftCell.add(new Paragraph("Student ID: " + id).setFont(font).setFontSize(10).setBold());
        leftCell.add(new Paragraph("Student Name: " + studName).setFont(font).setFontSize(10).setBold());
        leftCell.add(new Paragraph("Student Year: " + studYr).setFont(font).setFontSize(10).setBold());

        Cell rightCell = new Cell()
                .setBorder(null)
                .setVerticalAlignment(VerticalAlignment.TOP);

        rightCell.add(new Paragraph("School Year: " + schoolYr).setFont(font).setFontSize(10).setBold());
        rightCell.add(new Paragraph("Student Course: " + studCourse).setFont(font).setFontSize(10).setBold());

        infoTable.addCell(leftCell);
        infoTable.addCell(rightCell);

        document.add(infoTable);
    }

    private static void addGradesTable(Document document, PdfFont boldFont, PdfFont regularFont, int studId) {

        ColesEnrollmentSystem system = new ColesEnrollmentSystem();

        ArrayList<String[]> gradesData = new ArrayList<>();
        try {
            String query = "SELECT s.subjid, s.subjcode, g.prelim, g.midterm, g.prefinal, g.final "
                    + "FROM Enroll e "
                    + "INNER JOIN SubjectsTable s "
                    + "ON e.subjid = s.subjid "
                    + "LEFT JOIN Grades g ON e.eid = g.eid WHERE e.studid = " + studId;
            system.rs = system.st.executeQuery(query);

            while (system.rs.next()) {
                String subId = system.rs.getString("subjid");
                String subCode = system.rs.getString("subjcode");
                String prelim = system.rs.getString("prelim");
                String midterm = system.rs.getString("midterm");
                String prefinal = system.rs.getString("prefinal");
                String fin = system.rs.getString("final");

                if (prelim == null) {
                    prelim = "";
                }
                if (midterm == null) {
                    midterm = "";
                }
                if (prefinal == null) {
                    prefinal = "";
                }
                if (fin == null) {
                    fin = "";
                }

                String[] info = {subId, subCode, prelim, midterm, prefinal, fin};
                gradesData.add(info);
            }

        } catch (Exception e) {
            System.out.println("Failed to load subjects: " + e);
        }

        float[] columnWidths = {15f, 35f, 12.5f, 12.5f, 12.5f, 12.5f};
        Table gradesTable = new Table(UnitValue.createPercentArray(columnWidths));
        gradesTable.setWidth(UnitValue.createPercentValue(100));
        gradesTable.setMarginBottom(20);

        Table gradesTitleTable = new Table(UnitValue.createPercentArray(columnWidths));
        gradesTitleTable.setWidth(UnitValue.createPercentValue(100));
        gradesTitleTable.setMarginBottom(20);

        String[] headers = {"SubjID", "Subject Code", "Prelim", "Midterm", "Prefinal", "Final"};
        for (String header : headers) {
            Cell headerCell = new Cell()
                    .add(new Paragraph(header).setFont(boldFont).setFontSize(10))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(5);
            gradesTitleTable.addHeaderCell(headerCell);
        }

        for (String[] gradeRow : gradesData) {
            for (String cellData : gradeRow) {
                Cell dataCell = new Cell()
                        .add(new Paragraph(cellData).setFont(regularFont).setFontSize(9))
                        .setTextAlignment(TextAlignment.CENTER)
                        .setPadding(5);
                gradesTable.addCell(dataCell);
            }
        }

        document.add(gradesTitleTable);
        document.add(gradesTable);

        Paragraph subjectCount = new Paragraph("Number of Subjects Listed: " + gradesData.size())
                .setBold()
                .setFont(regularFont)
                .setFontSize(10)
                .setMarginBottom(40);
        document.add(subjectCount);
    }

    private static void addSignature(Document document, PdfFont font) {

        Table signTable = new Table(UnitValue.createPercentArray(new float[]{35f, 35f, 30f}));
        signTable.setWidth(UnitValue.createPercentValue(100));

        Cell blankCell1 = new Cell();
        blankCell1.setBorder(null);

        Cell blankCell2 = new Cell();
        blankCell2.setBorder(null);

        Cell signCell = new Cell();
        signCell.setBorder(null);
        signCell.setVerticalAlignment(VerticalAlignment.MIDDLE);
        signCell.setTextAlignment(TextAlignment.CENTER);

        Paragraph signatureLine = new Paragraph("_________________________")
                .setFont(font)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginTop(50)
                .setMarginBottom(5);
        signCell.add(signatureLine);

        Paragraph registrarText = new Paragraph("Registrar")
                .setBold()
                .setFont(font)
                .setFontSize(10)
                .setTextAlignment(TextAlignment.CENTER);
        signCell.add(registrarText);

        signTable.addCell(blankCell1);
        signTable.addCell(blankCell2);
        signTable.addCell(signCell);

        document.add(signTable);
    }

    static int pdfCounter = 1;

    public void createTORPDF(int studId) throws Exception {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyddMM");
        String formattedDate = today.format(formatter);

        String name = studId + "_" + formattedDate + "_4123" + pdfCounter + ".pdf";
        PdfWriter writer = new PdfWriter(name);
        pdfCounter++;
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf, PageSize.A4);

        // Create fonts
        PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
        PdfFont regularFont = PdfFontFactory.createFont(StandardFonts.HELVETICA);

        // Add header
        addHeader(document, boldFont, regularFont);

        Paragraph sheetTitle = new Paragraph("Official Transcripts of Records")
                .setFont(boldFont)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.CENTER)
                .setMarginBottom(30);

        document.add(sheetTitle);

        addTORStudentInfo(document, regularFont, studId);

        addTORStudentGrades(document, boldFont, regularFont, studId);

        addBottomTORPdf(document, boldFont, regularFont);

        document.close();

        try {
            File pdfFile = new File(name);
            if (pdfFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().open(pdfFile);
                } else {
                    System.out.println("AWT Desktop is not supported!");
                }
            } else {
                System.out.println("File does not exist.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String name;

    private static void addTORStudentInfo(Document document, PdfFont font, int studId) {
        ColesEnrollmentSystem system = new ColesEnrollmentSystem();

        String id = "" + studId;
        String studName = "";
        String studGender = "";
        String studCourse = "";

        try {
            system.rs = system.st.executeQuery("Select * from StudentsTable where studentid=" + studId);
            if (system.rs.next()) {
                studName = system.rs.getString("Name");
                studGender = system.rs.getString("Gender");
                studCourse = system.rs.getString("Course");
            }
        } catch (Exception e) {
            System.out.println("Failed to load student info: " + e);
        }

        name = studName;

        // Create a 2-column table for student information
        Table infoTable = new Table(UnitValue.createPercentArray(new float[]{50, 50}));
        infoTable.setWidth(UnitValue.createPercentValue(100));
        infoTable.setMarginBottom(20);

        Cell leftCell = new Cell()
                .setBorder(null)
                .setVerticalAlignment(VerticalAlignment.TOP);

        leftCell.add(new Paragraph("Student ID: " + id).setFont(font).setFontSize(10).setBold());
        leftCell.add(new Paragraph("Student Course: " + studCourse).setFont(font).setFontSize(10).setBold());

        Cell rightCell = new Cell()
                .setBorder(null)
                .setVerticalAlignment(VerticalAlignment.TOP);

        rightCell.add(new Paragraph("School Name: " + studName).setFont(font).setFontSize(10).setBold());
        rightCell.add(new Paragraph("Gender: " + studGender).setFont(font).setFontSize(10).setBold());

        infoTable.addCell(leftCell);
        infoTable.addCell(rightCell);

        document.add(infoTable);
    }

    public void addTORStudentGrades(Document document, PdfFont boldFont, PdfFont regularFont, int studId) {
        ColesEnrollmentSystem system = new ColesEnrollmentSystem();

        float[] columnWidths = {15f, 25f, 30f, 15f, 15f};
        Table gradesTable = new Table(UnitValue.createPercentArray(columnWidths));
        gradesTable.setWidth(UnitValue.createPercentValue(100));
        gradesTable.setMarginBottom(2);

        String[] headers = {"Subject ID", "Subject Code", "Descriptive Title", "Final", "Credit"};
        for (String header : headers) {
            Cell headerCell = new Cell()
                    .add(new Paragraph(header).setFont(boldFont).setFontSize(10))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setPadding(5);
            gradesTable.addHeaderCell(headerCell);
        }

        document.add(gradesTable);

        ArrayList<String> semesters = new ArrayList<>();
        String user = studId + name;

        try {
            system.rs = system.st.executeQuery("SHOW GRANTS FOR '" + user + "'@'%'");
            while (system.rs.next()) {
                String grant = system.rs.getString(1);
                if (grant.contains("ON")) {
                    String[] parts = grant.split("ON");
                    if (parts.length > 1) {
                        String dbPart = parts[1].trim();
                        String dbName = dbPart.split("\\.")[0].replace("`", "").trim();
                        if (!dbName.equals("*")) {
                            semesters.add(dbName);
                        }
                    }
                }
            }
            Collections.sort(semesters, new Comparator<String>() {
                @Override
                public int compare(String a, String b) {
                    try {
                    
                        String[] partsA = a.split("_Sy");
                        String[] partsB = b.split("_Sy");
                        String termA = partsA[0];
                        String termB = partsB[0];
                        String[] yearsA = partsA[1].split("_");
                        String[] yearsB = partsB[1].split("_");
                        int startYearA = Integer.parseInt(yearsA[0].trim());
                        int startYearB = Integer.parseInt(yearsB[0].trim());

                   
                        int termWeightA = getTermWeight(termA);
                        int termWeightB = getTermWeight(termB);

                     
                        if (startYearA != startYearB) {
                            return Integer.compare(startYearA, startYearB);
                        }
                  
                        return Integer.compare(termWeightA, termWeightB);
                    } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                  
                        return a.compareTo(b);
                    }
                }

                private int getTermWeight(String term) {
                    if (term.equals("1stSem")) {
                        return 1;
                    }
                    if (term.equals("2ndSem")) {
                        return 2;
                    }
                    if (term.equals("Summer")) {
                        return 3;
                    }
                    return Integer.MAX_VALUE; 
                }
            });
            
            int subCount = 0;

            for (String db : semesters) {

                Paragraph dbName = new Paragraph(db)
                        .setFont(regularFont)
                        .setFontSize(11)
                        .setTextAlignment(TextAlignment.LEFT)
                        .setMarginBottom(0)
                        .setUnderline(1, -2);
                document.add(dbName);

                try {
                    ArrayList<String[]> subjectData = new ArrayList<>();

                    system.st.executeUpdate("USE " + db);

                    String query = "SELECT s.subjid, s.subjcode, s.subjdescription, g.final, s.subjunit "
                            + "FROM Enroll e "
                            + "INNER JOIN SubjectsTable s "
                            + "ON e.subjid = s.subjid "
                            + "LEFT JOIN Grades g ON e.eid = g.eid WHERE e.studid = " + studId;
                    system.rs = system.st.executeQuery(query);

                    while (system.rs.next()) {
                        String subId = system.rs.getString("subjid");
                        String subCode = system.rs.getString("subjcode");
                        String subDescription = system.rs.getString("subjdescription");
                        String fin = system.rs.getString("final");
                        String subUnits = system.rs.getString("subjunit");

                        if (fin == null) {
                            fin = "No Grade";
                        }

                        String[] info = {subId, subCode, subDescription, fin, subUnits};
                        subjectData.add(info);

                    }

                    Table subjectTable = new Table(UnitValue.createPercentArray(columnWidths));
                    subjectTable.setWidth(UnitValue.createPercentValue(100));
                    subjectTable.setMarginBottom(0);
                    subjectTable.setBorder(null);

                    for (String[] subjectRow : subjectData) {
                        subCount++;
                        for (String cellData : subjectRow) {
                            Cell dataCell = new Cell()
                                    .add(new Paragraph(cellData).setFont(regularFont).setFontSize(9))
                                    .setTextAlignment(TextAlignment.CENTER)
                                    .setPadding(0)
                                    .setBorder(null);

                            subjectTable.addCell(dataCell);
                        }
                    }

                    document.add(subjectTable);

                } catch (Exception e) {
                    System.out.println("failed to load details of " + db + ": " + e);
                }

            }

            Paragraph subjectCount = new Paragraph("Number of Subjects Listed: " + subCount)
                    .setFont(regularFont)
                    .setFontSize(10)
                    .setMarginBottom(3)
                    .setBold();
            document.add(subjectCount);

        } catch (Exception e) {
            System.out.println("Failed to load semesters:  " + e);
        }

    }

    public static void addBottomTORPdf(Document document, PdfFont boldFont, PdfFont regularFont) {

        Table topLine = new Table(1);
        topLine.setWidth(UnitValue.createPercentValue(100));
        Cell lineCell = new Cell().add(new Paragraph(""))
                .setBorder(null)
                .setBorderTop(new SolidBorder(ColorConstants.BLACK, 1));
        topLine.addCell(lineCell);
        document.add(topLine);

        Table mainTable = new Table(2);
        mainTable.setWidth(UnitValue.createPercentValue(100));

        Cell leftHeader = new Cell().add(new Paragraph("Grading System up to SY 1981-82")
                .setFont(regularFont)
                .setFontSize(11))
                .setBorder(null)
                .setPadding(0);

        Cell rightHeader = new Cell().add(new Paragraph("New Grading System (1st Sem. SY1982-83 & up)")
                .setFont(regularFont)
                .setFontSize(11))
                .setBorder(null)
                .setPadding(0);

        mainTable.addCell(leftHeader);
        mainTable.addCell(rightHeader);

        StringBuilder leftGrades = new StringBuilder();
        leftGrades.append("95-100 = 1.0 = Excellent\n");
        leftGrades.append("90-94 = 1.5 = Very Good\n");
        leftGrades.append("85-89 = 2.0 = Good\n");
        leftGrades.append("80-84 = 2.5 = Fair\n");
        leftGrades.append("75-79 = 3.0 = Passed\n");
        leftGrades.append("Below 75 = 5.0 = Failed");

        Cell leftGradesCell = new Cell().add(new Paragraph(leftGrades.toString())
                .setFont(regularFont)
                .setFontSize(10))
                .setBorder(null)
                .setPadding(0)
                .setVerticalAlignment(VerticalAlignment.TOP);

        StringBuilder rightGrades = new StringBuilder();
        rightGrades.append("95-100 = 1.0 = Excellent\n");
        rightGrades.append("90-94 = 1.5 = Very Good\n");
        rightGrades.append("85-89 = 2.0 = Good\n");
        rightGrades.append("80-84 = 2.5 = Fair\n");
        rightGrades.append("75-79 = 3.0 = Passed\n");
        rightGrades.append("Below 75 = 5.0 = Failed");

        Cell rightGradesCell = new Cell().add(new Paragraph(rightGrades.toString())
                .setFont(regularFont)
                .setFontSize(10))
                .setBorder(null)
                .setPadding(0)
                .setVerticalAlignment(VerticalAlignment.TOP);

        mainTable.addCell(leftGradesCell);
        mainTable.addCell(rightGradesCell);

        document.add(mainTable);

        Paragraph fcfGrades = new Paragraph("FOR FCF Grades: O - Outstanding, HS - Highly Satisfactory, MS - Moderately Satisfactory, S - Satisfactory, F - Fair, P - Poor")
                .setFont(regularFont)
                .setFontSize(10)
                .setMargin(0);
        document.add(fcfGrades);

        Paragraph qualityPoints = new Paragraph("Quality Point Equivalent: 1.0 = 4.0, 1.5 = 3.5, 2.0 = 3.0, 2.5 = 2.5, 3.0 = 2.0")
                .setFont(regularFont)
                .setFontSize(10)
                .setMargin(0);
        document.add(qualityPoints);

        Paragraph validityNote = new Paragraph("NOT VALID WITHOUT SCHOOL SEAL")
                .setFont(boldFont)
                .setFontSize(12)
                .setTextAlignment(TextAlignment.LEFT)
                .setMarginTop(20);
        document.add(validityNote);

        Table bottomTable = new Table(3);
        bottomTable.setWidth(UnitValue.createPercentValue(100));

        Cell preparedBy = new Cell().add(new Paragraph("Prepared By: Arch")
                .setFont(regularFont)
                .setFontSize(10))
                .setBorder(null)
                .setPadding(10)
                .setPaddingLeft(0)
                .setUnderline(1, -2);

        Cell checkedBy = new Cell().add(new Paragraph("Checked By: allison")
                .setFont(regularFont)
                .setFontSize(10))
                .setBorder(null)
                .setPadding(10)
                .setUnderline(1, -2);

        bottomTable.addCell(preparedBy);
        bottomTable.addCell(new Cell().setWidth(280).setBorder(null));
        
        bottomTable.addCell(checkedBy);

        document.add(bottomTable);
        
        Table bottomTable2 = new Table(3);
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("/MM/dd/yyyy");
        String formattedDate = today.format(formatter);

        Cell dateCell = new Cell().add(new Paragraph("Date: " + formattedDate)
                .setFont(regularFont)
                .setFontSize(10))
                .setBorder(null)
                .setPadding(10)
                .setPaddingLeft(0);

        Cell registrarCell = new Cell().add(new Paragraph("Registrar: ATTY. ROLAND ")
                .setFont(regularFont)
                .setFontSize(10))
                .setBorder(null)
                .setPadding(10)
                .setUnderline(1, -2);

        bottomTable2.addCell(dateCell);
        bottomTable2.addCell(new Cell().setWidth(285).setBorder(null)); 
        bottomTable2.addCell(registrarCell);
        document.add(bottomTable2);
              
    }
}