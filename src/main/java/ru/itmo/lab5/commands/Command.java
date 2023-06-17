package ru.itmo.lab5.commands;

import ru.itmo.lab5.server.CollectionManager;

import java.io.Serializable;

public interface Command extends Serializable {
    String execute();
    String getName();
    void setCollectionManager(CollectionManager cm);
}

