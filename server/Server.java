package ru.itmo.lab5.server;

import ru.itmo.lab5.commands.Command;
import ru.itmo.lab5.commands.HelpCommand;
import ru.itmo.lab5.readers.OrganizationReader;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Arrays;

public class Server {

    private static final int BUF_SIZE = 4096;

    private CollectionManager collectionManager = new CollectionManager();

    public void start(int port) throws Exception {
        // создаем сокет и связываем его с портом сервера
        DatagramSocket serverSocket = new DatagramSocket(port);
        System.out.println("Server started on port " + port);

        byte[] buf = new byte[BUF_SIZE];


        while (true) {
            // принимаем датаграмму от клиента
            DatagramPacket packet = new DatagramPacket(buf, BUF_SIZE);
            serverSocket.receive(packet);

            // десериализуем команду из полученных данных

            ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData(), 0, packet.getLength());

            ObjectInputStream ois = new ObjectInputStream(bais);
            Command receivedCommand = (Command) ois.readObject();
            System.out.println(receivedCommand);

            receivedCommand.setCollectionManager(collectionManager);
            ois.close();
            bais.close();

            // обрабатываем команду
            String response = collectionManager.executeCommand(receivedCommand);

            // отправляем ответ клиенту
            InetAddress address = packet.getAddress();
            int clientPort = packet.getPort();
            byte[] responseData = response.getBytes();
            DatagramPacket responsePacket = new DatagramPacket(responseData, responseData.length, address, clientPort);
            responsePacket.setData(responseData, 0, responseData.length);
            serverSocket.setSendBufferSize(responseData.length);
            serverSocket.send(responsePacket);



        }
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.start(1234);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
