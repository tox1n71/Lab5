package ru.itmo.lab5.worker;

import ru.itmo.lab5.exceptions.InputException;

public class Location {
    private int x;
    private Long y; //Поле не может быть null
    private Integer z; //Поле не может быть null
    private String name; //Строка не может быть пустой, Поле не может быть null

    public Location(){}

    public Location(int x, Long y, int z, String name) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.name = name;
    }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public Long getY() {
        return y;
    }

    public void setY(Long y)   {

        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(Integer z)  {

        this.z = z;
    }

    public String getName() {
        return name;
    }

    public void setName(String name)  {

        this.name = name;
    }


    @Override
    public String toString() {
        return "Location{" +
                "\n\t\t\t\t\t x=" + x +
                ",\n\t\t\t\t\t y=" + y +
                ",\n\t\t\t\t\t z=" + z +
                ",\n\t\t\t\t\t name='" + name + '\'' +
                '}';
    }
    public String toXml() {
        return "\n\t\t\t<Location>" +
                "\n\t\t\t\t<x>" + x + "</x>"+
                "\n\t\t\t\t<y>" + y + "</y>"+
                "\n\t\t\t\t<z>" + z + "</z>"+
                "\n\t\t\t\t<name>'" + name + '\''+ "</name>"  + '\n'+
                "\t\t\t</Location>";
    }

}
