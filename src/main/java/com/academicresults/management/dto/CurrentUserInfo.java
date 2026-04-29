package com.academicresults.management.dto;

public class CurrentUserInfo {

    private String username;
    private String displayName;
    private String roleCode;

    public CurrentUserInfo(String username, String displayName, String roleCode) {
        this.username = username;
        this.displayName = displayName;
        this.roleCode = roleCode;
    }

    public String getUsername() {
        return username;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getRoleCode() {
        return roleCode;
    }
}