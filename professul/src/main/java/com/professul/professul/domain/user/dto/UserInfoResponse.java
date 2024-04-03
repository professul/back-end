package com.professul.professul.domain.user.dto;

public class UserInfoResponse {
    private String name;
    private Long userId;

    public UserInfoResponse(String name, Long userId) {
        this.name = name;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setUserName(String name) {
        this.name = name;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
