package ru.itmo.lab5.worker;

import java.io.Serializable;

public class Organization implements Comparable<Organization>, Serializable {

    private int id;

    public int getId() {
        return id;
    }

    private String fullName; //Строка не может быть пустой, Значение этого поля должно быть уникальным, Поле может быть null
    private Float annualTurnover; //Поле может быть null, Значение поля должно быть больше 0
    private long employeesCount; //Значение поля должно быть больше 0
    private Address postalAddress; //Поле не может быть null


    public Organization(){}
    public Organization(String fullName, Float annualTurnover, long employeesCount, Address postalAddress) {
        this.fullName = fullName;
        this.annualTurnover = annualTurnover;
        this.employeesCount = employeesCount;
        this.postalAddress = postalAddress;
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {

        this.fullName = fullName;
    }

    public Float getAnnualTurnover() {
        return annualTurnover;
    }

    public void setAnnualTurnover(Float annualTurnover)  {

        this.annualTurnover = annualTurnover;
    }

    public long getEmployeesCount() {
        return employeesCount;
    }

    public void setEmployeesCount(long employeesCount) {
        this.employeesCount = employeesCount;
    }

    public Address getPostalAddress() {
        return postalAddress;
    }

    public void setPostalAddress(Address postalAddress) {
        this.postalAddress = postalAddress;
    }




    @Override
    public String toString() {
        return "organization{" +
                "\n\t\t\t fullName=\"" + fullName + "\"" +
                ",\n\t\t\t annualTurnover=" + annualTurnover +
                ",\n\t\t\t employeesCount=" + employeesCount +
                ",\n\t\t\t postalAddress=" + postalAddress +
                '}';
    }
    public String toXml() {
        return "<Organization>" +
                "\n\t\t<fullName>" + fullName + "</fullName>"+
                "\n\t\t<annualTurnover>" + annualTurnover + "</annualTurnover>"+
                "\n\t\t<employeesCount>" + employeesCount + "</employeesCount>"+
                "\n\t\t<postalAddress>" + postalAddress.toXml() + "\t\t</postalAddress>" +'\n'+
                "\t</Organization>";
    }

    @Override
    public int compareTo(Organization o) {
        int res = this.annualTurnover.compareTo(o.getAnnualTurnover());
        if (res == 0){
            res = this.fullName.compareTo(o.getFullName());
        }
        return res;
    }
}
