package com.example.hyuntrace.security;

import com.example.hyuntrace.domain.Member;
import com.example.hyuntrace.domain.MemberRole;
import com.example.hyuntrace.dto.MemberDTO;
import com.example.hyuntrace.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request)throws OAuth2AuthenticationException{

        log.info("userRequest........");
        log.info(request);
        log.info("oauth2 user.............................................");

        ClientRegistration clientRegistration = request.getClientRegistration();
        String clientName = clientRegistration.getClientName();
        log.info("NAME : "+clientName);

        OAuth2User oAuth2User = super.loadUser(request);
        Map<String,Object> paramMap = oAuth2User.getAttributes();

        String email = null;

        switch (clientName){
            case "kakao":
                email = getKakaoEmail(paramMap);
                break;
        }

        log.info("==============================================");
        log.info(email);
        log.info("==============================================");

        return generateDTO(email, paramMap);
    }

    private MemberDTO generateDTO(String email, Map<String,Object>params){

        Optional<Member> result = memberRepository.findByEmail(email);

        if(result.isEmpty()){
            Member member = Member.builder()
                    .mid(email)
                    .mpw(passwordEncoder.encode("1111"))
                    .email(email)
                    .social(true)
                    .build();
            member.addRole(MemberRole.USER);
            memberRepository.save(member);

            MemberDTO memberDTO = new MemberDTO(email, "1111", email,false,true,
                    Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));
            memberDTO.setProps(params);

            return memberDTO;
        }else{
            Member member = result.get();
            MemberDTO memberDTO = new MemberDTO(
                    member.getMid(),
                    member.getMpw(),
                    member.getEmail(),
                    member.isDel(),
                    member.isSocial(),
                    member.getRoleSet().stream()
                            .map(memberRole -> new SimpleGrantedAuthority("ROLE_"+memberRole.name()))
                            .collect(Collectors.toList())
            );
            return memberDTO;
        }
    }



    private String getKakaoEmail(Map<String,Object>paramMap){

        log.info("KAKAO ---------------------------------------------------------------");
        Object value = paramMap.get("kakao_account");
        log.info(value);

        LinkedHashMap accountMap = (LinkedHashMap) value;
        String email = (String)accountMap.get("email");
        log.info("email..."+email);

        return email;
    }
}
