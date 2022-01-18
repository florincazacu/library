package com.example.library.model.security;

import javax.persistence.Embeddable;

@Embeddable
public class Authority {

    private String permission;

    public Authority() {
    }

    public Authority(String permission) {
        this.permission = permission;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }
}
