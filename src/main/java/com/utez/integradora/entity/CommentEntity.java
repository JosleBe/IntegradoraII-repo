package com.utez.integradora.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "comment")
public class CommentEntity {
    @Id
    private String id;
    private String autor;
    private String texto;
    private String imagen;
    private LocalDateTime fecha;
    @ManyToOne
    @JoinColumn(name= "blog_id", nullable = false)
    private CampaignEntity blogEntity;
}
