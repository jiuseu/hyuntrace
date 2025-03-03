package com.example.hyuntrace.security.handler;


import com.example.hyuntrace.domain.Member;
import com.example.hyuntrace.dto.MemberDTO;
import com.example.hyuntrace.service.MemberService;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;

import java.io.IOException;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {

    private final PasswordEncoder passwordEncoder;
    private final RequestCache requestCache = new HttpSessionRequestCache();
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException, AuthenticationException {

        log.info("APILoginSuccessHandler......run...............");
        log.info("==========================");
        log.info(authentication.getName());
        log.info("==========================");

        String mid = authentication.getName();
        MemberDTO memberDTO =  (MemberDTO) authentication.getPrincipal();

        if(memberDTO.getSocial().booleanValue() == true){
            log.info("Social User....................................");
        }

        SavedRequest savedRequest = requestCache.getRequest(request,response);
        String targetUrl = "/board/list";

        if(savedRequest != null && !savedRequest.getRedirectUrl().contains("/api/")){
            targetUrl = savedRequest.getRedirectUrl();
        }
        response.sendRedirect(targetUrl);
    }

}
