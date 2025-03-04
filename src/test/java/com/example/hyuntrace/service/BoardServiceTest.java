package com.example.hyuntrace.service;

import com.example.hyuntrace.dto.BoardDTO;
import com.example.hyuntrace.dto.PageRequestDTO;
import com.example.hyuntrace.dto.PageResponseDTO;
import com.example.hyuntrace.dto.RecommendResponseDTO;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@SpringBootTest
@Log4j2
public class BoardServiceTest {

    @Autowired
    private BoardService boardService;
    @Autowired
    private RecommendService recommendService;

    @Test
    public void registerTest(){

        IntStream.rangeClosed(1,100).forEach(num -> {
           BoardDTO boardDTO = BoardDTO.builder()
                   .title("Local Title "+num)
                   .content("Local Content "+ num)
                   .writer("LocalUser"+num)
                   .build();
           boardService.register(boardDTO);
           log.info(boardDTO);
        });
    }

    @Test
    public void readTest(){
        Long bno = 25L;
        BoardDTO boardDTO = boardService.read(bno);
        RecommendResponseDTO responseDTO = recommendService.voteCounts(bno);

        log.info("추천비추천 없는 데이터 : "+boardDTO);
        log.info("추천비추천 없는 데이터 : "+responseDTO);

        Long bno2 = 109L;
        BoardDTO boardDTO2 = boardService.read(bno2);
        RecommendResponseDTO responseDTO2 = recommendService.voteCounts(bno2);

        log.info("추천비추천 있는 데이터 : "+boardDTO2);
        log.info("추천비추천 는 데이터: "+responseDTO2);

    }

    @Test
    public void modifyTest(){
        Long bno = 100L;
        BoardDTO result = boardService.read(bno);
        log.info("수정 전 BoardDTO : "+result);

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(bno)
                .title("Local Modify Title "+bno+" !!!")
                .content("Local Modify Content "+bno+" !!!")
                .build();
        boardService.modify(boardDTO);

        result = boardService.read(bno);
        log.info("수정 후 BoardDTO : "+result);
    }

    @Test
    public void removeTest(){
        Long bno = 1L;
        log.info("삭제 전 BoardDTO : "+boardService.read(bno));

        boardService.remove(bno);
        log.info("삭제 후 BoardDTO : "+boardService.read(bno));
    }

    @Test
    public void listTest(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .type("tcw")
                .keyword("")
                .build();

        PageResponseDTO pageResponseDTO = boardService.list(pageRequestDTO);
        pageResponseDTO.getDtoList().forEach(list -> log.info(list));
    }

    @Test
    public void boardImageRegisterTest(){

        BoardDTO boardDTO = BoardDTO.builder()
                .title("Board File Title...")
                .content("Board File Content ....")
                .writer("writer00")
                .build();

        boardDTO.setFileNames(
                Arrays.asList(
                        UUID.randomUUID()+"_"+"aaa.jpg",
                        UUID.randomUUID()+"_"+"bbb.jpg",
                        UUID.randomUUID()+"_"+"ccc.jpg"
                ));
        Long bno = boardService.register(boardDTO);
        log.info("bno : "+ bno);
    }

    @Test
    public void boardImageReadTest(){

        Long bno = 103L;
        BoardDTO boardDTO = boardService.read(bno);
        log.info(boardDTO);
        log.info("=============================================");

        for(String fileName : boardDTO.getFileNames()){
            log.info(fileName);
        }
    }

    @Test
    public void boardImageModifyTest(){

        BoardDTO boardDTO = BoardDTO.builder()
                .bno(103L)
                .content("Board File Fix Content ....")
                .title("Board File Title...")
                .build();

        boardDTO.setFileNames(
                Arrays.asList(
                        UUID.randomUUID()+"_"+"xxx.jpg",
                        UUID.randomUUID()+"_"+"yyy.jpg",
                        UUID.randomUUID()+"_"+"zzz.jpg"
                ));
        boardService.modify(boardDTO);

        Long bno = 103L;
        boardDTO = boardService.read(bno);
        log.info(boardDTO);
    }
}
