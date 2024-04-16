package com.webapp.coursefeedbacksystem.dto;

import lombok.Getter;

import java.util.Date;

@Getter
public class FeedbackRequest {

    public String courseName;
    public Date courseDate;
    public String courseTopic;
}
