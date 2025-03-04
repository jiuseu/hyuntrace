package com.example.hyuntrace.dto;

import com.example.hyuntrace.domain.Board;
import com.example.hyuntrace.domain.Recommendation;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BoardDTO {

    private Long bno;

    @NotEmpty
    @Size(min = 3, max = 100)
    private String title;

    @NotEmpty
    private String content;

    @NotEmpty
    private String writer;

    private List<String> fileNames;


    public List<String> setMapperFileNames(Board board){
        return board.getImageSet().stream()
                .map(boardImage -> boardImage.getUuid()+"_"+boardImage.getFileName()).collect(Collectors.toList());
    }

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime regDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime modDate;
}
