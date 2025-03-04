package com.example.hyuntrace.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class RecommendResponseDTO {

    private Long upVote;
    private Long downVote;

}
