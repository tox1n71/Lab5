package ru.itmo.lab5;


import org.xml.sax.SAXException;

import ru.itmo.lab5.readers.CommandReader;
import ru.itmo.lab5.readers.OrganizationReader;
import ru.itmo.lab5.readers.WorkerReader;
import ru.itmo.lab5.worker.Worker;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.TreeSet;

import static ru.itmo.lab5.readers.XMLReader.parseXML;


class Main {

    public static void main(String[] args) {
        final String EnvironmentalVariable = "MY_FILE";
        String fileName = System.getenv(EnvironmentalVariable);
        if (fileName == null) {
            System.err.println("Переменная окружения не найдена. Установите переменную окружения и повторите попытку");
        } else {
            try {
                File file = new File(fileName);
                if (!file.canRead()){
                    System.err.println("Ваш файл закрыт для чтения. Измените права доступа или выберите другой файл");
                } else if (!file.canWrite()) {
                    System.err.println("Ваш файл закрыт для записи. Измените права доступа или выберите другой файл");
                }
                else {
                OrganizationReader organizationReader = new OrganizationReader();
                WorkerReader workerReader = new WorkerReader(organizationReader);
                if (file.length() == 0) {
                    TreeSet<Worker> workers = new TreeSet<>();
                    CommandReader commandReader = new CommandReader(organizationReader, workerReader, file, workers);
                    commandReader.readCommand();
                } else {

                    TreeSet<Worker> workers = parseXML(file, organizationReader);
                    CommandReader commandReader = new CommandReader(organizationReader, workerReader, file, workers);
                    commandReader.readCommand();
                }}

            } catch (FileNotFoundException e) {
                System.err.println("Файл заданный переменной окружения не найден. Добавьте файл или проверьте переменную окружения и повторите попытку");
            } catch (ParserConfigurationException | SAXException e) {
                System.err.println("Файл не валидный. Необходимо выбрать другой файл.");
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }

        }
    }
}

