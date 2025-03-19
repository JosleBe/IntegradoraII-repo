package com.utez.integradora.repository;

import com.utez.integradora.entity.Contacto;
import com.utez.integradora.entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ContactoRepository extends MongoRepository<Contacto, String> {
    List<Contacto> findByReceptor(UserEntity receptor);
    boolean existsByReceptorAndEmisor(UserEntity receptor, UserEntity emisor);
}
