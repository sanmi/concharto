package com.tech4d.tsm.auth;

public class AuthConstants {
    public static final String SESSION_AUTH_USERNAME = "username";
    public static final String SESSION_AUTH_TARGET_URI = "targetURI";
    public static final String SESSION_AUTH_ROLES = "roles";
    public static final String[] PATTERN_REQUIRES_AUTHENTICATION = {"search","edit","admin","member","private"};
    //TODO this is simplistic, probably want some sort of map or properties file
    public static final String ROLE_EDIT = "edit";
    public static final String ROLE_ADMIN = "admin";
    public static final String[] PATTERN_REQUIRES_AUTHORIZATION = {ROLE_EDIT,ROLE_ADMIN};
}
