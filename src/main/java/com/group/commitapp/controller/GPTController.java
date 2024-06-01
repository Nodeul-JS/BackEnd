package com.group.commitapp.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import okhttp3.*;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
        // 코드 수정 필요 -> MVC패턴으로 맞추기
@RestController
@RequestMapping(value = "/api/gpt")
@Tag(name = "GPT", description = "GPT 관련 API")
public class GPTController {

    @GetMapping("/api/gpt")
    public String callGpt(@Param("input") String input) throws IOException {

        OkHttpClient okhttp = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        String url = "https://api.openai.com/v1/chat/completions";
        MediaType mediaType = MediaType.parse("application/json");

        String requestBody = String.format("{\"model\": \"gpt-3.5-turbo\",\"messages\": [{\"role\": \"user\",\"content\": \"%s\"}],\"temperature\": 0.7}", input);

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + "")
                .post(RequestBody.create(mediaType, requestBody))
                .build();

        Response response = okhttp.newCall(request).execute();
        if (!response.isSuccessful()) {
            System.out.println("실패!");
        }

        JsonNode jsonNode = new ObjectMapper().readTree(response.body().string());
        String output = jsonNode.get("choices").get(0).get("message").get("content").asText();

        return output;
    }
}
