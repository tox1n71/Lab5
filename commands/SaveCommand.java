package ru.itmo.lab5.commands;

import ru.itmo.lab5.server.CollectionManager;

import java.io.IOException;

public class SaveCommand {
    private CollectionManager cm;
    private String name = "save";
    public String execute() throws IOException {
        return cm.save();
    }


    public String getName() {
        return name;
    }


    public void setCollectionManager(CollectionManager cm) {
        this.cm = cm;
    }
}

