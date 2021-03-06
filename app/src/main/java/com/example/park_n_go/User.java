package com.example.park_n_go;

public class User {
    // fields
    private String name;
    private String role;
    private String email;
    private String password;
    private String id;

    // Getter and setters


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    // Constructors
    public User(){}

    public User(String name, String role, String email, String password, String id) {
        this.name = name;
        this.role = role;
        this.email = email;
        this.password = password;
        this.id=id;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", id='" + id + '\''+
                '}';
    }
}
