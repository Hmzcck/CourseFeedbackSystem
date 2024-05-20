package com.webapp.coursefeedbacksystem.controller;

import com.webapp.coursefeedbacksystem.dto.*;
import com.webapp.coursefeedbacksystem.model.CourseModel;
import com.webapp.coursefeedbacksystem.model.FeedbackModel;
import com.webapp.coursefeedbacksystem.model.URLModel;
import com.webapp.coursefeedbacksystem.model.UserModel;
import com.webapp.coursefeedbacksystem.service.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.service.invoker.UrlArgumentResolver;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Value;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {
    private final AuthenticationService authService;
    private final UserService userService;
    private final EmailService emailService;
    private final RestTemplate template;
    private final FeedbackService feedbackService;
    private final CourseService courseService;
    private final JwtService jwtService;
    private final URLService URLService;

    @Value("gpt-3.5-turbo")
    private String model;

    @Value(("https://api.openai.com/v1/chat/completions"))
    private String apiURL;

    public UserController(AuthenticationService authService, UserService userService, EmailService emailService,
            RestTemplate template, FeedbackService feedbackService, CourseService courseService,
            JwtService jwtService, URLService URLService) {
        this.authService = authService;
        this.userService = userService;
        this.emailService = emailService;
        this.template = template;
        this.feedbackService = feedbackService;
        this.courseService = courseService;
        this.jwtService = jwtService;
        this.URLService = URLService;
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
    public ResponseEntity<AuthenticationResponse> login(@RequestBody UserModel request) {
        AuthenticationResponse response = authService.authenticate(request);
        return ResponseEntity.ok(response);
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

    @GetMapping("/check-link")
    public ResponseEntity<Void> handleLink(@RequestParam String param1,
            @RequestParam String param2,
            @RequestParam String param3) {
        String uid = param1 + "&param2=" + param2 + "&param3=" + param3;
        URLModel link = URLService.findByUrl("http://localhost:8080/check-link?param1=" + uid);
        if (link != null) {
            CourseModel course = courseService.getCourseById(param2).orElse(null);
            FeedbackModel feedback = feedbackService.getFeedbackById(param3);

            if (course != null && feedback != null) {
                String cName = course.getName();
                String fTopic = feedback.getTopic();
                String date = feedback.getDate().toString();

                // Using UriComponentsBuilder to construct the URL safely
                String reactUrl = UriComponentsBuilder
                        .fromHttpUrl("http://localhost:3000/submitform")
                        .queryParam("uid", uid)
                        .queryParam("courseName", cName)
                        .queryParam("feedbackTopic", fTopic)
                        .queryParam("date", date)
                        .toUriString();

                HttpHeaders headers = new HttpHeaders();
                headers.setLocation(URI.create(reactUrl));
                return new ResponseEntity<>(headers, HttpStatus.FOUND);
            }
        }
        System.out.println("Link not found");
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/submit-form")
    public ResponseEntity<Object> submitForm(@RequestHeader("url") String url,
            @RequestBody String comment) {
        if (url != null) {
            try {
                String[] urlArr = url.split("&");
                System.out.println(urlArr[0]);
                System.out.println(urlArr[1]);
                System.out.println(urlArr[2]);
                String userId = urlArr[0];
                String courseId = urlArr[1].split("=")[1];
                String feedbackId = urlArr[2].split("=")[1];
                System.out.println(userId);
                System.out.println(courseId);
                System.out.println(feedbackId);
                UserModel user = userService.findUserById(userId);
                System.out.println(user.getEmail());
                // Proceed with your logic
                System.out.println(user.getEmail());
                System.out.println(comment);
                System.out.println(user.getEmail());
                CourseModel course = courseService.getCourseById(courseId).get();
                System.out.println(course.getName());
                FeedbackModel feedback = feedbackService.getFeedbackById(feedbackId);
                System.out.println("feedback");
                System.out.println(feedback.getTopic());
                feedbackService.submitForm(comment, user, course, feedback);
                System.out.println("feedback submitted");
                String fullURL = "http://localhost:8080/check-link?param1=" + userId + "&param2=" + courseId + "&param3="
                        + feedbackId;
                URLModel urlModel = URLService.findByUrl(fullURL);
                URLService.deleteURL(urlModel);
                return ResponseEntity.ok().build();
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing or invalid Authorization header");
        }
    }

    @GetMapping("/get-courses")
    public List<String> getCourses(@RequestHeader String Authorization) {
        UserModel user = jwtService.findByToken(Authorization.split(" ")[1]).getUser();
        if(user.getRole().equals("teacher")) {
            List<CourseModel> courses = userService.getTeachingCourses(user);
            List<String> courseNames = new ArrayList<>();
            for (CourseModel course : courses) {
                courseNames.add(course.getName());
            }
            return courseNames;
        }
        return null; 
    };

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
    public ResponseEntity<List<FeedbackSummary>> getMethodName(@RequestHeader String Authorization) {
        UserModel user = jwtService.findByToken(Authorization.split(" ")[1]).getUser();
        List<FeedbackSummary> feedbackDtos = feedbackService.getFeedbackSummariesForUser(user);
        return ResponseEntity.ok(feedbackDtos);
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
