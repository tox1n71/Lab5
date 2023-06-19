package ru.itmo.lab5.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.itmo.lab5.worker.*;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;

public class DataProvider {

    String url;
    String username;
    String password;

    public DataProvider() {
        this.url = "jdbc:postgresql://localhost:5555/studs";
//        this.url = "jdbc:postgresql://localhost:5432/studs";
        this.username = "s368791";
        this.password = "7qc3bUPfUtx4bI0P";
    }

    public TreeSet<Worker> getWorkers() {
        TreeSet<Worker> workers = new TreeSet<>();
        try {
            Class.forName("org.postgresql.Driver").getDeclaredConstructor().newInstance();

            try (Connection conn = DriverManager.getConnection(url, username, password)) {

                String workerSql = "SELECT * from worker";
                PreparedStatement workerStatement = conn.prepareStatement(workerSql);
                ResultSet workerResultSet = workerStatement.executeQuery();
                while (workerResultSet.next()) {
                    int workerId = workerResultSet.getInt("id");
                    String name = workerResultSet.getString("name");
                    double coordinates_x = workerResultSet.getDouble("coordinates_x");
                    double coordinates_y = workerResultSet.getDouble("coordinates_y");
                    LocalDate creationDate = workerResultSet.getDate("creation_date").toLocalDate();
                    int salary = workerResultSet.getInt("salary");
                    LocalDate startDate = workerResultSet.getDate("start_date").toLocalDate();
                    LocalDateTime endDate = null;
                    if (workerResultSet.getTimestamp("end_date") != null){
                        endDate = workerResultSet.getTimestamp("end_date").toLocalDateTime();

                    }
                    String positionString = workerResultSet.getString("position");
                    int organization_id = workerResultSet.getInt("organization_id");
                    int user_id = workerResultSet.getInt("user_id");
                    Coordinates coordinates = new Coordinates(coordinates_x, coordinates_y);
                    Worker worker = new Worker();
                    worker.setId(workerId);
                    worker.setName(name);
                    worker.setCoordinates(coordinates);
                    worker.setCreationDate(creationDate);
                    worker.setSalary(salary);
                    worker.setStartDate(startDate);
                    worker.setEndDate(endDate);
                    worker.setPosition(Position.stringToPosition(positionString));

                    String sql = "SELECT * from organization where id = ?";
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setInt(1, organization_id);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    while (resultSet.next()) {
                        int id = resultSet.getInt("id");
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
                        organization.setId(id);
                        worker.setOrganization(organization);
                    }
                    String userSql = "SELECT * from users where id = ?";
                    PreparedStatement userStatement = conn.prepareStatement(userSql);
                    userStatement.setInt(1, user_id);
                    ResultSet userRes = userStatement.executeQuery();
                    while (userRes.next()) {
                        int id = userRes.getInt("id");
                        String userName = userRes.getString("username");
                        String password = userRes.getString("password_hash");
                        User user = new User(user_id, userName, password);
                        worker.setUser(user);
                    }
                    workers.add(worker);
                }
            }
            return workers;
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
            return null;
        }

    }


