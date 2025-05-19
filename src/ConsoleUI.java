import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class ConsoleUI {
    private final Scanner scanner;
    private final EmployeeService employeeService;
    private final AttendanceService attendanceService;
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ConsoleUI(EmployeeService employeeService, AttendanceService attendanceService) {
        this.employeeService = employeeService;
        this.attendanceService = attendanceService;
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    employeeManagementMenu();
                    break;
                case 2:
                    attendanceManagementMenu();
                    break;
                case 0:
                    running = false;
                    System.out.println("Exiting application. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
        }
    }

    private void displayMainMenu() {
        System.out.println("\n========== EMPLOYEE MANAGEMENT SYSTEM ==========");
        System.out.println("1. Employee Management");
        System.out.println("2. Attendance Management");
        System.out.println("0. Exit");
        System.out.println("===============================================");
    }

    private void employeeManagementMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n========== EMPLOYEE MANAGEMENT ==========");
            System.out.println("1. Add New Employee");
            System.out.println("2. View All Employees");
            System.out.println("3. Search Employee by ID");
            System.out.println("4. Search Employees by Name");
            System.out.println("5. Update Employee");
            System.out.println("6. Delete Employee");
            System.out.println("0. Back to Main Menu");
            System.out.println("===============================================");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    addEmployee();
                    break;
                case 2:
                    viewAllEmployees();
                    break;
                case 3:
                    searchEmployeeById();
                    break;
                case 4:
                    searchEmployeesByName();
                    break;
                case 5:
                    updateEmployee();
                    break;
                case 6:
                    deleteEmployee();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }

    private void attendanceManagementMenu() {
        boolean running = true;
        while (running) {
            System.out.println("\n========== ATTENDANCE MANAGEMENT ==========");
            System.out.println("1. Check-In Employee");
            System.out.println("2. Check-Out Employee");
            System.out.println("3. Mark Employee as Absent");
            System.out.println("4. View Today's Attendance");
            System.out.println("5. View Attendance by Date");
            System.out.println("6. View Attendance for an Employee");
            System.out.println("7. View Attendance Summary for an Employee");
            System.out.println("0. Back to Main Menu");
            System.out.println("===============================================");
            
            int choice = getIntInput("Enter your choice: ");
            
            switch (choice) {
                case 1:
                    checkInEmployee();
                    break;
                case 2:
                    checkOutEmployee();
                    break;
                case 3:
                    markEmployeeAbsent();
                    break;
                case 4:
                    viewTodayAttendance();
                    break;
                case 5:
                    viewAttendanceByDate();
                    break;
                case 6:
                    viewAttendanceForEmployee();
                    break;
                case 7:
                    viewAttendanceSummary();
                    break;
                case 0:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid choice! Please try again.");
            }
            
            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }
    }

    // Employee Management Methods
    private void addEmployee() {
        System.out.println("\n----- Add New Employee -----");
        int id = employeeService.getNextEmployeeId();
        System.out.println("Employee ID: " + id + " (auto-generated)");
        
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        
        System.out.print("Enter position: ");
        String position = scanner.nextLine();
        
        double salary = getDoubleInput("Enter salary: $");
        
        Employee employee = new Employee(id, name, position, salary);
        boolean success = employeeService.addEmployee(employee);
        
        if (success) {
            System.out.println("Employee added successfully!");
        } else {
            System.out.println("Failed to add employee. Employee ID might already exist.");
        }
    }

    private void viewAllEmployees() {
        System.out.println("\n----- All Employees -----");
        List<Employee> employees = employeeService.getAllEmployees();
        displayEmployeesAsTable(employees);
    }

    private void searchEmployeeById() {
        System.out.println("\n----- Search Employee by ID -----");
        int id = getIntInput("Enter employee ID: ");
        
        Employee employee = employeeService.findEmployeeById(id);
        if (employee != null) {
            System.out.println("Employee found:");
            displayEmployeeAsTable(employee);
        } else {
            System.out.println("No employee found with ID: " + id);
        }
    }
    
    private void displayEmployeeAsTable(Employee employee) {
        System.out.println("\n==============================================================");
        System.out.println("|| EMPLOYEE DETAILS                                         ||");
        System.out.println("==============================================================");
        System.out.println("|| ID:       " + padRight(String.valueOf(employee.getId()), 46) + "||");
        System.out.println("|| Name:     " + padRight(employee.getName(), 46) + "||");
        System.out.println("|| Position: " + padRight(employee.getPosition(), 46) + "||");
        System.out.println("|| Salary:   " + padRight("$" + String.format("%.2f", employee.getSalary()), 46) + "||");
        System.out.println("==============================================================");
    }
    
    private void displayEmployeesAsTable(List<Employee> employees) {
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
            return;
        }
        
        System.out.println("\n==============================================================");
        System.out.println("|| EMPLOYEE LIST                                            ||");
        System.out.println("==============================================================");
        System.out.println("|| ID    | Name                 | Position          | Salary       ||");
        System.out.println("||-------+----------------------+-------------------+--------------||");
        
        for (Employee employee : employees) {
            System.out.printf("|| %-5d | %-20s | %-17s | $%-12.2f ||\n", 
                    employee.getId(), 
                    truncateString(employee.getName(), 20), 
                    truncateString(employee.getPosition(), 17), 
                    employee.getSalary());
        }
        
        System.out.println("==============================================================");
        System.out.println("Total employees: " + employees.size());
    }
    
    private String padRight(String s, int n) {
        return String.format("%-" + n + "s", s);
    }
    
    private String truncateString(String str, int maxLength) {
        if (str == null) {
            return "";
        }
        return str.length() <= maxLength ? str : str.substring(0, maxLength - 3) + "...";
    }

    private void searchEmployeesByName() {
        System.out.println("\n----- Search Employees by Name -----");
        System.out.print("Enter name to search: ");
        String name = scanner.nextLine();
        
        List<Employee> employees = employeeService.findEmployeesByName(name);
        if (employees.isEmpty()) {
            System.out.println("No employees found with name containing: " + name);
        } else {
            System.out.println("Employees found:");
            displayEmployeesAsTable(employees);
        }
    }

    private void updateEmployee() {
        System.out.println("\n----- Update Employee -----");
        int id = getIntInput("Enter employee ID to update: ");
        
        Employee employee = employeeService.findEmployeeById(id);
        if (employee == null) {
            System.out.println("No employee found with ID: " + id);
            return;
        }
        
        System.out.println("Current employee details:");
        displayEmployeeAsTable(employee);
        System.out.println("\nEnter new details (leave blank to keep current value):");
        
        System.out.print("Enter new name [" + employee.getName() + "]: ");
        String name = scanner.nextLine();
        if (!name.trim().isEmpty()) {
            employee.setName(name);
        }
        
        System.out.print("Enter new position [" + employee.getPosition() + "]: ");
        String position = scanner.nextLine();
        if (!position.trim().isEmpty()) {
            employee.setPosition(position);
        }
        
        System.out.print("Enter new salary [$" + employee.getSalary() + "]: ");
        String salaryStr = scanner.nextLine();
        if (!salaryStr.trim().isEmpty()) {
            try {
                double salary = Double.parseDouble(salaryStr);
                employee.setSalary(salary);
            } catch (NumberFormatException e) {
                System.out.println("Invalid salary format. Keeping current salary.");
            }
        }
        
        boolean success = employeeService.updateEmployee(employee);
        if (success) {
            System.out.println("Employee updated successfully!");
            System.out.println("Updated employee details:");
            displayEmployeeAsTable(employee);
        } else {
            System.out.println("Failed to update employee.");
        }
    }

    private void deleteEmployee() {
        System.out.println("\n----- Delete Employee -----");
        int id = getIntInput("Enter employee ID to delete: ");
        
        Employee employee = employeeService.findEmployeeById(id);
        if (employee == null) {
            System.out.println("No employee found with ID: " + id);
            return;
        }
        
        System.out.println("Employee to be deleted:");
        displayEmployeeAsTable(employee);
        
        System.out.print("Are you sure you want to delete this employee? (y/n): ");
        String confirmation = scanner.nextLine().trim().toLowerCase();
        
        if (confirmation.equals("y") || confirmation.equals("yes")) {
            boolean success = employeeService.deleteEmployee(id);
            if (success) {
                System.out.println("Employee deleted successfully!");
            } else {
                System.out.println("Failed to delete employee.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    // Attendance Management Methods
    private void checkInEmployee() {
        System.out.println("\n----- Check-In Employee -----");
        int id = getIntInput("Enter employee ID: ");
        
        Employee employee = employeeService.findEmployeeById(id);
        if (employee == null) {
            System.out.println("No employee found with ID: " + id);
            return;
        }
        
        System.out.println("Employee: " + employee.getName());
        
        // Default to today
        LocalDate date = LocalDate.now();
        System.out.println("Date: " + date.format(DATE_FORMATTER) + " (Today)");
        
        // Default to current time
        LocalTime time = LocalTime.now();
        System.out.println("Check-in time: " + time);
        
        boolean success = attendanceService.checkIn(id, date, time);
        if (success) {
            System.out.println("Check-in recorded successfully!");
        } else {
            System.out.println("Failed to record check-in. Employee might already be checked in for today.");
        }
    }

    private void checkOutEmployee() {
        System.out.println("\n----- Check-Out Employee -----");
        int id = getIntInput("Enter employee ID: ");
        
        Employee employee = employeeService.findEmployeeById(id);
        if (employee == null) {
            System.out.println("No employee found with ID: " + id);
            return;
        }
        
        System.out.println("Employee: " + employee.getName());
        
        // Default to today
        LocalDate date = LocalDate.now();
        System.out.println("Date: " + date.format(DATE_FORMATTER) + " (Today)");
        
        // Check if employee is checked in
        Attendance attendance = attendanceService.getAttendanceByEmployeeAndDate(id, date);
        if (attendance == null || attendance.getCheckInTime() == null) {
            System.out.println("Employee has not checked in today. Check-out not allowed.");
            return;
        }
        
        // Default to current time
        LocalTime time = LocalTime.now();
        System.out.println("Check-out time: " + time);
        
        boolean success = attendanceService.checkOut(id, date, time);
        if (success) {
            System.out.println("Check-out recorded successfully!");
            
            // Show hours worked
            attendance = attendanceService.getAttendanceByEmployeeAndDate(id, date);
            System.out.printf("Hours worked today: %.2f hours\n", attendance.getHoursWorked());
        } else {
            System.out.println("Failed to record check-out. Check-out time must be after check-in time.");
        }
    }

    private void markEmployeeAbsent() {
        System.out.println("\n----- Mark Employee as Absent -----");
        int id = getIntInput("Enter employee ID: ");
        
        Employee employee = employeeService.findEmployeeById(id);
        if (employee == null) {
            System.out.println("No employee found with ID: " + id);
            return;
        }
        
        System.out.println("Employee: " + employee.getName());
        
        // Get date from user
        LocalDate date = getDateInput("Enter date (yyyy-MM-dd) or leave blank for today: ");
        if (date == null) {
            date = LocalDate.now();
        }
        System.out.println("Date: " + date.format(DATE_FORMATTER));
        
        boolean success = attendanceService.markAbsent(id, date);
        if (success) {
            System.out.println("Employee marked as absent successfully!");
        } else {
            System.out.println("Failed to mark employee as absent.");
        }
    }

    private void viewTodayAttendance() {
        System.out.println("\n----- Today's Attendance -----");
        LocalDate today = LocalDate.now();
        System.out.println("Date: " + today.format(DATE_FORMATTER));
        
        List<Attendance> attendanceList = attendanceService.getAttendanceByDate(today);
        displayAttendanceList(attendanceList);
    }

    private void viewAttendanceByDate() {
        System.out.println("\n----- Attendance by Date -----");
        LocalDate date = getDateInput("Enter date (yyyy-MM-dd): ");
        if (date == null) {
            System.out.println("Invalid date format. Returning to menu.");
            return;
        }
        
        System.out.println("Date: " + date.format(DATE_FORMATTER));
        
        List<Attendance> attendanceList = attendanceService.getAttendanceByDate(date);
        displayAttendanceList(attendanceList);
    }

    private void viewAttendanceForEmployee() {
        System.out.println("\n----- Attendance for Employee -----");
        int id = getIntInput("Enter employee ID: ");
        
        Employee employee = employeeService.findEmployeeById(id);
        if (employee == null) {
            System.out.println("No employee found with ID: " + id);
            return;
        }
        
        System.out.println("Employee: " + employee.getName());
        
        List<Attendance> attendanceList = attendanceService.getAttendanceByEmployeeId(id);
        displayAttendanceList(attendanceList);
    }

    private void viewAttendanceSummary() {
        System.out.println("\n----- Attendance Summary for Employee -----");
        int id = getIntInput("Enter employee ID: ");
        
        Employee employee = employeeService.findEmployeeById(id);
        if (employee == null) {
            System.out.println("No employee found with ID: " + id);
            return;
        }
        
        System.out.println("Employee: " + employee.getName());
        
        // Get date range from user
        System.out.println("Enter date range (leave blank for current month):");
        LocalDate startDate = getDateInput("Start date (yyyy-MM-dd): ");
        LocalDate endDate = getDateInput("End date (yyyy-MM-dd): ");
        
        // Default to current month if no dates provided
        if (startDate == null || endDate == null) {
            LocalDate now = LocalDate.now();
            startDate = LocalDate.of(now.getYear(), now.getMonth(), 1);
            endDate = startDate.plusMonths(1).minusDays(1);
        }
        
        System.out.println("\nAttendance Summary for " + startDate.format(DATE_FORMATTER) + 
                " to " + endDate.format(DATE_FORMATTER) + ":");
        
        Map<String, Long> summary = attendanceService.getAttendanceSummary(id, startDate, endDate);
        long presentDays = summary.getOrDefault("PRESENT", 0L);
        long absentDays = summary.getOrDefault("ABSENT", 0L);
        long halfDays = summary.getOrDefault("HALF_DAY", 0L);
        long lateDays = summary.getOrDefault("LATE", 0L);
        
        System.out.println("Present Days: " + presentDays);
        System.out.println("Absent Days: " + absentDays);
        System.out.println("Half Days: " + halfDays);
        System.out.println("Late Days: " + lateDays);
        
        // Calculate total days
        long totalDays = presentDays + absentDays + halfDays + lateDays;
        System.out.println("Total Days Recorded: " + totalDays);
        
        // Calculate attendance percentage
        if (totalDays > 0) {
            double percentage = (double) (presentDays + halfDays * 0.5) / totalDays * 100.0;
            System.out.printf("Attendance Percentage: %.2f%%\n", percentage);
        }
        
        // Show total hours worked
        double totalHours = attendanceService.getTotalHoursWorked(id, startDate, endDate);
        System.out.printf("Total Hours Worked: %.2f hours\n", totalHours);
    }

    private void displayAttendanceList(List<Attendance> attendanceList) {
        if (attendanceList.isEmpty()) {
            System.out.println("No attendance records found.");
            return;
        }
        
        System.out.println("\n----- Attendance Records -----");
        for (Attendance attendance : attendanceList) {
            Employee employee = employeeService.findEmployeeById(attendance.getEmployeeId());
            String employeeName = (employee != null) ? employee.getName() : "Unknown";
            
            System.out.println("Employee: " + employeeName + " (ID: " + attendance.getEmployeeId() + ")");
            System.out.println("Date: " + attendance.getDate());
            System.out.println("Status: " + attendance.getStatus());
            
            String checkInTime = (attendance.getCheckInTime() != null) ? 
                    attendance.getCheckInTime().toString() : "Not checked in";
            String checkOutTime = (attendance.getCheckOutTime() != null) ? 
                    attendance.getCheckOutTime().toString() : "Not checked out";
            
            System.out.println("Check-in: " + checkInTime);
            System.out.println("Check-out: " + checkOutTime);
            
            if (attendance.getCheckInTime() != null && attendance.getCheckOutTime() != null) {
                System.out.printf("Hours worked: %.2f hours\n", attendance.getHoursWorked());
            }
            
            System.out.println("--------------------------");
        }
        System.out.println("Total records: " + attendanceList.size());
    }

    // Utility methods
    private int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private double getDoubleInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private LocalDate getDateInput(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        
        if (input.isEmpty()) {
            return null;
        }
        
        try {
            return LocalDate.parse(input, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format. Please use yyyy-MM-dd format.");
            return null;
        }
    }
}
