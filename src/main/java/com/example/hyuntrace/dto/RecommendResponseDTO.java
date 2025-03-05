package com.example.hyuntrace.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class RecommendResponseDTO {

    private Long upVote;
    private Long downVote;

}
