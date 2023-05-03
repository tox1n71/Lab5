package ru.itmo.lab5.server;


import org.apache.commons.lang3.RandomStringUtils;
import ru.itmo.lab5.exceptions.InputException;
import ru.itmo.lab5.exceptions.ParserExeption;
import ru.itmo.lab5.exceptions.UniqueException;
import ru.itmo.lab5.exceptions.WrongScriptDataException;
import ru.itmo.lab5.worker.*;

import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.regex.*;
import java.io.*;
import javax.xml.parsers.*;

import org.xml.sax.*;

import static ru.itmo.lab5.server.WorkerReader.formatter;
import static ru.itmo.lab5.server.WorkerReader.formatterTime;


public class CommandReader {
    Scanner scanner = new Scanner(System.in);
    boolean isProgramActive = true;


    File fileName;
    TreeSet<Worker> workers;
    HashSet<Integer> workersIds = new HashSet<>();
    HashSet<String> executedScripts = new HashSet<>();
    Stack<String> commandHistory = new Stack<>();
    OrganizationReader organizationReader;
    WorkerReader workerReader;


    ZonedDateTime zonedDateTime;

    {
        zonedDateTime = ZonedDateTime.now();
    }

    public CommandReader(OrganizationReader organizationReader, WorkerReader workerReader, File fileName, TreeSet<Worker> workers) throws ParserConfigurationException, IOException, SAXException {
        this.organizationReader = organizationReader;
        this.workerReader = workerReader;
        this.fileName = fileName;
        this.workers = workers;

    }


