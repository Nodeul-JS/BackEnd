
package com.group.commitapp.service;

import com.group.commitapp.config.jwt.util.JwtUtil;
import com.group.commitapp.dto.oauth.AccessToken;
import com.group.commitapp.dto.oauth.OAuthInfo;
import com.group.commitapp.domain.User;
import com.group.commitapp.dto.response.user.LoginResponse;
import com.group.commitapp.repository.UserRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private String clientId;
    private String redirectUrl;
    private String loginUrl;
    private String state;
    private String clientSecret;
    private String tokenUrl;
    private String userUrl;

    public AuthService(
            UserRepository userRepository,
            JwtUtil jwtUtil,
            @Value("${oauth2.user.github.client-id}") String clientId,
            @Value("${oauth2.user.github.redirect-url}") String redirectUrl,
            @Value("${oauth2.user.github.login-url}") String loginUrl,
            @Value("${oauth2.user.github.client-secret}") String clientSecret,
            @Value("${oauth2.user.github.token-url}") String tokenUrl,
            @Value("${oauth2.user.github.user-url}") String userUrl
    ) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.clientId = clientId;
        this.redirectUrl = redirectUrl;
        this.loginUrl = loginUrl;
        this.state = UUID.randomUUID().toString();
        this.clientSecret = clientSecret;
        this.tokenUrl = tokenUrl;
        this.userUrl = userUrl;
    }

    public RedirectView requestCode(RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("client_id", clientId);
        redirectAttributes.addAttribute("redirect_url", redirectUrl);
        redirectAttributes.addAttribute("state", state);

        return new RedirectView(loginUrl);
    }

    public AccessToken getAccessToken(String code, String state) {
        Map<String, String> bodies = new HashMap<>();
        bodies.put("client_id", clientId);
        bodies.put("client_secret", clientSecret);
        bodies.put("code", code);
        bodies.put("state", state);

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<Object> request = new HttpEntity<>(bodies, headers);
        ResponseEntity<AccessToken> response = new RestTemplate()
                .postForEntity(tokenUrl, request, AccessToken.class);

        return response.getBody();
    }

    public OAuthInfo getGitHubUserInfo(AccessToken accessTokenInfo) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "token " + accessTokenInfo.accessToken());

        HttpEntity<Object> request = new HttpEntity<>(headers);
        ResponseEntity<OAuthInfo> response = new RestTemplate()
                .exchange(userUrl, HttpMethod.GET, request, OAuthInfo.class);

        return response.getBody();
    }

    public LoginResponse login(OAuthInfo oAuthInfo) {
        User findUser = userRepository.findByProviderId(oAuthInfo.getIdNumber()).orElseGet(() -> forceJoin(oAuthInfo));

        String token = jwtUtil.createToken(String.valueOf(findUser.getId()));
        return new LoginResponse(token, oAuthInfo.getUsername());
    }

    private User forceJoin(OAuthInfo oAuthInfo) {
        User user = User.create(oAuthInfo.getUsername(), oAuthInfo.getIdNumber());
        System.out.println(user.getLevel());
        return userRepository.save(user);
    }
}
