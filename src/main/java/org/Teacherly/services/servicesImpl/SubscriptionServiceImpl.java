package org.Teacherly.services.servicesImpl;

import lombok.extern.slf4j.Slf4j;
import org.Teacherly.data.models.Profile;
import org.Teacherly.data.models.Subscription;
import org.Teacherly.data.models.User;
import org.Teacherly.data.repositories.ProfileRepo;
import org.Teacherly.data.repositories.SubscriptionRepo;
import org.Teacherly.data.repositories.UserRepo;
import org.Teacherly.dtos.response.SubscriberResponse;
import org.Teacherly.services.servicesInterfaces.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepo subscriptionRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ProfileRepo profileRepo;

    @Override
    public Subscription subscribe(Subscription subscription) {
        User user = userRepo.findById(subscription.getSubscribedToUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.getProfile().getSubscribersUserIds() == null) user.getProfile().setSubscribersUserIds(new ArrayList<>());
        profileRepo.save(user.getProfile());
        Profile profile = profileRepo.findById(user.getProfile().getId()).orElseThrow(() -> new IllegalArgumentException("Profile not found"));
        addSubscribersToListAndSave(subscription, profile, user);
        return subscriptionRepo.save(subscription);
    }

    private void addSubscribersToListAndSave(Subscription subscription, Profile profile, User user) {
        profile.getSubscribersUserIds().add(subscription.getSubscriberUserId());
        Profile savedProfile = profileRepo.save(profile);
        user.setProfile(savedProfile);
    }

    @Override
    public String unsubscribe(Subscription subscription) {
        User user = userRepo.findById(subscription.getSubscribedToUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.getProfile().getSubscribersUserIds() == null) user.getProfile().setSubscribersUserIds(new ArrayList<>());
        profileRepo.save(user.getProfile());
        removeSubscriberFromListAndSave(subscription, user);
        userRepo.save(user);
        Subscription subscription1 = subscriptionRepo
                .findBySubscribedToUserIdAndSubscriberUserId(subscription.getSubscribedToUserId(), subscription.getSubscriberUserId());
        subscriptionRepo.delete(subscription1);
        return "deleted successfully";
    }

    private void removeSubscriberFromListAndSave(Subscription subscription, User user) {
        Profile profile = profileRepo.findById(user.getProfile().getId()).orElseThrow(() -> new IllegalArgumentException("Profile not found"));
        profile.getSubscribersUserIds().remove(subscription.getSubscriberUserId());
        Profile savedProfile = profileRepo.save(profile);
        user.setProfile(savedProfile);
    }


    @Override
    public List<SubscriberResponse> getSubscribers(String userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (user.getProfile().getSubscribersUserIds() == null) user.getProfile().setSubscribersUserIds(new ArrayList<>());
        List<String> subscribersUserIds = user.getProfile().getSubscribersUserIds();
        return subscribersUserIds.stream()
                .map(this::mapSubscriberToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubscriberResponse> getSubscribedToUser(String userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        List<String> subscribedToUserIds = user.getProfile().getSubscribedToUserIds();
        return subscribedToUserIds.stream()
                .map(this::mapSubscriberToResponse)
                .collect(Collectors.toList());
    }

    public SubscriberResponse mapSubscriberToResponse(String subscriberUserId) {
        return getSubscriberResponse(subscriberUserId);
    }

    public SubscriberResponse getSubscriberResponse(String subscriberUserId) {
        User user = userRepo.findById(subscriberUserId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return SubscriberResponse.builder()
                .id(user.getId())
                .firstName(user.getProfile().getFirstName())
                .lastName(user.getProfile().getLastName())
                .email(user.getEmail())
                .imageUrl(user.getProfile().getImageUrl())
                .build();
    }

    public void deleteAll() {
        subscriptionRepo.deleteAll();
    }

}
