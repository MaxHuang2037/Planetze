package com.example.b07demosummer2024;

public class User {

    private String uid;
    private String name;
    private String email;
    private boolean firstTime;

    private int[] totalEmissionsByCategory;

    public User() {}

    public User(String uid, String name, String email, boolean firstTime) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.firstTime = firstTime;
    }

    // Getters and setters
    public String getId() { return uid; }
    public void setId(String uid) { this.uid = uid; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public boolean getFirstTime() { return firstTime; }
    public void setFirstTime(boolean firstTime) { this.firstTime = firstTime; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
