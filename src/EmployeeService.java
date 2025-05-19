import java.util.List;
import java.util.stream.Collectors;

public class EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public boolean addEmployee(Employee employee) {
        return repository.addEmployee(employee);
    }

    public List<Employee> getAllEmployees() {
        return repository.getAllEmployees();
    }

    public Employee findEmployeeById(int id) {
        return repository.getAllEmployees().stream()
                .filter(e -> e.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Employee> findEmployeesByName(String name) {
        return repository.getAllEmployees().stream()
                .filter(e -> e.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }
    
    public List<Employee> findEmployeesByPosition(String position) {
        return repository.getAllEmployees().stream()
                .filter(e -> e.getPosition().toLowerCase().contains(position.toLowerCase()))
                .collect(Collectors.toList());
    }

    public boolean updateEmployee(Employee employee) {
        return repository.updateEmployee(employee);
    }

    public boolean deleteEmployee(int id) {
        return repository.deleteEmployee(id);
    }

    public int getNextEmployeeId() {
        List<Employee> employees = repository.getAllEmployees();
        if (employees.isEmpty()) {
            return 1;
        }
        
        return employees.stream()
                .mapToInt(Employee::getId)
                .max()
                .orElse(0) + 1;
    }
}
