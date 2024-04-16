package com.webapp.coursefeedbacksystem.repository;

import com.webapp.coursefeedbacksystem.dto.Token;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepository extends MongoRepository<Token, String> { // Assuming the ID type is String

    // MongoDB doesn't directly support joins like relational databases,
    // but you can use the @Query annotation to achieve similar results if
    // necessary.
    // The following method assumes you're storing the userId as a field in your
    // Token documents.
    List<Token> findAllByUserIdAndLoggedOutFalse(String userId);

    Optional<Token> findByToken(String token);
}
