package com.example.hyuntrace.repository.search;

import com.example.hyuntrace.domain.Board;
import com.example.hyuntrace.dto.BoardDTO;
import com.example.hyuntrace.dto.BoardListAllDTO;
import com.example.hyuntrace.dto.BoardListWithReplyDTO;
import com.example.hyuntrace.dto.PageRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSearch {

    Page<Board> list(String[] types, String keyword, Pageable pageable);

    Page<BoardListWithReplyDTO> boardListWithReply(String[] types, String keyword, Pageable pageable);

    Page<BoardListAllDTO> boardListAll(String[] types,String keyword, Pageable pageable);
}
