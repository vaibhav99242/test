package com.nats.utility;

import com.google.cloud.functions.HttpRequest;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nats.constant.Constant;
import com.nats.exception.CustomException;
import com.nats.model.PermissionModel;
import com.nats.model.PubSubModel;
import com.nats.model.RequestModel;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class validates HttPRequest object and throwing custom exceptions
 *
 * @author vaibhav-scaletech
 */
public class Validator {

    /**
     * Default constructor to deny the object creation of Handler class, The class can have only static members.
     */
    private Validator() {
        throw new CustomException(Constant.UTILITY_CLASS_ERROR);
    }

    /**
     * @param request, HttpRequest request contains the request json object.
     * @return JsonElement will be returned which are Parsed from HttpRequest.
     */
    public static JsonElement validateRequest(HttpRequest request) throws IOException {
        // Read request body
        final StringBuilder requestBodyBuilder = new StringBuilder();
        try (final BufferedReader reader = new BufferedReader(
                new InputStreamReader(request.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                requestBodyBuilder.append(line);
            }
        }

        final String requestBody = requestBodyBuilder.toString();
        final JsonElement jsonElement;

        try {
            jsonElement = JsonParser.parseString(requestBody);
        } catch (Exception e){
            throw new CustomException(Constant.MALFORMED_JSON_REQUEST + e.getMessage());
        }

        if(!jsonElement.isJsonObject())
            throw new CustomException(Constant.REQUEST_CONTENT_MUST_BE_JSON);

        final JsonObject jsonObject = jsonElement.getAsJsonObject();

        if (jsonObject.isEmpty() ||
                !jsonObject.has(Constant.USER_ID) ||
                    jsonObject.get(Constant.USER_ID).isJsonNull())
            throw new CustomException(Constant.USER_ID_MANDATORY);


        if(jsonObject.get(Constant.USER_ID)
                .toString()
                    .replace("\"", "")
                        .trim()
                            .isEmpty())
            throw new CustomException(Constant.USER_ID_CAN_NOT_EMPTY);

        return jsonElement;
    }

    public static void validatePermission(RequestModel requestModel) {

        if(requestModel.getPermissions() == null){
            setDefaultPubSub(requestModel);
            return;
        }

        final PermissionModel permissions = requestModel.getPermissions();

        if (permissions.getPub() == null)
            requestModel.getPermissions().setPub(getDefaultPublisher());
        else if (permissions.getPub().getAllow() == null
                    || permissions.getPub().getDeny() == null)
            throw new CustomException(Constant.INVALID_PUBLISHER_PERMISSION_OBJECT);


        if (permissions.getSub() == null)
            requestModel.getPermissions().setSub(getDefaultSubscriber(requestModel.getUserId()));
        else if (permissions.getSub().getAllow() == null
                    || permissions.getSub().getDeny() == null)
            throw new CustomException(Constant.INVALID_SUBSCRIBER_PERMISSION_OBJECT);
    }

    private static void setDefaultPubSub(RequestModel requestModel){
        final PermissionModel permissionModel = new PermissionModel();
        permissionModel.setPub(getDefaultPublisher());
        permissionModel.setSub(getDefaultSubscriber(requestModel.getUserId()));
        requestModel.setPermissions(permissionModel);
    }
    private static PubSubModel getDefaultPublisher() {
        final PubSubModel publisher = new PubSubModel();
        publisher.setAllow(new ArrayList<>());
        publisher.setDeny(new ArrayList<>(List.of("*")));
        return publisher;
    }

    private static PubSubModel getDefaultSubscriber(String userId) {
        final PubSubModel subscriber = new PubSubModel();
        subscriber.setAllow(new ArrayList<>(List.of(userId)));
        subscriber.setDeny(new ArrayList<>());
        return subscriber;
    }
}
