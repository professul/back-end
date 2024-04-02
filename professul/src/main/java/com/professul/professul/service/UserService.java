package com.professul.professul.service;

import com.professul.professul.dto.JoinDTO;
import com.professul.professul.dto.ModifyUserDto;
import com.professul.professul.entity.User;

public interface UserService {

    void joinProcess(JoinDTO joinDTO) throws Exception;

    User modifyUser(Long userId, ModifyUserDto modifyUserDto) throws Exception;
}
