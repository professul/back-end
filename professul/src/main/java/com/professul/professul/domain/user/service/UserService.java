package com.professul.professul.domain.user.service;

import com.professul.professul.domain.user.dto.JoinDTO;
import com.professul.professul.domain.user.dto.ModifyUserDto;
import com.professul.professul.domain.user.entity.User;

public interface UserService {

    void joinProcess(JoinDTO joinDTO) throws Exception;

    User modifyUserName(Long userId, String newName) throws Exception;

    User modifyUserPassword(Long userId, String newPassword) throws Exception;

    User findUserById(Long userId) throws Exception;

    Boolean checkPassword(Long userId, String password);
}
