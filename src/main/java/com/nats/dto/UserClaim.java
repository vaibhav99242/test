package com.nats.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nats.model.PermissionModel;
import com.nats.model.PubSubModel;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * The class managing all the properties for NATS field in the JWT Token
 *
 * @author vaibhav-scaletech
 */
public class UserClaim {
    private static final long NO_LIMIT = -1;
    final String issuerAccount;
    final PubSubModel pub;
    final PubSubModel sub;
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public UserClaim(String issuerAccount, PubSubModel pub, PubSubModel sub) {
        this.issuerAccount = issuerAccount;
        this.pub = pub;
        this.sub = sub;
    }

    public static boolean nullOrEmpty(String s) {
        return s != null && !s.trim().isEmpty();
    }

    /**
     * @return JSON Object is returned by containing all the required user fields for JWT.
     * @throws JSONException, If the JSON Object, or it's structure is invalid.
     * @throws JsonProcessingException, Occurs when there is a mismatch between the expected Java type and the actual type encountered during deserialization.
     */
    public JSONObject toJson() throws JSONException, JsonProcessingException {
        final String TYPE = "user";
        final int VERSION = 2;

        JSONObject json = new JSONObject();

        if (nullOrEmpty(issuerAccount)) json.put("issuer_account", issuerAccount);

        if (pub != null) {
            final String jsonString = objectMapper.writeValueAsString(pub);
            json.put("pub", new JSONObject(jsonString));
        }

        if (sub != null) {
            final String jsonString = objectMapper.writeValueAsString(sub);
            json.put("sub", new JSONObject(jsonString));
        }

        json.put("type", TYPE);
        json.put("version", VERSION);
        json.put("subs", NO_LIMIT);
        json.put("data", NO_LIMIT);
        json.put("payload", NO_LIMIT);

        return json;
    }
}
