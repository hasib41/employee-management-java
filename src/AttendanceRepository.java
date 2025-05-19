import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AttendanceRepository {
    private final String filePath;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    public AttendanceRepository(String filePath) {
        this.filePath = filePath;
        // Create the file if it doesn't exist
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (IOException e) {
            System.out.println("Error creating attendance file: " + e.getMessage());
        }
    }

    public List<Attendance> getAllAttendance() {
        List<Attendance> attendanceList = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    Attendance attendance = parseAttendance(line);
                    if (attendance != null) {
                        attendanceList.add(attendance);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading attendance data: " + e.getMessage());
        }
        
        return attendanceList;
    }

    public void saveAllAttendance(List<Attendance> attendanceList) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Attendance attendance : attendanceList) {
                writer.write(attendance.toCsvString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving attendance data: " + e.getMessage());
        }
    }

    public boolean addAttendance(Attendance attendance) {
        List<Attendance> attendanceList = getAllAttendance();
        
        // Check if there's already an attendance record for this employee on this date
        boolean exists = attendanceList.stream()
                .anyMatch(a -> a.getEmployeeId() == attendance.getEmployeeId() 
                              && a.getDate().equals(attendance.getDate()));
        
        if (exists) {
            return false; // Attendance record for this date already exists
        }
        
        attendanceList.add(attendance);
        saveAllAttendance(attendanceList);
        return true;
    }

    public boolean updateAttendance(Attendance updatedAttendance) {
        List<Attendance> attendanceList = getAllAttendance();
        boolean found = false;
        
        for (int i = 0; i < attendanceList.size(); i++) {
            Attendance attendance = attendanceList.get(i);
            if (attendance.getEmployeeId() == updatedAttendance.getEmployeeId() && 
                attendance.getDate().equals(updatedAttendance.getDate())) {
                attendanceList.set(i, updatedAttendance);
                found = true;
                break;
            }
        }
        
        if (found) {
            saveAllAttendance(attendanceList);
            return true;
        }
        return false;
    }

    public List<Attendance> getAttendanceByEmployeeId(int employeeId) {
        return getAllAttendance().stream()
                .filter(a -> a.getEmployeeId() == employeeId)
                .collect(Collectors.toList());
    }

    public List<Attendance> getAttendanceByDate(LocalDate date) {
        return getAllAttendance().stream()
                .filter(a -> a.getDate().equals(date))
                .collect(Collectors.toList());
    }

    public Attendance getAttendanceByEmployeeAndDate(int employeeId, LocalDate date) {
        return getAllAttendance().stream()
                .filter(a -> a.getEmployeeId() == employeeId && a.getDate().equals(date))
                .findFirst()
                .orElse(null);
    }

    private Attendance parseAttendance(String csvLine) {
        String[] parts = csvLine.split(",");
        Attendance attendance = new Attendance();
        
        if (parts.length >= 5) {
            try {
                attendance.setEmployeeId(Integer.parseInt(parts[0]));
                attendance.setDate(LocalDate.parse(parts[1], DATE_FORMATTER));
                
                // Check-in time might be empty
                if (!parts[2].isEmpty()) {
                    attendance.setCheckInTime(LocalTime.parse(parts[2], TIME_FORMATTER));
                }
                
                // Check-out time might be empty
                if (!parts[3].isEmpty()) {
                    attendance.setCheckOutTime(LocalTime.parse(parts[3], TIME_FORMATTER));
                }
                
                attendance.setStatus(parts[4]);
                return attendance;
            } catch (NumberFormatException | DateTimeParseException e) {
                System.out.println("Error parsing attendance data: " + e.getMessage());
            }
        }
        
        return null;
    }
}
