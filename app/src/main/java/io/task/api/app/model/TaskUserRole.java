package io.task.api.app.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="taskuserroles")
public class TaskUserRole implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="role_id", unique=true, nullable=false, precision=10)
    private int roleId;
    @Column(name="role_title", unique=true, nullable=false, length=30)
    private String roleTitle;

    @OneToMany(mappedBy = "role")
    private List<TaskUser> appuserOwner;
    
    /** Default constructor. */
    public TaskUserRole() {
        super();
    }

    /**
     * Access method for roleId.
     *
     * @return the current value of roleId
     */
    public int getRoleId() {
        return roleId;
    }

    /**
     * Setter method for roleId.
     *
     * @param aRoleId the new value for roleId
     */
    public void setRoleId(int aRoleId) {
        roleId = aRoleId;
    }

    /**
     * Access method for roleTitle.
     *
     * @return the current value of roleTitle
     */
    public String getRoleTitle() {
        return roleTitle;
    }

    /**
     * Setter method for roleTitle.
     *
     * @param aRoleTitle the new value for roleTitle
     */
    public void setRoleTitle(String aRoleTitle) {
        roleTitle = aRoleTitle;
    }

    /**
     * Compares the key for this instance with another Appuserroles.
     *
     * @param other The object to compare to
     * @return True if other object is instance of class Appuserroles and the key objects are equal
     */
    private boolean equalKeys(Object other) {
        if (this==other) {
            return true;
        }
        if (!(other instanceof TaskUserRole)) {
            return false;
        }
        TaskUserRole that = (TaskUserRole) other;
        if (this.getRoleId() != that.getRoleId()) {
            return false;
        }
        return true;
    }

    /**
     * Compares this instance with another Appuserroles.
     *
     * @param other The object to compare to
     * @return True if the objects are the same
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof TaskUserRole)) return false;
        return this.equalKeys(other) && ((TaskUserRole)other).equalKeys(this);
    }

    /**
     * Returns a hash code for this instance.
     *
     * @return Hash code
     */
    @Override
    public int hashCode() {
        int i;
        int result = 17;
        i = getRoleId();
        result = 37*result + i;
        return result;
    }

    /**
     * Returns a debug-friendly String representation of this instance.
     *
     * @return String representation of this instance
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("[Appuserroles |");
        sb.append(" roleId=").append(getRoleId());
        sb.append("]");
        return sb.toString();
    }

    /**
     * Return all elements of the primary key.
     *
     * @return Map of key names to values
     */
    public Map<String, Object> getPrimaryKey() {
        Map<String, Object> ret = new LinkedHashMap<String, Object>(6);
        ret.put("roleId", Integer.valueOf(getRoleId()));
        return ret;
    }

}
