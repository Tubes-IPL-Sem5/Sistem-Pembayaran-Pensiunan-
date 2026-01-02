/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.pensiunan.util;

public final class Session {

    private static int idAkun;
    private static int idPensiunan;
    private static String username;
    private static String role;
    private static String nama;

    private Session() {
        // cegah instansiasi
    }

    public static void setSession(
            int idAkun,
            String username,
            String role,
            int idPensiunan,
            String nama
    ) {
        Session.idAkun = idAkun;
        Session.username = username;
        Session.role = role;
        Session.idPensiunan = idPensiunan;
        Session.nama = nama;
    }

    public static int getIdAkun() {
        return idAkun;
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

    public static void clear() {
        idAkun = 0;
        idPensiunan = 0;
        username = null;
        role = null;
        nama = null;
    }
}
