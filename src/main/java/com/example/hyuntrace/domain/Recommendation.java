package com.example.hyuntrace.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Recommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recommendId;

    @ManyToOne
    @JoinColumn(name= "mid")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "bno")
    private Board board;

    @Enumerated(EnumType.STRING)
    private RecommendType recommendType;

}
