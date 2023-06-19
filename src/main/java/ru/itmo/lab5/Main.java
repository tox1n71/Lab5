//package ru.itmo.lab5;
//
//
//
//import ru.itmo.lab5.utils.DataProvider;
//import ru.itmo.lab5.worker.Address;
//import ru.itmo.lab5.worker.Location;
//import ru.itmo.lab5.worker.Organization;
//
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.time.LocalDate;
//import java.util.ArrayList;
//
//public class Main {
////    public static void main(String[] args) {
////        System.out.println("Hello world!");
////        Configuration configuration = new Configuration().addAnnotatedClass(Worker.class).addAnnotatedClass(Organization.class)
////                .addAnnotatedClass(User.class);
////
////        SessionFactory sessionFactory = configuration.buildSessionFactory();
////        Session session = sessionFactory.openSession();
////        session.beginTransaction();
////        Worker worker = session.get(Worker.class, 1);
//////        System.out.println(worker.getName());
//////        System.out.println(worker.getOrganization().getFullName());
//////        User user = session.get(User.class,1);
//////        System.out.println(user.getWorkerList().get(0).toString());
//////        User user1 = new User("test",new ArrayList<>(Arrays.asList(worker)),"test");
//////        session.save(user1);
////        User user1 = new User("test4",new ArrayList<>(Arrays.asList(worker)),"test");
////        session.persist(user1);
////        session.getTransaction().commit();
////        sessionFactory.close();
////    }
//public static void main(String[] args) {
//    try {
//        String url = "jdbc:postgresql://localhost:5555/studs";
//        String username = "s368791";
//        String password = "7qc3bUPfUtx4bI0P";
//
//
//        Class.forName("org.postgresql.Driver").getDeclaredConstructor().newInstance();
//
//        try (Connection conn = DriverManager.getConnection(url, username, password)) {
//            String sql1 = "SELECT * from worker";
//            PreparedStatement preparedStatement1 = conn.prepareStatement(sql1);
//            ResultSet resultSet1 = preparedStatement1.executeQuery();
//            while(resultSet1.next()){
//
//                int id = resultSet1.getInt("id");
//                int salary = resultSet1.getInt("salary");
//                double x = resultSet1.getDouble("coordinates_x");
//                double y = resultSet1.getDouble("coordinates_y");
//                LocalDate creationDate;
//
//            String sql = "SELECT * from organization";
//            PreparedStatement preparedStatement = conn.prepareStatement(sql);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while(resultSet.next()){
//
//                String fullName = resultSet.getString("fullname");
//                float anualTurnover = resultSet.getFloat("annualTurnover");
//                int employeesCount = resultSet.getInt("employeescount");
//                String street = resultSet.getString("address_street");
//                String address_zipcode = resultSet.getString("address_zipcode");
//                String address_town = resultSet.getString("address_town");
//                int address_x = resultSet.getInt("address_x");
//                int address_y = resultSet.getInt("address_y");
//                int address_z = resultSet.getInt("address_z");
//                Location town = new Location(address_x, Long.valueOf(address_y), address_z, address_town);
//                Address postalAddress = new Address(street, address_zipcode, town);
//                Organization organization = new Organization(fullName, anualTurnover,Long.valueOf(employeesCount), postalAddress);
//                System.out.println(organization);
//            }
//        } catch (Exception ex) {
//            System.out.println("Connection failed...");
//
//            System.out.println(ex);
//        }
//
//        try (Connection conn = DriverManager.getConnection(url, username, password)) {
//
//            String sql = "SELECT * from organization";
//            PreparedStatement preparedStatement = conn.prepareStatement(sql);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while(resultSet.next()){
//
//                String fullName = resultSet.getString("fullname");
//                float anualTurnover = resultSet.getFloat("annualTurnover");
//                int employeesCount = resultSet.getInt("employeescount");
//                String street = resultSet.getString("address_street");
//                String address_zipcode = resultSet.getString("address_zipcode");
//                String address_town = resultSet.getString("address_town");
//                int address_x = resultSet.getInt("address_x");
//                int address_y = resultSet.getInt("address_y");
//                int address_z = resultSet.getInt("address_z");
//                Location town = new Location(address_x, Long.valueOf(address_y), address_z, address_town);
//                Address postalAddress = new Address(street, address_zipcode, town);
//                Organization organization = new Organization(fullName, anualTurnover,Long.valueOf(employeesCount), postalAddress);
//                System.out.println(organization);
//            }
//        } catch (Exception ex) {
//            System.out.println("Connection failed...");
//
//            System.out.println(ex);
//        }
//    } catch (Exception e) {
//    }
//
//}
//}