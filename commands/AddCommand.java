package ru.itmo.lab5.commands;

import ru.itmo.lab5.server.CollectionManager;
import ru.itmo.lab5.worker.Worker;

import java.time.LocalDate;
import java.util.TreeSet;

public class AddCommand  implements Command {


    private Worker worker;
    private String name = "add_if_min";
    CollectionManager collectionManager;
    public AddCommand(Worker worker) {
        this.worker = worker;

    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public String execute(){
        collectionManager.add(worker);
        return ("Введенный элемент добавлен в коллекцию с id" + worker.getId());
    }

    @Override
    public String getName() {
        return name;
    }
    public void setCollectionManager(CollectionManager cm) {
        this.collectionManager = cm;
    }
}
