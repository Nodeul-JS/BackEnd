package com.group.commitapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.group.commitapp.domain.CommitHistory;
import com.group.commitapp.repository.CommitHistoryRepository;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class GitHubService {

    private final OkHttpClient httpClient = new OkHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private CommitHistoryRepository commitHistoryRepository;

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

            System.out.println("today: "+today.format(formatter));
            for (JsonNode event : events) {
                if (event.get("type").asText().equals("PushEvent")) { //푸쉬만 가져오기
                    String eventDate = event.get("created_at").asText().substring(0, 10);
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


}
