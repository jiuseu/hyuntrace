package com.example.hyuntrace.repository;

import com.example.hyuntrace.dto.BoardListWithReplyDTO;
import com.example.hyuntrace.dto.PageRequestDTO;
import com.example.hyuntrace.repository.search.BoardSearch;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@SpringBootTest
@Log4j2
public class SearchRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void boardListReplyTest(){
        String[] types = {"t","c","w"};
        String keyword = "테스트";
        Pageable pageable = PageRequest.of(0,10, Sort.by("bno").descending());

        Page<BoardListWithReplyDTO> result = boardRepository.boardListWithReply(types,keyword,pageable);
        result.forEach(board -> log.info(board));
    }
}
