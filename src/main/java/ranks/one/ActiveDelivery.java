package ranks.one;

import java.sql.Date;

public class ActiveDelivery {
    private int deliveryId;
    private Date deliveryDate;
    private String clientName;
    private String vehicleRegistration;
    private String deliveryStatus;

    public ActiveDelivery(int deliveryId, Date deliveryDate, String clientName, String vehicleRegistration, String deliveryStatus) {
        this.deliveryId = deliveryId;
        this.deliveryDate = deliveryDate;
        this.clientName = clientName;
        this.vehicleRegistration = vehicleRegistration;
        this.deliveryStatus = deliveryStatus;
    }

    public int getDeliveryId() { return deliveryId; }
    public Date getDeliveryDate() { return deliveryDate; }
    public String getClientName() { return clientName; }
    public String getVehicleRegistration() { return vehicleRegistration; }
    public String getDeliveryStatus() { return deliveryStatus; }
}