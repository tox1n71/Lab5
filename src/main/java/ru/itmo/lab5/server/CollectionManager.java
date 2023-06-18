package ru.itmo.lab5.server;

import org.apache.commons.lang3.RandomStringUtils;
import org.xml.sax.SAXException;
import ru.itmo.lab5.commands.Command;
import ru.itmo.lab5.exceptions.InputException;
import ru.itmo.lab5.exceptions.UniqueException;
import ru.itmo.lab5.exceptions.WrongScriptDataException;
import ru.itmo.lab5.readers.OrganizationReader;
import ru.itmo.lab5.utils.DataProvider;
import ru.itmo.lab5.worker.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

import static ru.itmo.lab5.readers.WorkerReader.formatter;
import static ru.itmo.lab5.readers.WorkerReader.formatterTime;


public class CollectionManager {


    File fileName;
    TreeSet<Worker> workers;
    HashSet<Integer> workersIds = new HashSet<>();
    HashSet<String> executedScripts = new HashSet<>();
    Stack<String> commandHistory = new Stack<>();
    OrganizationReader organizationReader = new OrganizationReader();
    HashSet<String> commands = new HashSet<>();
    DataProvider dataProvider = new DataProvider();


    {
        commands.add("help");
        commands.add("show");
        commands.add("add");
        commands.add("info");
        commands.add("history");
        commands.add("print_descending");
        commands.add("clear");
        commands.add("filter_less_than_organization");
        commands.add("remove_lower");
        commands.add("min_by_name");//TODO: !fi
        commands.add("remove_by_id");
        commands.add("add_if_min");
        commands.add("update_by_id");
        commands.add("exit");
        commands.add("execute_script");
    }


    ZonedDateTime zonedDateTime;

    {
        zonedDateTime = ZonedDateTime.now();
    }

    public CollectionManager(File fileName, TreeSet<Worker> workers) throws ParserConfigurationException, IOException, SAXException {
        this.fileName = fileName;
        this.workers = workers;
    }

    public CollectionManager() {
    }

    public String executeCommand(Command command) {
        if (commandHistory.size() > 7) {
            commandHistory.remove(0);
        }
        Iterator<String> iterator = commands.iterator();
        while (iterator.hasNext()) {
            String myCommand = iterator.next();
            if (command.getName().equals(myCommand)) {
                commandHistory.push(command.getName());
                return command.execute();
            }
        }
        return "Команда не найдена";
    }


    public String help() {
        return "help : вывести справку по доступным командам\n" +
                "info : вывести в стандартный поток вывода информацию о коллекции (тип, дата инициализации, количество элементов и т.д.)\n" +
                "show : вывести в стандартный поток вывода все элементы коллекции в строковом представлении\n" +
                "add {element} : добавить новый элемент в коллекцию\n" +
                "update_by_id id {element} : обновить значение элемента коллекции, id которого равен заданному\n" +
                "remove_by_id id : удалить элемент из коллекции по его id\n" +
                "clear : очистить коллекцию\n" +
                "execute_script file_name : считать и исполнить скрипт из указанного файла. В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.\n" +
                "exit : завершить программу (без сохранения в файл)\n" +
                "add_if_min {element} : добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции\n" +
                "remove_lower {element} : удалить из коллекции все элементы, меньшие, чем заданный\n" +
                "history : вывести последние 8 команд (без их аргументов)\n" +
                "min_by_name : вывести любой объект из коллекции, значение поля name которого является минимальным\n" +
                "filter_less_than_organization organization : вывести элементы, значение поля organization которых меньше заданного\n" +
                "print_descending : вывести элементы коллекции в порядке убывания";
    }

    public String info() {
        return ("Тип коллекции: " + workers.getClass().getSimpleName()
                + "\nКоличество элементов в коллекции: " + workers.size()
                + "\nДата инициализации: " + zonedDateTime);
    }

    public String show() {
        return workers.toString();
    }

    public String exit() throws IOException {
        save();
        return "Коллекция сохранена на сервере.\n" + "Завершение работы...";
    }

    public String add(Worker worker) {
        worker.setId(getFreeId());
        worker.setCreationDate(LocalDate.now());
        organizationReader.organizationsFullNames.add(worker.getOrganization().getFullName());
        if (workers.add(worker)) {
            return ("Введенный элемент добавлен в коллекцию с id: " + worker.getId());
        } else {
            return "Работник не уникален и не был добавлен в коллекцию";
        }
    }

    public String clear() {
        workers.stream().map(Worker::getId).forEach(workersIds::remove);
        workers.clear();
        return "Коллекция очищена";
    }


