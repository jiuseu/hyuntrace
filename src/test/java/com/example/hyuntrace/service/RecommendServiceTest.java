package com.example.hyuntrace.service;

import com.example.hyuntrace.domain.RecommendType;
import com.example.hyuntrace.dto.BoardDTO;
import com.example.hyuntrace.dto.RecommendRequestDTO;
import com.example.hyuntrace.dto.RecommendResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StopWatch;

@SpringBootTest
@Log4j2
public class RecommendServiceTest {

    @Autowired
    private RecommendService recommendService;
    @Autowired
    private BoardService boardService;

    @Test
    public void recommendAddTest(){

        Long bno = 109L;
        String mid = "testUser00";
        RecommendRequestDTO dto = RecommendRequestDTO.builder()
                .boardId(bno)
                .memberId(mid)
                .voteType("UpVote")
                .build();

        recommendService.addVote(dto);
        BoardDTO boardDTO = boardService.read(bno);
        RecommendResponseDTO responseDTO = recommendService.voteCounts(boardDTO.getBno());

        log.info("BoardDTO : "+boardDTO);
        log.info("추천 비추천 DTO : "+responseDTO);

    }

}
