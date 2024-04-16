package com.webapp.coursefeedbacksystem.repository;

import com.webapp.coursefeedbacksystem.model.URLModel;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface URLRepository extends MongoRepository<URLModel, String> {

    public URLModel findByUrl(String url);
}
