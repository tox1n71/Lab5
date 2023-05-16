package ru.itmo.lab5.readers;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.TreeSet;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ru.itmo.lab5.exceptions.InputException;
import ru.itmo.lab5.worker.*;

import static ru.itmo.lab5.readers.WorkerReader.formatter;
import static ru.itmo.lab5.readers.WorkerReader.formatterTime;

public class XMLReader {

    public static TreeSet<Worker> parseXML(File file, OrganizationReader organizationReader) throws ParserConfigurationException, IOException, SAXException {
        TreeSet<Worker> workers = new TreeSet<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(file);
        NodeList nodeList = document.getDocumentElement().getElementsByTagName("Worker");
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                boolean add = true;
                Element element = (Element) node;
                Worker worker = new Worker();
                Organization organization = new Organization();
                Coordinates coordinates = new Coordinates();
                try {
                    int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
                    worker.setId(id);
                }catch (Exception e){
                    add = false;
                }
                String name = element.getElementsByTagName("name").item(0).getTextContent();
                worker.setName(name);
                try {
                    double x = Double.parseDouble(element.getElementsByTagName("x").item(0).getTextContent());
                coordinates.setX(x);}catch (Exception e){
                    add = false;
                }
                try {
                    double y = Double.parseDouble(element.getElementsByTagName("y").item(0).getTextContent());
                coordinates.setY(y);}catch (Exception e){
                    add = false;
                }
                worker.setCoordinates(coordinates);
                LocalDate creationDate = null;
                try {
                    if (!element.getElementsByTagName("creationDate").item(0).getTextContent().equals("null")) {
                        creationDate = LocalDate.parse(element.getElementsByTagName("creationDate").item(0).getTextContent(), formatter);
                    }
                    else {
                        throw new InputException("null startdate");
                    }
                    worker.setCreationDate(creationDate);
                }catch (DateTimeParseException | InputException e){
                    add = false;
                }
                try {
                    int salary = Integer.parseInt(element.getElementsByTagName("salary").item(0).getTextContent());
                    if (salary <= 0) {
                        throw new Exception();
                    }
                    worker.setSalary(salary);
                } catch (Exception e){
                    add = false;
                }
                LocalDate startDate = null;
                try {
                    if (!element.getElementsByTagName("startDate").item(0).getTextContent().equals("null")) {
                        startDate = LocalDate.parse(element.getElementsByTagName("startDate").item(0).getTextContent(), formatter);
                    }
                    else {
                        throw new InputException("null startdate");
                    }
                    worker.setStartDate(startDate);
                }
                catch (DateTimeParseException | InputException e){
                    add = false;
                }
                try {
                    LocalDateTime endDate = null;

                if (!element.getElementsByTagName("endDate").item(0).getTextContent().equals("null")) {
                    endDate = LocalDateTime.parse(element.getElementsByTagName("endDate").item(0).getTextContent(), formatterTime);
                }
                worker.setEndDate(endDate);}
                catch (DateTimeParseException e){
                    add = false;
                }
                try {
                    Position position = Position.valueOf(element.getElementsByTagName("position").item(0).getTextContent());

                worker.setPosition(position);}
                catch (IllegalArgumentException e){
                    add =false;
                }
                try{
                    String organizationFullName = element.getElementsByTagName("fullName").item(0).getTextContent();
                    if (organizationReader.checkOrgFullName(organizationFullName)){
                        organization.setFullName(organizationFullName);
                    }
                }catch (Exception e){
                    add =false;
                }
                try {
                    float annualTurnover = Float.parseFloat(element.getElementsByTagName("annualTurnover").item(0).getTextContent());
                    organization.setAnnualTurnover(annualTurnover);
                }catch (Exception e){
                    add=false;
                }
                try {
                    int employeesCount = Integer.parseInt(element.getElementsByTagName("employeesCount").item(0).getTextContent());
                    organization.setEmployeesCount(employeesCount);
                }catch (Exception e){
                    add = false;
                }
                Address address = new Address();
                try {
                    String street = element.getElementsByTagName("street").item(0).getTextContent();
                    address.setStreet(street);
                }catch (Exception e){
                    add = false;
                }
                try {
                    String zipCode = element.getElementsByTagName("zipCode").item(0).getTextContent();
                    address.setZipCode(zipCode);
                }catch (Exception e){
                    add = false;
                }
                Location location = new Location();
                try {
                    int locationX = Integer.parseInt(element.getElementsByTagName("x").item(1).getTextContent());
                    location.setX(locationX);
                }catch (Exception e){
                    add = false;
                }
                try {
                    long locationY = Long.parseLong(element.getElementsByTagName("y").item(1).getTextContent());
                    location.setY(locationY);
                }catch (Exception e){
                    add = false;
                }
                try {
                    int locationZ = Integer.parseInt(element.getElementsByTagName("z").item(0).getTextContent());
                    location.setZ(locationZ);
                }catch (Exception e){
                    add = false;
                }
                try {
                    String locationName = element.getElementsByTagName("name").item(1).getTextContent();
                    location.setName(locationName);
                }catch (Exception e){
                    add = false;
                }
                address.setTown(location);
                organization.setPostalAddress(address);
                worker.setOrganization(organization);
                if (add){
                    workers.add(worker);
                }
            }
        }

        return workers;
    }

}
