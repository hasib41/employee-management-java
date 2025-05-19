# Employee Management System

A simple console-based Employee Management System in Java that uses text files as a database.

## Features

### Employee Management
- Add new employees
- View all employees
- Search employees by ID
- Search employees by name
- Update employee information
- Delete employees

### Attendance Management
- Record employee check-in/check-out
- Mark employees as absent
- View attendance by date
- View attendance for specific employees
- Generate attendance summary reports

## Project Structure

```
employeeManagement/
├── src/
│   ├── Employee.java            # Employee model class
│   ├── EmployeeRepository.java  # Data access layer for employee file operations
│   ├── EmployeeService.java     # Service layer for employee business logic
│   ├── Attendance.java          # Attendance model class
│   ├── AttendanceRepository.java # Data access layer for attendance file operations
│   ├── AttendanceService.java   # Service layer for attendance business logic
│   ├── ConsoleUI.java           # User interface for console interaction
│   └── Main.java                # Application entry point
├── data/
│   ├── employees.txt            # Text file database for employee records
│   └── attendance.txt           # Text file database for attendance records
└── bin/                         # Directory for compiled class files
```

## How to Compile and Run

### Using Command Line

1. Navigate to the project directory:
   ```
   cd C:\Users\hasib\OneDrive\Desktop\employeeManagement
   ```

2. Compile all Java files:
   ```
   javac -d bin src\*.java
   ```

3. Run the application:
   ```
   java -cp bin Main
   ```

### Using the Included Batch File

1. Navigate to the project directory
2. Run the batch file:
   ```
   .\run.bat
   ```

### Using an IDE (like IntelliJ IDEA or Eclipse)

1. Open the project in your IDE
2. Configure the IDE to output compiled classes to a 'bin' directory
3. Run the Main class

## Data Format

### Employee Data (employees.txt)
Employee data is stored in CSV format:
```
id,name,position,salary
```

Example:
```
1,John Doe,Software Engineer,75000.0
2,Jane Smith,HR Manager,65000.0
```

### Attendance Data (attendance.txt)
Attendance data is stored in CSV format:
```
employeeId,date,checkInTime,checkOutTime,status
```

Example:
```
1,2025-05-18,09:00:00,17:30:00,PRESENT
2,2025-05-18,08:45:00,17:15:00,PRESENT
3,2025-05-18,,,ABSENT
```

Date format: yyyy-MM-dd
Time format: HH:mm:ss
Status values: PRESENT, ABSENT, LATE, HALF_DAY

## Note

This is a simple implementation intended for educational purposes. For a production system, consider using a more robust database solution.
