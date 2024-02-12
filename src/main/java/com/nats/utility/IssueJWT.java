package com.nats.utility;

import com.nats.constant.Constant;
import com.nats.dto.Claim;
import com.nats.dto.UserClaim;
import com.nats.exception.CustomException;
import io.nats.client.NKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.time.Duration;
import static io.nats.client.support.Encoding.base32Encode;
import static io.nats.client.support.Encoding.toBase64Url;

/**
 * For creating and encoding JWT Header, Payload and Signature
 *
 * @author vaibhav-scaletech
 */
public class IssueJWT {

    /**
     * Default constructor to deny the object creation of Handler class, The class can have only static members.
     */
    private IssueJWT() {
        throw new CustomException(Constant.UTILITY_CLASS_ERROR);
    }

    private static final String ENCODED_CLAIM_HEADER =
            toBase64Url("{\"typ\":\"JWT\", \"alg\":\"ed25519-nkey\"}");

    /**
     * @param signingKey, Account signingKey to sign created JWT.
     * @param publicUserKey, Account's public key is used to identify the principal that is the subject of the JWT.
     * @param userId, Unique identifier associated with a user.
     * @param expiration, Till the time JWT is valid.
     * @param issuedAt, The time when JWT was issued.
     * @param audience, The audience represents the recipients or entities for which the JWT is intended to be processed.
     * @param nats, NATS User properties.
     * @return Generated and Signed new JSON Web Token will be returned.
     * @throws GeneralSecurityException, Thrown during the creation and verification of the JWT signature
     */
    public static String issueJWT(
            NKey signingKey,
            String publicUserKey,
            String userId,
            Duration expiration,
            long issuedAt,
            String audience,
            UserClaim nats)
            throws GeneralSecurityException, IOException {

        final Claim claim = new Claim();
        claim.setAud(audience);
        claim.setIat(issuedAt);
        claim.setIss(new String(signingKey.getPublicKey()));
        claim.setUserId(userId);
        claim.setSub(publicUserKey);
        claim.setExp(expiration);
        claim.setNats(nats);

        final MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
        final byte[] encoded = sha256.digest(claim.toString().getBytes(StandardCharsets.US_ASCII));
        claim.setJti(new String(base32Encode(encoded)));

        final String encBody = toBase64Url(claim.toString());

        final byte[] sig = (ENCODED_CLAIM_HEADER + Constant.JWT_SEPARATOR + encBody).getBytes(StandardCharsets.UTF_8);

        final String encSig = toBase64Url(signingKey.sign(sig));

        return ENCODED_CLAIM_HEADER + Constant.JWT_SEPARATOR + encBody + Constant.JWT_SEPARATOR + encSig;
    }
}
