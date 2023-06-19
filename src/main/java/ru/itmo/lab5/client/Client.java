package ru.itmo.lab5.client;

import ru.itmo.lab5.commands.*;
import ru.itmo.lab5.exceptions.WrongScriptDataException;
import ru.itmo.lab5.readers.OrganizationReader;
import ru.itmo.lab5.readers.WorkerReader;
import ru.itmo.lab5.utils.User;
import ru.itmo.lab5.worker.Organization;
import ru.itmo.lab5.worker.Worker;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static ru.itmo.lab5.readers.UserReader.readUser;

public class Client {

    private static final int BUF_SIZE = 16384;


    private OrganizationReader organizationReader;
    private WorkerReader workerReader;

    private DatagramSocket socket;

    private HashSet<String> executedScripts = new HashSet<>();

    public void connect(String hostname, int port) throws Exception {
        // Создаем сокет для отправки датаграмм на сервер


        socket = new DatagramSocket();

        InetAddress address = InetAddress.getByName(hostname);

        Scanner scanner = new Scanner(System.in);
        socket.setSoTimeout(3000);
        User user = null;
        System.out.println("Добро пожаловать на кровавую арену смерти!\nАвторизируйтесь(login) или зарегестрируйтесь(register), чтобы воспользоваться программой.");
        while (true) {
            try {

                while (user == null){
                String logInput = scanner.nextLine();

                switch (logInput.trim()) {
                    case "login":
                        User loggingUser = readUser();
                        LoginCommand loginCommand = new LoginCommand(loggingUser);
                        String response = sendCommandWithUserToServer(loginCommand, address,port);
                        if (response.equals("Вход выполнен")) {
                            System.out.println(response);
                            user = loggingUser;
                        } else {
                            System.out.println(response);
                        }
                        break;
                    case "register":
                        User registerUser = readUser();
                        RegisterCommand registerCommand = new RegisterCommand(registerUser);
                        String registerResponse = sendCommandWithUserToServer(registerCommand, address,port);
                        System.out.println(registerResponse);
                        break;
                    case "logout":
                        user = null;
                        System.out.println("Выход выполнен");
                        break;
                    default:
                        System.out.println("Авторизируйтесь(login) или зарегестрируйтесь(register), чтобы воспользоваться программой.");
                        break;
                }}




            // Завершаем цикл, если пользователь вышел из системы или сменил пользователя
            if (user == null) {
                break;
            }

            // Основной цикл программы
            System.out.print(">> ");
            String input = scanner.nextLine();
                switch (input.trim()) {

                    case "help":
                        HelpCommand helpCommand = new HelpCommand();
                        try {
                            sendCommandToServer(helpCommand, address, port);
                        } catch (IOException e) {
                            System.err.println(e.getMessage() + "\n" + Arrays.toString(e.getStackTrace()));
                        }
                        break;
                    case "exit":
                        ExitCommand exitCommand = new ExitCommand();
                        try {
                            sendCommandToServer(exitCommand, address, port);
                        } catch (IOException e) {
                            System.err.println(e.getMessage());
                        }
                        System.exit(0);
                        socket.close();
                        break;
                    case "logout":
                        user = null;
                        System.out.println("Авторизуйтесь снова, чтобы воспользоваться программой");
                        break;
                    case "add":
                        organizationReader = receiveOrgReaderFromServer("lol", address, port);
                        workerReader = new WorkerReader(organizationReader, user);
                        Worker worker = workerReader.readWorker();
                        AddCommand addCommand = new AddCommand(worker);
                        sendCommandToServer(addCommand, address, port);
                        break;
                    case "show":
                        ShowCommand showCommand = new ShowCommand();
                        try {
                            sendCommandToServer(showCommand, address, port);
                            break;
                        } catch (IOException e) {
                            System.err.println(e.getMessage());
                        }
                    case "info":
                        InfoCommand infoCommand = new InfoCommand();
                        try {
                            sendCommandToServer(infoCommand, address, port);
                        } catch (IOException e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    case "print_descending":
                        PrintDescendingCommand printDescendingCommand = new PrintDescendingCommand();
                        try {
                            sendCommandToServer(printDescendingCommand, address, port);
                        } catch (IOException e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    case "clear":
                        ClearCommand clearCommand = new ClearCommand(user);
                        try {
                            organizationReader = receiveOrgReaderFromServer("lol", address, port);
                            sendCommandToServer(clearCommand, address, port);
                            organizationReader.organizationsFullNames.clear();
                        } catch (IOException e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    case "history":
                        HistoryCommand historyCommand = new HistoryCommand();
                        try {
                            sendCommandToServer(historyCommand, address, port);
                        } catch (IOException e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    case "add_if_min":
                        organizationReader = receiveOrgReaderFromServer("lol", address, port);
                        workerReader = new WorkerReader(organizationReader, user);
                        Worker mayBeAddedWorker = workerReader.readWorker();
                        AddIfMinCommand addIfMinCommand = new AddIfMinCommand(mayBeAddedWorker);
                        try {
                            sendCommandToServer(addIfMinCommand, address, port);
                        } catch (IOException e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    case "remove_lower":
                        organizationReader = receiveOrgReaderFromServer("lol", address, port);
                        workerReader = new WorkerReader(organizationReader);
                        Worker removeLowerThanThis = workerReader.readWorker();
                        RemoveLowerCommand removeLowerCommand = new RemoveLowerCommand(removeLowerThanThis, user);
                        try {
                            sendCommandToServer(removeLowerCommand, address, port);
                        } catch (IOException e) {
                            System.err.println(e.getMessage());
                        }
                    case "filter_less_than_organization":
                        organizationReader = receiveOrgReaderFromServer("lol", address, port);
                        workerReader = new WorkerReader(organizationReader);
                        Organization organization = organizationReader.readOrganization();
                        FilterLessThanOrganizationCommand filterLessThanOrganizationCommand = new FilterLessThanOrganizationCommand(organization);
                        try {
                            sendCommandToServer(filterLessThanOrganizationCommand, address, port);
                        } catch (IOException e) {
                            System.err.println(e.getMessage());
                        }
                        break;
                    default:
                        if (input.matches("remove_by_id \\d*")) {
                            Pattern pattern = Pattern.compile("\\d+");
                            Matcher matcher = pattern.matcher(input);
                            if (matcher.find()) {
                                int id = Integer.parseInt(matcher.group());
                                RemoveByIdCommand removeByIdCommand = new RemoveByIdCommand(id, user);
                                try {
                                    sendCommandToServer(removeByIdCommand, address, port);
                                } catch (IOException e) {
                                    System.err.println(e.getMessage());
                                }
                                break;
                            } else {
                                System.out.println("Неверный формат команды");
                            }
                        } else if (input.matches("update_by_id \\d+")) {
                            organizationReader = receiveOrgReaderFromServer("lol", address, port);
                            workerReader = new WorkerReader(organizationReader, user);
                            Pattern pattern = Pattern.compile("\\d+");
                            Matcher matcher = pattern.matcher(input);
                            if (matcher.find()) {
                                int id = Integer.parseInt(matcher.group());
                                Worker updatedWorker = workerReader.readWorker();
                                UpdateIdCommand updateIdCommand = new UpdateIdCommand(id, updatedWorker);
                                try {
                                    sendCommandToServer(updateIdCommand, address, port);
                                } catch (IOException e) {
                                    System.err.println(e.getMessage());
                                }
                            } else {
                                System.out.println("Неверный формат команды");
                            }

                        } else if (input.matches("execute_script \\S*")) {
                            organizationReader = receiveOrgReaderFromServer("lol", address, port);
                            String[] tokens = input.split(" ");
                            if (tokens.length == 2) {
                                String scriptFileName = tokens[1];
                                executedScripts.add(scriptFileName);
                                if (scriptFileName != null) {
                                    executeScript(scriptFileName, address, port, user);
                                    executedScripts.remove(scriptFileName);
                                } else System.err.println("Файл не найден");
                            }
                        } else {
                            System.out.println("Команда или ее параметр введен(-а) неверно");
                        }
                }
            } catch (NoSuchElementException e) {
                ExitCommand exitCommand = new ExitCommand();
                sendCommandToServer(exitCommand, address, port);
                System.exit(0);
            } catch (SocketTimeoutException e) {
                System.out.println("Не удалось подключится к серверу. Повторите попытку позже");
            }

            // Закрываем ресурсы
        }
        socket.close();


    }


    private void sendCommandToServer(Command command, InetAddress address, int port) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(command);
        oos.flush();

        byte[] serializedCommand = baos.toByteArray();

        DatagramPacket packet = new DatagramPacket(serializedCommand,
                serializedCommand.length,
                address,
                port);
        socket.send(packet);

        // получаем ответ от сервера
        try {
            byte[] buffer = new byte[BUF_SIZE];

            packet = new DatagramPacket(buffer, BUF_SIZE);
            socket.receive(packet);
            String response = new String(packet.getData(), 0, packet.getLength());
            System.out.println(response);
        } catch (SocketTimeoutException e) {
            System.out.println("Не удалось подключится к серверу. Повторите попытку позже.");
        }

    }

    private OrganizationReader receiveOrgReaderFromServer(String lol, InetAddress address, int port) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(lol);
        oos.flush();

        byte[] serializedCommand = baos.toByteArray();

        DatagramPacket packet = new DatagramPacket(serializedCommand,
                serializedCommand.length,
                address,
                port);
        socket.send(packet);

        byte[] buffer = new byte[BUF_SIZE];
        packet = new DatagramPacket(buffer, BUF_SIZE);
        socket.receive(packet);
        ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
        ObjectInputStream ois = new ObjectInputStream(bais);
        OrganizationReader response = (OrganizationReader) ois.readObject();
        ois.close();
        bais.close();

        return response;

    }

    private boolean checkServerIsAlive(String lol, InetAddress address, int port) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(lol);
        oos.flush();

        byte[] serializedMessage = baos.toByteArray();

        DatagramPacket packet = new DatagramPacket(serializedMessage,
                serializedMessage.length,
                address,
                port);
        socket.send(packet);

        // получаем ответ от сервера
        byte[] buffer = new byte[BUF_SIZE];
        packet = new DatagramPacket(buffer, BUF_SIZE);
        socket.receive(packet);
        ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());
        ObjectInputStream ois = new ObjectInputStream(bais);
        boolean isAlive = (boolean) ois.readObject();
        ois.close();
        bais.close();

        return isAlive;

    }

    private void executeScript(String scriptFileName, InetAddress address, int port, User user) throws IOException {
        File file = new File(scriptFileName);
        ExecuteScriptCommand executeScriptCommand = new ExecuteScriptCommand();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                switch (line) {
                    case "add" -> {
                        try {
                            Worker scriptWorker = executeScriptCommand.readWorkerFromFile(reader, organizationReader);
                            AddCommand scriptAdd = new AddCommand(scriptWorker);
                            sendCommandToServer(scriptAdd, address, port);
                        } catch (WrongScriptDataException e) {
                            System.err.println(e.getMessage());
                        }
                    }
                    case "help" -> {
                        HelpCommand scriptHelp = new HelpCommand();
                        sendCommandToServer(scriptHelp, address, port);
                    }
                    case "info" -> {
                        InfoCommand scriptInfo = new InfoCommand();
                        sendCommandToServer(scriptInfo, address, port);
                    }
                    case "show" -> {
                        ShowCommand scriptShow = new ShowCommand();
                        sendCommandToServer(scriptShow, address, port);
                    }
                    case "clear" -> {
                        ClearCommand scriptClear = new ClearCommand(user);
                        sendCommandToServer(scriptClear, address, port);
                    }
                    case "history" -> {
                        HistoryCommand scriptHistory = new HistoryCommand();
                        sendCommandToServer(scriptHistory, address, port);
                    }
                    case "min_by_name" -> {
                        MinByNameCommand scriptMinByName = new MinByNameCommand();
                        sendCommandToServer(scriptMinByName, address, port);
                    }
                    case "print_descending" -> {
                        PrintDescendingCommand scriptDesc = new PrintDescendingCommand();
                        sendCommandToServer(scriptDesc, address, port);
                    }
                    case "add_if_min" -> {
                        Worker scriptWorker = null;
                        try {
                            scriptWorker = executeScriptCommand.readWorkerFromFile(reader, organizationReader);
                        } catch (WrongScriptDataException e) {
                            throw new RuntimeException(e);
                        }
                        AddIfMinCommand scriptAddIfMin = new AddIfMinCommand(scriptWorker);
                        sendCommandToServer(scriptAddIfMin, address, port);
                    }
                    case "remove_lower" -> {
                        Worker scriptWorker = null;
                        try {
                            scriptWorker = executeScriptCommand.readWorkerFromFile(reader, organizationReader);
                        } catch (WrongScriptDataException e) {
                            throw new RuntimeException(e);
                        }
                        RemoveLowerCommand scriptRemoveLower = new RemoveLowerCommand(scriptWorker, user);
                    }
                    default -> {
                        if (line.matches("remove_by_id \\d+")) {
                            Pattern pattern = Pattern.compile("\\d+");
                            Matcher matcher = pattern.matcher(line);
                            if (matcher.find()) {
                                int id = Integer.parseInt(matcher.group());
                                RemoveByIdCommand scriptRemoveByID = new RemoveByIdCommand(id, user);
                                sendCommandToServer(scriptRemoveByID, address, port);
                            } else {
                                System.out.println("Неверный формат команды remove_by_id {id} в скрипте");
                            }
                        } else if (line.matches("update_by_id \\d+")) {
                            Pattern pattern = Pattern.compile("\\d+");
                            Matcher matcher = pattern.matcher(line);
                            if (matcher.find()) {
                                int id = Integer.parseInt(matcher.group());
                                try {
                                    Worker sciptWorker = executeScriptCommand.readWorkerFromFile(reader, organizationReader);
                                    UpdateIdCommand sciptUpdateById = new UpdateIdCommand(id, sciptWorker);
                                    sendCommandToServer(sciptUpdateById, address, port);
                                } catch (WrongScriptDataException e) {
                                    System.out.println(e.getMessage());
                                }
                            } else {
                                System.out.println("Неверный формат команды update_by_id id в скрипте");
                            }
                        } else if (line.matches("execute_script \\S*")) {
                            String[] tokens = line.split(" ");
                            if (tokens.length == 2) {
                                String inScriptFileName = tokens[1];
                                if (!executedScripts.contains(inScriptFileName)) {
                                    executedScripts.add(inScriptFileName);
                                    executeScript(inScriptFileName, address, port, user);
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
        } catch (FileNotFoundException e) {
            System.err.println("Файл со скриптом не найден");
        }
    }

    private String sendCommandWithUserToServer(Command command, InetAddress address, int port) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(command);
        oos.flush();

        byte[] serializedCommand = baos.toByteArray();

        DatagramPacket packet = new DatagramPacket(serializedCommand,
                serializedCommand.length,
                address,
                port);
        socket.send(packet);

        // получаем ответ от сервера
        try {
            byte[] buffer = new byte[BUF_SIZE];

            packet = new DatagramPacket(buffer, BUF_SIZE);
            socket.receive(packet);
            String response = new String(packet.getData(), 0, packet.getLength());
            return response;
        } catch (SocketTimeoutException e) {
            return("Не удалось подключится к серверу. Повторите попытку позже.");
        }
    }

    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.connect("localhost", 1236);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}