    public void readCommand() throws IOException {
        setUsedIdsAndOrgNames();
        while (isProgramActive) {
            System.out.print(">> ");
            try {
                String input = scanner.nextLine();
                // commandHistory.push(input.trim()); // Добавляем команду в стек
                if (commandHistory.size() > 8) {
                    commandHistory.remove(0); // Ограничиваем размер стека 8 элементами
                }

                switch (input.trim()) {
                    case "help" -> {
                        help();
                        commandHistory.push(input.trim());
                    }
                    case "info" -> {
                        info();
                        commandHistory.push(input.trim());
                    }
                    case "show" -> {
                        show();
                        commandHistory.push(input.trim());
                    }
                    case "exit" -> {
                        exit();
                        commandHistory.push(input.trim());
                    }
                    case "add" -> {
                        add();
                        commandHistory.push(input.trim());
                    }
                    case "filter_less_than_organization" -> {
                        filterLessThanOrganization();
                        commandHistory.push(input.trim());
                    }
                    case "clear" -> {
                        clear();
                        commandHistory.push(input.trim());
                    }
                    case "min_by_name" -> {
                        minByName();
                        commandHistory.push(input.trim());
                    }
                    case "print_descending" -> {
                        printDescending();
                        commandHistory.push(input.trim());
                    }
                    case "history" -> {
                        history();
                        commandHistory.push(input.trim());
                    }
                    case "add_if_min" -> {
                        addIfMin();
                        commandHistory.push(input.trim());
                    }
                    case "remove_lower" -> {
                        removeLower();
                        commandHistory.push(input.trim());
                    }
                    case "save" -> {
                        try {
                            save();
                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                    }
                    default -> {
                        if (input.matches("remove_by_id \\d+")) {
                            Pattern pattern = Pattern.compile("\\d+");
                            Matcher matcher = pattern.matcher(input);
                            if (matcher.find()) {
                                int id = Integer.parseInt(matcher.group());
                                removeById(id);
                            } else {
                                System.out.println("Неверный формат команды");
                            }
                            commandHistory.push(input.trim());
                        } else if (input.matches("update_by_id \\d+")) {
                            Pattern pattern = Pattern.compile("\\d+");
                            Matcher matcher = pattern.matcher(input);
                            if (matcher.find()) {
                                int id = Integer.parseInt(matcher.group());
                                updateById(id);
                            } else {
                                System.out.println("Неверный формат команды");
                            }
                            commandHistory.push(input.trim());
                        } else if (input.matches("execute_script \\S*")) {
                            String[] tokens = input.split(" ");
                            if (tokens.length == 2) {
                                String scriptFileName = tokens[1];
                                executedScripts.add(scriptFileName);
                                try {
                                    executeScript(scriptFileName);
                                    executedScripts.remove(scriptFileName);
                                } catch (FileNotFoundException e) {
                                    System.err.println("Файл со скриптом не найден");
                                }
                            } else {
                                System.out.println("Неверный формат команды");
                            }
                            commandHistory.push(input.trim());
                        } else {
                            System.out.println("Команда или ее параметр введен(-а) неверно");
                        }
                    }
                }
            } catch (NoSuchElementException e) {
                isProgramActive = false;
                System.err.println("Программа была прервана сочетанием клавиш cntl+d");
            }
        }
    }


    private void help() {
        System.out.println("help : вывести справку по доступным командам\n" +
                "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                "add {element} : добавить новый элемент в коллекцию\n" +
                "update id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                "remove_by_id id : удалить элемент из коллекции по его id\n" +
                "clear : очистить коллекцию\n" +
                "save : сохранить коллекцию в файл\n" +
                "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                "exit : завершить программу (без сохранения в файл)\n" +
                "add_if_min {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции\n" +
                "remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный\n" +
                "history : вывести последние 8 команд (без их аргументов)\n" +
                "min_by_name : вывести любой объект из коллекции, значение поля name которого является минимальным\n" +
                "filter_less_than_organization organization : вывести элементы, значение поля organization которых меньше заданного\n" +
                "print_descending : вывести элементы коллекции в порядке убывания");
    }

    private void info() {
        System.out.println("Тип коллекции: " + workers.getClass().getSimpleName()
                + "\nКоличество элементов в коллекции: " + workers.size()
                + "\nДата инициализации: " + zonedDateTime);
    }

    private void show() {
        System.out.println(workers);
    }

    private void exit() {
        isProgramActive = false;
        System.out.println("Завершение программы...");
    }

    private void add() {
        Worker worker = workerReader.readWorker();
        worker.setId(getFreeId());
        worker.setCreationDate(LocalDate.now());
        workers.add(worker);
        System.out.println("Введенный элемент добавлен в коллекцию с id" + worker.getId());
    }

    private void clear() {
        Iterator<Worker> iterator = workers.iterator();
        while (iterator.hasNext()) {
            Worker worker = iterator.next();
            workersIds.remove(worker.getId());
            organizationReader.organizationsFullNames.remove(worker.getOrganization().getFullName());
        }
        workers.clear();
        System.out.println("Коллекция очищена");
    }

    private void removeById(int id) {
        boolean found = false;
        if (workers.isEmpty()) {
            System.out.println("Коллекция пуста");
        } else {
            Iterator<Worker> iterator = workers.iterator();
            while (iterator.hasNext()) {
                Worker worker = iterator.next();
                if (worker.getId() == id) {
                    organizationReader.removeOrgNameFromList(worker.getOrganization().getFullName());
                    iterator.remove();
                    System.out.println("Элемент с id " + id + " удален из коллекции");
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("Элемент с id " + id + " не найден в коллекции");
            }
        }
    }


    private int getFreeId() {
        Integer id = 1 + (int) (Math.random() * Integer.MAX_VALUE);
        while (workersIds.contains(id)) {
            id = 1 + (int) (Math.random() * Integer.MAX_VALUE);
        }
        return id;
    }


    private void filterLessThanOrganization() {
        Organization inputOrganization = organizationReader.readOrganization();
        for (Worker worker : workers) {
            if (inputOrganization.getAnnualTurnover() == null) {
                System.err.println("Сравнить с null значением не получится");
                break;
            }
            if (!(worker.getOrganization().getAnnualTurnover() == null)) {
                if (worker.getOrganization().compareTo(inputOrganization) < 0) {
                    System.out.println(worker);
                } else {
                    System.out.println("Работников с меньшей организацией нет");
                }
            }
        }
    }

    private void minByName() {
        if (workers.isEmpty()) {
            System.out.println("Список работников пуст.");
            return;
        }
        Worker minWorker = null;
        Iterator<Worker> iterator = workers.iterator();
        while (iterator.hasNext()) {
            Worker currentWorker = iterator.next();
            if (minWorker == null || currentWorker.getName().compareTo(minWorker.getName()) < 0) {
                minWorker = currentWorker;
            }
        }

        if (minWorker == null) {
            System.out.println("Не удалось найти работника с минимальным именем.");
        } else {
            System.out.println("Работник с минимальным именем: \n" + minWorker);
        }
    }

    private void printDescending() {
        Iterator<Worker> descendingIterator = workers.descendingIterator();
        while (descendingIterator.hasNext()) {
            Worker worker = descendingIterator.next();
            System.out.println(worker);
        }
    }

    private void history() {
        System.out.println("История вводимых команд:");
        for (int i = commandHistory.size() - 1; i >= 0; i--) {
            String command = commandHistory.get(i);
            System.out.println(command.split(" ")[0]);
        }
    }

    private void addIfMin() {
        boolean added = false;
        if (workers.isEmpty()) {
            System.err.println("Коллекция пуста, воспользуйтесь командой 'add'");
        } else {
            Worker mayBeAddedWorker = workerReader.readWorker();
            if (mayBeAddedWorker.compareTo(workers.first()) < 0) {
                mayBeAddedWorker.setId(getFreeId());
                mayBeAddedWorker.setCreationDate(LocalDate.now());
                workers.add(mayBeAddedWorker);
                added = true;
                System.out.println("Введенный элемент меньше и добавлен в коллекцию с id " + mayBeAddedWorker.getId());
            }
            if (!added) {
                System.out.println("Введенный элемент больше минимального");
            }
        }
    }

    private void removeLower() {
        boolean removed = false;
        if (workers.isEmpty()) {
            System.err.println("Коллекция пуста, нет элементов для сравнения");
        } else {
            Iterator<Worker> iterator = workers.iterator();
            Worker removeLowerThanThisWorker = workerReader.readWorker();
            while (iterator.hasNext()) {
                Worker worker = iterator.next();
                if (worker.compareTo(removeLowerThanThisWorker) < 0) {
                    iterator.remove();
                    System.out.println("Удален элемент с id " + worker.getId());
                    removed = true;
                }
            }
            if (!removed) {
                System.out.println("Элементы меньше чем заданный не найдены");
            }
        }
    }

    private void updateById(int id) {
        boolean updated = false;
        if (workers.isEmpty()) {
            System.out.println("Коллекция пуста");
        } else {
            Iterator<Worker> iterator = workers.iterator();
            Worker returnmentWorker;
            while (iterator.hasNext()) {
                Worker worker = iterator.next();
                if (worker.getId() == id) {
                    returnmentWorker = workerReader.readWorker();
                    returnmentWorker.setId(worker.getId());
                    returnmentWorker.setCreationDate(worker.getCreationDate());
                    iterator.remove();
                    workers.add(returnmentWorker);
                    updated = true;
                    break;
                }
            }
            if (!updated) {
                System.out.println("Работник с таким id не найден");
            }
        }
    }

    private void save() throws IOException {
        FileWriter writer = new FileWriter(fileName, false);
        writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
        writer.write("<Workers>\n");

        for (Worker worker : workers) {
            writer.write(worker.toXml());
        }
        writer.write("</Workers>");
        writer.close();
        System.out.println("Сохранение завершено");
    }

    private void setUsedIdsAndOrgNames() {
        Iterator<Worker> iterator = workers.iterator();
        while (iterator.hasNext()) {
            Worker worker = iterator.next();
            workersIds.add(worker.getId());
            organizationReader.organizationsFullNames.add(worker.getOrganization().getFullName());
        }
    }

    private void executeScript(String ScriptFileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(ScriptFileName));
        String line;
        while ((line = reader.readLine()) != null) {
            switch (line) {
                case "add" -> {
                    try {
                        Worker worker = readWorkerFromFile(reader);

                    workers.add(worker);
                    System.out.println("Введенный элемент добавлен в коллекцию с id" + worker.getId());
                    setUsedIdsAndOrgNames();}catch (WrongScriptDataException e){
                        System.err.println(e.getMessage());
                    }
                }
                case "help" -> help();
                case "info" -> info();
                case "show" -> show();
                case "clear" -> clear();
                case "save" -> save();
                case "exit" -> exit();
                case "history" -> history();
                case "min_by_name" -> minByName();
                case "print_descending" -> printDescending();
                case "add_if_min" -> {
                    boolean added = false;

                    try {
                        Worker worker = readWorkerFromFile(reader);
                        try {
                            if (worker.compareTo(workers.first()) < 0) {
                                workers.add(worker);
                                added = true;
                                System.out.println("Введенный элемент меньше и добавлен в коллекцию с id " + worker.getId());
                            }
                        } catch (NoSuchElementException e) {
                            System.err.println("Команда add_if_min из скрипта не может быть выполнена, так как коллекция пуста.\n Добавьте элемент командой add");
                        }
                    }catch (WrongScriptDataException e){
                        System.err.println(e.getMessage());
                    }
                    if (!added) {
                        System.out.println("Введенный элемент больше минимального");
                    }
                }
                case "remove_lower" -> {
                    boolean removed = false;
                    Iterator<Worker> iterator = workers.iterator();
                    try {
                        Worker removeLowerThanThisWorker = readWorkerFromFile(reader);

                    while (iterator.hasNext()) {
                        Worker worker = iterator.next();
                        try {
                            if (worker.compareTo(removeLowerThanThisWorker) < 0) {
                                iterator.remove();
                                System.out.println("Удален элемент с id " + worker.getId());
                                removed = true;
                            }
                        } catch (NoSuchElementException e) {
                            System.err.println("Команда remove_lower из скрипта не может быть выполнена, так как коллекция пуста.\n Добавьте элемент командой add");
                        }
                        if (!removed) {
                            System.out.println("Элементы меньше чем заданный не найдены");
                        }
                    }}catch (WrongScriptDataException e){
                        System.err.println(e.getMessage());
                    }
                }
                default -> {
                    if (line.matches("remove_by_id \\d+")) {
                        Pattern pattern = Pattern.compile("\\d+");
                        Matcher matcher = pattern.matcher(line);
                        if (matcher.find()) {
                            int id = Integer.parseInt(matcher.group());
                            removeById(id);
                        } else {
                            System.out.println("Неверный формат команды");
                        }
                    } else if (line.matches("update_by_id \\d+")) {
                        Pattern pattern = Pattern.compile("\\d+");
                        Matcher matcher = pattern.matcher(line);
                        if (matcher.find()) {
                            int id = Integer.parseInt(matcher.group());
                            updateById(id);
                        } else {
                            System.out.println("Неверный формат команды");
                        }
                    } else if (line.matches("execute_script \\S*")) {
                        String[] tokens = line.split(" ");
                        if (tokens.length == 2) {
                            String inScriptFileName = tokens[1];
                            if (!executedScripts.contains(inScriptFileName)) {
                                executedScripts.add(inScriptFileName);
                                try {
                                    BufferedReader inScriptReader = new BufferedReader(new FileReader(inScriptFileName));
                                    String inScriptLine;
                                    while ((inScriptLine = inScriptReader.readLine()) != null) {
                                        if (inScriptLine.startsWith("execute_script")) {
                                            String scriptName = inScriptLine.split(" ")[1];
                                            executeScript(scriptName);
                                        } else {
                                            // Обработка других команд
                                        }
                                    }
                                    inScriptReader.close();
                                } catch (IOException e) {
                                    System.out.println("Ошибка при выполнении скрипта " + inScriptFileName + ": " + e.getMessage());
                                }
                                executedScripts.remove(inScriptFileName);
                            } else {
                                System.out.println("Скрипт " + inScriptFileName + " уже был выполнен. Пропускаем...");
                            }
                        }
                    }
                }
            }
        }
        reader.close();
    }

    private Worker readWorkerFromFile(BufferedReader readerDad) throws IOException, WrongScriptDataException {
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
            worker.setId(getFreeId());
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

