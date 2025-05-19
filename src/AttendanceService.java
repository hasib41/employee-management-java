import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    public boolean markAbsent(int employeeId, LocalDate date) {
        // Verify employee exists
        Employee employee = employeeService.findEmployeeById(employeeId);
        if (employee == null) {
            return false;
        }
        
        // Check if attendance for this employee and date already exists
        Attendance existingAttendance = repository.getAttendanceByEmployeeAndDate(employeeId, date);
        
        if (existingAttendance != null) {
            // Update existing record
            existingAttendance.setCheckInTime(null);
            existingAttendance.setCheckOutTime(null);
            existingAttendance.setStatus("ABSENT");
            return repository.updateAttendance(existingAttendance);
        } else {
            // Create new attendance record
            Attendance newAttendance = new Attendance(employeeId, date, null, null, "ABSENT");
            return repository.addAttendance(newAttendance);
        }
    }

    public List<Attendance> getAttendanceByEmployeeId(int employeeId) {
        return repository.getAttendanceByEmployeeId(employeeId);
    }

    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return repository.getAttendanceByDate(date);
    }

    public Attendance getAttendanceByEmployeeAndDate(int employeeId, LocalDate date) {
        return repository.getAttendanceByEmployeeAndDate(employeeId, date);
    }

    public Map<String, Long> getAttendanceSummary(int employeeId, LocalDate startDate, LocalDate endDate) {
        List<Attendance> attendanceList = repository.getAttendanceByEmployeeId(employeeId);
        
        // Filter by date range
        List<Attendance> filteredList = attendanceList.stream()
                .filter(a -> !a.getDate().isBefore(startDate) && !a.getDate().isAfter(endDate))
                .collect(Collectors.toList());
        
        // Count by status
        Map<String, Long> summary = filteredList.stream()
                .collect(Collectors.groupingBy(
                        Attendance::getStatus,
                        Collectors.counting()
                ));
        
        return summary;
    }

    public double getTotalHoursWorked(int employeeId, LocalDate startDate, LocalDate endDate) {
        List<Attendance> attendanceList = repository.getAttendanceByEmployeeId(employeeId);
        
        // Filter by date range and calculate total hours
        return attendanceList.stream()
                .filter(a -> !a.getDate().isBefore(startDate) && !a.getDate().isAfter(endDate))
                .mapToDouble(Attendance::getHoursWorked)
                .sum();
    }
}
