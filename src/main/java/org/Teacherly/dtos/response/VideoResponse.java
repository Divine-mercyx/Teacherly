package org.Teacherly.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;


@Builder
@Data
@AllArgsConstructor
public class VideoResponse {
    private String id;
    private String title;
    private String description;
    private String url;
    private String ownerFirstName;
    private String ownerLastName;
    private String ownerId;
    private LocalDateTime createdAt;
}
