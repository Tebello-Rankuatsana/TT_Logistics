package ranks.one;

import java.sql.Date;

public class VehicleMaintenance {
    private int maintenanceId;
    private String vehicleInfo;
    private Date maintenanceDate;
    private String maintenanceType;
    private double cost;

    public VehicleMaintenance(int maintenanceId, String vehicleInfo, Date maintenanceDate, String maintenanceType, double cost) {
        this.maintenanceId = maintenanceId;
        this.vehicleInfo = vehicleInfo;
        this.maintenanceDate = maintenanceDate;
        this.maintenanceType = maintenanceType;
        this.cost = cost;
    }

    public int getMaintenanceId() { return maintenanceId; }
    public String getVehicleInfo() { return vehicleInfo; }
    public Date getMaintenanceDate() { return maintenanceDate; }
    public String getMaintenanceType() { return maintenanceType; }
    public double getCost() { return cost; }
}