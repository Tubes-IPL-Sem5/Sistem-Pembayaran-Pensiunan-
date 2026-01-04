package com.mycompany.pensiunan.model;

public class User {

    private int idAkun;
    private int idHrd;
    private int idPensiunan;
    private String username;
    private String peran;
    private String nama;
    private boolean authenticated;
    private int idKeuangan;

    public User(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public User(int idAkun, String username, String peran,
                int idPensiunan, String nama, boolean authenticated) {
        this.idAkun = idAkun;
        this.username = username;
        this.peran = peran;
        this.idPensiunan = idPensiunan;
        this.nama = nama;
        this.authenticated = authenticated;
    }
    
        public User(int idAkun,
                String username,
                String passwordHash,
                String peran,
                int idPensiunan,
                String nama,
                boolean authenticated) {

        this.idAkun = idAkun;
        this.username = username;
        this.passwordHash = passwordHash;
        this.peran = peran;
        this.idPensiunan = idPensiunan;
        this.nama = nama;
        this.authenticated = authenticated;
    }
    
    private String passwordHash;

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public int getIdKeuangan() { return idKeuangan; }
    public void setIdKeuangan(int idKeuangan) { this.idKeuangan = idKeuangan; }

    
    public int getIdAkun() {
        return idAkun;
    }
    
    public int getIdHrd() {
        return idHrd;
    }

    public void setIdHrd(int idHrd) {
        this.idHrd = idHrd;
    }
    
    public void setIdPensiunan(int idPensiunan) {
        this.idPensiunan = idPensiunan;
    }

    public int getIdPensiunan() {
        return idPensiunan;
    }

    public String getUsername() {
        return username;
    }

    public String getPeran() {
        return peran;
    }

    public String getNama() {
        return nama;
    }
}
