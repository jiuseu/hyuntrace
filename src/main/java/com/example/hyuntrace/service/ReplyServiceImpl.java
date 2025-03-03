package com.example.hyuntrace.service;

import com.example.hyuntrace.config.CustomMapper;
import com.example.hyuntrace.domain.Reply;
import com.example.hyuntrace.dto.PageRequestDTO;
import com.example.hyuntrace.dto.PageResponseDTO;
import com.example.hyuntrace.dto.ReplyDTO;
import com.example.hyuntrace.repository.ReplyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class ReplyServiceImpl implements ReplyService{

    private final ReplyRepository replyRepository;
    private final CustomMapper customMapper;

    @Override
    public Long register(ReplyDTO replyDTO){
        Reply reply = customMapper.toReplyEntity(replyDTO);
        Long rno = replyRepository.save(reply).getRno();

        return rno;
    }

    @Override
    public ReplyDTO read(Long rno){
        Optional<Reply> result = replyRepository.findById(rno);
        Reply reply = result.orElseThrow(() -> new NullPointerException("CAN NOT FIND Reply rno......."));
        ReplyDTO replyDTO = customMapper.toReplyDTO(reply);

        return replyDTO;
    }

    @Override
    public void modify(ReplyDTO replyDTO){
        Optional<Reply> result = replyRepository.findById(replyDTO.getRno());
        Reply reply = result.orElseThrow(() -> new NullPointerException("CAN NOT FIND Reply rno......."));
        reply.changeReplyContent(replyDTO.getReplyContent());

        replyRepository.save(reply);
    }

    @Override
    public void remove(Long rno){
        try{
            replyRepository.deleteById(rno);
        }catch (NullPointerException e){
          log.info("이미 삭제했거나 없는 댓글 입니다.");
          log.info(e.getMessage());
        }
    }

    @Override
    public PageResponseDTO<ReplyDTO>list(Long bno, PageRequestDTO pageRequestDTO){

        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() <= 0 ? 0 : pageRequestDTO.getPage()-1, pageRequestDTO.getSize(), Sort.by("rno").ascending() );
        Page<Reply> result = replyRepository.replyOfBoard(bno,pageable);

        List<ReplyDTO> dtoList = result.stream().map(reply -> customMapper.toReplyDTO(reply)).collect(Collectors.toList());

        return PageResponseDTO.<ReplyDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .total((int)result.getTotalElements())
                .dtoList(dtoList)
                .build();
    }
}
