package com.example.hyuntrace.security;

import com.example.hyuntrace.domain.Member;
import com.example.hyuntrace.dto.MemberDTO;
import com.example.hyuntrace.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class APIUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username)throws UsernameNotFoundException{

        log.info("APIUserDetailsService username : "+username);
        Optional<Member> result = memberRepository.getWithRoles(username);
        Member member = result.orElseThrow(() -> new UsernameNotFoundException("CAN NOT FIND USERNAME"));

        MemberDTO dto = new MemberDTO(
                member.getMid(),
                member.getMpw(),
                member.getEmail(),
                member.isDel(),
                false,
                member.getRoleSet().stream().map(memberRole -> new SimpleGrantedAuthority(
                        "ROLE_"+memberRole.name()
                )).collect(Collectors.toList())
        );

        log.info(dto);

        return dto;
    }
}
