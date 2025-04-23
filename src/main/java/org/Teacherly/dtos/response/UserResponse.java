package org.Teacherly.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.Teacherly.data.models.Profile;

@Data
@AllArgsConstructor
public class UserResponse {
    String token;
    String id;
    String email;
    Profile profile;
}
