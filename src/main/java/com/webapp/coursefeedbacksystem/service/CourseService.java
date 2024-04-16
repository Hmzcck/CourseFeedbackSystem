package com.webapp.coursefeedbacksystem.service;

import com.webapp.coursefeedbacksystem.model.CourseModel;
import com.webapp.coursefeedbacksystem.repository.CourseRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public CourseModel createCourse(CourseModel courseModel) {
        return courseRepository.save(courseModel);
    }

    public void deleteCourse(CourseModel courseModel) {
        courseRepository.delete(courseModel);
    }

    public List<CourseModel> getAllCourses() {
        return courseRepository.findAll();
    }

    public Optional<CourseModel> getCourseByName(String courseName) {
        return courseRepository.findByName(courseName);
    }

    public Optional<CourseModel> getCourseById(String courseId) {
        return courseRepository.findById(courseId);
    }

}
