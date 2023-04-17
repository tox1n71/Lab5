package ru.itmo.lab5.exceptions;

import java.io.IOException;

public class InputException extends IOException {

    public InputException(String msg){
        super(msg);
    }
    public InputException(){}
}
