package ru.itmo.lab5.worker;


import org.hibernate.boot.model.relational.SqlStringGenerationContext;
import ru.itmo.lab5.utils.User;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

import static ru.itmo.lab5.readers.WorkerReader.formatter;
import static ru.itmo.lab5.readers.WorkerReader.formatterTime;

public class Worker implements Comparable<Worker>, Serializable {
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private LocalDate creationDate;//Поле не может быть null, Значение этого поля должно генерироваться автоматически

    private Integer salary; //Поле не может быть null, Значение поля должно быть больше 0
    private LocalDate startDate; //Поле не может быть null
    private LocalDateTime endDate; //Поле может быть null
    private Position position; //Поле не может быть null
    private Organization organization; //Поле может быть null
    private User user;


    public Worker(){}

    public Worker(String name, Coordinates coordinates, int salary, LocalDate startDate, LocalDateTime endDate, Position position, Organization organization){
        this.name = name;
        this.coordinates = coordinates;
        this.salary = salary;
        this.startDate = startDate;
        this.endDate = endDate;
        this.position = position;
        this.organization = organization;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name){
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }
    public String getCreationDateString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate localDate = LocalDate.now();
        String formattedDate = localDate.format(formatter);
        return formattedDate;
    }

    public void setCreationDate(LocalDate creationDate) throws NullPointerException {
        if (creationDate == null){
            throw new NullPointerException("Дата создания не может быть null");
        }
        this.creationDate = creationDate;
    }

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary){
        this.salary = salary;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate){

        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position){
        this.position = position;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int compareTo(Worker o) {
        int res = this.salary.compareTo(o.getSalary());
        if (res == 0){
            res = this.organization.compareTo(o.getOrganization());
        }
        return res;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Worker worker = (Worker) o;
        return salary.equals(worker.salary) && Objects.equals(organization, worker.organization);
    }

    @Override
    public int hashCode() {
        return Objects.hash(salary, organization);
    }

    @Override
    public String toString() {
        if (getEndDate() == null){
            return "Worker{" +
                    "\n\t owner=" + user.getName() +
                    "\n\t id=" + id +
                    ",\n\t name=" + name  +
                    ",\n\t coordinates=" + coordinates +
                    ",\n\t creationDate=" + creationDate.format(formatter) +
                    ",\n\t salary=" + salary +
                    ",\n\t startDate=" + startDate.format(formatter) +
                    ",\n\t endDate=" + endDate +
                    ",\n\t position=" + position +
                    ",\n\t organization=" + organization +
                    "\n\t}\n";
        }
        else {
            return "Worker{" +
                    "\n\t id=" + id +
                    ",\n\t name=" + name  +
                    ",\n\t coordinates=" + coordinates +
                    ",\n\t creationDate=" + creationDate.format(formatter) +
                    ",\n\t salary=" + salary +
                    ",\n\t startDate=" + startDate.format(formatter) +
                    ",\n\t endDate=" + endDate.format(formatterTime) +
                    ",\n\t position=" + position +
                    ",\n\t organization=" + organization +
                    "\n\t}\n";
        }
    }

    public String toXml() {
        if (getEndDate() == null){
            return "<Worker>" +
                    "\n\t<id>" + id + "</id>"+
                    "\n\t<name>" + name + "</name>"+
                    "\n\t<coordinates>" + coordinates.toXml() + "</coordinates>\t"+
                    "\n\t<creationDate>" + creationDate.format(formatter) + "</creationDate>"+
                    "\n\t<salary>" + salary + "</salary>"+
                    "\n\t<startDate>" + startDate.format(formatter) + "</startDate>"+
                    "\n\t<endDate>" + endDate + "</endDate>"+
                    "\n\t<position>" + position + "</position>"+
                    "\n\t<organization>" + organization.toXml() + "</organization>\n" +
                    "</Worker>\n";
        }
        else {
            return "<Worker>" +
                    "\n\t<id>" + id + "</id>"+
                    "\n\t<name>" + name + "</name>"+
                    "\n\t<coordinates>" + coordinates.toXml() + "</coordinates>\t"+
                    "\n\t<creationDate>" + creationDate.format(formatter) + "</creationDate>"+
                    "\n\t<salary>" + salary + "</salary>"+
                    "\n\t<startDate>" + startDate.format(formatter) + "</startDate>"+
                    "\n\t<endDate>" + endDate.format(formatterTime) + "</endDate>"+
                    "\n\t<position>" + position + "</position>"+
                    "\n\t<organization>" + organization.toXml() + "</organization>\n" +
                    "</Worker>\n";
        }

    }
}





