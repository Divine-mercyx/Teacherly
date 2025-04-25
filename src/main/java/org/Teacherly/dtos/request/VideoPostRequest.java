package org.Teacherly.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.Teacherly.data.models.Video;

@Data
public class VideoPostRequest {
    @NotNull(message = "token should not be null")
    String token;
    @NotNull(message = "id should not be null")
    String id;
    @NotNull(message = "video should not be null")
    Video video;
}
