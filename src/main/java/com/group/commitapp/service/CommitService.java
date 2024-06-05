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
import org.springframework.beans.factory.annotation.Value;
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
    private final String accessToken;
    private GptService gptService;

    public CommitService(CommitHistoryRepository commitHistoryRepository
            , UserRepository userRepository
            ,@Value("${oauth2.user.github.access-token}") String accessToken, GptService gptService) {
        this.commitHistoryRepository = commitHistoryRepository;
        this.userRepository = userRepository;
        this.accessToken = accessToken;
        this.gptService = gptService;
    }

    public List<String> getTodayCommitUrls(String githubId) throws IOException {
        String url = "https://api.github.com/users/" + githubId + "/events";
        System.out.println("url: "+url);
//        System.out.println(accessToken);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("authorization","Bearer " + accessToken)
                .addHeader("Accept", "application/vnd.github.v3+json")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36")
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
                        commitUrls.add("https://api.github.com/repos/"
                                + event.get("repo").get("name").asText()
                                + "/commit/"
                                + event.get("payload").get("head").asText()
                        );
//                        commitUrls.add("https://github.com/"
//                                + event.get("repo").get("name").asText()
//                                + "/commit/"
//                                + event.get("payload").get("head").asText()
//                        );

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
//        return commitHistoryRepository.findByUser(user);
        return commitHistoryRepository.findByUserOrderByHistoryIdAsc(user);
    }

//    @Autowired
//    private GPTService gptService;
    /**
     * GPT내용 추가 해야함
     * */
    public String getCommitFromUrl(String url){
        Request request = new Request.Builder()
                .url(url)
                .addHeader("authorization", "Bearer " + accessToken)
                .addHeader("Accept", "application/vnd.github.v3+json")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }

            return response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "{}";
    }
    public CommitHistory addReviewToCommit(String githubId) throws IOException {
        // 커밋 히스토리를 데이터베이스에서 조회합니다.
        User user = userRepository.findByGithubId(githubId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid github ID: " + githubId));

        // GPT API를 호출하여 리뷰를 받습니다.
        List<String> urls = getTodayCommitUrls(githubId);
        String githubLink = urls.get(urls.size() -1); //가장 최근 커밋 이력 url
        String commitData = getCommitFromUrl(githubLink); //가장 최근 커밋의 내용 json으로 보관
        String response = gptService.requestGPT(commitData); //지피티 답변

        // ObjectMapper 인스턴스 생성
        ObjectMapper objectMapper = new ObjectMapper();
        // JSON 문자열을 JsonNode로 파싱
        JsonNode jsonNode = objectMapper.readTree(response);
        // summary와 code_review 값을 추출
        String title = jsonNode.get("summary").asText();
        String description = jsonNode.get("code_review").asText();


//        String title = "한줄 요약 제목";
//        String description = githubLink + "의 피드백 내용이 들어갈거임";
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
