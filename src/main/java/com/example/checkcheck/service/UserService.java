package com.example.checkcheck.service;

import com.example.checkcheck.dto.requestDto.RefreshTokenRequestDto;
import com.example.checkcheck.dto.responseDto.TokenFactory;
import com.example.checkcheck.model.RefreshToken;
import com.example.checkcheck.repository.RefreshTokenRepository;
import com.example.checkcheck.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.sasl.AuthenticationException;
import javax.servlet.http.HttpServletResponse;

@Service
@RequiredArgsConstructor
public class UserService {

    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    public TokenFactory accessAndRefreshTokenProcess(String username, HttpServletResponse response) {
        String refreshToken = jwtTokenProvider.createRefreshToken(username);
        System.out.println("refreshToken = " + refreshToken);
        String token = jwtTokenProvider.createToken(username);
        response.setHeader("Authorization", "Bearer "+token);
        response.setHeader("Access-Token-Expire-Time", String.valueOf(30*60*1000L));
        System.out.println("accesstoken = " + token);

        return new TokenFactory(token, refreshToken);
    }

    @Transactional
    public TokenFactory refreshAccessToken(String refreshToken
//            , RefreshTokenRequestDto refreshTokenRequest
    ) throws AuthenticationException {
//        String refreshToken = refreshTokenRequest.getRefreshToken();

        String id = jwtTokenProvider.getPayload(refreshToken);
//        String existingRefreshToken = refreshTokenRepository.findByTokenValue(id);
        System.out.println("id = " + id);
        RefreshToken refresh = refreshTokenRepository.findByTokenKey(id).orElse(null);
        String compareToken = refresh.getTokenValue();

        System.out.println("compareToken = " + compareToken);
        if (!compareToken.equals(refreshToken)) {
            throw new AuthenticationException("refresh token이 유효하지 않습니다.222");
        }

        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw new AuthenticationException("refresh token이 유효하지 않습니다.111");
        }

        String newAccessToken = jwtTokenProvider.createToken(id);


        return new TokenFactory(newAccessToken);
    }
}