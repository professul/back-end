package com.professul.professul.domain.user.service;

import com.professul.professul.domain.user.dto.ChangePasswordDto;
import com.professul.professul.domain.user.dto.JoinDTO;
import com.professul.professul.domain.user.entity.User;
import com.professul.professul.domain.user.entity.UserRole;
import com.professul.professul.review.entity.Review;
import com.professul.professul.util.PageInfo;

import java.util.List;

public interface UserService {

    void joinProcess(JoinDTO joinDTO) throws Exception;

    User modifyUserName(Long userId, String newName) throws Exception;

    void modifyUserPassword(Long userId, ChangePasswordDto changePasswordDto) throws Exception;

    User findUserById(Long userId) throws Exception;

    Boolean checkPassword(Long userId, String password);

    void suspendUser(Long userId) throws Exception;

    void banUser(Long userId) throws Exception;

    void withdrawUser(Long userId) throws Exception;

    List<Review> getReviewListByUser(Long userId, PageInfo pageInfo) throws Exception;

}
