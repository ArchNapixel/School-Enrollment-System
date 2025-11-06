-- ========================================
-- DATABASE SELECTION
-- ========================================
-- Before running this script, make sure you have selected the correct database.
-- Your database names follow this format:
--   - 1stSemSY2024_2025 (First Semester)
--   - 2ndSemSY2024_2025 (Second Semester)
--   - SummerSY2024_2025 (Summer)
--
-- Execute this line FIRST (replace with your actual database name):
-- USE 1stSemSY2024_2025;
-- ========================================

-- ========================================
-- INSERT 50 STUDENTS INTO DATABASE
-- ========================================

INSERT INTO StudentsTable (Name, Address, Contact, email, Course, Gender, YearLevel) 
VALUES 
('John Doe', '123 Main St', '09111111111', 'john.doe@email.com', 'BS Computer Science', 'Male', '1st Year'),
('Jane Smith', '456 Oak Ave', '09112222222', 'jane.smith@email.com', 'BS Information Technology', 'Female', '1st Year'),
('Michael Johnson', '789 Pine Rd', '09113333333', 'michael.j@email.com', 'BS Computer Science', 'Male', '2nd Year'),
('Sarah Williams', '321 Elm St', '09114444444', 'sarah.w@email.com', 'BS Business Administration', 'Female', '1st Year'),
('David Brown', '654 Maple Dr', '09115555555', 'david.b@email.com', 'BS Information Technology', 'Male', '3rd Year'),
('Emily Davis', '987 Cedar Ln', '09116666666', 'emily.d@email.com', 'BS Computer Science', 'Female', '2nd Year'),
('James Wilson', '111 Birch Way', '09117777777', 'james.w@email.com', 'BS Engineering', 'Male', '1st Year'),
('Jennifer Martinez', '222 Spruce Ct', '09118888888', 'jennifer.m@email.com', 'BS Business Administration', 'Female', '2nd Year'),
('Robert Garcia', '333 Ash Blvd', '09119999999', 'robert.g@email.com', 'BS Computer Science', 'Male', '1st Year'),
('Lisa Anderson', '444 Willow St', '09120000000', 'lisa.a@email.com', 'BS Information Technology', 'Female', '3rd Year'),
('Christopher Taylor', '555 Poplar Ave', '09121111111', 'chris.t@email.com', 'BS Engineering', 'Male', '2nd Year'),
('Amanda Thomas', '666 Sycamore Rd', '09122222222', 'amanda.t@email.com', 'BS Computer Science', 'Female', '1st Year'),
('Daniel Jackson', '777 Chestnut Dr', '09123333333', 'daniel.j@email.com', 'BS Business Administration', 'Male', '2nd Year'),
('Michelle White', '888 Hickory Ln', '09124444444', 'michelle.w@email.com', 'BS Information Technology', 'Female', '1st Year'),
('Matthew Harris', '999 Juniper Way', '09125555555', 'matthew.h@email.com', 'BS Computer Science', 'Male', '3rd Year'),
('Ashley Martin', '1010 Laurel St', '09126666666', 'ashley.m@email.com', 'BS Engineering', 'Female', '2nd Year'),
('Mark Thompson', '1111 Magnolia Ave', '09127777777', 'mark.t@email.com', 'BS Computer Science', 'Male', '1st Year'),
('Jessica Garcia', '1212 Acacia Rd', '09128888888', 'jessica.g@email.com', 'BS Business Administration', 'Female', '2nd Year'),
('Donald Lee', '1313 Palm Dr', '09129999999', 'donald.l@email.com', 'BS Information Technology', 'Male', '1st Year'),
('Nicole Rodriguez', '1414 Elm Ct', '09130000000', 'nicole.r@email.com', 'BS Computer Science', 'Female', '3rd Year'),
('Kevin Clark', '1515 Oak St', '09131111111', 'kevin.c@email.com', 'BS Engineering', 'Male', '2nd Year'),
('Katherine Lewis', '1616 Pine Ave', '09132222222', 'katherine.l@email.com', 'BS Computer Science', 'Female', '1st Year'),
('Ryan Walker', '1717 Maple Rd', '09133333333', 'ryan.w@email.com', 'BS Information Technology', 'Male', '2nd Year'),
('Samantha Hall', '1818 Cedar Way', '09134444444', 'samantha.h@email.com', 'BS Business Administration', 'Female', '1st Year'),
('Jason Allen', '1919 Birch Ln', '09135555555', 'jason.a@email.com', 'BS Computer Science', 'Male', '3rd Year'),
('Rachel Young', '2020 Spruce St', '09136666666', 'rachel.y@email.com', 'BS Engineering', 'Female', '2nd Year'),
('Gary King', '2121 Ash Ave', '09137777777', 'gary.k@email.com', 'BS Computer Science', 'Male', '1st Year'),
('Deborah Wright', '2222 Willow Rd', '09138888888', 'deborah.w@email.com', 'BS Information Technology', 'Female', '2nd Year'),
('Edward Lopez', '2323 Poplar Way', '09139999999', 'edward.l@email.com', 'BS Business Administration', 'Male', '1st Year'),
('Brenda Hill', '2424 Sycamore Ct', '09140000000', 'brenda.h@email.com', 'BS Computer Science', 'Female', '3rd Year'),
('Ronald Scott', '2525 Chestnut Ave', '09141111111', 'ronald.s@email.com', 'BS Engineering', 'Male', '2nd Year'),
('Donna Green', '2626 Hickory Rd', '09142222222', 'donna.g@email.com', 'BS Computer Science', 'Female', '1st Year'),
('Paul Adams', '2727 Juniper St', '09143333333', 'paul.a@email.com', 'BS Information Technology', 'Male', '2nd Year'),
('Carol Nelson', '2828 Laurel Way', '09144444444', 'carol.n@email.com', 'BS Business Administration', 'Female', '1st Year'),
('Steven Carter', '2929 Magnolia Ct', '09145555555', 'steven.c@email.com', 'BS Computer Science', 'Male', '3rd Year'),
('Sandra Mitchell', '3030 Acacia Ave', '09146666666', 'sandra.m@email.com', 'BS Engineering', 'Female', '2nd Year'),
('Andrew Perez', '3131 Palm Rd', '09147777777', 'andrew.p@email.com', 'BS Computer Science', 'Male', '1st Year'),
('Kathleen Roberts', '3232 Elm Way', '09148888888', 'kathleen.r@email.com', 'BS Information Technology', 'Female', '2nd Year'),
('Kenneth Phillips', '3333 Oak Ct', '09149999999', 'kenneth.p@email.com', 'BS Business Administration', 'Male', '1st Year'),
('Shirley Campbell', '3434 Pine St', '09150000000', 'shirley.c@email.com', 'BS Computer Science', 'Female', '3rd Year'),
('Jerry Parker', '3535 Maple Ave', '09151111111', 'jerry.p@email.com', 'BS Engineering', 'Male', '2nd Year'),
('Angela Evans', '3636 Cedar Rd', '09152222222', 'angela.e@email.com', 'BS Computer Science', 'Female', '1st Year'),
('Dennis Edwards', '3737 Birch Way', '09153333333', 'dennis.e@email.com', 'BS Information Technology', 'Male', '2nd Year'),
('Helen Collins', '3838 Spruce Ave', '09154444444', 'helen.c@email.com', 'BS Business Administration', 'Female', '1st Year'),
('Walter Reeves', '3939 Ash Ct', '09155555555', 'walter.r@email.com', 'BS Computer Science', 'Male', '3rd Year'),
('Brenda Morris', '4040 Willow St', '09156666666', 'brenda.m@email.com', 'BS Engineering', 'Female', '2nd Year'),
('Peter Murphy', '4141 Poplar Rd', '09157777777', 'peter.m@email.com', 'BS Computer Science', 'Male', '1st Year'),
('Diane Rogers', '4242 Sycamore Way', '09158888888', 'diane.r@email.com', 'BS Information Technology', 'Female', '2nd Year'),
('Harold Morgan', '4343 Chestnut Ct', '09159999999', 'harold.m@email.com', 'BS Business Administration', 'Male', '1st Year'),
('Julie Peterson', '4444 Hickory Ave', '09160000000', 'julie.p@email.com', 'BS Computer Science', 'Female', '3rd Year');

