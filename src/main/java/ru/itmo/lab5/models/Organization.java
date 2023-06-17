package ru.itmo.lab5.models;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity(name = "organization")
public class Organization implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "fullname")
    private String fullName;

    @Column(name = "employeescount")
    private Integer employeescount;
    @Column(name = "annualturnover")
    private Double annualturnover;
    @Column(name = "address_street")
    private String address_street;
    @Column(name = "address_zipcode")
    private String address_zipcode;

    @Column(name = "address_town")
    private String address_town;
    @Column(name = "address_x")
    private Integer address_x;
    @Column(name = "address_y")
    private Integer address_y;

    @Column(name = "address_z")
    private Integer address_z;

    @OneToOne(mappedBy = "organization")
    private Worker worker;


    public Organization() {
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getEmployeescount() {
        return employeescount;
    }

    public void setEmployeescount(Integer employeescount) {
        this.employeescount = employeescount;
    }

    public Double getAnnualturnover() {
        return annualturnover;
    }

    public void setAnnualturnover(Double annualturnover) {
        this.annualturnover = annualturnover;
    }

    public String getAddress_street() {
        return address_street;
    }

    public void setAddress_street(String address_street) {
        this.address_street = address_street;
    }

    public String getAddress_zipcode() {
        return address_zipcode;
    }

    public void setAddress_zipcode(String address_zipcode) {
        this.address_zipcode = address_zipcode;
    }

    public String getAddress_town() {
        return address_town;
    }

    public void setAddress_town(String address_town) {
        this.address_town = address_town;
    }

    public Integer getAddress_x() {
        return address_x;
    }

    public void setAddress_x(Integer address_x) {
        this.address_x = address_x;
    }

    public Integer getAddress_y() {
        return address_y;
    }

    public void setAddress_y(Integer address_y) {
        this.address_y = address_y;
    }

    public Integer getAddress_z() {
        return address_z;
    }

    public void setAddress_z(Integer address_z) {
        this.address_z = address_z;
    }

    public Organization(String fullName, Integer employeescount, Double annualturnover, String address_street, String address_zipcode, String address_town, Integer address_x, Integer address_y, Integer address_z, Worker worker) {
        this.fullName = fullName;
        this.employeescount = employeescount;
        this.annualturnover = annualturnover;
        this.address_street = address_street;
        this.address_zipcode = address_zipcode;
        this.address_town = address_town;
        this.address_x = address_x;
        this.address_y = address_y;
        this.address_z = address_z;
        this.worker = worker;
    }
}
