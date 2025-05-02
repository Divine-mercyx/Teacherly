package org.Teacherly.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetAllVideosRequest {
    @NotNull(message = "token should not be null")
    String token;
    @NotNull(message = "id should not be null")
    String id;
}
