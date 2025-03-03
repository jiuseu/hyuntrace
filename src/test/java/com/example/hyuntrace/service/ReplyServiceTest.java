package com.example.hyuntrace.service;

import com.example.hyuntrace.dto.PageRequestDTO;
import com.example.hyuntrace.dto.PageResponseDTO;
import com.example.hyuntrace.dto.ReplyDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class ReplyServiceTest {

    @Autowired
    private ReplyService replyService;

    @Test
    public void registerTest(){

        IntStream.rangeClosed(1,100).forEach(num -> {
            ReplyDTO replyDTO = ReplyDTO.builder()
                    .bno(102L)
                    .replyContent("Reply Content "+num)
                    .writer("Replyer"+num)
                    .build();
            replyService.register(replyDTO);
            log.info(replyDTO);
        });
    }

    @Test
    public void readTest(){
        Long rno = 1L;
        ReplyDTO replyDTO = replyService.read(rno);
        log.info(replyDTO);
    }

    @Test
    public void modifyTest(){
      ReplyDTO replyDTO = ReplyDTO.builder()
              .rno(1L)
              .replyContent("Reply Modify Count !!!!"+1L)
              .build();

      replyService.modify(replyDTO);
      ReplyDTO result = replyService.read(1L);
      log.info(result);
    }

    @Test
    public void removeTest(){
       replyService.remove(1L);
    }

    @Test
    public void listTest(){
        Long bno1 = 1L;
        Long bno2 = 102L;
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        PageResponseDTO<ReplyDTO> result1 = replyService.list(bno1, pageRequestDTO);
        PageResponseDTO<ReplyDTO> result2 = replyService.list(bno2, pageRequestDTO);

        log.info("=========================== result 1 ===========================");
        log.info(result1);
        log.info("=========================== result 2 ===========================");
        log.info(result2);
    }
}
