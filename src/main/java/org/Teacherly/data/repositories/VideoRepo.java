package org.Teacherly.data.repositories;

import org.Teacherly.data.models.Video;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoRepo extends MongoRepository<Video, String> {
    Video findByTitle(String title);
    boolean existsByTitle(String title);
}
