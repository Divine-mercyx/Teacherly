package org.Teacherly.services.servicesInterfaces;

import jakarta.validation.Valid;
import org.Teacherly.data.models.Subscription;
import org.Teacherly.dtos.request.*;
import org.Teacherly.dtos.response.SubscriberResponse;
import org.Teacherly.dtos.response.UserResponse;
import org.Teacherly.dtos.response.VideoResponse;

import java.util.List;

public interface UserService {
    UserResponse updateProfile(@Valid ProfileUpdateRequest request);
    VideoResponse PostVideo(@Valid VideoPostRequest request);
    List<VideoResponse> getAllVideos(@Valid GetAllVideosRequest request);
    List<VideoResponse> getAllVideoPostedByUser(@Valid GetAllVideosRequest request);
    Subscription subscribe(@Valid SubscribeRequest request);
    void unSubscribe(@Valid SubscribeRequest request);
    List<SubscriberResponse> getSubscribers(@Valid GetSubscribersRequest request);
    List<SubscriberResponse> getSubscribedToUser(@Valid GetSubscribersRequest request);
}
