package com.utez.integradora.controller;

import com.utez.integradora.entity.dto.ReqRes;
import com.utez.integradora.repository.UserRepository;
import com.utez.integradora.service.FacebookAuthService;
import com.utez.integradora.service.UsersManagementService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserAuthController {
    private final UserRepository userRepository;
    private final FacebookAuthService facebookAuthService;
    static final Logger logger = LoggerFactory.getLogger(UserAuthController.class);

    private final UsersManagementService usersManagementService;


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

    @PostMapping("/facebook")
    public ResponseEntity<ReqRes> facebookLogin(@RequestBody Map<String, String> requestBody) {
        String accessToken = requestBody.get("accessToken");

        if (accessToken == null || accessToken.isEmpty()) {
            return ResponseEntity.badRequest().body(new ReqRes(400, "Access token is required"));
        }

        ReqRes res = facebookAuthService.authenticateFacebookUser(accessToken);
        return ResponseEntity.status(res.getStatusCode()).body(res);
    }

}
