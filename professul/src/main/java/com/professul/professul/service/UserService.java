package com.professul.professul.service;

import com.professul.professul.dto.JoinDTO;
import com.professul.professul.dto.ModifyUserDto;

public interface UserService {

    void joinProcess(JoinDTO joinDTO) throws Exception;

    void modifyUser(ModifyUserDto modifyUserDto) throws Exception;
}
