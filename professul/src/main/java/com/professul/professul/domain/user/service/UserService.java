package com.professul.professul.domain.user.service;

import com.professul.professul.domain.user.dto.ChangePasswordDto;
import com.professul.professul.domain.user.dto.JoinDTO;
import com.professul.professul.domain.user.entity.User;
import com.professul.professul.exception.EmailAlreadyExistsException;
import com.professul.professul.exception.UserModificationException;
import com.professul.professul.exception.UserNotFoundException;

import java.time.LocalDate;

public interface UserService {

    void joinProcess(JoinDTO joinDTO) throws EmailAlreadyExistsException;

    void modifyUserName(Long userId, String newName) throws UserNotFoundException, UserModificationException;

    void modifyUserPassword(Long userId, ChangePasswordDto changePasswordDto) throws UserNotFoundException, UserModificationException;

    User findUserById(Long userId) throws UserNotFoundException;

    Boolean checkPassword(Long userId, String password);

    void suspendUser(Long userId, LocalDate until) throws UserNotFoundException, UserModificationException;

    void activateUser(Long userId) throws UserNotFoundException, UserModificationException;

    void banUser(Long userId, String reason) throws UserNotFoundException, UserModificationException;

    void withdrawUser(Long userId) throws UserNotFoundException, UserModificationException;
}
