package com.nats.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nats.model.PermissionModel;

/**
 * Record class for mapping HttpRequestBody with RequestModel.
 *
 * @author vaibhav-scaletech
 */
public class RequestModel {
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("permissions")
    private PermissionModel permissions;

    public String getUserId() {
        return userId;
    }

    public PermissionModel getPermissions() {
        return permissions;
    }

    public void setPermissions(PermissionModel permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "RequestModel{" +
                "userId='" + userId + '\'' +
                ", permissions=" + permissions +
                '}';
    }
}
