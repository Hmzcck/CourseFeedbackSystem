package com.webapp.coursefeedbacksystem.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "urls")
public class URLModel {
    @Id
    private String url;

    @DBRef
    private UserModel user;

    @DBRef
    private CourseModel course;

    @Indexed(name = "ttl_index", expireAfterSeconds = 200)
    private Date createdAt;

    public URLModel(String url, CourseModel course, UserModel user) {
        this.url = url;
        this.createdAt = new Date();
        this.course = course;
        this.user = user;

    }
}
