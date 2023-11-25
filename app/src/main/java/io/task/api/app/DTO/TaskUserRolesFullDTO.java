package io.task.api.app.DTO;

public class TaskUserRolesFullDTO implements IDTO {

    private static final long serialVersionUID = 1L;
    private int roleId;
    private String roleTitle;

    public TaskUserRolesFullDTO() {
        super();
    }

    public TaskUserRolesFullDTO(int roleId, String roleTitle) {
        super();
        this.roleId = roleId;
        this.roleTitle = roleTitle;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getRoleTitle() {
        return roleTitle;
    }

    public void setRoleTitle(String roleTitle) {
        this.roleTitle = roleTitle;
    }

    @Override
    public int getId() {
        return this.roleId;
    }

    @Override
    public void setId(int id) {
        roleId = id;
    }

}
