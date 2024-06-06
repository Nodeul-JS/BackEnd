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
//        System.out.println("requestGPT진입");
//        prompt = "다음 커밋 정보json을 보고 코드리뷰를 작성해줘 , 반드시 {summary:, code_review:}과 같은 json형식으로 한국어로 작성해줘, 무조건 json으로만 해야해." +
//                " summary는 한줄의 명사형으로 끝나야해. code_review는 300자 내외로 코드리뷰와 칭찬할점,개선점을 반드시 포함해서 종결형으로 작성해줘"
//                + prompt;
        prompt = "다음 커밋 정보 JSON을 보고 코드리뷰를 작성해주세요." +
                " 코드리뷰는 {\"summary\": string, \"code_review\": string }와 같은 json형식으로 한국어로 작성해주세요." +
                " summary는 한 줄의 명사형으로 끝나야 하며, code_review에는 칭찬할 점과 개선점을 반드시 포함해주세요." +
                " 개선점은 코드의 가독성, 성능, 유지보수 용이성 등에 관한 구체적인 제안이 되어야 합니다."+
                prompt;

        GPTRequest request = new GPTRequest(
                model, prompt, 0.2, 256, 0.8, 0.2,    0.1);
        GPTResponse gptResponse = restTemplate.postForObject(
                apiUrl
                , request
                , GPTResponse.class
        );

//        System.out.println("requestGPT탈출");
        return gptResponse.getChoices().get(0).getMessage().getContent();
    }
}
