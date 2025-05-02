package org.Teacherly.services.servicesImpl;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import org.Teacherly.data.models.Profile;
import org.Teacherly.data.models.Subscription;
import org.Teacherly.data.models.User;
import org.Teacherly.data.models.Video;
import org.Teacherly.data.repositories.ProfileRepo;
import org.Teacherly.data.repositories.SubscriptionRepo;
import org.Teacherly.data.repositories.UserRepo;
import org.Teacherly.data.repositories.VideoRepo;
import org.Teacherly.dtos.request.*;
import org.Teacherly.dtos.response.SubscriberResponse;
import org.Teacherly.dtos.response.UserResponse;
import org.Teacherly.dtos.response.VideoResponse;
import org.Teacherly.services.servicesInterfaces.UserService;
import org.Teacherly.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
public class UserServiceImpl implements UserService {

    @Autowired
    private ProfileRepo profileRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private SubscriptionServiceImpl subscriptionService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private VideoRepo videoRepo;

    @Autowired
    private SubscriptionRepo subscriptionRepo;

    @Autowired
    private Validator validator;

    @Autowired
    private EmailServiceImpl emailService;
    @Autowired
    private SubscriptionServiceImpl subscriptionServiceImpl;


    @Override
    public UserResponse updateProfile(@Valid ProfileUpdateRequest request) {
        Set<ConstraintViolation<ProfileUpdateRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) throw new ConstraintViolationException(violations);
        validateTokenAndId(request.getToken(), request.getId());
        User user = userRepo.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user"));
        Profile profile = profileRepo.save(request.getProfile());
        user.setProfile(profile);
        User savedUser = userRepo.save(user);
        String token = jwtUtils.generateToken(user.getId());
        return mapUserToUserResponse(token, savedUser);
    }

    private void validateTokenAndId(String request, String request1) {
        if (!jwtUtils.validateToken(request, request1)) throw new IllegalArgumentException("Invalid token");
    }

    @Override
    public VideoResponse PostVideo(@Valid VideoPostRequest request) {
        Set<ConstraintViolation<VideoPostRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) throw new ConstraintViolationException(violations);
        validateTokenAndId(request.getToken(), request.getId());
        User user = userRepo.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("user not found"));
        Video video = request.getVideo();
        video.setUser(user);
        Video savedVideo = videoRepo.save(video);
        messageSubscribersOnPost(user);
        return getVideoResponse(savedVideo);
    }

    private void messageSubscribersOnPost(User user) {
        List<SubscriberResponse> subscriptions = subscriptionServiceImpl.getSubscribers(user.getId());
        subscriptions
                .forEach(subscriberResponse -> sendUpdateToSubscribers(subscriberResponse, user));
    }

    private void sendUpdateToSubscribers(SubscriberResponse subscriber, User user) {
        emailService.sendSimpleMail(subscriber.getEmail(), "teacherly update", String.format("%s posted a video check out", user.getProfile().getFirstName()));
    }

    @Override
    public List<VideoResponse> getAllVideos(@Valid GetAllVideosRequest request) {
        Set<ConstraintViolation<GetAllVideosRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) throw new ConstraintViolationException(violations);
        validateTokenAndId(request.getToken(), request.getId());
        List<Video> videos = videoRepo.findAll();
        return getVideoResponses(videos);
    }

    private List<VideoResponse> getVideoResponses(List<Video> videos) {
        return videos.stream()
                .map(this::mapVideoToVideoResponse)
                .collect(Collectors.toList());
    }


    @Override
    public List<VideoResponse> getAllVideoPostedByUser(@Valid GetAllVideosRequest request) {
        Set<ConstraintViolation<GetAllVideosRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) throw new ConstraintViolationException(violations);
        validateTokenAndId(request.getToken(), request.getId());
        User user = userRepo.findById(request.getId())
                .orElseThrow(() -> new IllegalArgumentException("user not found"));
        List<Video> videos = videoRepo.findByUser(user);
        return getVideoResponses(videos);
    }


    @Override
    public Subscription subscribe(@Valid SubscribeRequest request) {
        Set<ConstraintViolation<SubscribeRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) throw new ConstraintViolationException(violations);
        validateTokenAndId(request.getToken(), request.getId());
        return subscriptionService.subscribe(request.getSubscription());
    }

    @Override
    public void unSubscribe(@Valid SubscribeRequest request) {
        Set<ConstraintViolation<SubscribeRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) throw new ConstraintViolationException(violations);
        validateTokenAndId(request.getToken(), request.getId());
        subscriptionService.unsubscribe(request.getSubscription());
    }

    @Override
    public List<SubscriberResponse> getSubscribers(@Valid GetSubscribersRequest request) {
        Set<ConstraintViolation<GetSubscribersRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) throw new ConstraintViolationException(violations);
        validateTokenAndId(request.getToken(), request.getId());
        return subscriptionService.getSubscribers(request.getId());
    }

    @Override
    public List<SubscriberResponse> getSubscribedToUser(@Valid GetSubscribersRequest request) {
        Set<ConstraintViolation<GetSubscribersRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) throw new ConstraintViolationException(violations);
        validateTokenAndId(request.getToken(), request.getId());
        return subscriptionService.getSubscribedToUser(request.getId());
    }

    private VideoResponse mapVideoToVideoResponse(Video video) {
        return getVideoResponse(video);
    }

    private VideoResponse getVideoResponse(Video savedVideo) {
        return VideoResponse.builder()
                .id(savedVideo.getId())
                .title(savedVideo.getTitle())
                .description(savedVideo.getDescription())
                .url(savedVideo.getUrl())
                .ownerFirstName(savedVideo.getUser().getProfile().getFirstName())
                .ownerLastName(savedVideo.getUser().getProfile().getLastName())
                .ownerId(savedVideo.getUser().getId())
                .createdAt(savedVideo.getCreatedAt())
                .build();
    }

    private static UserResponse mapUserToUserResponse(String token, User savedUser) {
        return new UserResponse(token, savedUser.getId(), savedUser.getEmail(), savedUser.getProfile());
    }


    public void deleteAllVideos() {
        videoRepo.deleteAll();
    }

    public void deleteAllProfile() {
        profileRepo.deleteAll();
    }

    public User findById(String id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("user not found"));
    }
}
