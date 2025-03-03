package com.example.hyuntrace.service;

import com.example.hyuntrace.domain.Member;
import com.example.hyuntrace.dto.MemberDTO;
import com.example.hyuntrace.dto.MemberInfoDTO;
import com.example.hyuntrace.dto.MemberJoinDTO;
import com.example.hyuntrace.dto.MemberModifyDTO;

public interface MemberService {

    static class MidExistException extends Exception{ //아이디 중복 예외
    }
    void join(MemberJoinDTO memberJoinDTO)throws MidExistException; //회원가입

    MemberInfoDTO memberInfo(); // 회원 조회

    static class MpwNotMatchException extends Exception{ //비밀번호 일치확인
    }
    String memberInfoAuth(String mpw)throws MpwNotMatchException;

    void modifyInfo(String mid, MemberModifyDTO memberModifyDTO);  //개인 정보 수정

    void deleteMember(MemberInfoDTO memberInfoDTO)throws MpwNotMatchException;
}
