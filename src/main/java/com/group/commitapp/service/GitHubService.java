package com.group.commitapp.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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

//            System.out.println("json: "+ events);
            System.out.println("today: "+today.format(formatter));
            for (JsonNode event : events) {
                if (event.get("type").asText().equals("PushEvent")) { //푸쉬만 가져오기
                    String eventDate = event.get("created_at").asText().substring(0, 10);
//                    System.out.println("eventDate: "+eventDate);
                    if (today.format(formatter).equals(eventDate)) {//오늘꺼만 가져오기
//                        System.out.println("event: "+event);
                        String repoName = event.get("repo").get("name").asText();
                        String sha = event.get("payload").get("head").asText();
//                        System.out.println(repoName);
//                        System.out.println(sha);
                        String commitUrl = "https://github.com/" + repoName + "/commit/" + sha;
//                        System.out.println(commitUrl);
                        commitUrls.add(commitUrl);
                    }
                }
            }

            return commitUrls;
        }
    }
}
