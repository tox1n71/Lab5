package ru.itmo.lab5.server;

import ru.itmo.lab5.exceptions.InputException;
import ru.itmo.lab5.exceptions.UniqueException;
import ru.itmo.lab5.worker.Address;
import ru.itmo.lab5.worker.Location;
import ru.itmo.lab5.worker.Organization;


import java.util.HashSet;

import java.util.Iterator;
import java.util.Scanner;

public class OrganizationReader {
    HashSet<String> organizationsFullNames = new HashSet<>();

    public Organization readOrganization() {
        Organization organization = new Organization();
        organization.setFullName(readFullName());
        organization.setAnnualTurnover(readAnnualTurnover());
        organization.setEmployeesCount(readEmployeesCount());
        organization.setPostalAddress(readPostalAddress());
        return organization;
    }

    private Location readLocation() {
        Scanner scanner = new Scanner(System.in);
        Location location = new Location();

        System.out.println("Введите координату организации x");
        int locX;
        while (true) {
            try {
                locX = Integer.parseInt(scanner.nextLine());
                break;
            } catch (Exception e) {
                System.err.println("Некорректный ввод координаты x. Введите число.");
            }
        }
        location.setX(locX);

        System.out.println("Введите координату организации y");
        Long locY;
        while (true) {
            try {
                locY = Long.parseLong(scanner.nextLine());
                break;
            } catch (Exception e) {
                System.err.println("Некорректный ввод координаты y. Введите число.");
            }
        }
        location.setY(locY);

        System.out.println("Введите координату организации z");
        Integer locZ;
        while (true) {
            try {
                locZ = Integer.parseInt(scanner.nextLine());
                break;
            } catch (Exception e) {
                System.err.println("Некорректный ввод координаты z. Введите число.");
            }
        }
        location.setZ(locZ);

        System.out.println("Введите название города организации");
        String townName = "";
        while (townName.trim().isEmpty()) {
            townName = scanner.nextLine();
            if (townName.trim().isEmpty()) {
                System.err.println("Имя не может быть пустым. Попробуйте еще раз.");
            }
        }
        location.setName(townName);

        return location;
    }

    private String readStreet() {
        String street = "";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите название улицы: ");
        while (street.trim().isEmpty()) {
            street = scanner.nextLine();
            if (street.trim().isEmpty()) {
                System.err.println("Название улицы не может быть пустым. Попробуйте еще раз.");
            }
        }
        return street.trim();
    }

    private String readZipCode() throws InputException {
        String zipCode = "";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите почтовый индекс: ");
        while (zipCode.trim().isEmpty() || zipCode.length() < 4) {
            zipCode = scanner.nextLine();
            if (zipCode.trim().isEmpty()) {
                System.err.println("Почтовый индекс не может быть пустым. Попробуйте еще раз.");
            } else if (zipCode.length() < 4) {
                System.err.println("Длина почтового индекса должна быть больше 4. Попробуйте еще раз.");
            }
        }
        return zipCode.trim();
    }

    private Address readPostalAddress() {
        Address postalAddress = new Address();

        postalAddress.setStreet(readStreet());


        try {
            postalAddress.setZipCode(readZipCode());
        } catch (InputException e) {
            System.err.println(e.getMessage());
        }
        postalAddress.setTown(readLocation());
        return postalAddress;
    }

    private String readFullName() {
        Scanner scanner = new Scanner(System.in);
        String fullName;
        boolean isUnique = false;
        System.out.println("Введите название организации");
        while (!isUnique) {
            fullName = scanner.nextLine();
            if (fullName.trim().equals("")) {
                System.err.println("Название организации не может быть пустым.");
                System.out.println("Введите название организации еще раз");
            }
            if (!fullName.trim().equals("")) {
                try {
                    checkOrgFullName(fullName);
                    isUnique = true;
                    organizationsFullNames.add(fullName);
                    return fullName;
                } catch (UniqueException e) {
                    System.err.println(e.getMessage() + organizationsFullNames);
                    System.out.println("Введите название организации еще раз");
                }
            }
        }
        return "";
    }
    private long readEmployeesCount() {
        Scanner scanner = new Scanner(System.in);
                long employeesCount = 0;
                boolean isValidInput = false;
                System.out.println("Введите количество сотрудников:");
                while (!isValidInput) {
                    try {
                employeesCount = Long.parseLong(scanner.nextLine());
                if (employeesCount <= 0) {
                    throw new InputException("Количество сотрудников должно быть больше 0. ");
                }
                isValidInput = true;
            } catch (NumberFormatException e) {
                System.err.println("Некорректный ввод. Попробуйте еще раз.");
            } catch (InputException e) {
                System.err.println(e.getMessage());
            }
        }
        return employeesCount;
    }



    private Float readAnnualTurnover() {
        Scanner scanner = new Scanner(System.in);
        Float annualTurnover = null;
        String aTString;
        System.out.println("Годовой оборот компании (вещественное число):");
        boolean isValidInput = false;
        while (!isValidInput) {
            aTString = scanner.nextLine().trim();
            try {
                if (aTString.equals("")) {
                    annualTurnover = null;
                    break;
                }
                else{annualTurnover = Float.parseFloat(aTString);
                if (annualTurnover <= 0) {
                    throw new InputException("Годовой оборот компании должен быть больше нуля. Попробуйте еще раз.");
                }
                isValidInput = true;}
            } catch (NumberFormatException e) {
                System.err.println("Некорректный ввод. Попробуйте еще раз.");
            } catch (InputException e) {
                System.err.println(e.getMessage());
            }
        }
        return annualTurnover;
    }


    protected boolean checkOrgFullName(String fullName) throws UniqueException {
        for (String takenName : organizationsFullNames) {
            if (fullName.equals(takenName)) {
                throw new UniqueException("Это название уже занято.");
            }
        }
        return true;
    }
    protected void removeOrgNameFromList(String orgName){
        Iterator<String> iterator= organizationsFullNames.iterator();
        while (iterator.hasNext()){
            String name = iterator.next();
            if (name.equals(orgName)){
                iterator.remove();
            }
        }
//        for (String name : organizationsFullNames){
//            if (name == orgName){
//                organizationsFullNames.remove(name);
//            }
//        }
    }
    protected void updateOrganizationsFullNames() {
        organizationsFullNames.clear();
    }
}
