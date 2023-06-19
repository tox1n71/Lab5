package ru.itmo.lab5.commands;


import ru.itmo.lab5.server.CollectionManager;
import ru.itmo.lab5.utils.User;

public class RegisterCommand implements Command{
    private String name = "register";
    private CollectionManager collectionManager;
    private User user;

    public RegisterCommand(User user) {
        this.user = user;
    }

    @Override
    public String execute() {
        return collectionManager.registerUser(user);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setCollectionManager(CollectionManager cm) {
        this.collectionManager = cm;
    }
}
