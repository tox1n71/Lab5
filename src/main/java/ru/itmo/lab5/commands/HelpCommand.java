package ru.itmo.lab5.commands;

import ru.itmo.lab5.server.CollectionManager;

public class HelpCommand implements Command {
    private String name = "help";
    private CollectionManager cm;

    @Override
    public String execute(){
        return cm.help();
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
