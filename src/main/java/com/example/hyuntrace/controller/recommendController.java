package com.example.hyuntrace.controller;

import com.example.hyuntrace.domain.RecommendType;
import com.example.hyuntrace.dto.RecommendRequestDTO;
import com.example.hyuntrace.dto.RecommendResponseDTO;
import com.example.hyuntrace.service.RecommendService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/recommend")
@Log4j2
@RequiredArgsConstructor
public class recommendController {

    private final RecommendService recommendService;

    @GetMapping("/view/{bno}")
    public ResponseEntity<?> recommendGet(@PathVariable Long bno){
        log.info("API Recommend Get View Controller...................");
        Map<String,String> resultMap = new HashMap<>();

        try {
            RecommendResponseDTO dto = recommendService.voteCounts(bno);
            log.info(dto);
            return ResponseEntity.ok(dto);
        }catch(Exception e){
           log.info("추천 비추천 데이터를 가져오지 못하였습니다.");
           resultMap.put("error",e.getMessage());

           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }
    }

    @PostMapping("/vote/{bno}")
    public ResponseEntity<?> likeBoard(@PathVariable Long bno, @RequestBody RecommendRequestDTO dto) {

        log.info("API Recommend Post Vote Controller...................");
        log.info(dto);
        Map<String,String> resultMap = new HashMap<>();

        try{
            recommendService.addVote(dto);
            return ResponseEntity.ok().body("게시글 추천 완료");

        }catch (IllegalStateException e){
        log.info("이미 추천 비추천을 하였습니다.");
        resultMap.put("error","이미 추천 비추천을 하였습니다.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resultMap);
        }
    }

}
