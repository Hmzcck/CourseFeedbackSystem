package com.webapp.coursefeedbacksystem.controller;

import com.webapp.coursefeedbacksystem.dto.*;
import com.webapp.coursefeedbacksystem.model.CourseModel;
import com.webapp.coursefeedbacksystem.model.FeedbackModel;
import com.webapp.coursefeedbacksystem.model.UserModel;
import com.webapp.coursefeedbacksystem.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class UserController {
    private final AuthenticationService authService;
    private final UserService userService;
    private final EmailService emailService;
    private final RestTemplate template;
    private final FeedbackService feedbackService;
    private final CourseService courseService;
    private final JwtService jwtService;

    @Value("${openai.api.model}")
    private String model;

    @Value(("${openai.api.url}"))
    private String apiURL;

    public UserController(AuthenticationService authService, UserService userService, EmailService emailService,
            RestTemplate template, FeedbackService feedbackService, CourseService courseService, JwtService jwtService) {
        this.authService = authService;
        this.userService = userService;
        this.emailService = emailService;
        this.template = template;
        this.feedbackService = feedbackService;
        this.courseService = courseService;
        this.jwtService = jwtService;
    }

    @GetMapping("/roleget") // Changed to a GET request
    public ResponseEntity<String> getRoleByMail(@RequestParam String email) { // Using @RequestParam
        UserModel user = userService.findUserByEmail(email);
        return ResponseEntity.ok(user.getRole()); // No need for Optional now
    }

    @PostMapping("/sendmail")
    public ResponseEntity<Boolean> sendmail(
            @RequestBody Email request) {
        return ResponseEntity.ok(emailService.sendEmail(request));
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody UserModel request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody UserModel request) {
        return ResponseEntity.ok(authService.authenticate(request));
    }

    @GetMapping("/chat")
    public String chat(@RequestParam("prompt") String prompt) {
        ChatGPTRequest request = new ChatGPTRequest(model, prompt);
        ChatGptResponse chatGptResponse = template.postForObject(apiURL, request, ChatGptResponse.class);
        assert chatGptResponse != null;
        return chatGptResponse.getChoices().get(0).getMessage().getContent();
    }

    @PostMapping("/request-feedback")
    public void requestFeedback(@RequestBody FeedbackRequest request) {
        List<String> url = feedbackService.requestFeedbackFromStudents(request);
    }

    @PostMapping("/add-course")
    public void addCourse(@RequestBody CourseModel courseModel) {
        courseService.createCourse(courseModel);
    }

    @PostMapping("/submit-form")
    public void submitForm(@RequestHeader String referer, @RequestBody String comment) {
        System.out.println(referer);
        System.out.println(comment);
        String[] idArr = referer.split("-");
        UserModel user = userService.findUserById(idArr[0]);
        System.out.println(user.getEmail());
        CourseModel course = courseService.getCourseById(idArr[1]).get();
        System.out.println(course.getName());
        FeedbackModel feedback = feedbackService.getFeedbackById(idArr[2]);
        System.out.println("feedback");
        System.out.println(feedback.getTopic());
        feedbackService.submitForm(comment, user, course, feedback);
    }

    @PostMapping("/test")
    public void testEndpoint(@RequestHeader String Authorization, @RequestBody String body) {
        System.out.println(jwtService.findByToken(Authorization.split(" ")[1]).getUser().getEmail());
        System.out.println(body);
    };

    @GetMapping("/info")
    public void getUrl(@RequestHeader(value = "Referer", required = false) String referer) {
        System.out.println(referer);
    }

    @GetMapping("/get-feedbacks")
    public List<FeedbackModel> getMethodName(@RequestHeader String Authorization) {
        UserModel user = jwtService.findByToken(Authorization.split(" ")[1]).getUser();
        List<CourseModel> courses = user.getCourses();
        List<FeedbackModel> feedbacks = new ArrayList<>();
        courses.forEach(course -> {
            feedbacks.addAll(course.getFeedbacks());
        });
        return feedbacks;
    }

    @PostMapping("/update-chatgpt-api")
    public void updateChatGPTAPI(@RequestBody String newAPI) {
        apiURL = newAPI;
    }

    @PostMapping("/update-email-server-config")
    public void updateEmailServerConfig(@RequestBody EmailServerConfig emailServerConfig) {
        emailService.updateEmailServerConfig(emailServerConfig);
    }

}
