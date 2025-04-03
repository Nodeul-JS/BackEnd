package com.group.commitapp.service;
import com.group.commitapp.common.enums.CustomResponseStatus;
import com.group.commitapp.common.exception.CustomException;
import com.group.commitapp.domain.User;
import com.group.commitapp.dto.user.UserInfoResponse;
import com.group.commitapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserInfoResponse> getAllUser() {
        List<User> users =  userRepository.findAll();
        return UserInfoResponse.fromList(users);
    }

    @Transactional(readOnly = true)
    public UserInfoResponse getUserByGithubId(String githubId) {
        User user = userRepository.findByGithubId(githubId)
                .orElseThrow(() -> new CustomException(CustomResponseStatus.MEMBER_NOT_FOUND));
        return UserInfoResponse.from(user);
    }
    @Transactional
    public void giveExperienceByUser(User user) {
        user.addExperience(10);
    }
}
