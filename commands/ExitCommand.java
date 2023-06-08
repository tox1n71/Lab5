package ru.itmo.lab5.commands;

import ru.itmo.lab5.server.CollectionManager;

import java.io.IOException;

public class ExitCommand implements Command {
    private String name = "exit";
    private CollectionManager cm;

    @Override
    public String execute(){
        try {
            return cm.exit();
        } catch (IOException e) {
            throw new RuntimeException("Произошел ексепшн: не удалось сохранить коллекцию в файл после завершения работы клиента");
        }
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
