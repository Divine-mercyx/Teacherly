package org.Teacherly.dtos.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SubscriberResponse {
    String id;
    String firstName;
    String lastName;
    String email;
    String imageUrl;
}
