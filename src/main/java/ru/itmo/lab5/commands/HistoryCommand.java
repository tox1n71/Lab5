package ru.itmo.lab5.commands;

import ru.itmo.lab5.server.CollectionManager;

public class HistoryCommand implements Command {
    private String name = "history";
    private CollectionManager cm;

    @Override
    public String execute(){
        return cm.history();
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
