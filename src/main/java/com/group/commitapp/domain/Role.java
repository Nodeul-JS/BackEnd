package com.group.commitapp.domain;

public enum Role {
    ROLE_USER("ROLE_USER"),
    ROLE_ADMIN("ROLE_ADMIN");

    private String value;

    Role(String auth) {
        this.value = auth;
    }

    public String getValue() {
        return value;
    }
}