package com.group.commitapp.service;


import com.group.commitapp.repository.BadgeHistoryRepository;
import com.group.commitapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GitService {
    private final UserRepository userRepository;
    private final BadgeHistoryRepository badgeHistoryRepository; // git 스트릭 유지시 뱃지 update



}
