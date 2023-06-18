package ru.itmo.lab5.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.itmo.lab5.worker.Address;
import ru.itmo.lab5.worker.Location;
import ru.itmo.lab5.worker.Organization;
import ru.itmo.lab5.worker.Worker;

import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class DataProvider {

    String url;
    String username;
    String password;

    public DataProvider() {
        this.url = "jdbc:postgresql://localhost:5432/vanek";
        this.username = "postgres";
        this.password = "1234";
    }

    public void getWorkers() {
        try {

            Class.forName("com.mysql.cj.jdbc.Driver").getDeclaredConstructor().newInstance();

            try (Connection conn = DriverManager.getConnection(url, username, password)) {

                String sql = "SELECT * from organization";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {

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
                    Organization organization = new Organization(fullName, anualTurnover, Long.valueOf(employeesCount), postalAddress);
                    System.out.println(organization);
                }
            } catch (Exception ex) {
                System.out.println("Connection failed...");

                System.out.println(ex);
            }
        } catch (Exception e) {
        }


    }

    public List<String> organizationsFullNames() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        try {
            Class.forName("org.postgresql.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                String sql = "SELECT fullname from organization";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery();
                List<String> fullNames = new ArrayList<>();
                while (resultSet.next()) {
                    fullNames.add(resultSet.getString(1));
                }
                return fullNames;
            }
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

//    public void saveWorkers(TreeSet<Worker> workers) {
//        try {
//            Class.forName("org.postgresql.Driver").getDeclaredConstructor().newInstance();
//            Connection connection = DriverManager.getConnection(url, username, password);
//            connection.setAutoCommit(false); // Отключаем автокоммит для ускорения операций вставки
//
//            PreparedStatement workerStatement = connection.prepareStatement(
//                    "INSERT INTO worker (name, coordinates_x, coordinates_y, creation_date, salary, " +
//                            "start_date, end_date, position, organization_id) " +
//                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
//
//            PreparedStatement orgStatement = connection.prepareStatement(
//                    "INSERT INTO organization (fullname, employeescount, annualturnover, address_street, " +
//                            "address_zipcode, address_town, address_x, address_y, address_z) " +
//                            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
//
//            for (Worker worker : workers) {
//                // Сохраняем объект Organization в таблицу organization
//                Organization organization = worker.getOrganization();
//                if (organization != null) {
//                    orgStatement.setString(1, organization.getFullName());
//                    orgStatement.setInt(2, Integer.parseInt(String.valueOf(organization.getEmployeesCount())));
//                    orgStatement.setDouble(3, organization.getAnnualTurnover());
//                    orgStatement.setString(4, organization.getPostalAddress().getStreet());
//                    orgStatement.setString(5, organization.getPostalAddress().getZipCode());
//                    orgStatement.setString(6, organization.getPostalAddress().getTown().getName());
//                    orgStatement.setInt(7, organization.getPostalAddress().getTown().getX());
//                    orgStatement.setInt(8, Integer.valueOf(String.valueOf(organization.getPostalAddress().getTown().getY())));
//                    orgStatement.setInt(9, organization.getPostalAddress().getTown().getZ());
//                    orgStatement.executeUpdate();
//
//
//                }
//
//                // Сохраняем объект Worker в таблицу worker
//                workerStatement.setString(1, worker.getName());
//                workerStatement.setDouble(2, worker.getCoordinates().getX());
//                workerStatement.setDouble(3, worker.getCoordinates().getY());
//                workerStatement.setDate(4, Date.valueOf(worker.getCreationDate()));
//                workerStatement.setInt(5, worker.getSalary());
//                workerStatement.setDate(6, Date.valueOf(worker.getStartDate()));
//                if (worker.getEndDate() != null) {
//                    workerStatement.setTimestamp(7, Timestamp.valueOf(worker.getEndDate()));
//                } else {
//                    workerStatement.setNull(7, Types.TIMESTAMP);
//                }
//                workerStatement.setString(8, worker.getPosition().toString());
//                if (organization != null) {
//                    workerStatement.setInt(9, organization.getId());
//                } else {
//                    workerStatement.setNull(9, Types.INTEGER);
//                }
//                workerStatement.executeUpdate();
//
//
//            }
//
//            connection.commit(); // Фиксируем транзакцию
//            workerStatement.close();
//            orgStatement.close();
//            connection.close();
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
//    }

    public void saveWorkers(TreeSet<Worker> workerTreeSet) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        try {
            Class.forName("org.postgresql.Driver").getDeclaredConstructor().newInstance();
            Connection connection = DriverManager.getConnection(url, username, password);
            for (Worker worker : workerTreeSet) {
                PreparedStatement workerStatement = connection.prepareStatement(
                        "INSERT INTO worker (name, coordinates_x, coordinates_y, creation_date, salary, " +
                                "start_date, end_date, position, organization_id, user_id) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                PreparedStatement orgStatement = connection.prepareStatement(
                        "INSERT INTO organization (fullname, employeescount, annualturnover, address_street, " +
                                "address_zipcode, address_town, address_x, address_y, address_z) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
                workerStatement.setString(1, worker.getName());
                workerStatement.setDouble(2, worker.getCoordinates().getX());
                workerStatement.setDouble(3, worker.getCoordinates().getY());
                workerStatement.setString(4, worker.getCreationDateString());
                workerStatement.setInt(5, worker.getSalary());
                workerStatement.setDate(6, Date.valueOf(worker.getStartDate()));
                if (worker.getEndDate() != null) {
                    workerStatement.setDate(7, Date.valueOf(String.valueOf(worker.getEndDate())));
                }
                workerStatement.setString(8, worker.getPosition().toString());
                workerStatement.setInt(9, worker.getOrganization().getId());
                workerStatement.setInt(10, worker.getUser().getId());

                orgStatement.setString(1, worker.getOrganization().getFullName());
                orgStatement.setInt(2, (int) worker.getOrganization().getEmployeesCount());
                orgStatement.setInt(3, Math.round(worker.getOrganization().getAnnualTurnover()));
                orgStatement.setString(4, worker.getOrganization().getPostalAddress().getStreet());
                orgStatement.setString(5, worker.getOrganization().getPostalAddress().getZipCode());
                orgStatement.setInt(6, worker.getOrganization().getPostalAddress().getTown().getX());
                orgStatement.setInt(7, worker.getOrganization().getPostalAddress().getTown().getY().intValue());
                orgStatement.setInt(9, worker.getOrganization().getPostalAddress().getTown().getZ());
                System.out.println("Success");

            }


        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (SecurityException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean checkUser(User receivedUser) {
        try {
            Class.forName("org.postgresql.Driver").getDeclaredConstructor().newInstance();
            Connection conn = DriverManager.getConnection(url, username, password);
            String sql = "SELECT * from users";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()){
                String userName = resultSet.getString("username");
                if (userName.equals(receivedUser.getName())){
                    String password = resultSet.getString("password");
                    if (password.equals(receivedUser.getPassword())){
                        return true;
                    }
                }
            }
        }
        catch (Exception e) {
        }
        return false;
    }
}
