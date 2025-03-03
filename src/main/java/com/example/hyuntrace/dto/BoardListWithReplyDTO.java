package com.example.hyuntrace.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BoardListWithReplyDTO {

    private Long bno;
    private String title;
    private String writer;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime regDate;
    private Long replyCount;
}
