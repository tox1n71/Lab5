package ru.itmo.lab5.commands;

import ru.itmo.lab5.server.CollectionManager;

public class RemoveByIdCommand implements Command {
    private int id;
    private String name = "remove_by_id";
    private CollectionManager collectionManager;

    public RemoveByIdCommand(int id) {
        this.id = id;

    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String execute(){
        return collectionManager.removeById(id);
    }

    @Override
    public String getName() {
        return name;
    }
    public void setCollectionManager(CollectionManager cm) {
        this.collectionManager = cm;
    }
}
    // Геттеры и сеттеры

