package com.utez.integradora.service;
import com.utez.integradora.entity.UserEntity;
import com.utez.integradora.entity.dto.FacebookUserResponse;
import com.utez.integradora.entity.dto.ReqRes;
import com.utez.integradora.repository.UserRepository;
import com.utez.integradora.service.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FacebookAuthService {

    @Value("${facebook.app-id}")
    private String appId;

    @Value("${facebook.app-secret}")
    private String appSecret;

    @Value("${facebook.graph-url}")
    private String graphUrl;

    private final UserRepository userService; // Tu servicio para manejar usuarios
    private final JwtUtils jwtUtils; // Para generar el token JWT

    public ReqRes authenticateFacebookUser(String accessToken) {
        ReqRes res = new ReqRes();
        RestTemplate restTemplate = new RestTemplate();

        try {
            // Realizar la solicitud a la API de Facebook para obtener datos del usuario
            String facebookRequestUrl = graphUrl + accessToken;
            FacebookUserResponse facebookUser = restTemplate.getForObject(facebookRequestUrl, FacebookUserResponse.class);

            if (facebookUser == null || facebookUser.getEmail() == null) {
                res.setStatusCode(400);
                res.setMessage("Invalid Facebook token or missing email.");
                return res;
            }

            // Buscar si el usuario ya existe en la base de datos
            Optional<UserEntity> userOptional = userService.findByEmail(facebookUser.getEmail());
            UserEntity user;

            if (userOptional.isEmpty()) {
                // Registrar al usuario si no existe
                user = new UserEntity();
                user.setEmail(facebookUser.getEmail());
                user.setName(facebookUser.getFirstName());
                user.setLastName(facebookUser.getLastName());
                user = userService.save(user);

                res.setMessage("User successfully registered!");
            } else {
                // Si el usuario ya existe
                user = userOptional.get();
                res.setMessage("User logged in successfully.");
            }

            // Generar el token JWT para el usuario
            String jwtToken = jwtUtils.generateToken(user);
            res.setToken(jwtToken);
            res.setStatusCode(200);
            res.setUser(user);

        } catch (Exception e) {
            res.setStatusCode(500);
            res.setMessage("Error during Facebook authentication: " + e.getMessage());
        }
        return res;
    }
}
