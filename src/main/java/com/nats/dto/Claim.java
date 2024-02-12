package com.nats.dto;

import com.nats.dto.UserClaim;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.nats.exception.CustomException;
import java.time.Duration;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class manages all the required properties of JWT Token
 *
 * @author vaibhav-scaletech
 */
public class Claim {
    private String aud;
    private String jti;
    private long iat;
    private String iss;
    private String userId;
    private String sub;
    private Duration exp;
    private UserClaim nats;

    public void setAud(String aud) {
        this.aud = aud;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public void setIat(long iat) {
        this.iat = iat;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public void setExp(Duration exp) {
        this.exp = exp;
    }

    public void setNats(UserClaim nats) {
        this.nats = nats;
    }

    /**
     * @return JSON Object with all the required properties of creation and sign JWT.
     */
    @Override
    public String toString() {

        final JSONObject json = new JSONObject();

        try {
            json.put("aud", aud);
            json.put("jti", jti);
            json.put("iat", iat);
            json.put("iss", iss);
            json.put("user_id", userId);
            json.put("sub", sub);

            if (exp != null && !exp.isZero() && !exp.isNegative()) {
                final long seconds = exp.toMillis() / 1000;
                json.put("exp", iat + seconds); // relative to the iat
            }

            json.put("nats", nats.toJson());

        } catch (JSONException | JsonProcessingException e) {
            throw new CustomException(e.getMessage());
        }

        return json.toString();
    }
}
