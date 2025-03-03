package com.example.hyuntrace.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberModifyDTO {

    private String mpw;
    private String mpwCheck;
    private String email;
}
