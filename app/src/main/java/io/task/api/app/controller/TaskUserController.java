package io.task.api.app.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.management.relation.RoleNotFoundException;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.exceptions.JWTVerificationException;

import io.task.api.app.DTO.TaskUserBasicDTO;
import io.task.api.app.DTO.TaskUserBasicUpdateDTO;
import io.task.api.app.DTO.TaskUserFullDTO;
import io.task.api.app.DTO.TaskUserMiddleDTO;
import io.task.api.app.model.TaskUser;
import io.task.api.app.security.PersonDetails;
import io.task.api.app.service.TaskUserService;
import io.task.api.app.utils.AppConstants;
import io.task.api.app.utils.DataNotDecryptedException;
import io.task.api.app.utils.DataNotEncryptedException;
import io.task.api.app.utils.TaskApiAppError;
import io.task.api.app.utils.PasswordNotChangedException;
import io.task.api.app.utils.UserNotCreatedException;
import io.task.api.app.utils.UserNotFoundException;
import io.task.api.app.utils.UserNotUpdatedException;

@RestController
public class TaskUserController {

    private static final Logger LOGGER = Logger.getLogger(TaskUserController.class.getName());

    private final TaskUserService appUserService;
    private final ModelMapper modelMapper;

    public TaskUserController(TaskUserService appUserService, ModelMapper modelMapper) {
        super();
        this.appUserService = appUserService;
        this.modelMapper = modelMapper;
    }

    @GetMapping(AppConstants.USER_PATH)
    public TaskUserBasicDTO getUserInfo(@PathVariable("username") String username) {
        String encryptedUserName = appUserService.getEncryptedData(username);
        TaskUserBasicDTO decryptedDTO = convertToTaskUserBasicDTO(appUserService.showUserInfosByName(encryptedUserName));

        return decryptedDTO;
    }

    @GetMapping(AppConstants.ADMIN_SELECTED_USER_ID)
    public TaskUserFullDTO getUserFullInfo(@PathVariable("id") String id) {
        LOGGER.info(AppConstants.GETTING_USER_INFO + id);

        return convertToFullTaskUserDTO(appUserService.showUserById(Integer.valueOf(id)));
    }

    @PatchMapping(AppConstants.ADMIN_SELECTED_USER)
    public Map<String, String> updateUserByAdmin(@PathVariable("username") String username,
            @RequestBody TaskUserBasicUpdateDTO appuserBasicUpdateDTO, BindingResult bindingResult) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        boolean isAdmin = personDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        LOGGER.info("Is Admin: " + isAdmin);

