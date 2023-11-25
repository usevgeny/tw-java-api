package io.task.api.app.service;

import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.task.api.app.model.TaskUser;
import io.task.api.app.repository.TaskUserRepository;
import io.task.api.app.security.PersonDetails;
import io.task.api.app.utils.AppConstants;

@Service
public class PersonDetailsService implements UserDetailsService {

    private static final Logger LOGGER = Logger.getLogger(PersonDetailsService.class.getName());
    private final TaskUserRepository taskUserRepository;

    @Autowired
    public PersonDetailsService(TaskUserRepository taskUserRepository) {
        this.taskUserRepository = taskUserRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<TaskUser> person = taskUserRepository.findOneByUserName(username);
        if (!person.isPresent()) {
            LOGGER.info(username +" was not found");
            throw new UsernameNotFoundException(AppConstants.USER_WITH_THIS_NAME_WAS_NOT_FOUND+" "+username);
        }
        LOGGER.info("user " + person.get().getUserId() +" is identyfied ");
        return new PersonDetails(person.get());
    }

}
