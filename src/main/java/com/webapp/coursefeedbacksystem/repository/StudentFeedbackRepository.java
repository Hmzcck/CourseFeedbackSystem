package com.webapp.coursefeedbacksystem.repository;

import com.webapp.coursefeedbacksystem.model.StudentFeedbackModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface StudentFeedbackRepository extends MongoRepository<StudentFeedbackModel, String> {

}
