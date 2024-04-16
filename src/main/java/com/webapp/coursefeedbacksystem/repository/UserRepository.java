package com.webapp.coursefeedbacksystem.repository;

import com.webapp.coursefeedbacksystem.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserModel, String> {
    // Example custom query to find a user by email
    Optional<UserModel> findByEmail(String email);
}
