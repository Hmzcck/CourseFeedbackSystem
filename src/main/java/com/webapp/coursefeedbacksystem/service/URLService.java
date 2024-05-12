package com.webapp.coursefeedbacksystem.service;

import com.webapp.coursefeedbacksystem.model.CourseModel;
import com.webapp.coursefeedbacksystem.model.URLModel;
import com.webapp.coursefeedbacksystem.model.UserModel;
import com.webapp.coursefeedbacksystem.model.FeedbackModel;
import com.webapp.coursefeedbacksystem.repository.URLRepository;
import org.springframework.stereotype.Service;

@Service
public class URLService {

    private final URLRepository repository;

    public URLService(URLRepository repository) {
        this.repository = repository;
    }

    public URLModel findByUrl(String url) {
        return repository.findByUrl(url);
    }

    public URLModel createURL(String url, CourseModel courseModel, UserModel userModel, FeedbackModel feedbackModel) {
        return repository.save(new URLModel(url, courseModel, userModel, feedbackModel));
    }

    public void deleteURL(URLModel url) {
        repository.delete(url);
    }

    public URLModel getURL(String URL) {
        return repository.findByUrl(URL);
    }
}
