package com.group.commitapp.controller;

import com.group.commitapp.common.dto.ApiResponse;
import com.group.commitapp.common.enums.CustomResponseStatus;
import com.group.commitapp.service.GptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Tag(name = "Chat-GPT", description = "GPT-4o Chat API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/gpt")
public class GPTController {

	@Value("${gpt.model}")
	private String model;

	@Value("${gpt.api.url}")
	private String apiUrl;

	private final RestTemplate restTemplate;
	@Autowired private final GptService gptService;

	@PostMapping("/chat")
	@Operation(summary = "GPT프롬프팅 답변 테스트 디버깅용", description = "GPT-4o Chat")
	public ResponseEntity<ApiResponse<String>> chat(@RequestBody String prompt) {
		String response = gptService.requestGPT(prompt);
		return ResponseEntity.ok(ApiResponse.createSuccess(response, CustomResponseStatus.SUCCESS));
	}
}
