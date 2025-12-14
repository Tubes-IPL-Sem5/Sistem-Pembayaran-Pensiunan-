package com.mycompany.pensiunan.model;

public class User {
    private int idAkun;
    private String username;
    private String peran;
    private boolean isAuthenticated;

    public User(int idAkun, String username, String peran) {
        this.idAkun = idAkun;
        this.username = username;
        this.peran = peran;
        this.isAuthenticated = true;
    }

    public User() {
        this.isAuthenticated = false;
    }
    
    public int getIdAkun() { return idAkun; }
    public String getUsername() { return username; }
    public String getPeran() { return peran; }
    public boolean isAuthenticated() { return isAuthenticated; }
}