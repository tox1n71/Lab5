package org.example.models;

import jakarta.persistence.*;


import java.io.Serializable;
import java.util.List;

@Entity(name = "users")
public class User implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "username")
    private String username;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "username", referencedColumnName = "username")
    private List<Worker> workerList;
    @Column(name = "password_hash")
    private String password;


    public User(String username, List<Worker> workerList, String password) {
        this.username = username;
        this.workerList = workerList;
        this.password = password;
    }

    public User() {
    }

    public List<Worker> getWorkerList() {
        return workerList;
    }

    public void setWorkerList(List<Worker> workerList) {
        this.workerList = workerList;
    }
}
