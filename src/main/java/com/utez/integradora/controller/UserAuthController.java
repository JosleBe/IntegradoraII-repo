package com.utez.integradora.controller;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.utez.integradora.entity.dto.ReqRes;
import com.utez.integradora.repository.UserRepository;
import com.utez.integradora.service.JwtUtils;
import com.utez.integradora.service.UsersManagementService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserAuthController {
    private final UserRepository userRepository;

    static final Logger logger = LoggerFactory.getLogger(UserAuthController.class);
    private final JwtUtils jwtUtils;
    private final UsersManagementService usersManagementService;


    @PostConstruct
    public void initFirebase() {
        try {
            FileInputStream serviceAccount = new FileInputStream("src/main/resources/firebase-service-account.json");
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (Exception e) {
            logger.error("Error inicializando Firebase: {}", e.getMessage());
        }
    }
    @PostMapping("/register")
    public ResponseEntity<ReqRes> register(@RequestBody ReqRes reqRes ) {
        return ResponseEntity.ok(usersManagementService.register(reqRes));
    }

    @PostMapping("/login")
    public ResponseEntity<ReqRes> login(@RequestBody ReqRes reqRes ) {
        return ResponseEntity.ok(usersManagementService.login(reqRes));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ReqRes> refresh(@RequestBody ReqRes reqRes ) {
        return ResponseEntity.ok(usersManagementService.refreshToken(reqRes));
    }
    // 🔹 Nuevo endpoint para generar Firebase Custom Token
    @PostMapping("/firebase-token")
    public ResponseEntity<Map<String, String>> getFirebaseToken(@RequestBody Map<String, String> request) {
        logger.info("HOKA");
        try {
            String jwtToken = request.get("jwt");
            String password = request.get("password");
            // 🔸 Validar el JWT y obtener el ID del usuario
            String userId = jwtUtils.validateJwt(jwtToken);
            if (userId == null) {
                return ResponseEntity.badRequest().body(Map.of("error", "JWT inválido"));
            }

            // 🔸 Crear usuario en Firebase si no existe
            UserRecord user;
            try {   
                user = FirebaseAuth.getInstance().getUser(userId);
            } catch (Exception e) {
                user = FirebaseAuth.getInstance().createUser(
                        new UserRecord.CreateRequest()
                                .setUid(userId)
                                .setEmail(userId)
                                .setDisplayName(userId)
                                .setPassword(password)
                );
            }

            // 🔸 Generar un Firebase Custom Token
            String firebaseToken = FirebaseAuth.getInstance().createCustomToken(userId);

            return ResponseEntity.ok(Map.of("firebaseToken", firebaseToken));
        } catch (Exception e) {
            logger.error("Error generando Firebase Token: {}", e.getMessage());
            return ResponseEntity.internalServerError().body(Map.of("error", "Error interno"));
        }
    }


}
