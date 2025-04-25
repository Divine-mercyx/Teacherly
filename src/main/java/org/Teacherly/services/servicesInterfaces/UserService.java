package org.Teacherly.services.servicesInterfaces;

import jakarta.validation.Valid;
import org.Teacherly.dtos.request.ProfileUpdateRequest;
import org.Teacherly.dtos.request.VideoPostRequest;
import org.Teacherly.dtos.response.UserResponse;
import org.Teacherly.dtos.response.VideoResponse;

public interface UserService {
    UserResponse updateProfile(@Valid ProfileUpdateRequest request);
    VideoResponse PostVideo(@Valid VideoPostRequest request);
}
