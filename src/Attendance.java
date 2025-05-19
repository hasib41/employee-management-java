import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Attendance {
    private int employeeId;
    private LocalDate date;
    private LocalTime checkInTime;
    private LocalTime checkOutTime; // Can be null if employee hasn't checked out yet
    private String status; // "PRESENT", "ABSENT", "LATE", "HALF_DAY"

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public Attendance(int employeeId, LocalDate date, LocalTime checkInTime, LocalTime checkOutTime, String status) {
        this.employeeId = employeeId;
        this.date = date;
        this.checkInTime = checkInTime;
        this.checkOutTime = checkOutTime;
        this.status = status;
    }

    public Attendance() {
        // Default constructor for file reading
    }

    // Getters and setters
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(LocalTime checkInTime) {
        this.checkInTime = checkInTime;
    }

    public LocalTime getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(LocalTime checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Convert to CSV string for file storage
    public String toCsvString() {
        String checkInStr = (checkInTime != null) ? checkInTime.format(TIME_FORMATTER) : "";
        String checkOutStr = (checkOutTime != null) ? checkOutTime.format(TIME_FORMATTER) : "";
        
        return employeeId + "," + 
               date.format(DATE_FORMATTER) + "," + 
               checkInStr + "," + 
               checkOutStr + "," + 
               status;
    }

    // Method to calculate hours worked (if checked out)
    public double getHoursWorked() {
        if (checkInTime == null || checkOutTime == null) {
            return 0.0;
        }
        
        int checkInMinutes = checkInTime.getHour() * 60 + checkInTime.getMinute();
        int checkOutMinutes = checkOutTime.getHour() * 60 + checkOutTime.getMinute();
        
        // If checked out earlier than checked in (error or overnight shift)
        if (checkOutMinutes < checkInMinutes) {
            return 0.0;
        }
        
        return (checkOutMinutes - checkInMinutes) / 60.0;
    }

    @Override
    public String toString() {
        String checkInStr = (checkInTime != null) ? checkInTime.toString() : "Not checked in";
        String checkOutStr = (checkOutTime != null) ? checkOutTime.toString() : "Not checked out";
        
        return "Employee ID: " + employeeId + 
               ", Date: " + date + 
               ", Check-in: " + checkInStr + 
               ", Check-out: " + checkOutStr + 
               ", Status: " + status +
               ", Hours: " + String.format("%.2f", getHoursWorked());
    }
}
