package com.webapp.coursefeedbacksystem.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Setter
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "people")
public class UserModel implements UserDetails {
    @Id
    private String id;

    private String email;

    private String name;

    private String surname;

    private String role;

    private String password;

    private List<CourseModel> courses; // Storing the course IDs as references for student
    private List<String> feedbacks; // Storing the feedback IDs as references for student
    private List<CourseModel> teachingCourses; // Only for teachers
    // private List<String> managedCourses; // Only for admins

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of();
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
