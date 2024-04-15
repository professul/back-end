package com.professul.professul.domain.user.service;

import com.professul.professul.domain.user.dto.ChangePasswordDto;
import com.professul.professul.domain.user.dto.JoinDTO;
import com.professul.professul.domain.user.entity.User;

public interface UserService {

    void joinProcess(JoinDTO joinDTO) throws Exception;

    User modifyUserName(Long userId, String newName) throws Exception;

    void modifyUserPassword(Long userId, ChangePasswordDto changePasswordDto) throws Exception;

    User findUserById(Long userId) throws Exception;

    Boolean checkPassword(Long userId, String password);

    void deactivateUser(Long userId) throws Exception;

}
