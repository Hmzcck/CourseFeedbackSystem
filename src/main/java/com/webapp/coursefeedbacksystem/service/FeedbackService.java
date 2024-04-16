package com.webapp.coursefeedbacksystem.service;

import com.webapp.coursefeedbacksystem.dto.Email;
import com.webapp.coursefeedbacksystem.dto.FeedbackRequest;
import com.webapp.coursefeedbacksystem.model.CourseModel;
import com.webapp.coursefeedbacksystem.model.FeedbackModel;
import com.webapp.coursefeedbacksystem.model.StudentFeedbackModel;
import com.webapp.coursefeedbacksystem.model.UserModel;
import com.webapp.coursefeedbacksystem.repository.CourseRepository;
import org.springframework.stereotype.Service;
import com.webapp.coursefeedbacksystem.repository.FeedbackRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class FeedbackService {

    private final EmailService emailService;
    private final CourseRepository courseRepository;
    private final URLService urlService;
    private final FeedbackRepository feedbackRepository;

    public FeedbackService(EmailService emailService, CourseRepository courseRepository, URLService urlService,
            FeedbackRepository feedbackRepository) {
        this.emailService = emailService;
        this.courseRepository = courseRepository;
        this.urlService = urlService;
        this.feedbackRepository = feedbackRepository;
    }

    public FeedbackModel getFeedbackById(String id) {
        return feedbackRepository.findById(id).orElseThrow();
    }

    public FeedbackModel createFeedback(FeedbackModel feedbackModel) {
        return feedbackRepository.save(feedbackModel);
    }

    public FeedbackModel addFeedbackToCourse(FeedbackModel feedbackModel, CourseModel courseModel) {
        List<FeedbackModel> feedbackModels = courseModel.getFeedbacks();
        if (feedbackModels != null && !feedbackModels.isEmpty()) {
            feedbackModels.add(feedbackModel);
        } else {
            feedbackModels = new ArrayList<FeedbackModel>();
            feedbackModels.add(feedbackModel);
        }
        courseModel.setFeedbacks(feedbackModels);
        courseRepository.save(courseModel);
        return feedbackModel;
    }

    public String generateUrl(UserModel student) {
        // URL olustur
        String url = "test";
        return url;

    }

    public List<String> requestFeedbackFromStudents(FeedbackRequest feedbackRequest) {
        CourseModel course = courseRepository.findByName(feedbackRequest.getCourseName()).orElseThrow();
        Date date = feedbackRequest.getCourseDate();
        String topic = feedbackRequest.getCourseTopic();
        List<UserModel> students = course.getStudents();
        List<String> urls = new ArrayList<>();
        students.forEach(student -> {
            String url = generateUrl(student);
            // sendMailToStudent
            urls.add(url);
            emailService.sendEmail(new Email(
                    student.getEmail(),
                    course.getName(),
                    url));
        });
        FeedbackModel feedbackModel = createFeedback(new FeedbackModel(date, null, topic, urls));
        addFeedbackToCourse(feedbackModel, course);
        for (int i = 0; i < urls.size(); i++) {
            urlService.createURL(urls.get(i), course, students.get(i));
        }
        return urls;
    }

    public void submitForm(String comment, UserModel user, CourseModel course, FeedbackModel feedback) {

        if (feedback != null) {
            List<StudentFeedbackModel> studentFeedbacks = feedback.getStudentFeedbacks();
            if (studentFeedbacks != null) {
                studentFeedbacks.add(new StudentFeedbackModel(user, feedback, course, comment));
                feedback.setStudentFeedbacks(studentFeedbacks);
                createFeedback(feedback);
            } else {
                studentFeedbacks = new ArrayList<StudentFeedbackModel>();
                studentFeedbacks.add(new StudentFeedbackModel(user, feedback, course, comment));
                feedback.setStudentFeedbacks(studentFeedbacks);
                createFeedback(feedback);
            }

        } else {
            throw new RuntimeException("Feedback not found");
        }

    }
}
