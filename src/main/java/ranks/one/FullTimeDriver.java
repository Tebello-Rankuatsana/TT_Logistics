package ranks.one;

import java.sql.Date;

public class FullTimeDriver {
    private int personId;
    private String fullName;
    private String employeeNumber;
    private double salary;
    private Date hireDate;

    public FullTimeDriver(int personId, String fullName, String employeeNumber, double salary, Date hireDate) {
        this.personId = personId;
        this.fullName = fullName;
        this.employeeNumber = employeeNumber;
        this.salary = salary;
        this.hireDate = hireDate;
    }

    public int getPersonId() { return personId; }
    public String getFullName() { return fullName; }
    public String getEmployeeNumber() { return employeeNumber; }
    public double getSalary() { return salary; }
    public Date getHireDate() { return hireDate; }
}