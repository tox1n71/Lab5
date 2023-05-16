package ru.itmo.lab5.commands;

public enum CommandType {
    HELP("help"),
    INFO("info"),
    SHOW("show"),
    ADD("add"),
    UPDATE("update"),
    REMOVE_BY_ID("remove_by_id"),
    CLEAR("clear"),
    EXECUTE_SCRIPT("execute_script"),
    EXIT("exit"),
    ADD_IF_MIN("add_if_min"),
    REMOVE_LOWER("remove_lower"),
    HISTORY("history"),
    MIN_BY_NAME("min_by_name"),
    FILTER_LESS_THAN_ORGANIZATION("filter_less_than_organization"),
    PRINT_DESCENDING("print_descending");
    private final String value;

    CommandType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CommandType fromString(String command) {
        for (CommandType type : CommandType.values()) {
            if (type.getValue().equalsIgnoreCase(command)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown command: " + command);
    }
}

