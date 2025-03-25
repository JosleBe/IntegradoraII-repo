package com.utez.integradora.repository;

import com.utez.integradora.entity.CommentEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CommentRepository extends MongoRepository<CommentEntity, String > {
    List<CommentEntity> findByBlogEntityId(String campaignId);
}
