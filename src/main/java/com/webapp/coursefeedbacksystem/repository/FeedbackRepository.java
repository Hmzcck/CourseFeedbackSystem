package com.webapp.coursefeedbacksystem.repository;

import com.webapp.coursefeedbacksystem.model.FeedbackModel;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FeedbackRepository extends MongoRepository<FeedbackModel, String> {
    Optional<FeedbackModel> findById(String feedbackId);
}
