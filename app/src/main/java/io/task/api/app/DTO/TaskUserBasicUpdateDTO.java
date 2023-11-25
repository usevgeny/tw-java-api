package io.task.api.app.DTO;

import javax.validation.constraints.Size;

public class TaskUserBasicUpdateDTO implements IDTO {

    private static final long serialVersionUID = 1L;

    private int userId;

    @Size(min = 3, max = 500, message = "Please choose a valid name betwean 3 and 15 characters")
    private String userName;

    @Size(min = 3, max = 500, message = "Please choose a valid email betwean 5 and 25 characters")
    private String userEmail;

    private TaskUserRolesBasicDTO role;

    private String userPhoneNumber;



    public TaskUserBasicUpdateDTO(
            @Size(min = 3, max = 500, message = "Please choose a valid name betwean 3 and 15 characters") String userName,
            @Size(min = 3, max = 500, message = "Please choose a valid email betwean 5 and 25 characters") String userEmail,
            TaskUserRolesBasicDTO role, String userPhoneNumber) {
        super();
        this.userName = userName;
        this.userEmail = userEmail;
        this.role = role;
        this.userPhoneNumber = userPhoneNumber;
    }

    public TaskUserBasicUpdateDTO() {

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

    public TaskUserRolesBasicDTO getRole() {
        return role;
    }

    public void setRole(TaskUserRolesBasicDTO role) {
        this.role = role;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    @Override
    public int getId() {
        return getUserId();
    }

    @Override
    public void setId(int id) {
        setUserId(id);
    }

}
