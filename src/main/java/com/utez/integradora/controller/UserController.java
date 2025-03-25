package com.utez.integradora.controller;


import com.utez.integradora.entity.Contacto;
import com.utez.integradora.entity.UserEntity;
import com.utez.integradora.entity.dto.ContactDto;
import com.utez.integradora.entity.dto.MessageRequest;
import com.utez.integradora.entity.dto.ReqRes;
import com.utez.integradora.repository.ContactoRepository;
import com.utez.integradora.repository.UserRepository;
import com.utez.integradora.service.UsersManagementService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final UsersManagementService usersManagementService;
    private final ContactoRepository contactoRepository;
    private final UserRepository usuarioRepository;

    @GetMapping("/admin/get-all-users")
    public ResponseEntity<ReqRes> getAllUsers() {
        return ResponseEntity.ok(usersManagementService.getAllUsers());
    }
    @GetMapping("/admin/get-users/{id}")
    public ResponseEntity<ReqRes> getUser(@PathVariable String id) {
        return ResponseEntity.ok(usersManagementService.getUserById(id));
    }

    @PutMapping("/admin/update/{id}")
    public ResponseEntity<ReqRes> updateUser(@PathVariable String id, @RequestBody ReqRes reqRes ) {
        return ResponseEntity.ok(usersManagementService.updateUser(id, reqRes));
    }

    @GetMapping("/adminuser/get-profile")
    public ResponseEntity<ReqRes> getProfile() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        ReqRes reqRes = usersManagementService.getMyInfo(email);
        return ResponseEntity.status(reqRes.getStatusCode()).body(reqRes);
    }
    @PostMapping("/send")
    public ResponseEntity<?> sendMessage(@RequestBody MessageRequest messageRequest) {
        log.info("Sending message");

        Optional<UserEntity> emisor = usuarioRepository.findByEmail(messageRequest.getEmisorEmail());
        Optional<UserEntity> receptor = usuarioRepository.findByEmail(messageRequest.getReceptorEmail());

        if (emisor.isEmpty() || receptor.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }

        UserEntity emisorEntity = emisor.get();
        UserEntity receptorEntity = receptor.get();
        if (!contactoRepository.existsByReceptorAndEmisor(receptorEntity, emisorEntity)) {

            Contacto contacto = new Contacto();
            contacto.setReceptor(receptorEntity);
            contacto.setEmisor(emisorEntity);
            contactoRepository.save(contacto);
        }


        return ResponseEntity.ok("Mensaje enviado y contacto guardado");
    }

    @GetMapping("/{email}/contacts")
    public ResponseEntity<List<ContactDto>> getUserContacts(@PathVariable String email) {
        log.info("Get contactos");
        // Buscar el usuario por su email
        Optional<UserEntity> usuario = usuarioRepository.findByEmail(email);

        if (usuario.isEmpty()) {
            return ResponseEntity.badRequest().body(null);
        }

        List<Contacto> contactos = contactoRepository.findByReceptor(usuario.orElse(null));
        List<ContactDto> contacts = new ArrayList<>();

        for (Contacto contacto : contactos) {
            ContactDto contactDTO = new ContactDto();
            contactDTO.setEmail(contacto.getEmisor().getEmail());
            contactDTO.setNombre(contacto.getEmisor().getName());
            contacts.add(contactDTO);
        }

        return ResponseEntity.ok(contacts);
    }
    @PostMapping("/change-password")
    public ResponseEntity<String> changePassword(@RequestBody ReqRes reqRes) {
        Optional<UserEntity> optionalUserEntity = usersManagementService.findByEmail(reqRes.getEmail());

        if (!optionalUserEntity.isPresent()) {
            return ResponseEntity.status(404).body("Usuario no encontrado");
        }

        UserEntity userEntity = optionalUserEntity.get();

        if (!usersManagementService.checkPassword(userEntity, reqRes.getPassword())) {
            return ResponseEntity.status(403).body("Contraseña actual incorrecta");
        }

        usersManagementService.changePassword(userEntity, reqRes.getNewPassword());

        return ResponseEntity.ok("Contraseña cambiada exitosamente");
    }
    @GetMapping("/adminuser/all-admins")
    public ResponseEntity<List<ReqRes>> getAllAdmins() {
        log.info("Obteniendo todos los administradores");

        List<UserEntity> admins = usuarioRepository.findAllByRole("ADMIN");

        List<ReqRes> adminDtos = admins.stream()
                .map(admin -> new ReqRes(admin.getName(), admin.getEmail()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(adminDtos);
    }
    @PostMapping("/create-account")
    public ResponseEntity<ReqRes> register(@RequestBody ReqRes reqRes ) {
        return ResponseEntity.ok(usersManagementService.register(reqRes));
    }
}
