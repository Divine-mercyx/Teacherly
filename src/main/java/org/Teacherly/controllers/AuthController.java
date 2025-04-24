package org.Teacherly.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.Teacherly.data.models.User;
import org.Teacherly.dtos.request.ChangePasswordRequest;
import org.Teacherly.dtos.request.UserLoginRequest;
import org.Teacherly.dtos.response.UserResponse;
import org.Teacherly.services.servicesImpl.AuthServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/auth")
@Tag(name = "Auth controller", description = "Handles user authentication, otp and password recovery")
@RestController
public class AuthController {

    @Autowired
    private AuthServiceImpl authService;

    @Operation(summary = "register a new user")
    @PostMapping("/register")
    public ResponseEntity<UserResponse> register(@RequestBody @Valid User user) {
        return ResponseEntity.ok(authService.register(user));
    }

    @Operation(summary = "login a user")
    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody @Valid UserLoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @Operation(summary = "send otp to user for changing password")
    @GetMapping("/send-otp")
    public ResponseEntity<String> sendOTP(@RequestParam("email") String email) {
        authService.sendOtp(email);
        return ResponseEntity.ok("OTP sent");
    }

    @Operation(summary = "change password")
    @PostMapping("/forgot-password/change-password")
    public ResponseEntity<String> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        authService.changePassword(request);
        return ResponseEntity.ok("Password changed");
    }
}
