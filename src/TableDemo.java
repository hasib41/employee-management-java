public class TableDemo {
    public static void main(String[] args) {
        System.out.println("Employee Table Display Example:");
        
        // Table header
        System.out.println("\n+--------+----------------------+----------------------+---------------+");
        System.out.println("| ID     | Name                 | Position             | Salary        |");
        System.out.println("+--------+----------------------+----------------------+---------------+");
        
        // Sample data rows
        System.out.printf("| %-6d | %-20s | %-20s | $%-12.2f |\n", 
                4, "Hasan", "Software engineer", 20000.00);
        System.out.printf("| %-6d | %-20s | %-20s | $%-12.2f |\n", 
                5, "John Smith", "Project Manager", 45000.00);
        System.out.printf("| %-6d | %-20s | %-20s | $%-12.2f |\n", 
                6, "Maria Rodriguez", "UX Designer", 35000.00);
        
        // Table footer
        System.out.println("+--------+----------------------+----------------------+---------------+");
        System.out.println("Total employees: 3");
    }
}
