package ru.itmo.lab5.server;

import ru.itmo.lab5.exceptions.InputException;
import ru.itmo.lab5.worker.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

import static ru.itmo.lab5.worker.Position.stringToPosition;

public class WorkerReader {


    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    DateTimeFormatter formatterTime = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    OrganizationReader organizationReader;
    public WorkerReader(OrganizationReader organizationReader){
        this.organizationReader = organizationReader;
    }

    private LocalDate readStartDate() {
        Scanner scanner = new Scanner(System.in);
        LocalDate date = null;
        boolean validInput = false;
        do {
            System.out.println("Введите дату начала работы в формате dd/MM/yyyy: ");
            String inputDate = scanner.nextLine();
            try {
                date = LocalDate.parse(inputDate, formatter);
                validInput = true;
            } catch (DateTimeParseException e) {
                System.err.println("Некорректный формат даты. Попробуйте еще раз.");
            }
        } while (!validInput);
        return date;
    }

    private LocalDateTime readEndDate() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Введите дату конца работы в формате dd/MM/yyyy HH:mm\nИли нажмите Enter, чтобы оставить поле пустым: ");
            String inputDate = scanner.nextLine();
            if (inputDate.isEmpty())
                return null;
            try {
                return LocalDateTime.parse(inputDate, formatterTime);
            } catch (DateTimeParseException e) {
                System.err.println("Неверный формат даты и времени! Попробуйте еще раз.");
            }
        }
    }

    private String readName() throws InputException {
        String name = "";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите имя: ");
        while (name.trim().isEmpty()) {
            name = scanner.nextLine();
            if (name.trim().isEmpty()) {
                System.err.println("Имя не может быть пустым. Попробуйте еще раз.");
            }
        }
        if (name == null) {
            throw new InputException("Имя не может быть null.");
        }
        return name.trim();
    }

    private int readSalary() {
        Scanner scanner = new Scanner(System.in);
        int salary = 0;
        boolean isValidInput = false;
        System.out.println("Введите число - зарплату:");
        while (!isValidInput) {
            try {
                salary = Integer.parseInt(scanner.nextLine());
                if (salary <= 0) {
                    throw new InputException("Зарплата должна быть больше нуля. Попробуйте еще раз.");
                }
                isValidInput = true;
            } catch (NumberFormatException e) {
                System.err.println("Некорректный ввод. Попробуйте еще раз.");
            } catch (InputException e) {
                System.err.println(e.getMessage());
            }
        }
        return salary;
    }


    private Position readPosition() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите должность работника:\nLABORER\nENGINEER\nHEAD_OF_DEPARTMENT\nLEAD_DEVELOPER\nBAKER");
        String pos = scanner.nextLine();
        try {
            return stringToPosition(pos);
        } catch (InputException e) {
            System.err.println("Введена неверная должность. Попробуйте еще раз");
            return readPosition();
        }
    }

    private Coordinates readCoordinates() throws InputException {
        Scanner scanner = new Scanner(System.in);
        Coordinates coordinates = new Coordinates();
        System.out.println("Введите координату x (вещественное число):");
        double x;
        while (true) {
            try {
                x = Double.parseDouble(scanner.nextLine());
                break;
            } catch (Exception e) {
                System.err.println("Некорректный ввод координаты x. Введите вещественное число.");

            }
        }
        coordinates.setX(x);
        System.out.println("Введите координату y (вещественное число):");
        Double y;
        while (true) {
            try {
                y = Double.parseDouble(scanner.nextLine());
                break;
            } catch (Exception e) {
                System.err.println("Некорректный ввод координаты y. Введите вещественное число.");
            }
        }
        coordinates.setY(y);
        return coordinates;
    }


    public Worker readWorker() {
        Worker worker = new Worker();
        try {
            worker.setName(readName());
        } catch (InputException e) {
            System.err.println(e.getMessage());
        }
        try {
            worker.setCoordinates(readCoordinates());
        } catch (InputException e) {
            System.err.println(e.getMessage());
        }
        try {
            worker.setSalary(readSalary());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        worker.setStartDate(readStartDate());

        worker.setEndDate(readEndDate());
        worker.setPosition(readPosition());

        worker.setOrganization(organizationReader.readOrganization());
        return worker;
    }
}
