package com.utez.integradora.config;

import com.utez.integradora.entity.UserEntity;
import com.utez.integradora.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor

public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        UserEntity user = new UserEntity();
        user.setEmail("testweb@example.com");
        user.setPassword(passwordEncoder.encode("testweb"));
        user.setRole("ADMIN");
        user.setName("Jose Leonardo");
        user.setSexo("M");
        user.setPhone("7775012448");
        user.setLastName("Martinez");
        user.setDireccion("Cuernavca, Morelos");
        if(userRepository.existsByEmail(user.getEmail()) == false) {
            userRepository.save(user);
        }
        UserEntity user2 = new UserEntity();
        user2.setEmail("joslebe321@gmail.com");
        user2.setPassword(passwordEncoder.encode("Dende321"));
        user2.setRole("ADMIN");
        user2.setName("Josle");
        user2.setSexo("M");
        user2.setPhone("7775012448");
        user2.setLastName("Martinez");
        user2.setDireccion("Cuernavca, Morelos");
        if(userRepository.existsByEmail(user2.getEmail()) == false) {
            userRepository.save(user2);
        }
        UserEntity user4 = new UserEntity();
        user4.setEmail("jossma@gmail.com");
        user4.setPassword(passwordEncoder.encode("josleBe"));
        user4.setRole("ADMIN");
        user4.setName("Josle");
        user4.setSexo("M");
        user4.setPhone("7775012448");
        user4.setLastName("Martinez");
        user4.setDireccion("Cuernavca, Morelos");
        if(userRepository.existsByEmail(user4.getEmail()) == false) {
            userRepository.save(user4);
        }

    }
}
