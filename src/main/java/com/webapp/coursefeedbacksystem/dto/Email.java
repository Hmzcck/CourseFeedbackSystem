package com.webapp.coursefeedbacksystem.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Email {
    public String toEmail;
    public String subject;
    public String body;
}
