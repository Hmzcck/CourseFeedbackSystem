package com.webapp.coursefeedbacksystem.service;

import com.webapp.coursefeedbacksystem.dto.Email;
import com.webapp.coursefeedbacksystem.dto.FeedbackRequest;
import com.webapp.coursefeedbacksystem.dto.FeedbackSummary;
import com.webapp.coursefeedbacksystem.model.CourseModel;
import com.webapp.coursefeedbacksystem.model.FeedbackModel;
import com.webapp.coursefeedbacksystem.model.StudentFeedbackModel;
import com.webapp.coursefeedbacksystem.model.UserModel;
import com.webapp.coursefeedbacksystem.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.webapp.coursefeedbacksystem.repository.FeedbackRepository;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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

    @SuppressWarnings("deprecation")
    public String buildUrl(String baseUrl, String param1, String param2, String param3) {
        return String.format("%s?param1=%s&param2=%s&param3=%s",
                baseUrl,
                URLEncoder.encode(param1),
                URLEncoder.encode(param2),
                URLEncoder.encode(param3)
        );
    }

    public String generateUrl(String userId, String courseId, String feedbackId) {        // URL olustur
        //String url = "http://localhost:8080/check-link?param1=" + userId + "&param2=" + courseId + "&param3=" + feedbackId;
        String urlBuilt =   buildUrl("http://localhost:8080/check-link", userId, courseId, feedbackId);
        return urlBuilt;
    }

    public List<String> requestFeedbackFromStudents(FeedbackRequest feedbackRequest) {
        CourseModel course = courseRepository.findByName(feedbackRequest.getCourseName()).orElseThrow();
        Date date = feedbackRequest.getCourseDate();
        String topic = feedbackRequest.getCourseTopic();
        List<UserModel> students = course.getStudents();
        List<String> urls = new ArrayList<>();
       
        FeedbackModel feedbackModel = createFeedback(new FeedbackModel(date, null, topic));
        students.forEach(student -> {
            String url = generateUrl(student.getId(), course.getId(), feedbackModel.getId());
            // sendMailToStudent
            urls.add(url);
            emailService.sendEmail(new Email(
                    student.getEmail(),
                    course.getName(),
                    url));
        });
        addFeedbackToCourse(feedbackModel, course);
        for (int i = 0; i < urls.size(); i++) {
            urlService.createURL(urls.get(i), course, students.get(i), feedbackModel);
        }
        return urls;
    }

    public void submitForm(String comment, UserModel user, CourseModel course, FeedbackModel feedback) {

        if (feedback != null) {
            List<StudentFeedbackModel> studentFeedbacks = feedback.getStudentFeedbacks();
            if (studentFeedbacks != null) {
                studentFeedbacks.add(new StudentFeedbackModel(user, comment));
                feedback.setStudentFeedbacks(studentFeedbacks);
                createFeedback(feedback);
            } else {
                studentFeedbacks = new ArrayList<StudentFeedbackModel>();
                studentFeedbacks.add(new StudentFeedbackModel(user, comment));
                feedback.setStudentFeedbacks(studentFeedbacks);
                createFeedback(feedback);
            }

        } else {
            throw new RuntimeException("Feedback not found");
        }

    }

       public List<FeedbackSummary> getFeedbackSummariesForUser(UserModel user) {
        List<CourseModel> courses = user.getCourses();
        List<FeedbackSummary> feedbackDtos = new ArrayList<>();

        for (CourseModel course : courses) {
            for (FeedbackModel feedback : course.getFeedbacks()) {
                feedbackDtos.add(new FeedbackSummary(
                    course.getName(),
                    feedback.getDate(),
                    feedback.getTopic(),
                    feedback.getSummary()
                ));
            }
        }
        return feedbackDtos;
    }
}
