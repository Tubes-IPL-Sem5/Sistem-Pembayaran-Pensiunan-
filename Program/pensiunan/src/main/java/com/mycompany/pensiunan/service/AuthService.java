package com.mycompany.pensiunan.service;

import com.mycompany.pensiunan.dao.UserDao;
import com.mycompany.pensiunan.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // WAJIB ada dependency BCrypt di pom.xml

public class AuthService {
    
private final UserDao userDAO = new UserDao();
private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User authenticate(String username, String rawPassword) {

        // ambil data akun + relasi role
        User user = userDAO.findAccountByUsername(username);

        // username tidak ditemukan
        if (user == null) {
            return new User(false);
        }

        // verifikasi password
        if (!passwordEncoder.matches(rawPassword, user.getPasswordHash())) {
            return new User(false);
        }

        // autentikasi valid
        user.setAuthenticated(true);
        return user;
    }

    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    
    
    
}