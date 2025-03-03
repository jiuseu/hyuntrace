package com.example.hyuntrace.service;

import com.example.hyuntrace.dto.*;

public interface BoardService {

    Long register(BoardDTO boardDTO);
    BoardDTO read(Long bno);
    void modify(BoardDTO boardDTO);
    void remove(Long bno);
    PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO);
    PageResponseDTO<BoardListWithReplyDTO> listWithReply(PageRequestDTO pageRequestDTO);

    PageResponseDTO<BoardListAllDTO> listAll(PageRequestDTO pageRequestDTO);
}
