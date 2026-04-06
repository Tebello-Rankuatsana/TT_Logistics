package ranks.one;

import java.sql.Date;

public class Vehicle {
    private int vehicleId;
    private String registrationNumber;
    private String vehicleType;
    private int capacity;
    private Date purchaseDate;
    private String depotName;

    public Vehicle(int vehicleId, String registrationNumber, String vehicleType, int capacity, Date purchaseDate, String depotName) {
        this.vehicleId = vehicleId;
        this.registrationNumber = registrationNumber;
        this.vehicleType = vehicleType;
        this.capacity = capacity;
        this.purchaseDate = purchaseDate;
        this.depotName = depotName;
    }

    // Getters
    public int getVehicleId() { return vehicleId; }
    public String getRegistrationNumber() { return registrationNumber; }
    public String getVehicleType() { return vehicleType; }
    public int getCapacity() { return capacity; }
    public Date getPurchaseDate() { return purchaseDate; }
    public String getDepotName() { return depotName; }
}