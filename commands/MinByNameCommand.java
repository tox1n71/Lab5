package ru.itmo.lab5.commands;

import ru.itmo.lab5.server.CollectionManager;
import ru.itmo.lab5.worker.Worker;

public class MinByNameCommand implements Command {
    private String name = "min_by_name";
    private CollectionManager cm;

    @Override
    public String execute(){
        return cm.minByName();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setCollectionManager(CollectionManager cm) {
        this.cm = cm;
    }
}