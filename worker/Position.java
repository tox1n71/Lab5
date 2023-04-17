package ru.itmo.lab5.worker;

import ru.itmo.lab5.exceptions.InputException;

public enum Position {
    LABORER,
    ENGINEER,
    HEAD_OF_DEPARTMENT,
    LEAD_DEVELOPER,
    BAKER;

    public static Position stringToPosition(String pos) throws InputException {
        return switch (pos) {
            case "LABORER" -> Position.LABORER;
            case "ENGINEER" -> Position.ENGINEER;
            case "HEAD_OF_DEPARTMENT" -> Position.HEAD_OF_DEPARTMENT;
            case "LEAD_DEVELOPER" -> Position.LEAD_DEVELOPER;
            case "BAKER" -> Position.BAKER;
            default -> throw new InputException();
        };
    }
}