    public HashSet<String> organizationsFullNames() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, SQLException {
        try {
            Class.forName("org.postgresql.Driver").getDeclaredConstructor().newInstance();
            try (Connection conn = DriverManager.getConnection(url, username, password)) {
                String sql = "SELECT fullname from organization";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery();
                HashSet<String> fullNames = new HashSet<>();
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
                // Сначала сохраняем объект Organization
                PreparedStatement orgStatement = connection.prepareStatement(
                        "INSERT INTO organization (fullname, employeescount, annualturnover, address_street, " +
                                "address_zipcode, address_town, address_x, address_y, address_z) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                orgStatement.setString(1, worker.getOrganization().getFullName());
                orgStatement.setInt(2, (int) worker.getOrganization().getEmployeesCount());
                orgStatement.setDouble(3, Double.valueOf(String.valueOf(worker.getOrganization().getAnnualTurnover())));
                orgStatement.setString(4, worker.getOrganization().getPostalAddress().getStreet());
                orgStatement.setString(5, worker.getOrganization().getPostalAddress().getZipCode());
                orgStatement.setString(6, worker.getOrganization().getPostalAddress().getTown().getName());
                orgStatement.setInt(7, worker.getOrganization().getPostalAddress().getTown().getX());
                orgStatement.setInt(8, worker.getOrganization().getPostalAddress().getTown().getY().intValue());
                orgStatement.setInt(9, worker.getOrganization().getPostalAddress().getTown().getZ());
                orgStatement.executeUpdate();
                ResultSet orgResult = orgStatement.getGeneratedKeys();
                if (orgResult.next()) {
                    int orgId = orgResult.getInt(1);
                    worker.getOrganization().setId(orgId);
                }

                // Затем сохраняем объект Worker с установленным идентификатором Organization
                PreparedStatement workerStatement = connection.prepareStatement(
                        "INSERT INTO worker (name, coordinates_x, coordinates_y, creation_date, salary, " +
                                "start_date, end_date, position, organization_id, user_id) " +
                                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                workerStatement.setString(1, worker.getName());
                workerStatement.setDouble(2, worker.getCoordinates().getX());
                workerStatement.setDouble(3, worker.getCoordinates().getY());
                workerStatement.setDate(4, Date.valueOf(worker.getCreationDate()));
                workerStatement.setInt(5, worker.getSalary());
                workerStatement.setDate(6, Date.valueOf(worker.getStartDate()));
                if (worker.getEndDate() != null) {
                    workerStatement.setTimestamp(7, Timestamp.valueOf(worker.getEndDate()));
                } else {
                    workerStatement.setNull(7, Types.TIMESTAMP);
                }
                workerStatement.setString(8, worker.getPosition().toString());
                workerStatement.setInt(9, worker.getOrganization().getId());
                workerStatement.setInt(10, getUserId(worker.getUser()));
                workerStatement.executeUpdate();
                ResultSet workerResult = workerStatement.getGeneratedKeys();
                if (workerResult.next()) {
                    int workerId = workerResult.getInt(1);
                    worker.setId(workerId);
                }

                System.out.println("Success");
            }
        } catch (Exception ex) {
            System.out.println("Connection failed...");
            System.out.println(ex);
        }
    }
    //TODO: remove by id


    public String checkUser(User receivedUser) {
        try {
            Class.forName("org.postgresql.Driver").getDeclaredConstructor().newInstance();
            Connection conn = DriverManager.getConnection(url, username, password);
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, receivedUser.getName());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    String passwordHash = resultSet.getString("password_hash");
                    PreparedStatement passwordStatement = conn.prepareStatement("SELECT password_hash FROM users where username = ?");
                    passwordStatement.setString(1, receivedUser.getName());
                    try (ResultSet passwordResultSet = passwordStatement.executeQuery()) {
                        if (passwordResultSet.next()) {
                            String receivedHash = receivedUser.getPassword();
                            if (passwordHash.equals(hashPassword(receivedHash))) {
                                return "Вход выполнен";
                            } else {
                                return "Неверный пароль.";
                            }
                        } else {
                            return "Ошибка в получении пароля.";
                        }
                    }
                } else {
                    return "Такого пользователя нет в системе.";
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return "Ошибка при авторизации";
        }
    }

    public String registerUser(User newUser) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT * FROM users WHERE username = ?";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
                preparedStatement.setString(1, newUser.getName());
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return "Пользователь с таким именем уже существует";
                    }
                }
            }

            // Если мы дошли до этого места, значит пользователь не занят
            String sqlInsert = "INSERT INTO users (username, password_hash) VALUES (?, ?)";
            try (PreparedStatement preparedStatement = conn.prepareStatement(sqlInsert)) {
                preparedStatement.setString(1, newUser.getName());
                preparedStatement.setString(2, hashPassword(newUser.getPassword()));
                int rowsInserted = preparedStatement.executeUpdate();
                return "Успешная регистрация. Воспользуйтесь командой login, чтобы авторизоваться";
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        return "Ошибка регистрации";
    }

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD2");
            byte[] bytes = md.digest(password.getBytes());
            BigInteger integers = new BigInteger(1, bytes);
            String newPassword = integers.toString(16);
            while (newPassword.length() < 32) {
                newPassword = "0" + newPassword;
            }
            return newPassword;
        } catch (NoSuchAlgorithmException exception) {
            System.err.println(exception.getMessage());
            System.err.println("Алгоритм хеширования пароля не найден!");
            throw new IllegalStateException(exception);
        }
    }

    public int getUserId(User user) {
        try (Connection conn = DriverManager.getConnection(url, username, password)) {
            String sql = "SELECT id FROM users WHERE username = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, user.getName());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                return id;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return 0;
    }
}
