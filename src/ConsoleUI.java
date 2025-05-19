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
            System.out.println("5. Search Employees by Position");
            System.out.println("6. Update Employee");
            System.out.println("7. Delete Employee");
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
                    searchEmployeesByPosition();
                    break;
                case 6:
                    updateEmployee();
                    break;
                case 7:
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
    
    private void searchEmployeesByPosition() {
        System.out.println("\n----- Search Employees by Position -----");
        System.out.print("Enter position to search: ");
        String position = scanner.nextLine();
        
        List<Employee> employees = employeeService.findEmployeesByPosition(position);
        if (employees.isEmpty()) {
            System.out.println("No employees found with position containing: " + position);
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
