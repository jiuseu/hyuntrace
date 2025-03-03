package com.example.hyuntrace.service;

import com.example.hyuntrace.config.CustomMapper;
import com.example.hyuntrace.domain.Board;
import com.example.hyuntrace.dto.*;
import com.example.hyuntrace.repository.BoardRepository;
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
public class BoardServiceImpl implements BoardService{

    private final BoardRepository boardRepository;
    private final CustomMapper customMapper;

    @Override
    public Long register(BoardDTO boardDTO){

        Board board = customMapper.toBoardEntity(boardDTO);
        board.addImages(boardDTO);

        Long bno = boardRepository.save(board).getBno();
        log.info("Register Board -------------------------------------");
        log.info(board);

        return bno;
    }

    @Override
    public BoardDTO read(Long bno){

        Optional<Board>result = boardRepository.findByIdWithImages(bno);
        Board board = result.orElseThrow(() -> new NullPointerException("Can Not Find bno"));

        BoardDTO boardDTO = customMapper.toBoardDTO(board);
        log.info("Read Board -------------------------------------");
        return boardDTO;
    }

    @Override
    public void modify(BoardDTO boardDTO){

        Optional<Board>result = boardRepository.findByIdWithImages(boardDTO.getBno());
        Board board = result.orElseThrow(() -> new NullPointerException("Can Not Find bno"));

        board.changeTitle(boardDTO.getTitle());
        board.changeContent(boardDTO.getContent());
        board.clearImages();
        board.addImages(boardDTO);

        log.info("Modify Board -------------------------------------");
        boardRepository.save(board);
    }

    @Override
    public void remove(Long bno){

        try{
            boardRepository.deleteById(bno);
            log.info("Remove Board -------------------------------------");
        }catch (NullPointerException e){
            log.info("Can Not Find Bno.................................");
        }

    }

    @Override
    public PageResponseDTO<BoardDTO> list(PageRequestDTO pageRequestDTO){

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<Board> result = boardRepository.list(types, keyword, pageable);
        List<BoardDTO> dtoList = result.stream().map(board -> customMapper.toBoardDTO(board)).collect(Collectors.toList());

        return PageResponseDTO.<BoardDTO>withAll()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total((int)result.getTotalElements())
                .build();
    }

    @Override
    public PageResponseDTO<BoardListWithReplyDTO> listWithReply(PageRequestDTO pageRequestDTO){

        String[] types = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BoardListWithReplyDTO> result = boardRepository.boardListWithReply(types,keyword,pageable);

        return PageResponseDTO.<BoardListWithReplyDTO>withAll()
                .total((int)result.getTotalElements())
                .dtoList(result.stream().toList())
                .pageRequestDTO(pageRequestDTO)
                .build();
    }

    @Override
    public PageResponseDTO<BoardListAllDTO> listAll(PageRequestDTO pageRequestDTO){
        String[] type = pageRequestDTO.getTypes();
        String keyword = pageRequestDTO.getKeyword();
        Pageable pageable = pageRequestDTO.getPageable("bno");

        Page<BoardListAllDTO> result = boardRepository.boardListAll(type,keyword,pageable);

        return PageResponseDTO.<BoardListAllDTO>withAll()
                .dtoList(result.stream().toList())
                .pageRequestDTO(pageRequestDTO)
                .total((int)result.getTotalElements())
                .build();
    }
}
