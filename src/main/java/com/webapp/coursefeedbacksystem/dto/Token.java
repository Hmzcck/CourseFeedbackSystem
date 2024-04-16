package com.webapp.coursefeedbacksystem.dto;

import com.webapp.coursefeedbacksystem.model.UserModel;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "token")
public class Token {

    // Getters and setters
    @Id
    private String id; // MongoDB typically uses String for IDs

    private String token;

    private boolean loggedOut;

    @DBRef
    private UserModel user; // Assuming you have a User document

}
