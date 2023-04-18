package ru.itmo.lab5;

import ru.itmo.lab5.server.CommandReader;
import ru.itmo.lab5.server.OrganizationReader;
import ru.itmo.lab5.server.WorkerReader;
import ru.itmo.lab5.worker.*;

import java.io.*;
import java.time.LocalDate;
import java.util.Iterator;
import java.util.TreeSet;

class Main{

    public static void main(String [] args) throws IOException{

//        OrganizationReader organizationReader = new OrganizationReader();
//        WorkerReader workerReader = new WorkerReader(organizationReader);
//        CommandReader commandReader = new CommandReader(organizationReader, workerReader);
//        commandReader.readCommand();

        Worker worker = new Worker();
        worker.setId(123123123);
        worker.setName("Ivan");
        worker.setCreationDate(LocalDate.now());
        worker.setPosition(Position.LEAD_DEVELOPER);
        worker.setSalary(15000);
        Coordinates coordinates = new Coordinates();
        coordinates.setX(1);
        coordinates.setY(Double.parseDouble("91"));
        Organization org = new Organization();
        Address address = new Address();
        Location loc = new Location();
        loc.setName("Los-Angeles");
        loc.setX(15);
        loc.setY(Long.parseLong("9158"));
        loc.setZ(Integer.parseInt("-19"));
        address.setZipCode("19875L");
        address.setStreet("84 Avylane Street");
        address.setTown(loc);
        org.setFullName("Apple Inc");
        org.setAnnualTurnover(Float.parseFloat("2000.98"));
        org.setEmployeesCount(400);
        org.setPostalAddress(address);
        worker.setCoordinates(coordinates);
        worker.setOrganization(org);
        String workerXML = worker.toXml();

        try {
            FileWriter fWriter = new FileWriter("LolKekMambet.txt");
            fWriter.write(workerXML);
            fWriter.close();
        }
        catch ( IOException e ) {
            e.printStackTrace();
        }
    }
}


