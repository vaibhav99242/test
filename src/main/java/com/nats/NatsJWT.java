package com.nats;

import com.nats.utility.Handler;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.nats.exception.CustomException;
import org.json.JSONObject;

import java.io.BufferedWriter;

/**
 * Entry point of Google Cloud Function with HttpRequest object.
 * Cloud function will only call the class if request is Authorized with valid API-KEY.
 *
 * @author vaibhav-scaletech
 */
public class NatsJWT implements HttpFunction {

    /**
     * This method will accept the request and send to Handler for validations
     * Also handles all the exception's and throwing custom exception
     *
     * @param request, HttpRequest request contains the request json object
     * @param response, HttpResponse response will be returned to Endpoint request
     * @throws Exception, Custom Exception has handled with the application
     */
    @Override
    public void service(final HttpRequest request, final HttpResponse response) throws Exception {
        final BufferedWriter writer = response.getWriter();
        try {
            Handler.getRequestBody(request, writer);
        } catch (CustomException e) {
            JSONObject errorResponse = new JSONObject();
            errorResponse.put("code",422);
            errorResponse.put("message",e.getMessage());
            writer.write(errorResponse.toString());
        }
    }
}
