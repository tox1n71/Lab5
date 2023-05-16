package ru.itmo.lab5.client;

import ru.itmo.lab5.commands.*;
import ru.itmo.lab5.readers.CommandReader;
import ru.itmo.lab5.readers.OrganizationReader;
import ru.itmo.lab5.readers.WorkerReader;
import ru.itmo.lab5.worker.Worker;

import java.io.*;
import java.net.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {

    private static final int BUF_SIZE = 4096;


    private OrganizationReader organizationReader;
    private WorkerReader workerReader;
    private CommandReader commandReader;

    private DatagramSocket socket;

    public void connect(String hostname, int port) throws Exception {
        // Создаем сокет для отправки датаграмм на сервер
        socket = new DatagramSocket();

        InetAddress address = InetAddress.getByName(hostname);


        Scanner scanner = new Scanner(System.in);
        organizationReader = new OrganizationReader();
        workerReader = new WorkerReader(organizationReader);

        // Цикл отправки команд на сервер
        boolean isProgrammActive = true;
        while (isProgrammActive) {
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
                    System.out.println("Завершение программы");
                    isProgrammActive = false;
                    socket.close();
                    break;
                case "add":
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
                    ClearCommand clearCommand = new ClearCommand();
                    try {
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
                    Worker mayBeAddedWorker = workerReader.readWorker();
                    AddIfMinCommand addIfMinCommand = new AddIfMinCommand(mayBeAddedWorker);
                    try {
                        sendCommandToServer(addIfMinCommand, address, port);
                    } catch (IOException e) {
                        System.err.println(e.getMessage());
                    }
                default:
                    if (input.matches("remove_by_id \\d*")) {
                        Pattern pattern = Pattern.compile("\\d+");
                        Matcher matcher = pattern.matcher(input);
                        if (matcher.find()) {
                            int id = Integer.parseInt(matcher.group());
                            RemoveByIdCommand removeByIdCommand = new RemoveByIdCommand(id);
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

                    } else {
                        System.out.println("Invalid command");
                    }
            }
        }

        // Закрываем ресурсы
        socket.close();
    }

    // Метод для отправки команды на сервер по протоколу UDP
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
        byte[] buffer = new byte[BUF_SIZE];
        packet = new DatagramPacket(buffer, BUF_SIZE);
        socket.receive(packet);
        String response = new String (packet.getData(), 0, packet.getLength());
        System.out.println(response);

    }


    public static void main(String[] args) {
        try {
            Client client = new Client();
            client.connect("localhost", 1234);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}