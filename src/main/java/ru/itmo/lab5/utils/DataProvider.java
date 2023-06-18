package ru.itmo.lab5.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.itmo.lab5.worker.Address;
import ru.itmo.lab5.worker.Location;
import ru.itmo.lab5.worker.Organization;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DataProvider {
    Configuration configuration;
    SessionFactory sessionFactory;
    Session session;








    public void getWorkers() {
        try {
            String url = "jdbc:mysql://localhost/store?serverTimezone=Europe/Moscow&useSSL=false";
            String username = "root";
            String password = "password";


            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();

            try (Connection conn = DriverManager.getConnection(url, username, password)) {

                String sql = "SELECT * from organization";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery();
                while(resultSet.next()){

                    String fullName = resultSet.getString("fullname");
                    float anualTurnover = resultSet.getFloat("annualTurnover");
                    int employeesCount = resultSet.getInt("employeescount");
                    String street = resultSet.getString("address_street");
                    String address_zipcode = resultSet.getString("address_zipcode");
                    String address_town = resultSet.getString("address_town");
                    int address_x = resultSet.getInt("address_x");
                    int address_y = resultSet.getInt("address_y");
                    int address_z = resultSet.getInt("address_z");
                    Location town = new Location(address_x, Long.valueOf(address_y), address_z, address_town);
                    Address postalAddress = new Address(street, address_zipcode, town);
                    Organization organization = new Organization(fullName, anualTurnover,Long.valueOf(employeesCount), postalAddress);
                    System.out.println(organization);
                }
            } catch (Exception ex) {
                System.out.println("Connection failed...");

                System.out.println(ex);
            }
        } catch (Exception e) {
        }
    }

}
