package ranks.one;

public class DriverWorkload {
    private int personId;
    private String driverName;
    private int numberOfDeliveries;
    private double totalHoursWorked;

    public DriverWorkload(int personId, String driverName, int numberOfDeliveries, double totalHoursWorked) {
        this.personId = personId;
        this.driverName = driverName;
        this.numberOfDeliveries = numberOfDeliveries;
        this.totalHoursWorked = totalHoursWorked;
    }

    public int getPersonId() {
        return personId;
    }

    public String getDriverName() {
        return driverName;
    }

    public int getNumberOfDeliveries() {
        return numberOfDeliveries;
    }

    public double getTotalHoursWorked() {
        return totalHoursWorked;
    }
}