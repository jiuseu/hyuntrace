package com.example.hyuntrace.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = "board")
public class Reply extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rno;

    private String replyContent;

    private String writer;

    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    public void changeReplyContent(String content){
        this.replyContent = content;
    }
}
