package ru.itmo.lab5.worker;



public class Coordinates {


    private double x;
    private Double y; //Поле не может быть null

    public Coordinates(){}


    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public Double getY() {
        return y;
    }

    public void setY(Double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
    public String toXml() {
        return "<Coordinates>" +
                "\n\t\t<x>" + x + "</x>"+
                "\n\t\t<y>" + y + "</x>"  + '\n'+
                "\t</Coordinates>";
    }
}
