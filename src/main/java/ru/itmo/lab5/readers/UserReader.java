package ru.itmo.lab5.readers;

import ru.itmo.lab5.utils.User;

import java.util.Scanner;

public class UserReader {
    public static User readUser(){
        String username = "";
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите username: ");
        while (username.trim().isEmpty()) {
            username = scanner.nextLine();
            if (username.trim().isEmpty()) {
                System.err.println("Username не может быть пустым. Попробуйте еще раз.");
            }
        }
        String password = "";
        System.out.println("Введите пароль: ");
        while (password.trim().isEmpty()) {
            password = scanner.nextLine();
            if (password.trim().isEmpty()) {
                System.err.println("Пароль не может быть пустым. Попробуйте еще раз.");
            }
        }
        return new User(getFreeId(), username, password);
    }

    public static int getFreeId() {
        int id = 1 + (int) (Math.random() * Integer.MAX_VALUE);
        return id;
    }
}
