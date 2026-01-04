package com.mycompany.pensiunan.service;

import com.mycompany.pensiunan.config.Koneksi;
import com.mycompany.pensiunan.dao.UserDao;
import com.mycompany.pensiunan.model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // WAJIB ada dependency BCrypt di pom.xml

public class AuthService {
    
private final UserDao userDAO = new UserDao();
private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

public User authenticate(String username, String rawPassword) {

    User user = userDAO.findAccountByUsername(username);

    if (user == null) return new User(false);

    if (!passwordEncoder.matches(rawPassword, user.getPasswordHash()))
        return new User(false);

    user.setAuthenticated(true);

    switch (user.getPeran()) {
        case "HRD":
            user.setIdHrd(userDAO.findIdHrdByIdAkun(user.getIdAkun()));
            break;
        case "PENSIUNAN":
            user.setIdPensiunan(
                userDAO.findIdPensiunanByIdAkun(user.getIdAkun())
            );
            break;
        case "KEUANGAN":
            user.setIdKeuangan(
                userDAO.findIdKeuanganByIdAkun(user.getIdAkun())
            );
            break;
    }

    return user;
}


    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean checkUsernameExists(String username) {
        String sql = "SELECT 1 FROM akun_pengguna WHERE username = ?";

        try (Connection c = Koneksi.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            return false;
        }
    }

    
    
}