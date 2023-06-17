package org.example.utils;

import org.example.models.Organization;
import org.example.models.User;
import org.example.models.Worker;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.ArrayList;
import java.util.List;

public class DataProvider {
    Configuration configuration;
    SessionFactory sessionFactory;
    Session session;
    public DataProvider() {
        this.configuration= new Configuration().addAnnotatedClass(Worker.class).addAnnotatedClass(Organization.class)
                .addAnnotatedClass(User.class);

        this.sessionFactory = configuration.buildSessionFactory();
        this.session = sessionFactory.openSession();
        session.beginTransaction();
    }

    public List<String> getOrganizationsFullNames(){
        List<Organization> organizations= session.createQuery("FROM organization").getResultList();
        List<String> organizationNames = new ArrayList<>();
        for (Organization organization: organizations){
            organizationNames.add(organization.getFullName());
        }
        return organizationNames;
    }

    public List<Worker> getWorkers(){
        return session.createQuery("from worker").getResultList();
    }

    public List<User> getUsers(){
        return session.createQuery("from users").getResultList();
    }
    public void saveOrganization(Organization organization){
        session.save(organization);
        session.getTransaction().commit();
    }

    public void saveWorker(Worker worker){
        session.save(worker);
        session.getTransaction().commit();
    }

    public void saveUser(User user){
        session.save(user);
        session.getTransaction().commit();
    }
}
