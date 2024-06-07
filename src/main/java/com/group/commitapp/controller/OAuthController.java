package com.group.commitapp.controller;

import com.group.commitapp.dto.response.user.LoginResponse;
import com.group.commitapp.service.AuthService;
import com.group.commitapp.dto.oauth.AccessToken;
import com.group.commitapp.dto.oauth.OAuthInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@Tag(name = "깃허브 OAuth", description = " 깃허브 OAuth 로그인 관련 API")
@RequestMapping("/api")
public class OAuthController {
    private final AuthService authService;
    private String frontUrl = "http://localhost:3000"; //로컬로 할때
//    private final String frontUrl = "http://15.165.210.42:3000"; //오소리 프론트
//    private final String frontUrl = "https://commit-js-one.vercel.app";

    @Operation(summary = "깃허브 OAuth 로그인 요청", description = "깃허브 OAuth 로그인")
    @GetMapping("/login")
    public RedirectView getCode(RedirectAttributes redirectAttributes) {
        return authService.requestCode(redirectAttributes);
    }

    @Operation(summary = "깃허브 OAuth 토큰 요청(백엔드용), 백엔드에서 다시 프론트url로 리다이렉션 걺", description = "깃허브 OAuth 토큰")
    @GetMapping("/login/oauth")
    public RedirectView getToken(
            @RequestParam String code,
            @RequestParam String state) {

        AccessToken accessTokenInfo = authService.getAccessToken(code, state);
        OAuthInfo gitHubUserInfo = authService.getGitHubUserInfo(accessTokenInfo);

        LoginResponse login = authService.login(gitHubUserInfo);
        return new RedirectView(frontUrl + "/MyProfile/" + login.githubId() + "?token=" + login.githubId());
//        return new RedirectView("http://localhost:3000/MyProfile/"+login.githubId()+"?token=" + login.githubId());
//        return new RedirectView("http://http://43.202.195.98:3000/MyProfile/"+login.githubId()+"?token=" + login.githubId());
    }


}