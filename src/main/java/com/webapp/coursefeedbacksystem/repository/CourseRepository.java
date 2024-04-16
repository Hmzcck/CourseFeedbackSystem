package com.webapp.coursefeedbacksystem.repository;

import com.webapp.coursefeedbacksystem.model.CourseModel;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CourseRepository extends MongoRepository<CourseModel, String> {
    Optional<CourseModel> findByName(String name);
}
