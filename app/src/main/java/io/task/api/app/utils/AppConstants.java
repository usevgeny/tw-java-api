package io.task.api.app.utils;

public class AppConstants {

    public static final String API_VERSION = "1";
    public static final String ROOT_APP_PATH = "/api/v" + API_VERSION + "/app";
    public static final String MAIN_PUBLIC_PAGE = ROOT_APP_PATH+"/mainpublic";
    public static final String ERROR_PATH = "/error";
    public static final String REGISTER_PATH = ROOT_APP_PATH + "/register";
    public static final String LOGIN_PATH = ROOT_APP_PATH + "/login";
    public static final String PROCESS_LOGIN_PATH = ROOT_APP_PATH + "/process_login";
    public static final String LOGOUT_PATH = ROOT_APP_PATH + "/logout";
    public static final String ADMIN_PATH = ROOT_APP_PATH + "/reggioio";
    public static final String ADMIN_ALL_USERS_PATH = ADMIN_PATH + "/allusers";
    public static final String ADMIN_SELECTED_USER = ADMIN_PATH + "/userprofile/{username}";
    public static final String ADMIN_SELECTED_USER_ID = ADMIN_PATH + "/userprofile/{id}";
    public static final String SUPER_USER_PATH = ROOT_APP_PATH + "/superuser/{username}/manage";
    public static final String USER_PATH = ROOT_APP_PATH + "/user";
    public static final String USER_MANAGE_PATH = USER_PATH+"/manage";
    public static final String TASKS_PATH = ROOT_APP_PATH+"/tasks";
    public static final String TASK_PATH = TASKS_PATH+"/{id}";
    public static final String PROJECT_PATH = TASKS_PATH+"/project/{projectName}";
    public static final String ALL_TASKS_PATH = TASKS_PATH+"/all";
    public static final String ACTIVE_TASKS_PATH = TASKS_PATH+"/active";

    public static final String USER_CHANGE_PASS_PATH = USER_MANAGE_PATH+"/changepass";
    public static final String GALLERY_PATH = USER_PATH + "/{username}/galleries";
    public static final String GALLERY_ID_PATH = GALLERY_PATH+"/{galleryId}";

    public static final String X_ENS_REQUEST_ID = "X-ENS-Request-Id";
    public static final String X_ENS_SESSION_ID = "X-ENS-Session-Id";
    public static final String HEADER_X_ON_BEHALF_OF = "X-On-Behalf-Of";
    public static final String HEADER_SET_COOKIE = "Set-Cookie";
    public static final String HEADER_COOKIE = "Cookie";

    public static final String BAD_REQUEST_DTO = "BAD INPUT REQUEST : ";

    public static final String MIME_TYPE_TEXT_PLAIN = "text/plain";
    public static final String MIME_TYPE_CSV = "text/csv";
    public static final String MIME_TYPE_PDF = "application/pdf";
    public static final String MIME_TYPE_JPG = "image/jpeg";
    public static final String MIME_TYPE_PNG = "image/png";
    public static final String MIME_TYPE_TIFF = "image/tiff";
    public static final String MIME_TYPE_ZIP = "application/zip";
    public static final String MIME_TYPE_RAR = "application/x-rar-compressed";
    public static final String MIME_TYPE_GZIP = "application/gzip";
    public static final String MIME_TYPE_TGZ = "application/x-tgz";

    public static final String USER_ROLE_ADMIN = "ROLE_ADMIN";
    public static final String USER_ROLE_SUPERUSER = "ROLE_SUPERUSER";
    public static final String USER_ROLE_USER = "ROLE_USER";
    public static final String USER_ROLE_GUEST = "ROLE_GUEST";

