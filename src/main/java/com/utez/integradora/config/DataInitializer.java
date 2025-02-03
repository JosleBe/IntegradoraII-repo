package com.utez.integradora.config;

import com.utez.integradora.entity.UserEntity;
import com.utez.integradora.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor

public class DataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        UserEntity user = new UserEntity();
        user.setEmail("test@example.com");
        user.setPassword(passwordEncoder.encode("test"));
        user.setRole("ADMIN");
        user.setName("Leonardo");
        userRepository.save(user);
    }
}
