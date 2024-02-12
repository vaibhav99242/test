package com.nats.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.nats.model.PubSubModel;

/**
 * The class for mapping allow and deny permissions for Publisher and Subscriber.
 *
 * @author vaibhav-scaletech
 */
public class PermissionModel {
    @JsonProperty("pub")
    private PubSubModel pub;
    @JsonProperty("sub")
    private PubSubModel sub;

    public PubSubModel getPub() {
        return this.pub;
    }

    public void setPub(PubSubModel pub) {
        this.pub = pub;
    }

    public PubSubModel getSub() {
        return this.sub;
    }

    public void setSub(PubSubModel sub) {
        this.sub = sub;
    }

    @Override
    public String toString() {
        return "PermissionModel{" +
                "pub=" + pub +
                ", sub=" + sub +
                '}';
    }
}