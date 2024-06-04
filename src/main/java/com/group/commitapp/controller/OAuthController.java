package com.group.commitapp.controller;

import com.group.commitapp.dto.response.user.LoginResponse;
import com.group.commitapp.service.AuthService;
import com.group.commitapp.dto.oauth.AccessToken;
import com.group.commitapp.dto.oauth.OAuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class OAuthController {
    private final AuthService authService;

    @GetMapping("/login")
    public RedirectView getCode(RedirectAttributes redirectAttributes) {
        return authService.requestCode(redirectAttributes);
    }

    @GetMapping("/login/oauth")
    public RedirectView getToken(
            @RequestParam String code,
            @RequestParam String state) {

        AccessToken accessTokenInfo = authService.getAccessToken(code, state);
        OAuthInfo gitHubUserInfo = authService.getGitHubUserInfo(accessTokenInfo);

        LoginResponse login = authService.login(gitHubUserInfo);
        return new RedirectView("http://localhost:3000/MyProfile/"+login.githubId()+"?token=" + login.githubId());
//        return new ResponseEntity<>(HttpStatusCode.valueOf(HttpStatus.OK.value())).ok().body(login);
//        return new ResponseEntity<>(login, HttpStatus.OK);
    }


}