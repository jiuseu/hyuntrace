package com.example.hyuntrace.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDTO {

    private String accessToken;
    private String refreshToken;
    private String mid;
}
