package com.nats.utility;

import com.google.cloud.secretmanager.v1.AccessSecretVersionResponse;
import com.google.cloud.secretmanager.v1.SecretManagerServiceClient;
import com.google.cloud.secretmanager.v1.SecretVersion;
import com.google.cloud.secretmanager.v1.SecretVersionName;
import com.nats.constant.Constant;
import com.nats.exception.CustomException;
import java.io.IOException;

/**
 * For getting secrets(Account Public Key, Account Seed Key) from Secret Manager
 *
 * @author vaibhav-scaletech
 */
public class GetSecrets {
    /**
     * Default constructor to deny the object creation of Handler class, The class can have only static members.
     */
    private GetSecrets() {
        throw new CustomException(Constant.UTILITY_CLASS_ERROR);
    }

    /**
     * @param projectId, Google cloud projectId where the secret's has created.
     * @param secretId, Google cloud secretId which needs to access.
     * @param secretVersion, Google cloud secret's version to access.
     * @return Secret's will be returned from a Google Cloud as a String if Authentication happened.
     */
    public static String getKey(
            final String projectId, final String secretId, final String secretVersion) {

        try (final SecretManagerServiceClient client = SecretManagerServiceClient.create()) {
            final SecretVersionName secretVersionName =
                    SecretVersionName.of(projectId, secretId, secretVersion);
            // Parameters are ProjectId, SecretId and VersionId

            final SecretVersion version = client.getSecretVersion(secretVersionName);
            final AccessSecretVersionResponse response = client.accessSecretVersion(version.getName());

            return response.getPayload().getData().toStringUtf8();
        } catch (IOException e) {
            throw new CustomException(e);
        }
    }
}
