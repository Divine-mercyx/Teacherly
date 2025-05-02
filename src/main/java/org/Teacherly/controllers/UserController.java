package org.Teacherly.controllers;

import jakarta.validation.Valid;
import org.Teacherly.data.models.Subscription;
import org.Teacherly.dtos.request.*;
import org.Teacherly.dtos.response.SubscriberResponse;
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

    @GetMapping("/myVideos")
    public ResponseEntity<List<VideoResponse>> getMyVideos(@RequestBody @Valid GetAllVideosRequest request) {
        return ResponseEntity.ok(userService.getAllVideoPostedByUser(request));
    }

    @PostMapping("/subscribe")
    public ResponseEntity<Subscription> subscribe(@RequestBody @Valid SubscribeRequest request) {
        return ResponseEntity.ok(userService.subscribe(request));
    }

    @PostMapping("/unsubscribe")
    public ResponseEntity<String> unsubscribe(@RequestBody @Valid SubscribeRequest request) {
        userService.unSubscribe(request);
        return ResponseEntity.ok("unsubscribe successfully");
    }

    @GetMapping("/getSubscribers")
    public ResponseEntity<List<SubscriberResponse>> getSubscribers(@RequestBody @Valid GetSubscribersRequest request) {
        return ResponseEntity.ok(userService.getSubscribers(request));
    }

    @GetMapping("/subscribedTo")
    public ResponseEntity<List<SubscriberResponse>> getSubscribedTo(@RequestBody @Valid GetSubscribersRequest request) {
        return ResponseEntity.ok(userService.getSubscribedToUser(request));
    }
}
