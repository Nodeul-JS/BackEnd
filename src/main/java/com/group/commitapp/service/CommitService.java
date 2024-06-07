package com.group.commitapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.commitapp.domain.CommitHistory;
import com.group.commitapp.domain.User;
import com.group.commitapp.dto.commit.CommitHistoryDTO;
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
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.regex.Matcher;
@Service

public class CommitService {

    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    private final CommitHistoryRepository commitHistoryRepository;
    private final UserRepository userRepository;
//    private final MemberRepository userRepository;
    private final String accessToken;
    private final UserService userService;
    private GptService gptService;
    private BadgeService badgeService;

    public CommitService(CommitHistoryRepository commitHistoryRepository
            , UserRepository userRepository
            , @Value("${oauth2.user.github.access-token}") String accessToken, GptService gptService , BadgeService badgeService, UserService userService){
        this.commitHistoryRepository = commitHistoryRepository;
        this.userRepository = userRepository;
        this.accessToken = accessToken;
        this.gptService = gptService;
        this.badgeService = badgeService;
        this.userService = userService;
    }

    public List<List<String>> getTodayCommitUrls(String githubId) throws IOException {
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
            List<List<String>> urls = new ArrayList<>();
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
                        String repoName = event.get("repo").get("name").asText();
                        String sha = event.get("payload").get("head").asText();
                        String commitUrl = "https://github.com/" + repoName+ "/commit/" + sha;
                        String apiUrl = "https://api.github.com/repos/" + repoName + "/commits/" + sha;
                        urls.add(List.of(commitUrl, apiUrl));
                    //테스트를 위해 모든 커밋을 다 불러옵니다.
//                    if (today.format(formatter).equals(eventDate)) {//오늘꺼만 가져오기
////                        String repoName = event.get("repo").get("name").asText();
////                        String sha = event.get("payload").get("head").asText();
////                        String commitUrl = "https://github.com/" + repoName+ "/commit/" + sha;
//                        commitUrls.add("https://api.github.com/repos/"
//                                + event.get("repo").get("name").asText()
//                                + "/commit/"
//                                + event.get("payload").get("head").asText()
//                        );
////                        commitUrls.add("https://github.com/"
////                                + event.get("repo").get("name").asText()
////                                + "/commit/"
////                                + event.get("payload").get("head").asText()
////                        );
//
//                    }
                }
            }
            return urls;
        }
    }

    public List<CommitHistory> getTodayCommitsByGithubId(String githubId) {
        return commitHistoryRepository.findTodayCommitsByGithubId(githubId);
    }
    public List<CommitHistory> getCommitsByGithubId(String githubId) {
        User user = userRepository.findByGithubId(githubId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 유저가 없습니다."));

        // 최근 3일간의 커밋 이력 조회
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(2); // 최근 3일을 확인하려면 2일을 빼야 함
        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date end = Date.from(endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<CommitHistory> recentCommits = commitHistoryRepository.findByUserAndCreatedAtBetweenOrderByCreatedAtAsc(user, start, end);

        // 커밋이 연속 3일간 있는지 확인
        boolean hasStreak = checkStreak(recentCommits, 3);

        // 3일 연속 커밋했으면 createStreakBadge 호출
        if (hasStreak) {
            Long badgeId = 2L; // 연속 커밋 뱃지 ID
            badgeService.createStreakBadge(user.getUserId(), badgeId);
        } else {
            // 3일간 커밋이 없으면 createDeadBadge 호출
            boolean hasNoCommitsForThreeDays = checkNoCommits(recentCommits, 3);
            if (hasNoCommitsForThreeDays) {
                Long badgeId = 3L; // Dead Badge ID
                badgeService.createDeadBadge(user.getUserId(), badgeId);
            }
        }

        // 전체 커밋 이력을 반환
        return commitHistoryRepository.findByUserOrderByHistoryIdAsc(user);
    }

    private boolean checkStreak(List<CommitHistory> recentCommits, int days) {
        LocalDate today = LocalDate.now();
        List<LocalDate> commitDates = recentCommits.stream()
                .map(commit -> commit.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .distinct()
                .collect(Collectors.toList());

        for (int i = 0; i < days; i++) {
            LocalDate dateToCheck = today.minusDays(i);
            if (!commitDates.contains(dateToCheck)) {
                return false;
            }
        }
        return true;
    }


        private boolean checkNoCommits(List<CommitHistory> recentCommits, int days) {
        LocalDate today = LocalDate.now();
        List<LocalDate> commitDates = recentCommits.stream()
                .map(commit -> commit.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
                .distinct()
                .collect(Collectors.toList());

        for (int i = 0; i < days; i++) {
            LocalDate dateToCheck = today.minusDays(i);
            if (commitDates.contains(dateToCheck)) {
                return false;
            }
        }
        return true;
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
        List<String> urls = getTodayCommitUrls(githubId).get(0);
                // 결과를 리스트로 수집합니다;
        String githubLink = urls.get(0); //가장 최근 커밋 이력 url
        String apiLink = urls.get(1); //가장 최근 커밋의 내용 json으로 보관
        System.out.println("githubLink: "+githubLink);
        System.out.println("apiLink: "+apiLink);

        String commitData = getCommitFromUrl(apiLink); //가장 최근 커밋의 내용 json으로 보관
        String response = gptService.requestGPT(commitData); //지피티 답변
//        String response = gptService.requestGPT(apiLink); //지피티 답변
        // ObjectMapper 인스턴스 생성
        Pattern pattern = Pattern.compile("\\{.*\\}", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(response);

        if (matcher.find()) {
            response = matcher.group();

        }
        System.out.println("response: "+response);
        ObjectMapper objectMapper = new ObjectMapper();
        // JSON 문자열을 JsonNode로 파싱
        JsonNode jsonNode = objectMapper.readTree(response);
        // summary와 code_review 값을 추출
        String title = jsonNode.get("summary").asText();

        String description = jsonNode.get("code_review").asText();
        System.out.println("json : " + jsonNode);
//        System.out.println("title: "+title);
//        System.out.println("description: "+description);
//        String title = "한줄 요약 제목";
//        String description = githubLink + "의 피드백 내용이 들어갈거임";
        //커밋이력은 여러개일수도 있지만, DB에는 1개만 넣는다고 가정할게요

        CommitHistory commitHistory = new CommitHistory(
                new CommitHistoryDTO(title, description, githubLink, githubId)
                , user
        );
        userService.giveExperienceByUser(user);
        userRepository.save(user);

        // 커밋 히스토리에 리뷰를 추가합니다.
        // 변경 사항을 데이터베이스에 저장합니다.
        return commitHistoryRepository.save(commitHistory);
    }


    public CommitHistory addGoodToCommit(Long historyId) {
        CommitHistory commitHistory = commitHistoryRepository.findById(historyId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid history ID: " + historyId));
        commitHistory.setGood(commitHistory.getGood() + 1);
        return commitHistoryRepository.save(commitHistory);
    }
    public CommitHistory addBadToCommit(Long historyId) {
        CommitHistory commitHistory = commitHistoryRepository.findById(historyId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid history ID: " + historyId));
        commitHistory.setBad(commitHistory.getBad() + 1);
        return commitHistoryRepository.save(commitHistory);
    }
}
