package com.group.commitapp.service;

import com.group.commitapp.common.enums.CustomResponseStatus;
import com.group.commitapp.common.exception.CustomException;
import com.group.commitapp.domain.Badge;
import com.group.commitapp.domain.BadgeHistory;
import com.group.commitapp.domain.User;
import com.group.commitapp.dto.badge.BadgeInfoResponse;
import com.group.commitapp.dto.badge.findBadgeListResponse;
import com.group.commitapp.repository.BadgeHistoryRepository;
import com.group.commitapp.repository.BadgeRepository;
import com.group.commitapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BadgeService {
private final BadgeHistoryRepository badgeHistoryRepository;
private final BadgeRepository badgeRepository;
private final UserRepository userRepository;


    @Transactional(readOnly = true)
    public List<findBadgeListResponse> findBadgeList(String githubId) { // 변수명 오류 수정: usersid -> usersId
        // 사용자 정보 조회
        User user = userRepository.findByGithubId(githubId)
                .orElseThrow(() -> new CustomException(CustomResponseStatus.MEMBER_NOT_FOUND));
        // 사용자의 뱃지 기록 조회
        return findBadgeListResponse.fromList(badgeHistoryRepository.findAllByUser(user));
    }


    @Transactional(readOnly = true)
    public BadgeInfoResponse findBadge(Long badgeId){ // find one!! Badge
        Badge badge = badgeRepository.findById(badgeId) // 그룹 리더 뱃지 Id = 1
                .orElseThrow(() -> new CustomException(CustomResponseStatus.BADGE_NOT_FOUND));
        return BadgeInfoResponse.from(badge);
    }

    @Transactional
    public void createLeaderBadge(User user) {
        Badge badge = badgeRepository.findById(1L) // 그룹 리더 뱃지 Id = 1
                .orElseThrow(() -> new CustomException(CustomResponseStatus.BADGE_NOT_FOUND));

        //이미 뱃지를 받은 이력이 있으면
        if (badgeHistoryRepository.findByUserAndBadge(user, badge).isPresent()) {
            return;
        }

        // Create and save BadgeHistory
        BadgeHistory badgeHistory = BadgeHistory.create(user, badge);
        badgeHistoryRepository.save(badgeHistory);
    }


    /**
     * 아래 코드들은 현재 사용X, 추후 개편
     * */
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
        BadgeHistory badgeHistory = BadgeHistory.create(user, badge);
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
        BadgeHistory badgeHistory = BadgeHistory.create(user, badge);
        badgeHistoryRepository.save(badgeHistory);
    }




}
