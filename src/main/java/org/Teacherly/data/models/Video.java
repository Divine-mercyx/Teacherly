package org.Teacherly.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
@Data
@AllArgsConstructor
public class Video {
    @Id
    private String id;
    private String title;
    private String description;
    private String url;
    @DBRef
    private User user;
    private LocalDateTime createdAt;

    public Video() {
        this.createdAt = LocalDateTime.now();
    }
}