        LOGGER.info("User " + username + " update by admin requested");
        return updateUser(appUserService.getEncryptedData(username), appuserBasicUpdateDTO, bindingResult, isAdmin);
    }

    @PatchMapping(AppConstants.USER_PATH)
    public Map<String, String> updateUserByOwner(@RequestBody TaskUserBasicUpdateDTO appuserBasicUpdateDTO,
            BindingResult bindingResult) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        String username = personDetails.getUsername();
        LOGGER.info("User update is requested by user: " + personDetails.getPerson().getUserId());
        return updateUser(username, appuserBasicUpdateDTO, bindingResult, false);
    }

    @DeleteMapping(AppConstants.ADMIN_SELECTED_USER_ID)
    public Map<String, String> deleteUser(@PathVariable("id") String id) {
        LOGGER.info(AppConstants.DELETING_USER + id);

        appUserService.deleteUserById(Integer.valueOf(id));
        Map<String, String> response = new HashMap<>();
        response.put(AppConstants.REQUEST_STATUS, String.valueOf(ResponseEntity.ok(HttpStatus.OK).getStatusCode()));
        response.put(AppConstants.DELETING_USER, id);

        return response;
    }

    @GetMapping(AppConstants.ADMIN_ALL_USERS_PATH)
    public List<TaskUserBasicDTO> showAllUsers() {

        return appUserService.showAllUsers().stream().map(this::convertToTaskUserBasicDTO).collect(Collectors.toList());

    }

    @PostMapping(AppConstants.USER_CHANGE_PASS_PATH)
    public Map<String, String> changeUserPassword(@RequestBody Map<String, String> newPassMap) {

        Map<String, String> response = new HashMap<>();

        // TODO pass validator here and response depending on new pass validation

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();

        String encryptedUserName = personDetails.getUsername();
        LOGGER.info("Change password request received from " + encryptedUserName);
        appUserService.changePassword(encryptedUserName, newPassMap.get(AppConstants.CURRENT_PASSWORD),
                newPassMap.get(AppConstants.NEW_PASSWORD));

        response.put(AppConstants.REQUEST_STATUS, String.valueOf(ResponseEntity.ok(HttpStatus.OK).getStatusCode()));
        response.put("response:", "Password succesfully  changed");
        return response;
    }

    @ExceptionHandler
    private ResponseEntity<TaskApiAppError> handleException(AccessDeniedException e) {
        TaskApiAppError response = new TaskApiAppError(AppConstants.ACCESS_DENIED + " " + e.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity(response, HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler
    private ResponseEntity<TaskApiAppError> handleException(JWTVerificationException e) {
        TaskApiAppError response = new TaskApiAppError(AppConstants.FORBIDDEN_MESSAGE + " " + e.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity(response, HttpStatus.FORBIDDEN);

    }

    @ExceptionHandler
    private ResponseEntity<TaskApiAppError> handleException(UserNotFoundException e) {
        TaskApiAppError response = new TaskApiAppError(AppConstants.USER_WITH_THIS_NAME_WAS_NOT_FOUND,
                System.currentTimeMillis());
        return new ResponseEntity(response, HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler
    private ResponseEntity<TaskApiAppError> handleException(UserNotCreatedException e) {
        TaskApiAppError response = new TaskApiAppError(AppConstants.USER_WAS_NOT_CREATED + e.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler
    private ResponseEntity<TaskApiAppError> handleException(UserNotUpdatedException e) {
        TaskApiAppError response = new TaskApiAppError(AppConstants.USER_NOT_UPDATED + e.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler
    private ResponseEntity<TaskApiAppError> handleException(PasswordNotChangedException e) {
        TaskApiAppError response = new TaskApiAppError(AppConstants.PASSWORD_NOT_CHANGED + e.getMessage(),
                System.currentTimeMillis());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler
    private ResponseEntity<TaskApiAppError> handleException(DataNotEncryptedException e) {
        TaskApiAppError response = new TaskApiAppError(AppConstants.USER_DATA_WAS_NOT_ENCRYPTED,
                System.currentTimeMillis());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler
    private ResponseEntity<TaskApiAppError> handleException(DataNotDecryptedException e) {
        TaskApiAppError response = new TaskApiAppError(AppConstants.DATA_HAS_NOT_BEEN_DECRYPTED,
                System.currentTimeMillis());
        return new ResponseEntity(response, HttpStatus.BAD_REQUEST);

    }

    public Map<String, String> updateUser(String userName, TaskUserBasicUpdateDTO appuserBasicUpdateDTO,
            BindingResult bindingResult, boolean isAdmin) {

        Map<String, String> response = new HashMap<>();

        if (appuserBasicUpdateDTO == null || userName == null) {
            response.put("Request status: ", String.valueOf(ResponseEntity.ok(HttpStatus.BAD_REQUEST).getStatusCode()));
            LOGGER.info("User not updated: " + userName);

        }

        TaskUser appuser = convertToAppUserFromBasicAdminDTO(appuserBasicUpdateDTO);

        // TODO: appuserValidator.validate(appuser, bindingResult);

        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            List<FieldError> errors = bindingResult.getFieldErrors();

            for (FieldError e : errors) {
                errorMessage.append(e.getDefaultMessage()).append(";");
            }
            throw new UserNotCreatedException(errorMessage.toString());
        }

        try {
            appUserService.updateTaskUser(appuser, userName, isAdmin);
        } catch (RoleNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        response.put("Request status: ", String.valueOf(ResponseEntity.ok(HttpStatus.OK).getStatusCode()));
        response.put("Response: ", "User is successfully updated");
        LOGGER.info("User updated");

        return response;
    }

    public TaskUser convertToAppUser(TaskUserBasicDTO appuserBasic) {
        LOGGER.info(appuserBasic.toString());
        return modelMapper.map(appuserBasic, TaskUser.class);
    }

    private TaskUserBasicDTO convertToTaskUserBasicDTO(TaskUser appuser) {
        LOGGER.info(appuser.toString());
        return modelMapper.map(appuser, TaskUserBasicDTO.class);
    }

    public TaskUser convertToAppUserFromFullDTO(TaskUserFullDTO appuserFull) {
        LOGGER.info(appuserFull.toString());
        return modelMapper.map(appuserFull, TaskUser.class);
    }

    private TaskUserFullDTO convertToFullTaskUserDTO(TaskUser appuser) {
        LOGGER.info(appuser.toString());
        return modelMapper.map(appuser, TaskUserFullDTO.class);
    }

    public TaskUser convertToAppUserFromMiddleDTO(TaskUserMiddleDTO appuserMiddle) {
        LOGGER.info(appuserMiddle.toString());
        return modelMapper.map(appuserMiddle, TaskUser.class);
    }

    private TaskUserMiddleDTO convertToMiddleTaskUserDTO(TaskUser appuser) {
        LOGGER.info(appuser.toString());
        return modelMapper.map(appuser, TaskUserMiddleDTO.class);
    }

    public TaskUser convertToAppUserFromBasicAdminDTO(TaskUserBasicUpdateDTO appuserMiddle) {
        LOGGER.info(appuserMiddle.toString());
        return modelMapper.map(appuserMiddle, TaskUser.class);
    }

    private TaskUserBasicUpdateDTO convertToBasicAdminTaskUserDTO(TaskUser appuser) {
        LOGGER.info(appuser.toString());
        return modelMapper.map(appuser, TaskUserBasicUpdateDTO.class);
    }

}
