package ru.itmo.lab5.commands;

import ru.itmo.lab5.server.CollectionManager;

public class InfoCommand implements Command{
    private String name = "info";
    private CollectionManager cm;

    @Override
    public String execute(){
        return cm.info();
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
