package com.example.hyuntrace.controller;

import com.example.hyuntrace.config.CustomMapper;
import com.example.hyuntrace.domain.Member;
import com.example.hyuntrace.dto.*;
import com.example.hyuntrace.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/member")
public class MemberRestController {

    private final MemberService memberService;
    private final CustomMapper customMapper;

    @PostMapping(value = "/join", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Object>> joinPost(@Valid @RequestBody MemberJoinDTO memberJoinDTO)  {

        log.info("============================== Member Join ..... 회원가입 처리 중.....==============================");
        log.info(memberJoinDTO);
        Map<String, Object> result = new HashMap<>();
        try{
            memberService.join(memberJoinDTO);
            log.info("=============================== 회원 가입 성공 !! ==================================");
            result.put("result", "성공");

            // 결과 맵에 토큰 추가
            result.put("result", "성공");
            return ResponseEntity.ok(result);
        }catch (MemberService.MidExistException exception){
            log.info("=============================== 오류가 발생하였습니다. ==================================");
            log.info(exception.getMessage());
            result.put("result", "실패");
            result.put("message", "이미 아이디가 존재합니다.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
        }
    }



    @GetMapping(value = "/info")
    public ResponseEntity<?> readMember(Authentication authentication){

        log.info("Member Info Read Api Controller .............................");

        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("인증되지 않은 요청입니다.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        log.info("인증된 사용자: {}", authentication.getPrincipal());

        try{
            MemberInfoDTO DTO = memberService.memberInfo();

            return ResponseEntity.ok(DTO);

        }catch (UsernameNotFoundException e) {
            log.warn("사용자를 찾을 수 없습니다: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자를 찾을 수 없습니다.");
        } catch (AuthenticationException e) {
            log.warn("인증되지 않은 사용자입니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        } catch (Exception e) {
            log.error("회원 정보 조회 중 오류 발생: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류가 발생했습니다.");
        }

    }

    @PostMapping("/logout")
    public void infoPost(){
        log.info("Member API POST INFO TEST ============================================");
    }

    @PostMapping(value = "/memberInfoAuth", consumes = MediaType.APPLICATION_JSON_VALUE)//본인확인
    public ResponseEntity<?> memberInfoAuth(@Valid @RequestBody MemberInfoDTO dto){

        log.info("Member Info Auth Api Controller .............................");
        Map<String,String> resultMap = new HashMap<>();

        try{
            String mid = memberService.memberInfoAuth(dto.getMpw());
            log.info("비밀번호가 일치 합니다.");
            resultMap.put("mid",mid);
            return ResponseEntity.status(HttpStatus.OK).body(resultMap);
        }catch (MemberService.MpwNotMatchException e){
            log.info("비밀번호가 불일치 합니다.");
            resultMap.put("결과","비밀번호 불일치");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }
    }

    @PutMapping(value = "/modify/{mid}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> memberModify(@PathVariable String mid,@Valid @RequestBody MemberModifyDTO memberModifyDTO){

        log.info("Member Info Modify Api Controller .............................");
        log.info(mid);
        Map<String, String> response = new HashMap<>();

        if(!memberModifyDTO.getMpw().equals(memberModifyDTO.getMpwCheck())){
            log.info("새 비밀 번호와 확인용 새 비밀번호가 일치하지 않습니다.");

            response.put("error","두 비밀번호가 일치하지 않습니다.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try{
            memberService.modifyInfo(mid, memberModifyDTO);
            return ResponseEntity.status(HttpStatus.OK).body("성공");
        }catch (Exception e){
            log.info("회원 정보 수정에 대한 오류가 발생하였습니다......................");
            log.info(e.getMessage());

            response.put("error", "회원 정보 수정 중 오류가 발생했습니다.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

    }

    @DeleteMapping(value = "/deleteMember/{mid}")
    public ResponseEntity<?> memberDelete(@PathVariable("mid") String mid, @RequestBody MemberInfoDTO memberInfoDTO,
                                          HttpSession session){

        log.info("API Member Delete run.........................");
        log.info(memberInfoDTO);
        Map<String,String> resultMap = new HashMap<>();

        try{
            memberService.deleteMember(memberInfoDTO);
            resultMap.put("성공","탈퇴가 완료되었습니다.");
            session.invalidate();

            return ResponseEntity.ok().body(resultMap);

        }catch (MemberService.MpwNotMatchException e){
            log.info("비밀 번호가 일치하지 않습니다.");
            resultMap.put("오류","비밀번호가 일치하지 않습니다.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }
    }
}