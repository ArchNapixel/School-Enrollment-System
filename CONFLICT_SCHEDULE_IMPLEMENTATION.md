# Schedule Conflict Checker Implementation

## Overview
Implemented a **Schedule Conflict Checker** feature in the StudentsForm that detects scheduling conflicts when a student enrolls in multiple subjects.

## Implementation Details

### Location
- **File**: `ColesEnrollmentSystemexer8/StudentsForm.java`
- **Menu Item**: AI Menu → "Conflict Schedule" (CheckBoxMenuItem)

### Components Implemented

#### 1. Action Listener
- **Method**: `ConflictSchedulemenuitemActionPerformed()`
- Validates that a student is selected
- Retrieves student ID and initiates conflict checking

#### 2. Conflict Checking Logic
- **Method**: `checkScheduleConflicts(int studentID)`
- Queries database for all enrolled subjects and their schedules
- Compares schedules for time and day overlaps
- Generates a conflict report with:
  - ✓ No conflicts found (with list of all schedules)
  - ✗ Conflicts detected (with specific conflicts listed)

#### 3. Conflict Detection Algorithm
- **Method**: `findConflicts(List<ScheduleInfo>)`
- Compares all pairs of enrolled subjects
- Uses `hasTimeConflict()` to check overlap

#### 4. Time & Day Overlap Detection
- **daysOverlap()**: Checks if schedule days overlap (e.g., MWF vs TTH)
- **timesOverlap()**: Checks if time ranges overlap
- **timeToMinutes()**: Converts HH:MM format to minutes for comparison

#### 5. Schedule Information Class
- **ScheduleInfo**: Inner class storing subject ID, code, and schedule string
- Format: "MWF 08:20-09:20" or "TTH 10:30-11:30"

## How It Works

### Schedule Format
Schedules are stored as: `DAY_CODE TIME_RANGE`
- **Day Codes**: MWF (Monday-Wednesday-Friday), TTH (Tuesday-Thursday)
- **Time Range**: HH:MM-HH:MM (24-hour format)

### Conflict Detection Logic
```
1. Parse student's enrolled subjects from database
2. For each pair of subjects:
   a. Check if days overlap (any common day letter)
   b. Check if times overlap (convert to minutes, compare ranges)
3. Report any conflicts found
```

### Example Scenarios

**NO CONFLICTS:**
```
Schedule 1: MWF 08:20-09:20
Schedule 2: MWF 11:35-12:35
Schedule 3: TTH 10:30-11:30
```
✓ All times and days don't overlap

**CONFLICTS:**
```
Schedule 1: MWF 11:00-12:00
Schedule 2: MWF 09:00-11:00
Schedule 3: TTH 10:40-11:25
```
✗ Schedule 1 and 2 conflict on MWF (11:00-12:00 overlaps with 09:00-11:00)

## Database Query
```sql
SELECT s.subjid, s.subjcode, s.subjschedule 
FROM SubjectsTable s 
INNER JOIN Enroll e ON s.subjid = e.subjid 
WHERE e.studid = ? 
ORDER BY s.subjschedule
```

## User Interface
- Navigate to **AI Menu** → **Conflict Schedule**
- Select a student from the student table
- Click "Conflict Schedule" to check for schedule conflicts
- A dialog box displays:
  - Status (conflicts or no conflicts)
  - Detailed list of all enrolled subject schedules
  - Specific conflicts if found

## Future Enhancements
1. **OptaPlanner Integration**: Add AI-based conflict resolution suggestions
2. **MySQL Stored Procedures**: Add database-level conflict checking using cursors
3. **Visual Conflict Highlighting**: Highlight conflicting schedules in the UI
4. **Alternative Schedule Recommendations**: Suggest alternative subjects with no conflicts
5. **Semester-wide Conflict Report**: Generate reports for all students

## Technical Notes
- Uses Java PreparedStatement for SQL injection prevention
- Time comparison uses 24-hour format (minutes since midnight)
- Day overlap detection uses character matching for flexibility
- Handles edge cases (empty schedules, invalid formats)
