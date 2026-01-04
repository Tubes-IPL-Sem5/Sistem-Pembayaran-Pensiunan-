/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pensiunan.util;

public final class Session {

    private static int idAkun;
    private static int idHrd;
    private static int idPensiunan;
    private static String username;
    private static String role;
    private static String nama;
    private static int idKeuangan;

    private Session() {
        
    }

    public static void setSession(
            int idAkun,
            int idHrd,
            int idKeuangan,
            String username,
            String role,
            int idPensiunan,
            String nama
    ) {
        Session.idAkun = idAkun;
        Session.idHrd = idHrd;
        Session.idKeuangan = idKeuangan;
        Session.username = username;
        Session.role = role;
        Session.idPensiunan = idPensiunan;
        Session.nama = nama;
    }

    public static int getIdAkun() {
        return idAkun;
    }
    
    public static int getIdHrd() {
        return idHrd;
    }

    public static int getIdPensiunan() {
        return idPensiunan;
    }

    public static String getUsername() {
        return username;
    }

    public static String getRole() {
        return role;
    }

    public static String getNama() {
        return nama;
    }
    
    public static int getIdKeuangan() { return idKeuangan; }
    public static void setIdKeuangan(int id) { idKeuangan = id; }


    public static void clear() {
        idAkun = 0;
        idPensiunan = 0;
        username = null;
        role = null;
        nama = null;
    }
}
