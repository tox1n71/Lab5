package ru.itmo.lab5.commands;

import ru.itmo.lab5.server.CollectionManager;
public class ClearCommand implements Command{
    private String name = "clear";
    private CollectionManager cm;

    @Override
    public String execute(){
        return cm.clear();
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
