package io.task.api.app.DTO;

import java.io.Serializable;

public class TaskUserRolesBasicDTO implements Serializable{

    private static final long serialVersionUID = 1L;
    private String roleTitle;
    
    
    public TaskUserRolesBasicDTO() {
        super();
    }
    public TaskUserRolesBasicDTO(String roleTitle) {
        super();
        this.roleTitle = roleTitle;
    }
    public String getRoleTitle() {
        return roleTitle;
    }
    public void setRoleTitle(String roleTitle) {
        this.roleTitle = roleTitle;
    }
    @Override
    public String toString() {
        return "TaskUserRolesBasicDTO [roleTitle=" + roleTitle + "]";
    }



}
