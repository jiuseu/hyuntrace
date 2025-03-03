package com.example.hyuntrace.service;

import com.example.hyuntrace.config.CustomMapper;
import com.example.hyuntrace.domain.Member;
import com.example.hyuntrace.domain.MemberRole;
import com.example.hyuntrace.dto.MemberDTO;
import com.example.hyuntrace.dto.MemberInfoDTO;
import com.example.hyuntrace.dto.MemberJoinDTO;
import com.example.hyuntrace.dto.MemberModifyDTO;
import com.example.hyuntrace.repository.MemberRepository;
import jakarta.websocket.Session;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;
    private final CustomMapper customMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void join(MemberJoinDTO memberJoinDTO)throws MidExistException{

        log.info("======================================= Member Join ......=======================================");
        String mid = memberJoinDTO.getMid();
        boolean exist = memberRepository.existsById(mid);

        if(exist){
            log.info("=========================== 해당 아이디는 이미 존재합니다.===========================");
            throw new MidExistException();
        }

        Member member = customMapper.toMemberEntity(memberJoinDTO);
        member.changePassword(passwordEncoder.encode(memberJoinDTO.getMpw()));
        member.addRole(MemberRole.USER);

        memberRepository.save(member);
    }

    @Override
    public MemberInfoDTO memberInfo() {
        log.info("============== 회원의 정보를 불러오고 있습니다. ========================================");

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("====== 회원 계정 : " + username + " ==============================");

        if (username == null || username.isEmpty()) {
            log.error("회원 계정이 유효하지 않습니다.");
            throw new IllegalArgumentException("Invalid username");
        }

        Optional<Member> result = memberRepository.getWithRoles(username);
        if (result.isEmpty()) {
            log.error("해당 회원 정보가 없습니다. username: " + username);
            throw new RuntimeException("Member not found");
        }

        Member member = result.get();
        log.info("MEMBER INFO MEMBER : " + member);
        MemberInfoDTO dto = MemberInfoDTO.builder()
                .mid(member.getMid())
                .mpw(member.getMpw())
                .email(member.getEmail())
                .auto(member.isSocial())
                .del(member.isDel())
                .roles(member.getRoleSet().stream().map(memberRole ->
                        "ROLE_"+memberRole.name()).collect(Collectors.toList()))
                .build();


        log.info("====== 회원 정보 : " + dto + " ==============================");

        return dto;
    }


    @Override
    public String memberInfoAuth(String mpw)throws MpwNotMatchException { //본인확인 -> 비밀번호 일치하면 본인(아이디를 통해)호출

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<Member> result = memberRepository.getWithRoles(username);
        Member member = result.orElseThrow();

        boolean isPasswordCorrect = BCrypt.checkpw(mpw, member.getMpw());
        log.info("비밀번호의 일치 결과 : "+isPasswordCorrect+ "    ==========================================");
        if(!isPasswordCorrect){
            throw new MpwNotMatchException();
        }
        return username;
    }

    @Override
    public void modifyInfo(String mid, MemberModifyDTO memberModifyDTO){

        log.info("수정한 개인정보를 검토중입니다. =========================================");
        log.info("수정 요청한 아이디 : "+mid);
        Optional<Member> result = memberRepository.getWithRoles(mid);
        Member member = result.orElseThrow();
        log.info("수정 전 개인정보 : "+member);
        member.changeEmail(memberModifyDTO.getEmail());
        member.changePassword(passwordEncoder.encode(memberModifyDTO.getMpw()));

        memberRepository.save(member);
    }

    @Override
    public void deleteMember(MemberInfoDTO memberInfoDTO)throws MpwNotMatchException{

        log.info("=================== MemberService Delete Member Start.......==================================");

        Member member = memberRepository.getWithRoles(memberInfoDTO.getMid()).orElseThrow();
        boolean mpwCheck = BCrypt.checkpw(memberInfoDTO.getMpw(),member.getMpw());

        if(!mpwCheck){
            log.info("탈퇴 검증 중 비밀번호가 일치하지 않습니다.");
            throw new MpwNotMatchException();
        }
        log.info("=================== 탈퇴 성공 ================================");
        memberRepository.deleteById(member.getMid());
    }
}
