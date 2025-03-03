package com.example.hyuntrace.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class PageResponseDTO<E> {

    private int page;
    private int size;
    private int total;
    private List<E> dtoList;

    private Boolean prev;
    private Boolean next;

    private int start;
    private int end;

    @Builder(builderMethodName = "withAll")
    private PageResponseDTO(PageRequestDTO pageRequestDTO, int total, List<E>dtoList){

        if(total <= 0){
            return;
        }

        this.page = pageRequestDTO.getPage();
        this.size = pageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        this.end = (int)(Math.ceil(this.page/10.0)) * 10;
        //만약 7페이지를 보고있다면 7/10.0 -> 0.7 반올림 -> 1 * 10 = 끝페이지가 10페이지인 것을 알 수 있다.
        this.start = this.end - 9;
        int last = (int)(Math.ceil(total/(double)this.size));
        //last 변수를 구하는 이유는 만약 게시판의 개수가 143개이라면 end변수는 140로 잡기 때문에 끝페이지(last)를 구할 이유가 있다.

        this.end = end > last ? last : end;
        //위의 예시를 이어서가면 140(end -> 가짜 페이지) > 134(last 실제 끝페이지) -> end가 크다면 실제 last를 end에 넣어주는게 맞음

        this.prev = this.start > 1;
        this.next = total > this.end * this.size;

    }
}
