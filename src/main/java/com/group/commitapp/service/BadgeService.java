package com.group.commitapp.service;

import com.group.commitapp.domain.Badge;
import com.group.commitapp.domain.BadgeHistory;
import com.group.commitapp.domain.User;
import com.group.commitapp.dto.badge.findBadgeDTO;
import com.group.commitapp.dto.badge.findBadgeListDTO;
import com.group.commitapp.repository.BadgeHistoryRepository;
import com.group.commitapp.repository.BadgeRepository;
import com.group.commitapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BadgeService {
private final BadgeHistoryRepository badgeHistoryRepository;
private final BadgeRepository badgeRepository;
private final UserRepository userRepository;


    @Transactional
    public List<findBadgeListDTO> findBadgeList(String githubId) { // 변수명 오류 수정: usersid -> usersId
        // 사용자 정보 조회
        User user = userRepository.findByGithubId(githubId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: There is No ID. Here is Service"));

        // 사용자의 뱃지 기록 조회
        List<BadgeHistory> badgeHistories = badgeHistoryRepository.findAllByUser(user);

        // 뱃지 기록에 해당하는 뱃지들을 조회하고 DTO 리스트로 변환
        List<findBadgeListDTO> badgeDTOs = new ArrayList<>();
        for (BadgeHistory badgeHistory : badgeHistories) {
            Badge badge = badgeRepository.findById(badgeHistory.getBadge().getBadgeId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid reward ID: Badge not found. Here is Service"));
            badgeDTOs.add(new findBadgeListDTO(badge, badgeHistory));
        }

        return badgeDTOs;
    }


    @Transactional
    public findBadgeDTO findBadge(Long badgeId) // find one!! Badge
    {
        Badge badge = badgeRepository.findById(badgeId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid badge ID!! : There is No ID. Here is Service"));
        return new findBadgeDTO(badge);

    }

    @Transactional
    public void createLeaderBadge(User user, Long badgeId) {
        Badge badge = badgeRepository.findById(badgeId) // 그룹 리더 뱃지 Id = 1
                .orElseThrow(() -> new IllegalArgumentException("Invalid badge ID!! : There is No ID. Here is Service"));

        if (badgeHistoryRepository.findByUserAndBadge(user, badge).isPresent()) {
            return;
        }

        // Create and save BadgeHistory
        BadgeHistory badgeHistory = BadgeHistory.saveBadgeHistory(user, badge);
        badgeHistoryRepository.save(badgeHistory);
    }

    @Transactional
    public void createStreakBadge(Long userId, Long badgeId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID!! : There is No ID. Here is Service"));
        Badge badge = badgeRepository.findById(badgeId) // 연속 커밋 뱃지 Id = 2
                .orElseThrow(() -> new IllegalArgumentException("Invalid badge ID!! : There is No ID. Here is Service"));
        if (badgeHistoryRepository.findByUserAndBadge(user, badge).isPresent()) {
            return;
        }
        // Create and save BadgeHistory
        BadgeHistory badgeHistory = BadgeHistory.saveBadgeHistory(user, badge);
        badgeHistoryRepository.save(badgeHistory);
    }

    @Transactional
    public void createDeadBadge(Long userId, Long badgeId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID!! : There is No ID. Here is Service"));
        Badge badge = badgeRepository.findById(badgeId) // Dead Badge ID :3
                .orElseThrow(() -> new IllegalArgumentException("Invalid badge ID!! : There is No ID. Here is Service"));
        if (badgeHistoryRepository.findByUserAndBadge(user, badge).isPresent()) {
            return;
        }
        // Create and save BadgeHistory
        BadgeHistory badgeHistory = BadgeHistory.saveBadgeHistory(user, badge);
        badgeHistoryRepository.save(badgeHistory);
    }




}
