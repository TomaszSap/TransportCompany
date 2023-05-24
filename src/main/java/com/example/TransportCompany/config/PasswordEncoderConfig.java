package com.example.TransportCompany.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
public class PasswordEncoderConfig {
    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    public static String encode(String password) {
        //String salt = BCrypt.gensalt(SALT_LENGTH); // generowanie soli
        return PASSWORD_ENCODER.encode(password); //+ salt); // haszowanie hasła z solą
    }

    public static boolean matches(String password, String encodedPassword) {
        return PASSWORD_ENCODER.matches(password, encodedPassword);
    }
}
