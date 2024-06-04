package com.group.commitapp.service;
import com.group.commitapp.domain.User;
import com.group.commitapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;


    public User getUserByGithubId(String githubId) {
        return userRepository.findByGithubId(githubId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: There is No ID. Here is Service"));
    }
}
