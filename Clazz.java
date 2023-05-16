package ru.itmo.lab5;

import java.util.Iterator;
import java.util.LinkedList;

public class Clazz<T> {
    LinkedList<T> list;
    public void clear(){
        Iterator<T> iterator = list.iterator();
        while(iterator.hasNext()){
            iterator.remove();
        }
    }

    public void add(T value){

    }

    public static void main(String[] args) {
        Clazz<String> inst = new Clazz<>();

        inst.clear();
        inst.add("str");

        Clazz<Integer> inst2 = new Clazz<>();
        inst2.clear();
        inst2.add(123);
    }
}
