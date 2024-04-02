package com.professul.professul.service;

import com.professul.professul.domain.user.dto.JoinDTO;
import com.professul.professul.domain.user.dto.ModifyUserDto;
import com.professul.professul.domain.user.entity.User;

public interface UserService {

    void joinProcess(JoinDTO joinDTO) throws Exception;

    User modifyUser(Long userId, ModifyUserDto modifyUserDto) throws Exception;
}
