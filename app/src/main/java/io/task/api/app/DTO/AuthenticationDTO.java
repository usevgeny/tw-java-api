package io.task.api.app.DTO;

import javax.validation.constraints.NotEmpty;

import io.task.api.app.utils.AppConstants;

public class AuthenticationDTO {
    
    @NotEmpty(message = AppConstants.USERNAME_CANNOT_BE_NULL)
    private String username;

    @NotEmpty(message = AppConstants.PASSWORD_CANNOT_BE_NULL)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    } 

}
