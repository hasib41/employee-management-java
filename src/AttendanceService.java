import java.time.LocalDate;
import java.time.LocalTime;

public class AttendanceService {
    private final AttendanceRepository repository;
    private final EmployeeService employeeService;

    public AttendanceService(AttendanceRepository repository, EmployeeService employeeService) {
        this.repository = repository;
        this.employeeService = employeeService;
    }

    public boolean checkIn(int employeeId, LocalDate date, LocalTime time) {
        // Verify employee exists
        Employee employee = employeeService.findEmployeeById(employeeId);
        if (employee == null) {
            return false;
        }
        
        // Check if attendance for this employee and date already exists
        Attendance existingAttendance = repository.getAttendanceByEmployeeAndDate(employeeId, date);
        
        if (existingAttendance != null) {
            // Already checked in
            if (existingAttendance.getCheckInTime() != null) {
                return false;
            }
            
            // Update existing record
            existingAttendance.setCheckInTime(time);
            existingAttendance.setStatus("PRESENT");
            return repository.updateAttendance(existingAttendance);
        } else {
            // Create new attendance record
            Attendance newAttendance = new Attendance(employeeId, date, time, null, "PRESENT");
            return repository.addAttendance(newAttendance);
        }
    }

    public boolean checkOut(int employeeId, LocalDate date, LocalTime time) {
        // Get existing attendance record
        Attendance attendance = repository.getAttendanceByEmployeeAndDate(employeeId, date);
        
        if (attendance == null || attendance.getCheckInTime() == null) {
            return false; // Can't check out if not checked in
        }
        
        // Make sure check-out time is after check-in time
        if (time.isBefore(attendance.getCheckInTime())) {
            return false;
        }
        
        // Update attendance record
        attendance.setCheckOutTime(time);
        
        // Update status if needed (e.g., HALF_DAY if less than X hours)
        double hoursWorked = attendance.getHoursWorked();
        if (hoursWorked < 4.0) {
            attendance.setStatus("HALF_DAY");
        }
        
        return repository.updateAttendance(attendance);
    }

    public Attendance getAttendanceByEmployeeAndDate(int employeeId, LocalDate date) {
        return repository.getAttendanceByEmployeeAndDate(employeeId, date);
    }
}