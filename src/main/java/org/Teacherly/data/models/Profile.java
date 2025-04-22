package org.Teacherly.data.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {
    @Id
    private String id;
    private String firstName;
    private String lastName;
    private String age;
    private String bio;
    private Role role;
    private List<String> subscribersUserIds;
    private List<String> subscribedToUserIds;
    private String imageUrl;
}
