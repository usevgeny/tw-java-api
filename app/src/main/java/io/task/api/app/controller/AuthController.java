package io.task.api.app.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.exceptions.JWTVerificationException;

import io.task.api.app.DTO.AuthenticationDTO;
import io.task.api.app.DTO.TaskUserMiddleDTO;
import io.task.api.app.model.TaskUser;
import io.task.api.app.service.TaskUserService;
import io.task.api.app.servicemetier.TaskUserManagementService;
import io.task.api.app.utils.AppConstants;
import io.task.api.app.utils.JWTUtil;
import io.task.api.app.utils.TaskApiAppError;
import io.task.api.app.utils.TaskUserValidator;
import io.task.api.app.utils.UserNotCreatedException;
import io.task.api.app.utils.UserNotFoundException;

@RestController
public class AuthController {
    private static final Logger LOGGER = Logger.getLogger(TaskUserController.class.getName());

    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TaskUserManagementService taskUserManagementService;

    @PostMapping(AppConstants.REGISTER_PATH)
    public Map<String, String> registerNewUser(@RequestBody TaskUserMiddleDTO appuserMiddleDTO,
            BindingResult bindingResult) {

        LOGGER.info(appuserMiddleDTO.toString());
        TaskUser appuser = convertToAppUserFromMiddleDTO(appuserMiddleDTO);

        taskUserValidator.validate(appuser, bindingResult);
        // TODO appuserRolesValidator validatevalidate(appuserrole, bindingResult)

        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();

            for (FieldError e : errors) {
                errorMessage.append(e.getDefaultMessage()).append(";");
            }
            throw new UserNotCreatedException(errorMessage.toString());
        }


        
        taskUserService.createTaskUser(appuser);
        String token = jwtUtil.generateToken(appuserMiddleDTO.getUserName());

        Map<String, String> reponseMap = new HashMap<>();
        reponseMap.put("status", String.valueOf(ResponseEntity.ok(HttpStatus.OK).getStatusCodeValue()));
        reponseMap.put("username", String.valueOf(appuserMiddleDTO.getUserName()));
        return reponseMap;

    }

    @PostMapping(AppConstants.LOGIN_PATH)
    public Map<String, String> performLogin(@RequestBody AuthenticationDTO authenticationDTO) {

        UsernamePasswordAuthenticationToken authInputToken = new UsernamePasswordAuthenticationToken(
                taskUserService.getEncryptedData(authenticationDTO.getUsername()), authenticationDTO.getPassword());
        LOGGER.info(authenticationDTO.getUsername() + " is authenticated");
        Map<String, String> reponseMap = new HashMap<>();
        try {
            authenticationManager.authenticate(authInputToken);
        } catch (BadCredentialsException e) {
            reponseMap.put("message: ", AppConstants.WRONG_CREDENTIALS);
            reponseMap.put("error: ", e.getMessage());
            return reponseMap;
        }

        String token = jwtUtil.generateToken(authenticationDTO.getUsername());
        reponseMap.put(AppConstants.JWT_TOKEN_HEADER, token);
        return reponseMap;
    }

    public TaskUser convertToAppUserFromMiddleDTO(TaskUserMiddleDTO appuserMiddle) {
        LOGGER.info(appuserMiddle.toString());
        return modelMapper.map(appuserMiddle, TaskUser.class);
    }

    private TaskUserMiddleDTO convertToMiddleTaskUserDTO(TaskUser appuser) {
        return modelMapper.map(appuser, TaskUserMiddleDTO.class);
    }

    @ExceptionHandler
    private ResponseEntity<TaskApiAppError> handleException(JWTVerificationException e) {
        LOGGER.info(e.getMessage());
        TaskApiAppError response = new TaskApiAppError(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler
    private ResponseEntity<TaskApiAppError> handleException(ServletException e) {
        LOGGER.info(e.getMessage());
        TaskApiAppError response = new TaskApiAppError(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler
    private ResponseEntity<TaskApiAppError> handleException(IOException e) {
        LOGGER.info(e.getMessage());
        TaskApiAppError response = new TaskApiAppError(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler
    private ResponseEntity<TaskApiAppError> handleException(AccessDeniedException e) {
        LOGGER.info(e.getMessage());
        TaskApiAppError response = new TaskApiAppError(e.getMessage(), System.currentTimeMillis());
        return new ResponseEntity(response, HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler
    private ResponseEntity<TaskApiAppError> handleException(UserNotFoundException e) {
        TaskApiAppError response = new TaskApiAppError(AppConstants.USER_NOT_FOUND + " " + e.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler
    private ResponseEntity<TaskApiAppError> handleException(UsernameNotFoundException e) {
        LOGGER.info(e.getMessage());
        TaskApiAppError response = new TaskApiAppError(AppConstants.USER_WITH_THIS_NAME_WAS_NOT_FOUND,
                System.currentTimeMillis());
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);

    }

}
