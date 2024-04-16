package com.webapp.coursefeedbacksystem.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "studentFeedback")
public class StudentFeedbackModel {

    @Id
    private String id;

    @DBRef
    private UserModel studentId; // student id
    @DBRef
    private FeedbackModel feedbackId; // feedback id
    @DBRef
    private CourseModel courseId;

    private String comment;

    public StudentFeedbackModel(UserModel studentId, FeedbackModel feedbackId, CourseModel courseId, String comment) {
        this.studentId = studentId;
        this.feedbackId = feedbackId;
        this.courseId = courseId;
        this.comment = comment;
    }
}
