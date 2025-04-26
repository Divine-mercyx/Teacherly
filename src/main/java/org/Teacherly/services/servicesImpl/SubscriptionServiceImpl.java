package org.Teacherly.services.servicesImpl;

import org.Teacherly.data.models.Subscription;
import org.Teacherly.data.models.User;
import org.Teacherly.data.repositories.SubscriptionRepo;
import org.Teacherly.data.repositories.UserRepo;
import org.Teacherly.dtos.response.SubscriberResponse;
import org.Teacherly.services.servicesInterfaces.SubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    @Autowired
    private SubscriptionRepo subscriptionRepo;

    @Autowired
    private UserRepo userRepo;

    @Override
    public Subscription subscribe(Subscription subscription) {
        User user = userRepo.findById(subscription.getSubscribedToUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.getProfile().getSubscribersUserIds().add(subscription.getSubscriberUserId());
        userRepo.save(user);
        return subscriptionRepo.save(subscription);
    }

    @Override
    public void unsubscribe(Subscription subscription) {
        User user = userRepo.findById(subscription.getSubscribedToUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.getProfile().getSubscribersUserIds().remove(subscription.getSubscriberUserId());
        Subscription subscription1 = subscriptionRepo
                .findBySubscribedToUserIdAndSubscriberUserId(subscription.getSubscribedToUserId(), subscription.getSubscriberUserId());
        subscriptionRepo.delete(subscription1);
    }


    @Override
    public List<SubscriberResponse> getSubscribers(String userId) {
        User user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
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

}
