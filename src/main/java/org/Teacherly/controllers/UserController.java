package org.Teacherly.controllers;

import jakarta.validation.Valid;
import org.Teacherly.dtos.request.GetAllVideosRequest;
import org.Teacherly.dtos.request.ProfileUpdateRequest;
import org.Teacherly.dtos.request.VideoPostRequest;
import org.Teacherly.dtos.response.UserResponse;
import org.Teacherly.dtos.response.VideoResponse;
import org.Teacherly.services.servicesInterfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authenticated")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/update")
    public ResponseEntity<UserResponse> updateProfile(@RequestBody @Valid ProfileUpdateRequest request) {
        return ResponseEntity.ok(userService.updateProfile(request));
    }

    @PostMapping("/post")
    public ResponseEntity<VideoResponse> post(@RequestBody @Valid VideoPostRequest request) {
        return ResponseEntity.ok(userService.PostVideo(request));
    }

    @GetMapping("/videos")
    public ResponseEntity<List<VideoResponse>> getVideos(@RequestBody @Valid GetAllVideosRequest request) {
        return ResponseEntity.ok(userService.getAllVideos(request));
    }
}
