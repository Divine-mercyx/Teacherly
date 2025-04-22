package org.Teacherly.data.repositories;

import org.Teacherly.data.models.Otp;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OtpRepo extends MongoRepository<Otp, String> {
}
