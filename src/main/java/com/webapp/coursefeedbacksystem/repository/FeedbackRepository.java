package com.webapp.coursefeedbacksystem.repository;

import com.webapp.coursefeedbacksystem.model.FeedbackModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeedbackRepository extends MongoRepository<FeedbackModel, String> {

}
