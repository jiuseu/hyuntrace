package com.example.hyuntrace.service;

import com.example.hyuntrace.dto.PageRequestDTO;
import com.example.hyuntrace.dto.PageResponseDTO;
import com.example.hyuntrace.dto.ReplyDTO;

public interface ReplyService {

    Long register(ReplyDTO replyDTO);
    ReplyDTO read(Long rno);
    void modify(ReplyDTO replyDTO);
    void remove(Long rno);
    PageResponseDTO<ReplyDTO> list(Long bno, PageRequestDTO pageRequestDTO);
}
