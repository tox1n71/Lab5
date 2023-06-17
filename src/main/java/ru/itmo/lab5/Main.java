package ru.itmo.lab5;

import ru.itmo.lab5.models.Organization;
import ru.itmo.lab5.models.User;
import ru.itmo.lab5.models.Worker;
import ru.itmo.lab5.utils.DataProvider;

import java.util.ArrayList;

public class Main {
//    public static void main(String[] args) {
//        System.out.println("Hello world!");
//        Configuration configuration = new Configuration().addAnnotatedClass(Worker.class).addAnnotatedClass(Organization.class)
//                .addAnnotatedClass(User.class);
//
//        SessionFactory sessionFactory = configuration.buildSessionFactory();
//        Session session = sessionFactory.openSession();
//        session.beginTransaction();
//        Worker worker = session.get(Worker.class, 1);
////        System.out.println(worker.getName());
////        System.out.println(worker.getOrganization().getFullName());
////        User user = session.get(User.class,1);
////        System.out.println(user.getWorkerList().get(0).toString());
////        User user1 = new User("test",new ArrayList<>(Arrays.asList(worker)),"test");
////        session.save(user1);
//        User user1 = new User("test4",new ArrayList<>(Arrays.asList(worker)),"test");
//        session.persist(user1);
//        session.getTransaction().commit();
//        sessionFactory.close();
//    }
public static void main(String[] args) {
    DataProvider dataProvider = new DataProvider();
//    System.out.println(dataProvider.getOrganizationsFullNames().get(0));
//    System.out.println(dataProvider.getWorkers());
//    System.out.println(dataProvider.getUsers());
    User user = new User("Dimon2",new ArrayList<>(),"parolDimona");
    Organization organization = new Organization("Apple",190,200.2,"Cupertino","197987","ya chto ebu", 299,300,301,new Worker());
//            LocalDate.ofYearDay(2023,40),"какая-то позиция",organization,user);
//    dataProvider.saveUser(user);
    dataProvider.saveOrganization(organization);
}
}