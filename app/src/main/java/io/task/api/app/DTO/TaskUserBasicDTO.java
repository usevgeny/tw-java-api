package io.task.api.app.DTO;

import javax.validation.constraints.Size;

public class TaskUserBasicDTO implements IDTO {

    private static final long serialVersionUID = 1L;

    private int userId;

    @Size(min = 3, max = 500, message = "Please choose a valid name betwean 3 and 15 characters")
    private String userName;

    @Size(min = 3, max = 500, message = "Please choose a valid email betwean 5 and 25 characters")
    private String userEmail;


    public TaskUserBasicDTO(int userId, String userName, String userEmail) {
        this.userId = userId;
        this.userName = userName;
        this.userEmail = userEmail;
    }

    public TaskUserBasicDTO() {
        super();
    }

    @Override
    public int getId() {
        return getUserId();
    }

    @Override
    public void setId(int id) {
        setUserId(id);
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    @Override
    public String toString() {
        return "TaskUserBasicDTO [userId=" + userId + ", userName=" + userName + ", userEmail=" + userEmail
                 + "]";
    }

}
