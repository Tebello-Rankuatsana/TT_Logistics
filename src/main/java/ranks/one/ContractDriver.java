package ranks.one;

public class ContractDriver {
    private int personId;
    private String fullName;
    private String contractNumber;
    private double hourlyRate;

    public ContractDriver(int personId, String fullName, String contractNumber, double hourlyRate) {
        this.personId = personId;
        this.fullName = fullName;
        this.contractNumber = contractNumber;
        this.hourlyRate = hourlyRate;
    }

    public int getPersonId() { return personId; }
    public String getFullName() { return fullName; }
    public String getContractNumber() { return contractNumber; }
    public double getHourlyRate() { return hourlyRate; }
}