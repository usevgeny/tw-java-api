package io.task.api.app.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.management.relation.RoleNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.task.api.app.model.TaskUser;
import io.task.api.app.repository.TaskUserRepository;
import io.task.api.app.utils.AlgoAES;
import io.task.api.app.utils.AppConstants;
import io.task.api.app.utils.DataNotDecryptedException;
import io.task.api.app.utils.DataNotEncryptedException;
import io.task.api.app.utils.NotAuthorisedUserException;
import io.task.api.app.utils.PasswordNotChangedException;
import io.task.api.app.utils.UserNotCreatedException;
import io.task.api.app.utils.UserNotFoundException;
import io.task.api.app.utils.UserNotUpdatedException;

@Service
public class TaskUserService {

    private final TaskUserRepository taskUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final TaskUserRolesService taskUserRolesService;
    private final AlgoAES algoAES;
    private static final Logger LOGGER = Logger.getLogger(TaskUserService.class.getName());

    public TaskUserService(TaskUserRepository taskUserRepository, PasswordEncoder passwordEncoder,
            TaskUserRolesService taskUserRolesService, AlgoAES algoAES) {
        this.taskUserRepository = taskUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.algoAES = algoAES;
        this.taskUserRolesService=taskUserRolesService;
    }

    public TaskUser showUserInfosByName(String userName) {
        
        LOGGER.info("showUserInfosByName: "+userName);
        return getDecryptedTaskUser(
                taskUserRepository.findOneByUserName(userName).orElseThrow(UserNotFoundException::new));
    }
    
