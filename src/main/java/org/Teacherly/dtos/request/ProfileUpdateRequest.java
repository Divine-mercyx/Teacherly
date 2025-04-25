package org.Teacherly.dtos.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.Teacherly.data.models.Profile;

@Data
public class ProfileUpdateRequest {
    @NotNull(message = "token should not be null")
    String token;
    @NotNull(message = "id should not be null")
    String id;
    @NotNull(message = "profile should not be null")
    Profile profile;
}
