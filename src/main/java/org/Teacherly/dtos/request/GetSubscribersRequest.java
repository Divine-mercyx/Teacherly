package org.Teacherly.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GetSubscribersRequest {
    @NotNull(message = "token should not be null")
    String token;
    @NotNull(message = "id should not be null")
    String id;
}
