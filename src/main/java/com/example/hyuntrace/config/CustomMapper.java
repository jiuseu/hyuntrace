package com.example.hyuntrace.config;

import com.example.hyuntrace.domain.Board;
import com.example.hyuntrace.domain.Member;
import com.example.hyuntrace.domain.Reply;
import com.example.hyuntrace.dto.BoardDTO;
import com.example.hyuntrace.dto.MemberDTO;
import com.example.hyuntrace.dto.MemberJoinDTO;
import com.example.hyuntrace.dto.ReplyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomMapper {

    @Mapping(target = "imageSet", ignore = true)
    @Mapping(target = "recommendationList", ignore = true)
    Board toBoardEntity(BoardDTO boardDTO);

//    @Mapping(target = "upVoteCount", expression = "java(new Board().upVoteCount())")
//    @Mapping(target = "downVoteCount", expression = "java(new Board().downVoteCount())")
    @Mapping(target = "fileNames", expression = "java(new BoardDTO().setMapperFileNames(board))")
    BoardDTO toBoardDTO(Board board);
    @Mapping(source = "bno", target = "board.bno")
    Reply toReplyEntity(ReplyDTO replyDTO);

    @Mapping(source = "board.bno", target = "bno")
    ReplyDTO toReplyDTO(Reply reply);

    @Mapping(target = "mpw", ignore = true)
    Member toMemberEntity(MemberJoinDTO memberJoinDTO);

    @Mapping(target = "mpw", ignore = true)
    Member toMemberEntity2(MemberDTO memberDTO);

    @Mapping(target = "roles", expression = "java(convertRolesToString(member))")
    MemberDTO toMemberDTO(Member member);

    default List<String> convertRolesToString(Member member) {
        return member.getRoleSet().stream()
                .map(role -> "ROLE_"+role.name())  // GrantedAuthority -> String 변환
                .collect(Collectors.toList());
    }
}
