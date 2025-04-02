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
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public List<UserInfoResponse> getAllUser() {
        return userRepository.findAll()
                .stream()
                .map(UserInfoResponse::new)
                .toList();
    }

    public User getUserByGithubId(String githubId) {
        return userRepository.findByGithubId(githubId)
                .orElseThrow(() -> new CustomException(CustomResponseStatus.MEMBER_NOT_FOUND));
    }

    public void giveExperienceByUser(User user) {
        user.addExperience(10);
    }
}
