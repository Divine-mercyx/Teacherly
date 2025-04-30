package org.Teacherly.services.servicesInterfaces;

import org.Teacherly.data.models.Subscription;
import org.Teacherly.dtos.response.SubscriberResponse;

import java.util.List;

public interface SubscriptionService {
    Subscription subscribe(Subscription subscription);
    String unsubscribe(Subscription subscription);
    List<SubscriberResponse> getSubscribers(String userId);
    List<SubscriberResponse> getSubscribedToUser(String userId);
}
