package ranks.one;

public class DriverAssignment {
    private int assignmentId;
    private String driverName;
    private String deliveryInfo;
    private String role;
    private double hoursWorked;

    public DriverAssignment(int assignmentId, String driverName, String deliveryInfo, String role, double hoursWorked) {
        this.assignmentId = assignmentId;
        this.driverName = driverName;
        this.deliveryInfo = deliveryInfo;
        this.role = role;
        this.hoursWorked = hoursWorked;
    }

    public int getAssignmentId() { return assignmentId; }
    public String getDriverName() { return driverName; }
    public String getDeliveryInfo() { return deliveryInfo; }
    public String getRole() { return role; }
    public double getHoursWorked() { return hoursWorked; }
}