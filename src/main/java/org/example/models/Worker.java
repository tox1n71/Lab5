package org.example.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity(name = "worker")
public class Worker implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name= "name")
    private String name;

    @Override
    public String toString() {
        return "Worker{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates_x=" + coordinates_x +
                ", coordinates_y=" + coordinates_y +
                ", creationdDate=" + creationdDate +
                ", salary=" + salary +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", position='" + position + '\'' +
                ", organization=" + organization +
                ", user=" + user +
                '}';
    }

    @Column(name = "coordinates_x")
    private double coordinates_x;

    @Column(name = "coordinates_y")
    private double coordinates_y;
    @Column(name = "creation_date")
    private LocalDate creationdDate;
    @Column(name = "salary")
    private int salary;
    @Column(name = "start_date")
    private LocalDate startDate;
    @Column(name ="end_date")
    private LocalDateTime endDate;
    @Column(name="position")
    private String position;
    @OneToOne
    @JoinColumn(name="organization_fullname", referencedColumnName = "fullname")
    private Organization organization;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "username",referencedColumnName = "username")
    private User user;

    public Worker(String name, double coordinates_x, double coordinates_y, LocalDate creationdDate, int salary, LocalDate startDate, LocalDateTime endDate, String position, Organization organization, User user) {
        this.name = name;
        this.coordinates_x = coordinates_x;
        this.coordinates_y = coordinates_y;
        this.creationdDate = creationdDate;
        this.salary = salary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.position = position;
        this.organization = organization;
        this.user = user;
    }

    public Worker() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCoordinates_x() {
        return coordinates_x;
    }

    public void setCoordinates_x(double coordinates_x) {
        this.coordinates_x = coordinates_x;
    }

    public double getCoordinates_y() {
        return coordinates_y;
    }

    public void setCoordinates_y(double coordinates_y) {
        this.coordinates_y = coordinates_y;
    }

    public LocalDate getCreationdDate() {
        return creationdDate;
    }

    public void setCreationdDate(LocalDate creationdDate) {
        this.creationdDate = creationdDate;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }


}
