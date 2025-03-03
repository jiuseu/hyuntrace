package com.example.hyuntrace.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberJoinDTO {

    @NotEmpty(message = "아이디는 필수입니다.")
    private String mid;
    @NotEmpty(message = "비밀번호는 필수입니다.")
    private String mpw;
    @NotEmpty(message = "이메일은 필수입니다.")
    private String email;
    private boolean social;
    private boolean del;
}
