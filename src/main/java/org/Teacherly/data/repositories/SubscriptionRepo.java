package org.Teacherly.data.repositories;

import org.Teacherly.data.models.Subscription;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepo extends MongoRepository<Subscription, String> {
    List<Subscription> findAllBySubscribedToUserId(String subscribedToId);
}
