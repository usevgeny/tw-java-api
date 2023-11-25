package io.task.api.app.DTO;

import javax.validation.constraints.Size;

public class TaskUserMiddleDTO extends TaskUserBasicDTO {

    private static final long serialVersionUID = 1L;

    @Size(min = 3, max = 500)
    private String userPasshash;

    public TaskUserMiddleDTO(int userId, String userName, String userEmail,
            @Size(min = 3, max = 500) String userPasshash) {
        super(userId, userName, userEmail);
        this.userPasshash = userPasshash;
    }

    public TaskUserMiddleDTO(int userId, String userName, String userEmail) {
        super(userId, userName, userEmail);
    }

    public TaskUserMiddleDTO() {}

    public String getUserPasshash() {
        return userPasshash;
    }

    public void setUserPasshash(String userPasshash) {
        this.userPasshash = userPasshash;
    }

}
