package com.webapp.coursefeedbacksystem.dto;

import java.util.Date;

import lombok.Getter;

@Getter
public class FeedbackSummary {
    private String courseName;
    private Date date;
    private String topic;
    private String summary;


    public FeedbackSummary(String courseName,Date date, String topic, String summary)  {
        this.courseName = courseName;
        this.date = date;
        this.topic = topic;
        this.summary = summary;

    }

    // Getters and setters
}
