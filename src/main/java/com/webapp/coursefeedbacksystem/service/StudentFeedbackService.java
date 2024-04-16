package com.webapp.coursefeedbacksystem.service;

import com.webapp.coursefeedbacksystem.model.StudentFeedbackModel;
import com.webapp.coursefeedbacksystem.repository.StudentFeedbackRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentFeedbackService {

    private final StudentFeedbackRepository repository;

    public StudentFeedbackService(StudentFeedbackRepository repository) {
        this.repository = repository;
    }

    public StudentFeedbackModel save(StudentFeedbackModel feedback) {
        return repository.save(feedback);
    }

    public void delete(StudentFeedbackModel feedback) {
        repository.delete(feedback);
    }

    public List<StudentFeedbackModel> findAll() {
        return repository.findAll();
    }

    public Optional<StudentFeedbackModel> findById(String id) {
        return repository.findById(id);
    }
}
