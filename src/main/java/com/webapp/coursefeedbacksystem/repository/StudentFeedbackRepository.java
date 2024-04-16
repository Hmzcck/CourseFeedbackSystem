package com.webapp.coursefeedbacksystem.repository;

import com.webapp.coursefeedbacksystem.model.StudentFeedbackModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface StudentFeedbackRepository extends MongoRepository<StudentFeedbackModel, String> {

    public Optional<StudentFeedbackModel> findByFeedbackId(String feedbackId);
}
