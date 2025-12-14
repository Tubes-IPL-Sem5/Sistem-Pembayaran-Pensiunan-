package com.mycompany.pensiunan.service;

import com.mycompany.pensiunan.dao.UserDao;
import com.mycompany.pensiunan.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // WAJIB ada dependency BCrypt di pom.xml

public class AuthService {
    
    private final UserDao userDAO = new UserDao();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(); // BCrypt Encoder

    /**
     * Memvalidasi kredensial pengguna dan mengembalikan objek User jika berhasil.
     * @param username Username input.
     * @param rawPassword Password plain text input.
     * @return User object yang terotentikasi, atau User object gagal.
     */
    public User authenticate(String username, String rawPassword) {
        
        
        String storedHash = userDAO.getPasswordHash(username);

        if (storedHash != null) {
            if (passwordEncoder.matches(rawPassword, storedHash)) {
                return userDAO.findAccountByUsername(username); 
                
            }
        }
        
        return new User();
    }
    
    /**
     * Method untuk mendapatkan hash password (digunakan untuk Registrasi/Update)
     */
    public String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}