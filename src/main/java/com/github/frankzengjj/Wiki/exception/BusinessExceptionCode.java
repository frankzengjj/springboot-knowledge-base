package com.github.frankzengjj.Wiki.exception;

public enum BusinessExceptionCode {

    USER_LOGIN_NAME_EXIST("Login Name Already Exists"),
    LOGIN_USER_ERROR("Incorrect Login Name or Password"),
    VOTE_REPEAT("You already liked this"),
    ;

    private String desc;

    BusinessExceptionCode(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
