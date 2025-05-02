package org.Teacherly.data.repositories;

import org.Teacherly.data.models.User;
import org.Teacherly.data.models.Video;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VideoRepo extends MongoRepository<Video, String> {
    Video findByTitle(String title);
    boolean existsByTitle(String title);
    List<Video> findByUser(User user);
}
