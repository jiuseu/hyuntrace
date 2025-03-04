package com.example.hyuntrace.service;

import com.example.hyuntrace.domain.Board;
import com.example.hyuntrace.domain.RecommendType;
import com.example.hyuntrace.dto.BoardDTO;
import com.example.hyuntrace.dto.RecommendRequestDTO;
import com.example.hyuntrace.dto.RecommendResponseDTO;

public interface RecommendService {

    void addVote(RecommendRequestDTO recommendRequestDTO);
    RecommendResponseDTO voteCounts(Long bno);

}
