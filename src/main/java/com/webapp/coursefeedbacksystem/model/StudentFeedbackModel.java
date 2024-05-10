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

    private String comment;

    public StudentFeedbackModel(UserModel studentId, String comment) {
        this.studentId = studentId;
        this.comment = comment;
    }
}
