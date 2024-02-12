package com.nats.constant;

import com.nats.exception.CustomException;

/**
 * Constant's for clean code
 *
 * @author vaibhav-scaletech
 */
public class Constant {
    public static final String UTILITY_CLASS_ERROR = "Can't create an object of utility class";
    public static final String CONTENT_TYPE_NOT_VALID = "Request body content type is not valid";
    public static final String USER_ID_MANDATORY = "User Id is mandatory";
    public static final String USER_ID_CAN_NOT_EMPTY = "User Id cannot be empty";
    public static final String REQUEST_CONTENT_MUST_BE_JSON = "Request content must be JSON Object";
    public static final String MALFORMED_JSON_REQUEST = "Malformed json request : ";
    public static final String INVALID_PUBLISHER_PERMISSION_OBJECT = "Invalid Publisher Permission Object, It must contains Allow and Deny permissions";
    public static final String INVALID_SUBSCRIBER_PERMISSION_OBJECT = "Invalid Subscriber Permission Object, It must contains Allow and Deny permissions";
    public static final String JWT_SEPARATOR = ".";
    public static final String USER_ID = "userId";
    public static final double EXPIRES = 8.64e+7;
//    public static final String PROJECT_ID = "dorado-412107";
    public static final String PROJECT_ID = "dorado-friendlist";
    public static final String ACCOUNT_ID = "NATS_ACCOUNT_ID";
    public static final String ACCOUNT_ID_VERSION = "1";
    public static final String SECRET_ID = "NATS_ACCOUNT_SEED";
    public static final String SECRET_ID_VERSION = "1";
    private Constant() {
        throw new CustomException(UTILITY_CLASS_ERROR);
    }
}
