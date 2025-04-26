package org.Teacherly.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.Teacherly.data.models.Subscription;

@Data
public class SubscribeRequest {
    @NotNull(message = "token should not be null")
    String token;
    @NotNull(message = "id should not be null")
    String id;
    @NotNull(message = "subscription should not be null")
    Subscription subscription;
}
