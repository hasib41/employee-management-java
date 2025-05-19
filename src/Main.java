public class Main {
    private static final String EMPLOYEE_DATA_FILE = "data/employees.txt";
    private static final String ATTENDANCE_DATA_FILE = "data/attendance.txt";

    public static void main(String[] args) {
        // Initialize employee components
        EmployeeRepository employeeRepository = new EmployeeRepository(EMPLOYEE_DATA_FILE);
        EmployeeService employeeService = new EmployeeService(employeeRepository);
        
        // Initialize attendance components
        AttendanceRepository attendanceRepository = new AttendanceRepository(ATTENDANCE_DATA_FILE);
        AttendanceService attendanceService = new AttendanceService(attendanceRepository, employeeService);
        
        // Initialize UI with both services
        ConsoleUI ui = new ConsoleUI(employeeService, attendanceService);
        
        // Start the UI
        System.out.println("Starting Employee Management System...");
        ui.start();
    }
}
