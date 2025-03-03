package com.example.hyuntrace.domain;

import com.example.hyuntrace.dto.BoardDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Board extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bno;

    private String title;

    private String content;

    private String writer;

    @OneToMany(mappedBy = "board", //BoardImage의 baord 변수에 매핑 mappedBy하면 좋은 점 자세한건 따로 기록 필요
               cascade = {CascadeType.ALL}, //상위 엔티티(Board)의 영향을 하위 엔티티(BoardImage)를 어떻게 할지 관리
               orphanRemoval = true,
               fetch = FetchType.LAZY)
    @Builder.Default
    @BatchSize(size = 20)
    private Set<BoardImage> imageSet = new HashSet<>();

    public void addImages(BoardDTO boardDTO){
        if(boardDTO.getFileNames() != null){
            boardDTO.getFileNames().forEach(file -> {
                String[] arr = file.split("_");
                file = file.replace(arr[0]+"_","");

                BoardImage boardImage = BoardImage.builder()
                        .uuid(arr[0])
                        .fileName(file)
                        .ord(imageSet.size())
                        .board(this)
                        .build();
                imageSet.add(boardImage);
            });
        }
    }public void addImages(String uuid, String fileName){
        BoardImage boardImage = BoardImage.builder()
                .uuid(uuid)
                .fileName(fileName)
                .board(this)
                .ord(imageSet.size())
                .build();
        imageSet.add(boardImage);
    }

    public void clearImages(){
        imageSet.forEach(boardImage -> boardImage.changeBoard(null));
        this.imageSet.clear();
    }

    public void changeContent(String content){
        this.content = content;
    }

    public void changeTitle(String title){
        this.title = title;
    }
}
