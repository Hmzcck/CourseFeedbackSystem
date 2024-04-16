package com.webapp.coursefeedbacksystem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailServerConfig {
    
    public String host;
    public int port;
    public String username;
    public String password;
}