-- ========================================
-- INSERT 50 SUBJECTS INTO DATABASE
-- ========================================

INSERT INTO SubjectsTable (subjcode, subjdescription, subjschedule, subjunit) 
VALUES 
('CS101', 'Introduction to Programming', 'MWF 08:20-09:20', 3),
('CS102', 'Data Structures', 'TTH 10:30-11:30', 4),
('CS103', 'Object-Oriented Programming', 'MWF 09:30-10:25', 3),
('CS104', 'Database Systems', 'TTH 14:00-15:30', 4),
('CS105', 'Web Development', 'MWF 10:30-11:25', 3),
('CS201', 'Algorithms', 'TTH 09:00-10:30', 4),
('CS202', 'Software Engineering', 'MWF 13:00-14:00', 3),
('CS203', 'Computer Networks', 'TTH 11:35-12:35', 4),
('CS204', 'Operating Systems', 'MWF 14:00-15:00', 3),
('CS205', 'Artificial Intelligence', 'TTH 08:20-09:50', 4),
('MATH101', 'Calculus I', 'MWF 08:00-09:00', 4),
('MATH102', 'Calculus II', 'TTH 09:30-11:00', 4),
('MATH201', 'Linear Algebra', 'MWF 10:00-11:00', 3),
('MATH202', 'Discrete Mathematics', 'TTH 13:00-14:30', 4),
('MATH203', 'Probability and Statistics', 'MWF 11:00-12:00', 3),
('ENG101', 'English Composition', 'TTH 08:00-09:30', 3),
('ENG102', 'Literature and Analysis', 'MWF 09:00-10:00', 3),
('ENG201', 'Technical Writing', 'TTH 10:00-11:30', 3),
('ENG202', 'World Literature', 'MWF 14:00-15:00', 3),
('ENG203', 'Creative Writing', 'TTH 15:00-16:30', 3),
('BUS101', 'Introduction to Business', 'MWF 08:30-09:30', 3),
('BUS102', 'Business Ethics', 'TTH 10:00-11:30', 3),
('BUS201', 'Management Principles', 'MWF 10:30-11:30', 3),
('BUS202', 'Marketing Fundamentals', 'TTH 13:00-14:30', 3),
('BUS203', 'Accounting Basics', 'MWF 11:30-12:30', 4),
('BUS204', 'Financial Management', 'TTH 14:00-15:30', 4),
('BUS205', 'Human Resources', 'MWF 13:00-14:00', 3),
('ENG101B', 'Engineering Mathematics', 'TTH 08:30-10:00', 4),
('ENG102B', 'Engineering Physics', 'MWF 09:30-10:30', 4),
('ENG201B', 'Thermodynamics', 'TTH 11:00-12:30', 4),
('ENG202B', 'Fluid Mechanics', 'MWF 11:00-12:00', 4),
('ENG203B', 'Materials Science', 'TTH 14:00-15:30', 3),
('IT101', 'Computer Fundamentals', 'MWF 08:00-09:00', 3),
('IT102', 'System Administration', 'TTH 09:00-10:30', 4),
('IT201', 'Network Administration', 'MWF 10:00-11:00', 4),
('IT202', 'Cybersecurity', 'TTH 13:30-15:00', 4),
('IT203', 'Cloud Computing', 'MWF 13:00-14:00', 3),
('SCI101', 'General Chemistry', 'MWF 09:00-10:00', 4),
('SCI102', 'General Biology', 'TTH 10:00-11:30', 4),
('SCI201', 'Organic Chemistry', 'MWF 14:00-15:00', 4),
('SCI202', 'Physics I', 'TTH 08:00-09:30', 4),
('SCI203', 'Physics II', 'MWF 11:00-12:00', 4),
('SOC101', 'Introduction to Sociology', 'TTH 14:30-16:00', 3),
('SOC102', 'Social Psychology', 'MWF 13:30-14:30', 3),
('PSY101', 'General Psychology', 'TTH 11:00-12:30', 3),
('PSY201', 'Cognitive Psychology', 'MWF 15:00-16:00', 3),
('HIST101', 'World History', 'TTH 15:00-16:30', 3),
('HIST102', 'Modern History', 'MWF 12:30-13:30', 3),
('ART101', 'Introduction to Art', 'TTH 13:00-14:30', 3),
('MUS101', 'Music Theory', 'MWF 15:00-16:00', 3),
('PHE101', 'Physical Education', 'TTH 16:00-17:00', 2);

-- ========================================
-- SUMMARY
-- ========================================
-- Total Students Inserted: 50 (IDs: 1001-1050)
-- Total Subjects Inserted: 50 (IDs: 2001-2050)
-- 
-- To execute this file:
-- 1. Open MySQL Workbench or MySQL Command Line
-- 2. Select your database: USE [your_database_name];
-- 3. Run: source 'C:/path/to/this/file.sql';
-- OR paste the contents directly into the MySQL editor
-- ========================================
