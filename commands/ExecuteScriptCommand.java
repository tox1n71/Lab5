package ru.itmo.lab5.commands;

import org.apache.commons.lang3.RandomStringUtils;
import ru.itmo.lab5.exceptions.InputException;
import ru.itmo.lab5.exceptions.UniqueException;
import ru.itmo.lab5.exceptions.WrongScriptDataException;
import ru.itmo.lab5.readers.OrganizationReader;
import ru.itmo.lab5.server.CollectionManager;
import ru.itmo.lab5.worker.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static ru.itmo.lab5.readers.WorkerReader.formatter;
import static ru.itmo.lab5.readers.WorkerReader.formatterTime;

public class ExecuteScriptCommand implements Command {


    private String name = "execute_script";
    private File file;
    private CollectionManager collectionManager;
    public ExecuteScriptCommand(){

    }

    public ExecuteScriptCommand(File file) {
        this.file = file;
    }
    @Override
    public String execute(){
        try {
            return collectionManager.executeScript();
        } catch (IOException e) {
            return "Ошибка в файле скрипта";
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public void setCollectionManager(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;
    }
    @Override
    public String getName(){
        return name;
    }
    // Геттеры и сеттеры
    public Worker readWorkerFromFile(BufferedReader readerDad, OrganizationReader organizationReader) throws IOException, WrongScriptDataException {
        BufferedReader reader = readerDad;
        {
            boolean add = true;
            Worker worker = new Worker();
            String name = reader.readLine();
            if (name == null || name.isEmpty()) {
                throw new InputException("Неверный формат имени, переделайте скрипт");
            }
            Coordinates coordinates = new Coordinates();
            try {
                double coordinateX = Double.parseDouble(reader.readLine());
                coordinates.setX(coordinateX);
            } catch (NumberFormatException e) {
                add = false;
            }
            try {
                double coordinateY = Double.parseDouble(reader.readLine());
                coordinates.setY(coordinateY);
            } catch (NumberFormatException | NullPointerException e) {
                add = false;
            }
            try {
                int salary = Integer.parseInt(reader.readLine());
                worker.setSalary(salary);
            } catch (NumberFormatException e) {
                add = false;
            }
            String startDateString = reader.readLine();
            if (startDateString == null || startDateString.isEmpty()) {
                add = false;
            }
            LocalDate startDate;
            try {
                startDate = LocalDate.parse(startDateString, formatter);
                worker.setStartDate(startDate);
            } catch (DateTimeParseException | NullPointerException e) {
                add = false;
            }

            String endDateString = reader.readLine();
            if (endDateString != null) {
                LocalDateTime endDate;
                try {
                    endDate = LocalDateTime.parse(endDateString, formatterTime);
                    worker.setEndDate(endDate);
                } catch (DateTimeParseException e) {
                    add = false;
                }
            } else {
                worker.setEndDate(null);
            }
            Position position;
            try {
                position = Position.valueOf(reader.readLine());
                worker.setPosition(position);
            } catch (IllegalArgumentException | NullPointerException e) {
                add = false;
            }
            String organizationName = reader.readLine();
            if (organizationName == null || organizationName.isEmpty()) {
                add = false;
            }
            try {
                organizationReader.checkOrgFullName(organizationName);
            } catch (UniqueException e) {
                System.err.println(e.getMessage() + organizationReader.organizationsFullNames);
                organizationName = "NotUniqueName#" + RandomStringUtils.randomAscii(4, 7);
            }
            Float annualTurnover;
            try {
                annualTurnover = Float.parseFloat(reader.readLine());
            } catch (NumberFormatException | NullPointerException e) {
                annualTurnover = null;
            }
            if (annualTurnover != null && annualTurnover <= 0) {
                add = false;
            }
            long employeesCount = 0;
            try {
                employeesCount = Long.parseLong(reader.readLine());
            } catch (NumberFormatException e) {
                add = false;
            }
            if (employeesCount <= 0) {
                add = false;
            }
            String street = reader.readLine();
            if (street == null || street.isEmpty()) {
                add = false;

            }
            String zipCode = reader.readLine();
            if (zipCode == null || zipCode.length() < 4) {
                add = false;

            }
            Location location = new Location();
            try {
                int locationX = Integer.parseInt(reader.readLine());
                location.setX(locationX);
            } catch (NumberFormatException e) {
                add = false;
            }
            try {
                long locationY = Long.parseLong(reader.readLine());
                location.setY(locationY);
            } catch (NumberFormatException e) {
                add = false;
            }
            try {
                int locationZ = Integer.parseInt(reader.readLine());
                location.setZ(locationZ);
            } catch (NumberFormatException e) {
                add = false;
            }
            String locationName = reader.readLine();
            if (locationName == null) {
                add = false;
            }
            location.setName(locationName);
            Address postalAddress = new Address();
            postalAddress.setStreet(street);
            postalAddress.setZipCode(zipCode);
            postalAddress.setTown(location);
            Organization organization = new Organization(organizationName, annualTurnover, employeesCount, postalAddress);
            worker.setOrganization(organization);
            worker.setId(1 + (int) (Math.random() * Integer.MAX_VALUE));
            worker.setCreationDate(LocalDate.now());
            worker.setCoordinates(coordinates);
            worker.setName(name);
            if (add)
                return worker;
            else
                throw new WrongScriptDataException("Данные в скрипте введены не верны. Измените скрипт и повторите попытку позже");
        }
    }

}
