package com.group.commitapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.commitapp.common.enums.CustomResponseStatus;
import com.group.commitapp.common.exception.CustomException;
import com.group.commitapp.domain.CommitHistory;
import com.group.commitapp.domain.User;
import com.group.commitapp.dto.commit.CommitHistoryResponse;
import com.group.commitapp.repository.CommitHistoryRepository;
import com.group.commitapp.repository.UserRepository;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CommitService {

	private final OkHttpClient httpClient = new OkHttpClient();
	private final ObjectMapper objectMapper = new ObjectMapper();

	private final CommitHistoryRepository commitHistoryRepository;
	private final UserRepository userRepository;
	//    private final MemberRepository userRepository;
	private final String accessToken;
	private final UserService userService;
	private final GptService gptService;
	private final BadgeService badgeService;

	public CommitService(
			CommitHistoryRepository commitHistoryRepository,
			UserRepository userRepository,
			@Value("${oauth2.user.github.access-token}") String accessToken,
			GptService gptService,
			BadgeService badgeService,
			UserService userService) {
		this.commitHistoryRepository = commitHistoryRepository;
		this.userRepository = userRepository;
		this.accessToken = accessToken;
		this.gptService = gptService;
		this.badgeService = badgeService;
		this.userService = userService;
	}

	public List<List<String>> getTodayCommitUrls(String githubId) throws IOException {
		String url = "https://api.github.com/users/" + githubId + "/events";
		Request request =
				new Request.Builder()
						.url(url)
						.addHeader("Authorization", "Bearer " + accessToken)
						.addHeader("Accept", "application/vnd.github.v3+json")
						.addHeader("User-Agent", "CommitAppService")
						.build();

		try (Response response = httpClient.newCall(request).execute()) {
			if (response.code() == 404) { // 해당 깃허브 id가 없을떄
				throw new CustomException(CustomResponseStatus.GITHUB_USER_NOT_FOUND);
			}
			if (!response.isSuccessful()) { // 깃허브 API호출 실패
				throw new CustomException(CustomResponseStatus.GITHUB_API_ERROR);
			}

			JsonNode events = objectMapper.readTree(response.body().string());
			List<List<String>> urls = new ArrayList<>();

			for (JsonNode event : events) {
				// PushEvent만 필터링
				if (!"PushEvent".equals(event.get("type").asText())) continue;

				//                ZonedDateTime kstDate = ZonedDateTime
				//                        .parse(event.get("created_at").asText())
				//                        .withZoneSameInstant(ZoneId.of("Asia/Seoul"));
				//                String eventDate =
				// kstDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
				//
				//                // 오늘 커밋한 이벤트만 필터링
				//                if (!eventDate.equals(LocalDate.now().toString())) continue;

				String repoName = event.get("repo").get("name").asText();
				String sha = event.get("payload").get("head").asText();
				String commitUrl = "https://github.com/" + repoName + "/commit/" + sha;
				String apiUrl = "https://api.github.com/repos/" + repoName + "/commits/" + sha;
				urls.add(List.of(commitUrl, apiUrl));
			}
			return urls;
		}
	}

	public List<CommitHistory> getTodayCommitsByGithubId(String githubId) {
		return commitHistoryRepository.findTodayCommitsByGithubId(githubId);
	}

	public List<CommitHistoryResponse> getCommitHistoriesByGithubId(String githubId) {
		User user =
				userRepository
						.findByGithubId(githubId)
						.orElseThrow(() -> new CustomException(CustomResponseStatus.MEMBER_NOT_FOUND));

		//        // 최근 3일간의 커밋 이력 조회
		//        LocalDate endDate = LocalDate.now();
		//        LocalDate startDate = endDate.minusDays(2); // 최근 3일을 확인하려면 2일을 빼야 함
		//        Date start = Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
		//        Date end =
		// Date.from(endDate.plusDays(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
		//
		//        List<CommitHistory> recentCommits =
		// commitHistoryRepository.findByUserAndCreatedAtBetweenOrderByCreatedAtAsc(user, start, end);

		//        // 커밋이 연속 3일간 있는지 확인
		//        boolean hasStreak = checkStreak(recentCommits, 3);
		//
		//        // 3일 연속 커밋했으면 createStreakBadge 호출
		//        if (hasStreak) {
		//            Long badgeId = 2L; // 연속 커밋 뱃지 ID
		//            badgeService.createStreakBadge(user.getId(), badgeId);
		//        } else {
		//            // 3일간 커밋이 없으면 createDeadBadge 호출
		//            boolean hasNoCommitsForThreeDays = checkNoCommits(recentCommits, 3);
		//            if (hasNoCommitsForThreeDays) {
		//                Long badgeId = 3L; // Dead Badge ID
		//                badgeService.createDeadBadge(user.getId(), badgeId);
		//            }
		//        }

		// 전체 커밋 이력을 반환
		List<CommitHistory> commitHistories = commitHistoryRepository.findByUserOrderByIdAsc(user);
		return CommitHistoryResponse.fromList(commitHistories);
	}

	//    private boolean checkStreak(List<CommitHistory> recentCommits, int days) {
	//        LocalDate today = LocalDate.now();
	//        List<LocalDate> commitDates = recentCommits.stream()
	//                .map(commit ->
	// commit.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
	//                .distinct()
	//                .collect(Collectors.toList());
	//
	//        for (int i = 0; i < days; i++) {
	//            LocalDate dateToCheck = today.minusDays(i);
	//            if (!commitDates.contains(dateToCheck)) {
	//                return false;
	//            }
	//        }
	//        return true;
	//    }

	//        private boolean checkNoCommits(List<CommitHistory> recentCommits, int days) {
	//        LocalDate today = LocalDate.now();
	//        List<LocalDate> commitDates = recentCommits.stream()
	//                .map(commit ->
	// commit.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDate())
	//                .distinct()
	//                .collect(Collectors.toList());
	//
	//        for (int i = 0; i < days; i++) {
	//            LocalDate dateToCheck = today.minusDays(i);
	//            if (commitDates.contains(dateToCheck)) {
	//                return false;
	//            }
	//        }
	//        return true;
	//    }

	/** GPT내용 추가 해야함 */
	public String getCommitFromUrl(String url) {
		Request request =
				new Request.Builder()
						.url(url)
						.addHeader("authorization", "Bearer " + accessToken)
						.addHeader("Accept", "application/vnd.github.v3+json")
						.addHeader(
								"User-Agent",
								"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/125.0.0.0 Safari/537.36")
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

	public CommitHistoryResponse addReviewToCommit(String githubId) throws IOException {
		// 커밋 히스토리를 데이터베이스에서 조회합니다.
		User user =
				userRepository
						.findByGithubId(githubId)
						.orElseThrow(() -> new CustomException(CustomResponseStatus.MEMBER_NOT_FOUND));

		// GPT API를 호출하여 리뷰를 받습니다.
		List<String> urls = getTodayCommitUrls(githubId).get(0);
		// 결과를 리스트로 수집합니다;
		String githubLink = urls.get(0); // 가장 최근 커밋 이력 url
		String apiLink = urls.get(1); // 가장 최근 커밋의 내용 json으로 보관
		System.out.println("githubLink: " + githubLink);
		System.out.println("apiLink: " + apiLink);

		String commitData = getCommitFromUrl(apiLink); // 가장 최근 커밋의 내용 json으로 보관
		String response = gptService.requestGPT(commitData); // 지피티 답변
		//        String response = gptService.requestGPT(apiLink); //지피티 답변
		// ObjectMapper 인스턴스 생성
		Pattern pattern = Pattern.compile("\\{.*\\}", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(response);

		if (matcher.find()) {
			response = matcher.group();
		}
		System.out.println("response: " + response);
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
		// 커밋이력은 여러개일수도 있지만, DB에는 1개만 넣는다고 가정할게요

		CommitHistory commitHistory = CommitHistory.create(title, description, githubLink, user);
		userService.giveExperienceByUser(user);
		userRepository.save(user);
		commitHistoryRepository.save(commitHistory);
		// 커밋 히스토리에 리뷰를 추가합니다.
		// 변경 사항을 데이터베이스에 저장합니다.
		return CommitHistoryResponse.from(commitHistory);
	}

	public int addGoodToCommit(Long historyId) {
		CommitHistory commitHistory =
				commitHistoryRepository
						.findById(historyId)
						.orElseThrow(() -> new CustomException(CustomResponseStatus.COMMIT_HISTORY_NOT_FOUND));
		commitHistory.addGood();
		return commitHistoryRepository.save(commitHistory).getGood();
	}

	public int addBadToCommit(Long historyId) {
		CommitHistory commitHistory =
				commitHistoryRepository
						.findById(historyId)
						.orElseThrow(() -> new CustomException(CustomResponseStatus.COMMIT_HISTORY_NOT_FOUND));
		commitHistory.addBad();
		return commitHistoryRepository.save(commitHistory).getBad();
	}

	public String getCommitStatusForToday(String githubId) throws IOException {
		// 먼저 해당 사용자가 존재하는지 확인
		if (userRepository.findByGithubId(githubId).isEmpty()) {
			throw new CustomException(CustomResponseStatus.MEMBER_NOT_FOUND);
		}
		if (getTodayCommitUrls(githubId).isEmpty()) {
			return "commitNotYet";
		}
		if (getTodayCommitsByGithubId(githubId).isEmpty()) {
			return "AINotYet";
		}
		return "commitDone";
	}

	// 테스트용 메서드
	public List<String> getTodayCommitUrlsForTest(String githubId) throws IOException {
		return getTodayCommitUrls(githubId).stream()
				.filter(urlList -> !urlList.isEmpty())
				.map(urlList -> urlList.get(0))
				.collect(Collectors.toList());
	}
}
