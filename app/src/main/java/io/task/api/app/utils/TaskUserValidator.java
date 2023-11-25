package io.task.api.app.utils;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import io.task.api.app.model.TaskUser;
import io.task.api.app.service.TaskUserService;

/**
 * 
 * @author evgeny
 * 
 *         regex password validation (?=.*[a-z]) : This matches the presence of
 *         at least one lowercase letter. (?=.*d) : This matches the presence of
 *         at least one digit i.e. 0-9. (?=.*[@#$%]) : This matches the presence
 *         of at least one special character. ((?=.*[A-Z]) : This matches the
 *         presence of at least one capital letter. {6,16} : This limits the
 *         length of password from minimum 6 letters to maximum 16 letters.
 */

@Component
public class TaskUserValidator implements Validator {
    private static final Logger LOGGER = Logger.getLogger(TaskUserValidator.class.getName());
    private final TaskUserService taskUserService;

    @Autowired
    public TaskUserValidator(TaskUserService taskUserService) {
        super();
        this.taskUserService = taskUserService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return TaskUser.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        LOGGER.info("User validation started");
        TaskUser taskUser = (TaskUser) target;

        validateUsername(taskUser, errors);
        validateEmail(taskUser, errors);
        validatePassword(taskUser, errors);
    }

    private void validateUsername(TaskUser taskUser, Errors errors) {
        String username = taskUser.getUserName();
        if (username == null) {
            errors.rejectValue("userName", "", "Username can not be null");
        } else if (username.length() < AppConstants.USERNAME_MIN_LENGTH
                || username.length() > AppConstants.USERNAME_MAX_LENGTH) {
            errors.rejectValue("userName", "", AppConstants.USERNAME_LENGTH_ERROR_MESSAGE);
        } else if (!username.matches(AppConstants.NAME_REGEX)) {
            errors.rejectValue("userName", "", "Username can only contain alpha-numeric values");
            errors.rejectValue("userPhoneNumber", "", "Invalid phone number");
        } else if (taskUserService.isUserExisting(taskUserService.getEncryptedUserName(taskUser))) {
            errors.rejectValue("userName", "", "This name is already taken");
        }
    }

    private void validateEmail(TaskUser taskUser, Errors errors) {
        String email = taskUser.getUserEmail();
        if (email == null) {
            errors.rejectValue("userEmail", "", "E-mail can not be null");
        } else if (!email.matches(AppConstants.EMAIL_REGEX)) {
            errors.rejectValue("userEmail", "", "E-mail is not valid");
        } else if (email.length() < 4 || email.length() > 60) {
            errors.rejectValue("userEmail", "", "E-mail is not valid. Check your email length");
        } else if (taskUserService.isEmailTaken(taskUserService.getEncryptedUserMail(taskUser))) {
            errors.rejectValue("userEmail", "", "This e-mail is already taken");
        }
    }

    private void validatePassword(TaskUser taskUser, Errors errors) {
        String password = taskUser.getUserPasshash();
        if (!password.matches(AppConstants.PASSWORD_REGEX)) {
            LOGGER.warning(password);
            errors.rejectValue("userPasshash", "", AppConstants.PASSWORD_NOTICE);
        }
    }

}
