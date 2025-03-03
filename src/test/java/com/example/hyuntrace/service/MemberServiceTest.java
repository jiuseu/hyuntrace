package com.example.hyuntrace.service;

import com.example.hyuntrace.domain.Member;
import com.example.hyuntrace.dto.MemberJoinDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
@Log4j2
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Test
    public void memberJoinTest() throws MemberService.MidExistException {

        MemberJoinDTO memberJoinDTO = MemberJoinDTO.builder()
                .mid("testUser00")
                .mpw("1234")
                .email("test1234@test.com")
                .social(false)
                .del(false)
                .build();

        memberService.join(memberJoinDTO);

    }
}
