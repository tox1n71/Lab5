package ru.itmo.lab5.worker;

import ru.itmo.lab5.exceptions.InputException;

public class Address {
    private String street; //Строка не может быть пустой, Поле может быть null

    private String zipCode; //Длина строки должна быть не меньше 4, Поле не может быть null
    private Location town; //Поле не может быть null

    public Address(){}
    public Address(String street, String zipCode, Location town) {
        this.street=street;
        this.zipCode=zipCode;
        this.town=town;
    }
    public String getStreet() {
        return street;
    }

    public void setStreet(String street)  {

        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode)  {
        this.zipCode = zipCode;
    }

    public Location getTown() {
        return town;
    }

    public void setTown(Location town) {

        this.town = town;
    }


    @Override
    public String toString() {
        return "Adress{" +
                "\n\t\t\t\t street=" + street +
                ",\n\t\t\t\t zipCode=" + zipCode +
                ",\n\t\t\t\t town=" + town +
                '}';
    }
    public String toXml() {
        return
                "\n\t\t\t<street>" + street + "</street>"+
                "\n\t\t\t<zipCode>" + zipCode + "</zipCode>"+
                "\n\t\t\t<town>" + town.toXml() + "\n\t\t\t</town>"+ '\n';
    }
}
