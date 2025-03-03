package com.example.hyuntrace.repository;

import com.example.hyuntrace.domain.Reply;
import com.example.hyuntrace.dto.ReplyDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReplyRepository extends JpaRepository<Reply,Long> {

    @Query("select r from Reply r where r.board.bno =:bno")
    Page<Reply> replyOfBoard(Long bno, Pageable pageable); //특정 게시글의 댓글 목록

    void deleteByBoard_Bno(Long bno);
}
