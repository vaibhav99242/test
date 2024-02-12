package com.nats.utility;

import com.nats.utility.JWTUtils;
import com.nats.utility.Validator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.functions.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.nats.constant.Constant;
import com.nats.exception.CustomException;
import com.nats.model.RequestModel;
import java.io.BufferedWriter;

/**
 * This class provides a logic to validate a HttpRequest object and Mapping the request
 *
 * @author vaibhav-scaletech
 */
public class Handler {

    /**
     * Default constructor to deny the object creation of Handler class, The class can have only static members.
     */
    private Handler() {
        throw new CustomException(Constant.UTILITY_CLASS_ERROR);
    }

    private static final Gson gson = new Gson();

    /**
     * @param request, HttpRequest request contains the request json object.
     * @param writer, write() in BufferedWriter object writing characters to a file in a buffered way.
     */
    public static void getRequestBody(HttpRequest request, BufferedWriter writer) {

        try {
            if (request.getContentType().orElse("").equals("application/json")) {

                JsonElement jsonElement = Validator.validateRequest(request);

                final JsonObject body = gson.fromJson(jsonElement, JsonObject.class);
                final RequestModel requestModel =
                        new ObjectMapper().readValue(body.toString(), RequestModel.class);

                Validator.validatePermission(requestModel);
                JWTUtils.generateResponse(requestModel, writer);

            } else throw new CustomException(Constant.CONTENT_TYPE_NOT_VALID);

        } catch (Exception e) {
            throw new CustomException(e.getMessage());
        }
    }
}
