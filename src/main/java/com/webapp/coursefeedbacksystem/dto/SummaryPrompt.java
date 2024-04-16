package com.webapp.coursefeedbacksystem.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SummaryPrompt {

    private String role;
    private String content;//prompt
}
