package com.example.hyuntrace.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class MemberInfoDTO {

    private String mid;
    private String mpw;
    private String email;
    private boolean auto;
    private boolean del;
    private List<String> roles;

}
