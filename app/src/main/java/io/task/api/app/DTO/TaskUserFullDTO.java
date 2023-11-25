package io.task.api.app.DTO;

import javax.validation.constraints.Size;

public class TaskUserFullDTO extends TaskUserMiddleDTO {

    private static final long serialVersionUID = 1L;
    private TaskUserRolesBasicDTO role;
    public TaskUserFullDTO(int userId, String userName, String userEmail, @Size(min = 3, max = 500) String userPasshash,
            String userPhoneNumber, TaskUserRolesBasicDTO role) {
        super(userId, userName, userEmail, userPasshash, userPhoneNumber);
        this.role = role;
    }
    public TaskUserFullDTO(int userId, String userName, String userEmail, @Size(min = 3, max = 500) String userPasshash,
            String userPhoneNumber) {
        super(userId, userName, userEmail, userPasshash, userPhoneNumber);
        // TODO Auto-generated constructor stub
    }
    public TaskUserFullDTO(int userId, String userName, String userEmail) {
        super(userId, userName, userEmail);
        // TODO Auto-generated constructor stub
    }
    public TaskUserFullDTO() {
    }
    public TaskUserRolesBasicDTO getRole() {
        return role;
    }
    public void setRole(TaskUserRolesBasicDTO role) {
        this.role = role;
    }
}
