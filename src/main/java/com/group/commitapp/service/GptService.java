package com.group.commitapp.service;


import com.group.commitapp.dto.gpt.GPTRequest;
import com.group.commitapp.dto.gpt.GPTResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import okhttp3.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GptService {

    @Value("${gpt.model}")
    private String model;

    @Value("${gpt.api.url}")
    private String apiUrl;
    private final RestTemplate restTemplate;

//    public GptService(GptService gptService, ) {
//        this.gptService = gptService;
//        this.restTemplate = gptService.getRestTemplate();

    //    }
    public String requestGPT(String prompt) {
        prompt = "다음 커밋 정보를 보고 , {summary:, code_review:}과 같은 json형식으로 한국어로 작성해줘, 무조건 json으로만 해야해." +
                " summary는 한줄의 명사형으로 끝나야해. code_review 300자 내외로 코드리뷰와 개선점을 종결형으로 작성해줘"
                + prompt;

        GPTRequest request = new GPTRequest(
                model, prompt, 1, 256, 1, 2, 2);
        GPTResponse gptResponse = restTemplate.postForObject(
                apiUrl
                , request
                , GPTResponse.class
        );

        return gptResponse.getChoices().get(0).getMessage().getContent();
    }
}
