package ru.itmo.lab5;

import ru.itmo.lab5.server.CommandReader;
import ru.itmo.lab5.server.OrganizationReader;
import ru.itmo.lab5.server.WorkerReader;
import ru.itmo.lab5.worker.Worker;

import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;

class Main{

    public static void main(String [] args) throws IOException{

        OrganizationReader organizationReader = new OrganizationReader();
        WorkerReader workerReader = new WorkerReader(organizationReader);
        CommandReader commandReader = new CommandReader(organizationReader, workerReader);
        commandReader.readCommand();
    }

}
