package com.example.hyuntrace.repository;

import com.example.hyuntrace.domain.RecommendType;
import com.example.hyuntrace.domain.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RecommendRepository extends JpaRepository<Recommendation,Long> {

    @Query("select r from Recommendation r where r.board.bno =:bno and r.member.mid =:mid")
    Optional<Recommendation> findByBoardIdAndMemberID(Long bno, String mid);

    // 특정 게시글의 추천 비추천 개수 조회
    @Query("select COUNT(r) from Recommendation r where r.board.bno =:bno and r.recommendType =:recommendType")
    long countVotes(@Param("bno") Long bno,@Param("recommendType")RecommendType recommendType);


}
