package com.nats.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 * The class provides allow and deny permission list's for publisher and subscriber
 */
public class PubSubModel {
    @JsonProperty("allow")
    private List<String> allow;
    @JsonProperty("deny")
    private List<String> deny;

    public List<String> getAllow() {
        return allow;
    }
    public void setAllow(List<String> allow) {
        this.allow = allow;
    }
    public List<String> getDeny() {
        return deny;
    }
    public void setDeny(List<String> deny) {
        this.deny = deny;
    }

    @Override
    public String toString() {
        return "PubSubModel{" +
                "allow=" + allow +
                ", deny=" + deny +
                '}';
    }
}
