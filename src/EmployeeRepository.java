import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeeRepository {
    private final String filePath;

    public EmployeeRepository(String filePath) {
        this.filePath = filePath;
        // Create the file if it doesn't exist
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error creating file: " + e.getMessage());
        }
    }

    public List<Employee> getAllEmployees() {
        List<Employee> employees = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    employees.add(parseEmployee(line));
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading employees: " + e.getMessage());
        }
        
        return employees;
    }

    public void saveAllEmployees(List<Employee> employees) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Employee employee : employees) {
                writer.write(employee.toCsvString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving employees: " + e.getMessage());
        }
    }

    public boolean addEmployee(Employee employee) {
        List<Employee> employees = getAllEmployees();
        
        // Check for duplicate ID
        for (Employee existingEmployee : employees) {
            if (existingEmployee.getId() == employee.getId()) {
                return false; // ID already exists
            }
        }
        
        employees.add(employee);
        saveAllEmployees(employees);
        return true;
    }

    public boolean updateEmployee(Employee updatedEmployee) {
        List<Employee> employees = getAllEmployees();
        boolean found = false;
        
        for (int i = 0; i < employees.size(); i++) {
            if (employees.get(i).getId() == updatedEmployee.getId()) {
                employees.set(i, updatedEmployee);
                found = true;
                break;
            }
        }
        
        if (found) {
            saveAllEmployees(employees);
            return true;
        }
        return false;
    }

    public boolean deleteEmployee(int id) {
        List<Employee> employees = getAllEmployees();
        boolean removed = employees.removeIf(employee -> employee.getId() == id);
        
        if (removed) {
            saveAllEmployees(employees);
            return true;
        }
        return false;
    }

    private Employee parseEmployee(String csvLine) {
        String[] parts = csvLine.split(",");
        Employee employee = new Employee();
        
        if (parts.length >= 4) {
            try {
                employee.setId(Integer.parseInt(parts[0]));
                employee.setName(parts[1]);
                employee.setPosition(parts[2]);
                employee.setSalary(Double.parseDouble(parts[3]));
            } catch (NumberFormatException e) {
                System.out.println("Error parsing employee data: " + e.getMessage());
            }
        }
        
        return employee;
    }
}
