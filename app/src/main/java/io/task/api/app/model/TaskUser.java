package io.task.api.app.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "taskuser")
public class TaskUser implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", unique = true, nullable = false, precision = 10)
    private int userId;
    @Column(name = "user_name", unique = true, nullable = false, length = 500)
    @Size(min = 3, max = 500, message = "Please choose a valid name ")
    private String userName;
    @Size(min = 3, max = 500, message = "Please choose a valid email ")
    @Column(name = "user_email", unique = true, nullable = false, length = 500)
    private String userEmail;
    @Column(name = "user_passhash", nullable = false, length = 1000)
    private String userPasshash;
    
    @Column(name="password_change_time")
    private LocalDateTime passwordChangeTime;

    @ManyToOne
    @JoinColumn(name = "user_role", referencedColumnName = "role_id")
    private TaskUserRole role;

    /** Default constructor. */
    public TaskUser() {
        super();
    }

    /**
     * Access method for userId.
     *
     * @return the current value of userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * Setter method for userId.
     *
     * @param aUserId the new value for userId
     */
    public void setUserId(int aUserId) {
        userId = aUserId;
    }

    /**
     * Access method for userName.
     *
     * @return the current value of userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Setter method for userName.
     *
     * @param aUserName the new value for userName
     */
    public void setUserName(String aUserName) {
        userName = aUserName;
    }
    /**
     * Access method for userEmail.
     *
     * @return the current value of userEmail
     */
    public String getUserEmail() {
        return userEmail;
    }

    /**
     * Setter method for userEmail.
     *
     * @param aUserEmail the new value for userEmail
     */
    public void setUserEmail(String aUserEmail) {
        userEmail = aUserEmail;
    }

    /**
     * Access method for userPasshash.
     *
     * @return the current value of userPasshash
     */
    public String getUserPasshash() {
        return userPasshash;
    }

    /**
     * Setter method for userPasshash.
     *
     * @param aUserPasshash the new value for userPasshash
     */
    public void setUserPasshash(String aUserPasshash) {
        userPasshash = aUserPasshash;
    }

    /**
     * Compares the key for this instance with another Appuser.
     *
     * @param other The object to compare to
     * @return True if other object is instance of class Appuser and the key objects
     *         are equal
     */
    private boolean equalKeys(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof TaskUser)) {
            return false;
        }
        TaskUser that = (TaskUser) other;
        if (this.getUserId() != that.getUserId()) {
            return false;
        }
        return true;
    }

    
    
    public TaskUserRole getRole() {
        return role;
    }

    public void setRole(TaskUserRole role) {
        this.role = role;
    }

    public LocalDateTime getPasswordChangeTime() {
        return passwordChangeTime;
    }

    public void setPasswordChangeTime(LocalDateTime passwordChangeTime) {
        this.passwordChangeTime = passwordChangeTime;
    }

    /**
     * Compares this instance with another Appuser.
     *
     * @param other The object to compare to
     * @return True if the objects are the same
     */
    @Override
    public boolean equals(Object other) {
        if (!(other instanceof TaskUser))
            return false;
        return this.equalKeys(other) && ((TaskUser) other).equalKeys(this);
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
        i = getUserId();
        result = 37 * result + i;
        return result;
    }

    /**
     * Returns a debug-friendly String representation of this instance.
     *
     * @return String representation of this instance
     */
    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("[Appuser |");
        sb.append(" userId=").append(getUserId());
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
        ret.put("userId", Integer.valueOf(getUserId()));
        return ret;
    }

}
