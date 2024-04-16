package com.webapp.coursefeedbacksystem.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "feedback")
public class FeedbackModel {

    @Id
    private String id;

    private Date date;
    private String topic;
    private String summary;
    private List<StudentFeedbackModel> studentFeedbacks;
    private List<String> url;

    public FeedbackModel(Date date, String s, String topic, List<String> urls) {
        this.date = date;
        this.topic = topic;
        this.summary = s;
        this.url = urls;
    }
}
