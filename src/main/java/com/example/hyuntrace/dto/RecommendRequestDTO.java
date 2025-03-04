package com.example.hyuntrace.dto;

import com.example.hyuntrace.domain.RecommendType;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RecommendRequestDTO {

    private Long boardId; // 게시글 ID
    private String memberId; // 회원 ID
    private String voteType; // 추천/비추천

}