    public String removeById(int id) {
        boolean found = false;
        String res = "";
        if (workers.isEmpty()) {
            return ("Коллекция пуста");
        } else {
            Iterator<Worker> iterator = workers.iterator();
            while (iterator.hasNext()) {
                Worker worker = iterator.next();
                if (worker.getId() == id) {
                    iterator.remove();
                    workersIds.remove(worker.getId());
                    organizationReader.removeOrgNameFromList(worker.getOrganization().getFullName());
                    res = "Элемент с id " + id + " удален из коллекции";
                    found = true;
                    break;
                }
            }
            if (!found) {
                res = ("Элемент с id " + id + " не найден в коллекции");
            }
        }
        return res;
    }


    public int getFreeId() {
        Integer id = 1 + (int) (Math.random() * Integer.MAX_VALUE);
        while (workersIds.contains(id)) {
            id = 1 + (int) (Math.random() * Integer.MAX_VALUE);
        }
        return id;
    }

    //??
    public String filterLessThanOrganization(Organization inputOrganization) {
        if (inputOrganization.getAnnualTurnover() == null) {
            return "Сравнить с null значением не получится";
        }

        List<Worker> filteredWorkers = workers.stream()
                .filter(worker -> worker.getOrganization().getAnnualTurnover() != null)
                .filter(worker -> worker.getOrganization().compareTo(inputOrganization) < 0)
                .toList();
        if (filteredWorkers.isEmpty()) {
            return "Работников с меньшей организацией нет";
        } else {
            return filteredWorkers.stream().map(Object::toString).collect(Collectors.joining());
        }
    }


    public String minByName() {
        Optional<Worker> minWorker = workers.stream()
                .min(Comparator.comparing(Worker::getName));
        return minWorker.map(worker -> "Работник с минимальным именем: \n" + worker).orElse("Не удалось найти работника с минимальным именем.");
    }


    public String printDescending() {
        return workers.descendingSet().stream()
                .map(Worker::toString)
                .collect(Collectors.joining("\n"));
    }

    public String history() {
        String res = "";
        res += ("История вводимых команд:\n");
        for (int i = commandHistory.size() - 1; i >= 0; i--) {
            String command = commandHistory.get(i);
            res += (command.split(" ")[0]) + "\n";
        }
        return res;
    }

    public String addIfMin(Worker mayBeAddedWorker) {
        if (workers.isEmpty()) {
            return ("Коллекция пуста, воспользуйтесь командой 'add'");
        }
        Optional<Worker> minWorker = workers.stream()
                .min(Comparator.naturalOrder());
        if (mayBeAddedWorker.compareTo(minWorker.get()) >= 0) {
            return ("Введенный элемент больше минимального");
        }
        mayBeAddedWorker.setId(getFreeId());
        mayBeAddedWorker.setCreationDate(LocalDate.now());
        workers.add(mayBeAddedWorker);
        organizationReader.organizationsFullNames.add(mayBeAddedWorker.getOrganization().getFullName());
        return ("Введенный элемент меньше и добавлен в коллекцию с id " + mayBeAddedWorker.getId());
    }


    public String removeLower(Worker removeLowerThanThisWorker) {
        List<Worker> removedWorkers = workers.stream()
                .filter(worker -> worker.compareTo(removeLowerThanThisWorker) < 0)
                .peek(worker -> {
                    organizationReader.organizationsFullNames.remove(worker.getOrganization().getFullName());
                    workersIds.remove(worker.getId());
                })
                .collect(Collectors.toList());

        if (removedWorkers.isEmpty()) {
            return "Элементы меньше чем заданный не найдены";
        } else {
            removedWorkers.forEach(workers::remove);
            return removedWorkers.stream()
                    .map(worker -> "Удален элемент с id " + worker.getId())
                    .collect(Collectors.joining("\n"));
        }
    }


    public String updateById(int id, Worker returnmentWorker) {
        Iterator<Worker> iterator = workers.iterator();
        while (iterator.hasNext()) {
            Worker worker = iterator.next();
            if (worker.getId() == id) {
                organizationReader.removeOrgNameFromList(worker.getOrganization().getFullName());
                returnmentWorker.setId(worker.getId());
                returnmentWorker.setCreationDate(worker.getCreationDate());
                organizationReader.organizationsFullNames.add(returnmentWorker.getOrganization().getFullName());
                iterator.remove();
                workers.add(returnmentWorker);
            }
        }
        return "Данные работника были обновлены";
    }

    public boolean workerWithIdExist(int id) {
        boolean exist = false;
        Iterator<Worker> iterator = workers.iterator();
        while (iterator.hasNext()) {
            Worker worker = iterator.next();
            exist = worker.getId() == id;
        }
        return exist;
    }

