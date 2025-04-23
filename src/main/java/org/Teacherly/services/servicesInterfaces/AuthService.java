package org.Teacherly.services.servicesInterfaces;

import jakarta.validation.Valid;
import org.Teacherly.data.models.User;
import org.Teacherly.dtos.request.ChangePasswordRequest;
import org.Teacherly.dtos.request.UserLoginRequest;
import org.Teacherly.dtos.response.UserResponse;

public interface AuthService {
    UserResponse register(@Valid User user);
    UserResponse login(@Valid UserLoginRequest request);
    String sendOtp(String email);
    void changePassword(ChangePasswordRequest request);
}
