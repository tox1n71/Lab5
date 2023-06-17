package ru.itmo.lab5.commands;

import ru.itmo.lab5.server.CollectionManager;

public class PrintDescendingCommand implements Command{
    private String name = "print_descending";
    private CollectionManager cm;

    @Override
    public String execute(){
        return cm.printDescending();
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



