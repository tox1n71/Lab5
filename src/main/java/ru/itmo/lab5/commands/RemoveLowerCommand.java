package ru.itmo.lab5.commands;

import ru.itmo.lab5.server.CollectionManager;
import ru.itmo.lab5.utils.User;
import ru.itmo.lab5.worker.Worker;

public class RemoveLowerCommand implements Command {


    private Worker worker;
    private String name = "remove_lower";
    private User user;
    CollectionManager collectionManager;
    public RemoveLowerCommand(Worker worker, User user) {
        this.worker = worker;
        this.user = user;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public String execute(){
        collectionManager.removeLower(worker, user);
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