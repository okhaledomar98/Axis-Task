package com.axis.automation.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * POJO representing a user entity for test data.
 * Used in registration and login test scenarios.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String confirmPassword;
}