    public TaskUser getEncryptedUserByEncryptedName(String userName) {
        
        LOGGER.info("getEncryptedUserByEncryptedName: "+userName);
        return taskUserRepository.findOneByUserName(userName).orElseThrow(UserNotFoundException::new);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void deleteUserById(int id) {
        taskUserRepository.deleteById(id);
    }

    public boolean isUserExisting(String username) {
        Optional<TaskUser> foudUser = taskUserRepository.findOneByUserName(username);
        return foudUser.isPresent();
    }

    public boolean isEmailTaken(String userEmail) {
        Optional<TaskUser> foudUser = taskUserRepository.findOneByUserEmail(userEmail);
        return foudUser.isPresent();
    }

    @Transactional
    public void createTaskUser(TaskUser taskUser) {

        TaskUser encryptedTaskUser;
        if (taskUser == null) {
            throw new UserNotCreatedException("User can not be null");
        }

        try {
            encryptedTaskUser = getEncryptedTaskUser(taskUser);
        } catch (Exception e) {
            throw new UserNotCreatedException(e.getMessage());
        }

        try {
            encryptedTaskUser.setRole(taskUserRolesService.getRoleByName(AppConstants.USER_ROLE_USER));
        } catch (Exception e) {
            throw new UserNotCreatedException(e.getMessage());
        }

        encryptedTaskUser.setPasswordChangeTime(LocalDateTime.now());
        encryptedTaskUser.setUserPasshash(passwordEncoder.encode(taskUser.getUserPasshash()));
        taskUserRepository.save(taskUserRepository.save(encryptedTaskUser));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<TaskUser> showAllUsers() {
        return taskUserRepository.findAll().stream().map(this::getDecryptedTaskUser).collect(Collectors.toList());
    }

    public TaskUser showUserById(Integer integer) {
        return getDecryptedTaskUser(
                taskUserRepository.findById(Integer.valueOf(integer)).orElseThrow(UserNotFoundException::new));
    }

    /**
     * 
     * @param taskUser an object sent from controller no field is encrypted yet
     * @param userName is an encrypted username a unique identifier allowing to find user in a database 
     * @param isAdmin allows admin to edit a target user's role
     * @throws RoleNotFoundException 
     */
    @PreAuthorize("hasRole('ROLE_ADMIN') or #userName == authentication.principal.username")
    @Transactional
    public void updateTaskUser(TaskUser taskUser, String userName, boolean isAdmin) throws RoleNotFoundException {

        if (userName == null) {
            throw new UsernameNotFoundException("Usename can not be null");
        }
        TaskUser userToBeUpdated = getDecryptedTaskUser(
                taskUserRepository.findOneByUserName(userName).orElseThrow(UserNotFoundException::new));

        if (isAdmin) {
            if (null != taskUser.getRole()) {
                userToBeUpdated.setRole(taskUserRolesService.getRoleByName(taskUser.getRole().getRoleTitle()));
            }

        } else if (!isAdmin && (null != taskUser.getRole())) {
            throw new UserNotUpdatedException("Your permissions level does not allow update your role, please contact your administrator");
        }

        if (taskUser.getUserName() != null) {
            userToBeUpdated.setUserName(taskUser.getUserName());
        }

        if (taskUser.getUserEmail() != null) {
            userToBeUpdated.setUserEmail(taskUser.getUserEmail());
        }

        TaskUser encryptedUser = getEncryptedTaskUser(userToBeUpdated);

        taskUserRepository.save(encryptedUser);
        // TODO: send an email to the updated user
    }

    @PreAuthorize("#claimerName == authentication.principal.username")
    @Transactional
    public void changePassword(String claimerName, String currentPassword, String newPassword)
            throws NotAuthorisedUserException, PasswordNotChangedException {

        if (claimerName == null) {
            throw new NotAuthorisedUserException("Username can not be null ");
        }

        if (currentPassword == null) {
            throw new PasswordNotChangedException("Current password can not be null ");
        }

        if (newPassword == null) {
            throw new PasswordNotChangedException("Your new password should not be null");
        }
        TaskUser userToBeUpdated = taskUserRepository.findOneByUserName(claimerName)
                .orElseThrow(UserNotFoundException::new);

        LOGGER.info("User n°" + userToBeUpdated.getUserId() + " requested to change his password");

        if (!passwordEncoder.matches(currentPassword, userToBeUpdated.getUserPasshash())) {
            throw new PasswordNotChangedException("Wrong input: current password is wrong");
        }
        if (passwordEncoder.matches(newPassword, userToBeUpdated.getUserPasshash())) {
            throw new PasswordNotChangedException("Your new password should not be the same as the old one");
        }

        userToBeUpdated.setUserPasshash(passwordEncoder.encode(newPassword));
        taskUserRepository.save(userToBeUpdated);

    }

    // TODO reset password -> send link to reset passwword

    // Encryption section

    public String getEncryptedData(String data) {
        String encryptedData = "";
        LOGGER.info("Got clear data for encryption: "+data);
        try {
            
            encryptedData = algoAES.encrypt(data);
        } catch (Exception e) {
            throw new DataNotEncryptedException(
                    "Data was not encrypted: " + String.valueOf(data) + " " + System.currentTimeMillis());
        }
        LOGGER.info("Encryption result: "+encryptedData);
        return encryptedData;
    }

    public String getDecryptedData(String encryptedData) {
        String decryptedData = "";

        LOGGER.info("Got encrypted data: "+encryptedData);
        try {
            decryptedData = algoAES.decrypt(encryptedData);
        } catch (Exception e) {
            throw new DataNotDecryptedException(
                    "Data was not decrypted: " + String.valueOf(encryptedData) + " " + System.currentTimeMillis());
        }
        LOGGER.info("Decryption result: "+decryptedData);
        return decryptedData;
    }

    public String getEncryptedUserName(TaskUser taskUser) {

        if (taskUser == null || taskUser.getUserName() == null) {
            throw new DataNotEncryptedException(
                    "Data was not encrypted: " + String.valueOf(taskUser) + " " + System.currentTimeMillis());
        }

        return getEncryptedData(taskUser.getUserName());
    }

    public String getEncryptedUserMail(TaskUser taskUser) {
        if (taskUser == null || taskUser.getUserEmail() == null) {
            throw new DataNotEncryptedException(
                    "Data was not encrypted: " + String.valueOf(taskUser) + " " + System.currentTimeMillis());
        }

        return getEncryptedData(taskUser.getUserEmail());
    }

    public String getDecryptedUserName(TaskUser taskUser) {
        if (taskUser == null || taskUser.getUserName() == null) {
            throw new DataNotEncryptedException("Data was not encrypted: " + " " + System.currentTimeMillis());
        }

        return getDecryptedData(taskUser.getUserName());
    }

    public String getDecryptedMail(TaskUser taskUser) {
        if (taskUser == null || taskUser.getUserEmail() == null) {
            throw new DataNotEncryptedException("Data was not encrypted: " + " " + System.currentTimeMillis());
        }

        return getDecryptedData(taskUser.getUserEmail());
    }

    public TaskUser getEncryptedTaskUser(TaskUser notEncryptedAppUser) {
        TaskUser encryptedAppUser = notEncryptedAppUser;
        encryptedAppUser.setUserEmail(getEncryptedUserMail(notEncryptedAppUser));
        encryptedAppUser.setUserName(getEncryptedUserName(notEncryptedAppUser));
        return encryptedAppUser;
    }

    public TaskUser getDecryptedTaskUser(TaskUser encryptedAppUser) {
        TaskUser decryptedAppUser = encryptedAppUser;
        decryptedAppUser.setUserEmail(getDecryptedMail(encryptedAppUser));
        decryptedAppUser.setUserName(getDecryptedUserName(encryptedAppUser));
        return decryptedAppUser;
    }

    public boolean isUserNameTaken(String encryptedUserName) {
        // TODO Auto-generated method stub
        return false;
    }

}
