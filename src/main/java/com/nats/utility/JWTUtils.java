package com.nats.utility;

import com.nats.utility.GetSecrets;
import com.nats.utility.IssueJWT;
import com.nats.constant.Constant;
import com.nats.dto.UserClaim;
import com.nats.exception.CustomException;
import com.nats.model.RequestModel;
import io.nats.client.NKey;
import java.io.BufferedWriter;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * The utility class managing the response parameters as a JSON Object
 *
 * @author vaibhav-scaletech
 */
public class JWTUtils {

//    private static final String ACCOUNT_ID =
//            GetSecrets.getKey(Constant.PROJECT_ID, Constant.ACCOUNT_ID, Constant.ACCOUNT_ID_VERSION);
//    private static final String ACCOUNT_SEED =
//            GetSecrets.getKey(Constant.PROJECT_ID, Constant.SECRET_ID, Constant.SECRET_ID_VERSION);

    private static final String ACCOUNT_ID = "ABE2OHWTEY6V5E26B7I2GUSGPEKBX47H3EM5EWFFOG3QBOGEI2AWXAKO";
    private static final String ACCOUNT_SEED = "SAANBKHSZQF67ONWNALORXOFIMU37NXI4IXCEXSTORV4JNVX2RYM2YIK74";

    /**
     * Default constructor to deny the object creation of Handler class, The class can have only static members.
     */
    private JWTUtils() {
        throw new CustomException(Constant.UTILITY_CLASS_ERROR);
    }

    /**
     * @return current time converted in seconds
     */
    public static long currentTimeSeconds() {
        return System.currentTimeMillis() / 1000;
    }

    /**
     * @param requestModel, referring The Request Model, by containing userId, pub and sub as a permission objects.
     * @param writer, write() in BufferedWriter object writing characters to a file in a buffered way.
     */
    public static void generateResponse(
            RequestModel requestModel, BufferedWriter writer) {

        try {
            final NKey nKeyAccount = NKey.fromSeed(ACCOUNT_SEED.toCharArray());
            final NKey user = NKey.createUser(new SecureRandom());
            final String publicUserKey = new String(user.getPublicKey());
            final LocalDateTime currentDateTime = LocalDateTime.now();
            final Duration expiration = Duration.ofMillis((long) Constant.EXPIRES);
            LocalDateTime newDateTime = currentDateTime.plus(expiration);

            final UserClaim claim = getClaim(requestModel);

            final String jwt =
                    IssueJWT.issueJWT(
                            nKeyAccount,
                            publicUserKey,
                            requestModel.getUserId(),
                            expiration,
                            currentTimeSeconds(),
                            "NATS",
                            claim);

            final String responseObject =
                    new JSONObject()
                            .put("jwt", jwt)
                            .put("userSeed", new String(user.getSeed()))
                            .put("expiry", newDateTime)
                            .toString();

            writer.write(responseObject);

        } catch (IOException | GeneralSecurityException | JSONException e) {
            throw new CustomException(e.getMessage());
        }
    }

    /**
     * @param requestModel, referring The Request Model, by containing userId, pub and sub as a permission objects.
     * @return UserClaim object contains all the fields required to generate and sign a JWT.
     */
    public static UserClaim getClaim(final RequestModel requestModel) {
        return new UserClaim(ACCOUNT_ID, requestModel.getPermissions().getPub(), requestModel.getPermissions().getSub());
    }
}