    public static final String NAME_REGEX = "^[a-zA-Z0-9]*$";
    public static final Integer USERNAME_MIN_LENGTH = 3;
    public static final Integer USERNAME_MAX_LENGTH = 20;
    public static final String USERNAME_LENGTH_ERROR_MESSAGE = " Username can not be less than "
            + USERNAME_MIN_LENGTH + " and larger than " + USERNAME_MAX_LENGTH
            + " symbols";
    public static final String EMAIL_REGEX = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?!-)(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
    public static final String USERPHONENUMBER_REGEX = "^\\+(?:[0-9] ?){6,14}[0-9]$"; // international phone numbers
                                                                                      // based on ITU-T standards
    public static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
    public static final String PASSWORD_NOTICE = "Password must contain at least one digit [0-9];"
            + "least one lowercase Latin character [a-z];" + "least one uppercase Latin character [A-Z];"
            + "least one special character like ! @ # & ( );"
            + "a length of at least 8 characters and a maximum of 20 characters.";
    public static final String USERNAME_CANNOT_BE_NULL = "Username can not be null";
    public static final String PASSWORD_CANNOT_BE_NULL = "Password can not be null";


    public static final long EXPIRATION_TIME = 12960; // 9 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String JWT_TOKEN_HEADER = "Authorization";
    public static final String JWT_TOKEN_SUBJECT = "User details";
    public static final String TOKEN_CANNOT_BE_VERIFIED = "Token Can not be verified";
    public static final String INVALID_JWT_TOKEN = "invalid jwt-token in Bearer header";
    public static final String LPAPP_ADMINISTRATION = "User management portal";
    public static final String TASK_API_APP = "task api app";
    public static final String AUTHORITIES = "Authorities";
    public static final String FORBIDDEN_MESSAGE = "You need to log on to acces this page";
    public static final String ACCESS_DENIED_MESSAGE = "You don't have permission to access this page";
    public static final String OPTIONS_HTTP_METHOD = "OPTIONS";
    
    
    public static final String USER_WITH_THIS_NAME_WAS_NOT_FOUND = "User with this name was not found";
    public static final String USER_NOT_FOUND = "User not found";
    public static final String USER_WAS_NOT_CREATED = "User was not created";
    public static final String WRONG_CREDENTIALS = "Wrong credentials";
    public static final String USER_DATA_WAS_NOT_ENCRYPTED =  "User data has not been not encrypted";
    public static final String DATA_HAS_NOT_BEEN_DECRYPTED = "Data has not been decrypted";
    public static final String FULL_AUTHENTICATION_REQUIRED = "Full authentication is required to access this page";
    public static final String ACCESS_DENIED = "/access_denied";
    public static final String JSON_INVALID_JWT = "{ \"error\": \"Unauthorized\", \"message\": \""+AppConstants.INVALID_JWT_TOKEN+"\"} ";
    public static final String FULL_AUTHENTICATION = "{ \"error\": \"Unauthorized\", \"message\": \""+"Full Authentication required "+"\"} ";
    public static final String PASSWORD_NOT_CHANGED = "Password change failed ";

    public static final String REQUEST_STATUS = "request status: ";
    public static final String DELETING_USER = "deleted user: ";
    public static final Object CURRENT_PASSWORD = "current password";
    public static final Object NEW_PASSWORD = "new password";
    public static final String GETTING_USER_INFO = "Getting info for user: ";
    public static final String USER_NOT_UPDATED = "User not updated: ";
    public static final String PHOTO_MOCK = "./ressources/static/BLANK.jpg"; // Paths.get(PHOTO_MOCK)
    public static final String COLLECTION_NOT_CREATED = "Collection not created";
    public static final String NO_OTP_NEEDED = "public_no_OTP";
    public static final String GALLERY_ID_SHOULD_BE_NUMERIC = "GalleryId should be int";
    public static final String DELETING_GALLERY = "deleted gallery: ";
    public static final String COLLECTION_NOT_FOUND = "Collection not found: ";
    public static final String INVALID_GALLERY_PASWORD = "Your gallery password is not valid, please contact your administrator";
    public static final String PHOTO_ADDED_TO_GALLERY = "Photo sucsessfully added to gallery: ";

}
