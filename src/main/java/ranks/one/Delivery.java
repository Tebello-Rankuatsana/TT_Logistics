package ranks.one;

import java.sql.Date;

public class Delivery {
    private int deliveryId;
    private Date deliveryDate;
    private String origin;
    private String destination;
    private String deliveryStatus;
    private String clientName;
    private String vehicleRegistration;

    public Delivery(int deliveryId, Date deliveryDate, String origin, String destination,
                    String deliveryStatus, String clientName, String vehicleRegistration) {
        this.deliveryId = deliveryId;
        this.deliveryDate = deliveryDate;
        this.origin = origin;
        this.destination = destination;
        this.deliveryStatus = deliveryStatus;
        this.clientName = clientName;
        this.vehicleRegistration = vehicleRegistration;
    }

    // Getters
    public int getDeliveryId() { return deliveryId; }
    public Date getDeliveryDate() { return deliveryDate; }
    public String getOrigin() { return origin; }
    public String getDestination() { return destination; }
    public String getDeliveryStatus() { return deliveryStatus; }
    public String getClientName() { return clientName; }
    public String getVehicleRegistration() { return vehicleRegistration; }
}