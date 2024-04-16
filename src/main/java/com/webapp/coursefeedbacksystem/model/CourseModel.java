package com.webapp.coursefeedbacksystem.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "courses")
public class CourseModel {

    @Id
    private String id;

    private String name;

    @DBRef
    private List<UserModel> teacher;

    @DBRef
    private List<UserModel> students;

    @DBRef
    private List<FeedbackModel> feedbacks;
}
