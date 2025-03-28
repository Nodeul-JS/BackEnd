package com.group.commitapp.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.group.commitapp.repository.CommitHistoryRepository;
import com.group.commitapp.repository.UserRepository;
//import com.group.commitapp.service.external.GptService;
//import com.group.commitapp.service.external.BadgeService;
//import com.group.commitapp.service.external.UserService;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
class CommitServiceTest {

    @Mock CommitHistoryRepository commitHistoryRepository;
    @Mock UserRepository userRepository;
    @Mock GptService gptService;
    @Mock BadgeService badgeService;
    @Mock UserService userService;

    // 실제 HTTP 호출이 일어나므로 여기선 통합성 있는 테스트
    CommitService commitService;

    @BeforeEach
    void setUp() {
        String accessToken = "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"; // 진짜 또는 테스트용 토큰
        commitService = new CommitService(
                commitHistoryRepository,
                userRepository,
                accessToken,
                gptService,
                badgeService,
                userService
        );
    }

    @Test
    void getTodayCommitUrls_실제호출_성공() throws IOException {
        // given
        String githubId = "ryuwldnjs";

        // when
        List<List<String>> result = commitService.getTodayCommitUrls(githubId);

        // then
        assertNotNull(result);
        System.out.println("✔ 결과 개수: " + result.size());
        result.forEach(urls ->
                System.out.println("commitUrl: " + urls.get(0) + ", apiUrl: " + urls.get(1))
        );
    }
}