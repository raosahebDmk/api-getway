package com.ms.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ms.model.AuthResponse;

import ch.qos.logback.core.model.Model;

@RestController
@RequestMapping("/auth")
public class TestController {

	@GetMapping("/login")
	public ResponseEntity<AuthResponse> login(@RegisteredOAuth2AuthorizedClient("okta") OAuth2AuthorizedClient client,@AuthenticationPrincipal OidcUser user, Model model)
	{
		System.out.println("email : "+user.getEmail());
		AuthResponse response = new AuthResponse();
		response.setUserId(user.getEmail());
		response.setAccessToken(client.getAccessToken().getTokenValue());
		response.setRefreshToken(client.getRefreshToken().getTokenValue());
		response.setExpireAt(client.getAccessToken().getExpiresAt().getEpochSecond());
		
		List<String> authorites =  user.getAuthorities().stream().map(grantedAuthority ->{
			return grantedAuthority.getAuthority();
		}).collect(Collectors.toList());
		
		response.setAuthories(authorites);
		
		
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
}
