package com.example.hyuntrace.repository.search;

import com.example.hyuntrace.config.CustomMapper;
import com.example.hyuntrace.domain.Board;
import com.example.hyuntrace.domain.QBoard;
import com.example.hyuntrace.domain.QReply;
import com.example.hyuntrace.dto.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;
import java.util.stream.Collectors;


public class BoardSearchImpl extends QuerydslRepositorySupport implements BoardSearch {

    private final CustomMapper customMapper;

    public BoardSearchImpl(CustomMapper customMapper){
        super(Board.class);
        this.customMapper = customMapper;
    }

    @Override
    public Page<Board> list(String[] types, String keyword, Pageable pageable){

        QBoard board = QBoard.board;
        JPQLQuery<Board> query = from(board);


        if((types != null && types.length > 0) && keyword != null){
            BooleanBuilder booleanBuilder = new BooleanBuilder();

            for(String type :types){
                switch (type){
                    case "t":
                        booleanBuilder.or(board.title.contains(keyword));
                        break;
                    case "c":
                        booleanBuilder.or(board.content.contains(keyword));
                        break;
                    case "w":
                        booleanBuilder.or(board.writer.contains(keyword));
                        break;
                }//end switch
            }//end for
            query.where(booleanBuilder);
        }//end if
        query.where(board.bno.gt(0L));

        this.getQuerydsl().applyPagination(pageable,query);

        List<Board> dtoList = query.fetch();
        Long count = query.fetchCount();

        return new PageImpl<>(dtoList,pageable,count);
    }

    @Override
    public Page<BoardListWithReplyDTO> boardListWithReply(String[] types, String keyword, Pageable pageable){

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        JPQLQuery<Board> query = from(board);
        query.leftJoin(reply).on(reply.board.eq(board));
        query.groupBy(board);

        if((types != null && types.length > 0) && keyword != null){
            BooleanBuilder booleanBuilder = new BooleanBuilder();
            for(String type : types){
               switch (type){
                   case "t":
                       booleanBuilder.or(board.title.contains(keyword));
                       break;
                   case "c":
                       booleanBuilder.or(board.content.contains(keyword));
                       break;
                   case "w":
                       booleanBuilder.or(board.writer.contains(keyword));
                       break;
               }//end switch
            }//end for
            query.where(booleanBuilder);
        }//end if
        query.where(board.bno.gt(0L));

        this.getQuerydsl().applyPagination(pageable,query);
        JPQLQuery<BoardListWithReplyDTO> dtoQuery = query.select(Projections.bean(BoardListWithReplyDTO.class,
                board.bno,
                board.title,
                board.writer,
                board.regDate,
                reply.count().as("replyCount")));

        List<BoardListWithReplyDTO> dtoList = dtoQuery.fetch();
        Long count = dtoQuery.fetchCount();

        return new PageImpl<>(dtoList,pageable,count);
    }

    @Override
    public Page<BoardListAllDTO> boardListAll(String[] types,String keyword, Pageable pageable){

        QBoard board = QBoard.board;
        QReply reply = QReply.reply;
        JPQLQuery<Board> boardJPQLQuery = from(board);
        boardJPQLQuery.leftJoin(reply).on(reply.board.eq(board));


        if((types != null && types.length > 0) && keyword != null){
          BooleanBuilder booleanBuilder = new BooleanBuilder();
          for(String type : types){
            switch (type){
                case "t":
                    booleanBuilder.or(board.title.contains(keyword));
                    break;
                case "c":
                    booleanBuilder.or(board.content.contains(keyword));
                    break;
                case "w":
                    booleanBuilder.or(board.writer.contains(keyword));
                    break;
            }//end switch
          }//end for
          boardJPQLQuery.where(booleanBuilder);
        }//end if
        boardJPQLQuery.where(board.bno.gt(0L));
        boardJPQLQuery.groupBy(board);
        this.getQuerydsl().applyPagination(pageable, boardJPQLQuery);

        JPQLQuery<Tuple> tupleJPQLQuery = boardJPQLQuery.select(board,reply.countDistinct());
        List<Tuple> tupleList = tupleJPQLQuery.stream().toList();

        List<BoardListAllDTO> dtoList = tupleList.stream().map(tuple -> {
            Board board1 = tuple.get(board);
            Long replyCount = tuple.get(1,Long.class);

            BoardListAllDTO dto =  BoardListAllDTO.builder()
                    .bno(board1.getBno())
                    .title(board1.getTitle())
                    .writer(board1.getWriter())
                    .regDate(board1.getRegDate())
                    .replyCount(replyCount)
                    .build();

            List<BoardImageDTO> imageDTO = board1.getImageSet().stream().sorted()
                    .map(boardImage -> BoardImageDTO.builder()
                            .uuid(boardImage.getUuid())
                            .fileName(boardImage.getFileName())
                            .ord(boardImage.getOrd())
                            .build()).collect(Collectors.toList());

            dto.setBoardImages(imageDTO);
            return dto;
        }).collect(Collectors.toList());

        Long count = boardJPQLQuery.fetchCount();

        return new PageImpl<>(dtoList,pageable,count);
    }
}
