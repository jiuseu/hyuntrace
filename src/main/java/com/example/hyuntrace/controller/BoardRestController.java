package com.example.hyuntrace.controller;

import com.example.hyuntrace.dto.*;
import com.example.hyuntrace.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/board")
@Log4j2
@RequiredArgsConstructor
public class BoardRestController {

    private final BoardService boardService;
    @Value("${com.example.upload.path}")
    private String uploadPath;

    @GetMapping(value = "/list")
    public ResponseEntity<PageResponseDTO<BoardDTO>>  list(PageRequestDTO pageRequestDTO){

        log.info("REST Board Controller API...............List GET...............");

        PageResponseDTO<BoardDTO> list = boardService.list(pageRequestDTO);
        log.info(pageRequestDTO);
        log.info(list);

        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/listReply")
    public ResponseEntity<PageResponseDTO<BoardListWithReplyDTO>> lisReply(PageRequestDTO pageRequestDTO){

        log.info("REST Board Controller API...............List Reply GET...............");

        PageResponseDTO<BoardListWithReplyDTO> list = boardService.listWithReply(pageRequestDTO);

        return ResponseEntity.ok(list);
    }

    @GetMapping(value = "/listAll")
    public ResponseEntity<PageResponseDTO<BoardListAllDTO>> listAll(PageRequestDTO pageRequestDTO){

        log.info("REST Board Controller API...............List All GET...............");

        PageResponseDTO<BoardListAllDTO> list = boardService.listAll(pageRequestDTO);

        return ResponseEntity.ok(list);
    }

    @PostMapping(value = "/register")
    public Long register(@Valid @RequestBody BoardDTO boardDTO){

        Long bno = boardService.register(boardDTO);

        return bno;
    }

    @GetMapping(value = "/read/{bno}")
    public BoardDTO readGet(@PathVariable("bno") Long bno){

        log.info("REST Board Controller API...............Read GET...............");
        BoardDTO boardDTO = boardService.read(bno);

        return boardDTO;
    }

    @PutMapping(value = "/modify/{bno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,String> modify(@PathVariable("bno")Long bno,@RequestBody BoardDTO boardDTO){

        boardDTO.setBno(bno);
        log.info(boardDTO);
        boardService.modify(boardDTO);

        return Map.of("Modify Result","Modify Success");
    }

    @DeleteMapping(value = "/remove/{bno}")
    public Map<String,String> remove(@PathVariable Long bno, @RequestBody BoardDTO boardDTO){

        if(boardDTO.getFileNames() != null && boardDTO.getFileNames().size() > 0){
            removeFiles(boardDTO.getFileNames());
        }
        boardService.remove(boardDTO.getBno());


        return Map.of("Remove Result","Remove Success");
    }

    public void removeFiles(List<String> fileNames){

        fileNames.forEach(fileName -> {
            Resource resource = new FileSystemResource(uploadPath+ File.separator+fileName);
            String resourceName = resource.getFilename();

            try{
                String contentType = Files.probeContentType(resource.getFile().toPath());
                resource.getFile().delete();

                if(contentType.startsWith("image")){
                    File thumbnailFile = new File(uploadPath+File.separator+"s_"+fileName);
                    thumbnailFile.delete();
                }
            }catch (Exception e){
                log.error(e.getMessage());
            }
        });

    }
}
