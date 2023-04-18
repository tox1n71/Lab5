package ru.itmo.lab5.server;


import ru.itmo.lab5.worker.*;

import java.io.FileWriter;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.regex.*;
import java.io.*;
import javax.xml.parsers.*;
import org.w3c.dom.*;
import org.xml.sax.*;


public class CommandReader {
    Scanner scanner = new Scanner(System.in);
    boolean isProgramActive = true;


    TreeSet<Worker> workers = new TreeSet<>();
    HashSet<Integer> workersIds = new HashSet<>();
    Stack<String> commandHistory = new Stack<>();
    OrganizationReader organizationReader;
    WorkerReader workerReader;

    ZonedDateTime zonedDateTime;

    {
        zonedDateTime = ZonedDateTime.now();
    }

    public CommandReader(OrganizationReader organizationReader, WorkerReader workerReader) {
        this.organizationReader = organizationReader;
        this.workerReader = workerReader;
    }


    public void readCommand() {

        while (isProgramActive) {
            System.out.print(">> ");
            String input = scanner.nextLine();
//            commandHistory.push(input.trim()); // Добавляем команду в стек
            if (commandHistory.size() > 8) {
                commandHistory.remove(0); // Ограничиваем размер стека 8 элементами
                commandHistory.push(input.trim());
            }
            if (input.trim().equals("help")) {
                help();
                commandHistory.push(input.trim());
            } else if (input.trim().equals("info")) {
                info();
                commandHistory.push(input.trim());
            } else if (input.trim().equals("show")) {
                show();
                commandHistory.push(input.trim());
            } else if (input.trim().equals("exit")) {
                exit();
                commandHistory.push(input.trim());
            } else if (input.trim().equals("add")) {
                add();
                commandHistory.push(input.trim());
            } else if (input.trim().equals("filter_less_than_organization")) {
                filterLessThanOrganization();
                commandHistory.push(input.trim());
            } else if (input.trim().equals("clear")) {
                clear();
                commandHistory.push(input.trim());
            } else if (input.matches("remove_by_id \\d+")) {
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(input);
                if (matcher.find()) {
                    int id = Integer.parseInt(matcher.group());
                    removeById(id);
                } else {
                    System.out.println("Неверный формат команды");
                }
                commandHistory.push(input.trim());
            } else if (input.trim().equals("min_by_name")) {
                minByName();
                commandHistory.push(input.trim());
            } else if (input.trim().equals("print_descending")) {
                printDescending();
                commandHistory.push(input.trim());
            } else if (input.trim().equals("history")) {
                history();
                commandHistory.push(input.trim());
            } else if (input.trim().equals("add_if_min")) {
                addIfMin();
                commandHistory.push(input.trim());
            }else if (input.trim().equals("remove_by_lower")){
                removeByLower();
                commandHistory.push(input.trim());
            }else if (input.matches("update_by_id \\d+")){
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(input);
                if (matcher.find()) {
                    int id = Integer.parseInt(matcher.group());
                    updateById(id);
                } else {
                    System.out.println("Неверный формат команды");
                }
                commandHistory.push(input.trim());
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
    }

    private void clear() {
        workers.clear();
        System.out.println("Коллекция очищена");
    }

    private void removeById(int id) {
        if (workers.isEmpty()){
            System.out.println("Коллекция пуста");
        }
        Iterator<Worker> iterator = workers.iterator();
        while (iterator.hasNext()) {
            Worker worker = iterator.next();
            if (worker.getId() == id) {
                organizationReader.removeOrgNameFromList(worker.getOrganization().getFullName());
                iterator.remove();
                System.out.println("Элемент с id " + id + " удален из коллекции");
                break;
            } else System.out.println("Элемент с id " + id + " не найден в коллекции");
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

    private void addIfMin() { // DOdelattttt 4-4ut
        if (workers.isEmpty()) {
            System.err.println("Коллекция пуста, воспользуйтесь командой 'add'");
        } else {
            Worker mayBeAddedWorker = workerReader.readWorker();
            if (mayBeAddedWorker.compareTo(workers.first()) < 0) {
                mayBeAddedWorker.setId(getFreeId());
                mayBeAddedWorker.setCreationDate(LocalDate.now());
                workers.add(mayBeAddedWorker);
            } else System.out.println("Введенный элемент больше минимального");

        }
    }

    private void removeByLower(){
        if (workers.isEmpty()){
            System.err.println("Коллекция пуста, нет элементов для сравнения");
        }
        else {
            Iterator<Worker> iterator = workers.iterator();
            Worker removeLowerThanThisWorker = workerReader.readWorker();
            while (iterator.hasNext()){
                Worker worker = iterator.next();
                if (worker.compareTo(removeLowerThanThisWorker) < 0){
                    iterator.remove();
                    System.out.println("Удален элемент с id " + worker.getId());
                }
                else {
                    System.out.println("Элементы меньше чем заданный не найдены");
                }
            }
        }
    }

    private void updateById(int id){
        if (workers.isEmpty()){
            System.out.println("Коллекция пуста");
        }
        Iterator<Worker> iterator = workers.iterator();
        Worker returnmentWorker;
        while(iterator.hasNext()){
            Worker worker = iterator.next();
            if (worker.getId() == id){
                returnmentWorker = workerReader.readWorker();
                returnmentWorker.setId(worker.getId());
                returnmentWorker.setCreationDate(worker.getCreationDate());
                iterator.remove();
                workers.add(returnmentWorker);
            }
            else {
                System.out.println("Работник с таким id не найден");
            }
        }
    }


}