    public String save() throws IOException {
        try (FileWriter writer = new FileWriter(fileName, false)) {
            writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n");
            writer.write("<Workers>\n");

            workers.stream()
                    .map(Worker::toXml)
                    .forEach(xml -> {
                        try {
                            writer.write(xml);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });

            writer.write("</Workers>");
            return ("Сохранение коллекции завершено");
        }
    }


    private void setUsedIdsAndOrgNames() {
        Iterator<Worker> iterator = workers.iterator();
        while (iterator.hasNext()) {
            Worker worker = iterator.next();
            workersIds.add(worker.getId());
            organizationReader.organizationsFullNames.add(worker.getOrganization().getFullName());
        }
    }

    public String executeScript() throws IOException {
        return "popa";
//        BufferedReader reader = new BufferedReader(new FileReader(file));
//        String line;
//        String res = "";
//        while ((line = reader.readLine()) != null) {
//            switch (line) {
//                case "add" -> {
//                    try {
//                        Worker worker = readWorkerFromFile(reader);
//
//                        res += add(worker) + "\n";
//                        setUsedIdsAndOrgNames();
//                    } catch (WrongScriptDataException e) {
//                        System.err.println(e.getMessage());
//                    }
//                }
//                case "help" -> res +=help()+ "\n";
//                case "info" -> res +=info()+ "\n";
//                case "show" -> res +=show()+ "\n";
//                case "clear" -> res +=clear()+ "\n";
////                case "exit" -> exit();
//                case "history" -> res +=history()+ "\n";
//                case "min_by_name" -> res +=minByName()+ "\n";
//                case "print_descending" -> res +=printDescending()+ "\n";
//                case "add_if_min" -> {
//                    boolean added = false;
//
//                    try {
//                        Worker worker = readWorkerFromFile(reader);
//                        try {
//                            res += addIfMin(worker)+ "\n";
//
//                        } catch (NoSuchElementException e) {
//                            res += "Команда add_if_min из скрипта не может быть выполнена, так как коллекция пуста.\n Добавьте элемент командой add";
//                        }
//                    } catch (WrongScriptDataException e) {
//                        res += (e.getMessage());
//                    }
//                    if (!added) {
//                        res += "Введенный элемент больше минимального"+ "\n";
//                    }
//                }
//                case "remove_lower" -> {
//                    try {
//                        Worker removeLowerThanThisWorker = readWorkerFromFile(reader);
//                        res += removeLower(removeLowerThanThisWorker);
//                    } catch (WrongScriptDataException e) {
//                        res += (e.getMessage());
//                    }
//                }
//                default -> {
//                    if (line.matches("remove_by_id \\d+")) {
//                        Pattern pattern = Pattern.compile("\\d+");
//                        Matcher matcher = pattern.matcher(line);
//                        if (matcher.find()) {
//                            int id = Integer.parseInt(matcher.group());
//                            res+= removeById(id)+ "\n";
//                        } else {
//                            res+=("Неверный формат команды")+ "\n";
//                        }
//                    } else if (line.matches("update_by_id \\d+")) {
//                        Pattern pattern = Pattern.compile("\\d+");
//                        Matcher matcher = pattern.matcher(line);
//                        if (matcher.find()) {
//                            int id = Integer.parseInt(matcher.group());
//                            try {
//                                Worker worker = readWorkerFromFile(reader);
//                                res += updateById(id, worker)+ "\n";
//                            } catch (WrongScriptDataException e) {
//                                res += e.getMessage()+ "\n";
//                            }
//                        } else {
//                            res += "Неверный формат команды"+ "\n";
//                        }
//                    }
////                    else if (line.matches("execute_script \\S*")) {
////                        String[] tokens = line.split(" ");
////                        if (tokens.length == 2) {
////                            String inScriptFileName = tokens[1];
////                            if (!executedScripts.contains(inScriptFileName)) {
////                                executedScripts.add(inScriptFileName);
////                                try {
////                                    BufferedReader inScriptReader = new BufferedReader(new FileReader(inScriptFileName));
////                                    String inScriptLine;
////                                    while ((inScriptLine = inScriptReader.readLine()) != null) {
////                                        if (inScriptLine.startsWith("execute_script")) {
////                                            String scriptName = inScriptLine.split(" ")[1];
////                                            executeScript(scriptName);
////                                        } else {
////                                            // Обработка других команд
////                                        }
////                                    }
////                                    inScriptReader.close();
////                                } catch (IOException e) {
////                                    System.out.println("Ошибка при выполнении скрипта " + inScriptFileName + ": " + e.getMessage());
////                                }
////                                executedScripts.remove(inScriptFileName);
////                            } else {
////                                System.out.println("Скрипт " + inScriptFileName + " уже был выполнен. Пропускаем...");
////                            }
//                        }
//                    }
//                }
//        reader.close();
//        return res;
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

    public void setOrganizationReader(OrganizationReader organizationReader) {
        this.organizationReader = organizationReader;
    }

    public TreeSet<Worker> getWorkers() {
        return workers;
    }




}

