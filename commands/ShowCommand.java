package ru.itmo.lab5.commands;

import ru.itmo.lab5.server.CollectionManager;

public class ShowCommand implements Command{
    private String name = "show";
    private CollectionManager cm;

    @Override
    public String execute(){
        return cm.show();
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
