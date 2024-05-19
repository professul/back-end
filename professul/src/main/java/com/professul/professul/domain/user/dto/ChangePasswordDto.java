package com.professul.professul.domain.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordDto {

    private String currentPassword;
    private String newPassword;
    private String confirmPassword;

    public ChangePasswordDto(String currentPassword, String newPassword, String confirmPassword) {
        this.currentPassword=currentPassword;
        this.newPassword=newPassword;
        this.confirmPassword=confirmPassword;
    }
}
