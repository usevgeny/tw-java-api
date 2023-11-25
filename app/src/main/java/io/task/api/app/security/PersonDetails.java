package io.task.api.app.security;

import java.util.Collection;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.task.api.app.model.TaskUser;

public class PersonDetails implements UserDetails {

    private static final long serialVersionUID = 1L;
    private final TaskUser person;

    @Autowired
    public PersonDetails(TaskUser person) {
        super();
        this.person = person;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        // SHOW_ACCOUNT, WITHDRAW_MONEY, SEND_MONEY -- authorities (actions)
        // ROLE_ADMIN, ROLE_USER -- roles but for sring security is the same
        return Collections.singletonList(new SimpleGrantedAuthority(person.getRole().getRoleTitle()));
    }

    @Override
    public String getPassword() {
        return this.person.getUserPasshash();
    }

    @Override
    public String getUsername() {
        return this.person.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // need in order to get the data of the authenticated person
    public TaskUser getPerson() {
        return this.person;
    }

}
