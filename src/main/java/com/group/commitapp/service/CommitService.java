package com.group.commitapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.commitapp.domain.CommitHistory;
import com.group.commitapp.domain.User;
import com.group.commitapp.dto.commit.CommitHistoryDTO;
import com.group.commitapp.dto.commit.CommitReviewDTO;
import com.group.commitapp.repository.CommitHistoryRepository;
import com.group.commitapp.repository.UserRepository;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommitService {

    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final CommitHistoryRepository commitHistoryRepository;
    private final UserRepository userRepository;
//    private final MemberRepository userRepository;

    public CommitService(CommitHistoryRepository commitHistoryRepository, UserRepository userRepository) {
        this.commitHistoryRepository = commitHistoryRepository;
        this.userRepository = userRepository;
    }

    public List<String> getTodayCommitUrls(String githubId) throws IOException {
        String url = "https://api.github.com/users/" + githubId + "/events";
        System.out.println("url: "+url);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Accept", "application/vnd.github.v3+json")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            assert response.body() != null;
            JsonNode events = objectMapper.readTree(response.body().string());
            List<String> commitUrls = new ArrayList<>();
            LocalDate today = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

//            System.out.println("today: "+today.format(formatter));
            for (JsonNode event : events) {
                if (event.get("type").asText().equals("PushEvent")) { //푸쉬만 가져오기
//                    String eventDate = event.get("created_at").asText().substring(0, 10);
                    // "created_at" 값을 가져옴
                    String utcDateStr = event.get("created_at").asText();
                    // UTC 날짜를 ZonedDateTime으로 변환
                    ZonedDateTime utcDate = ZonedDateTime.parse(utcDateStr);
                    // KST 시간대로 변환
                    ZonedDateTime kstDate = utcDate.withZoneSameInstant(ZoneId.of("Asia/Seoul"));
                    // 날짜 부분만 추출하여 문자열로 변환
                    String eventDate = kstDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    // 날짜 부분만 추출하여 문자열로 변환
                    if (today.format(formatter).equals(eventDate)) {//오늘꺼만 가져오기
//                        String repoName = event.get("repo").get("name").asText();
//                        String sha = event.get("payload").get("head").asText();
//                        String commitUrl = "https://github.com/" + repoName+ "/commit/" + sha;
                        commitUrls.add("https://github.com/"
                                + event.get("repo").get("name").asText()
                                + "/commit/"
                                + event.get("payload").get("head").asText()
                        );
                    }
                }
            }
            return commitUrls;
        }
    }



    public List<CommitHistory> getTodayCommitsByGithubId(String githubId) {
        return commitHistoryRepository.findTodayCommitsByGithubId(githubId);
    }
    public List<CommitHistory> getCommitsByGithubId(String githubId) {
        Optional<User> user = userRepository.findByGithubId(githubId);
        Optional.ofNullable(user.orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다.")));

        return commitHistoryRepository.findByUser(user);
    }

//    public CommitHistory saveCommitHistory(CommitHistoryDTO dto) {
//        User user = userRepository.findById(dto.getUserId())
//                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + dto.getUserId()));
//
//        CommitHistory commitHistory = new CommitHistory(dto, user);
//        return commitHistoryRepository.save(commitHistory);
//    }

//    @Autowired
//    private GPTService gptService;
    /**
     * GPT내용 추가 해야함
     * */

    public CommitHistory addReviewToCommit(String githubId) throws IOException {
        // 커밋 히스토리를 데이터베이스에서 조회합니다.
//        CommitHistory commitHistory = commitHistoryRepository.findById(commitHistoryId)
//                .orElseThrow(() -> new IllegalArgumentException("Invalid commit history ID: " + commitHistoryId));
//        System.out.println("githubId: "+githubId);
        User user = userRepository.findByGithubId(githubId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid github ID: " + githubId));

        // GPT API를 호출하여 리뷰를 받습니다.
//        String review = gptService.getReview(commitHistory.getDescription());
        String githubLink = getTodayCommitUrls(githubId).get(0);
        String title = "한줄 요약 제목";
        String description = githubLink + "의 피드백 내용이 들어갈거임";
        //커밋이력은 여러개일수도 있지만, DB에는 1개만 넣는다고 가정할게요

        CommitHistory commitHistory = new CommitHistory(
                new CommitHistoryDTO(title, description, githubLink, githubId)
                , user
        );

        // 커밋 히스토리에 리뷰를 추가합니다.
        // 변경 사항을 데이터베이스에 저장합니다.
        return commitHistoryRepository.save(commitHistory);
    }
}
