package com.example.hyuntrace.controller;

import com.example.hyuntrace.dto.PageRequestDTO;
import com.example.hyuntrace.dto.PageResponseDTO;
import com.example.hyuntrace.dto.ReplyDTO;
import com.example.hyuntrace.service.ReplyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/reply")
@RequiredArgsConstructor
@Log4j2
public class ReplyRestController {

    private final ReplyService replyService;

    @GetMapping("/{rno}")
    public ReplyDTO read(@PathVariable("rno")Long rno){
        ReplyDTO replyDTO = replyService.read(rno);

        return replyDTO;
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Long> register(@Valid @RequestBody ReplyDTO replyDTO,
                                     BindingResult bindingResult)throws BindException {

        Long rno = replyService.register(replyDTO);
        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }

       Map<String,Long> result = new HashMap<>();
       result.put("rno",rno);

       return result;
    }

    @PutMapping(value = "/{rno}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,String> modify(@PathVariable("rno")Long rno,@Valid @RequestBody ReplyDTO replyDTO,
                                     BindingResult bindingResult)throws BindException{

        replyDTO.setRno(rno);
        if(bindingResult.hasErrors()){
            throw new BindException(bindingResult);
        }
        replyService.modify(replyDTO);
        log.info(replyDTO);

        return Map.of("replyResult", "Success");
    }

    @DeleteMapping(value = "/{rno}")
    public Map<String,String> remove(@PathVariable("rno")Long rno){
        replyService.remove(rno);

        return Map.of("result","Success");
    }

    @GetMapping(value = "/list/{bno}")
    public PageResponseDTO<ReplyDTO> list(@PathVariable("bno")Long bno,PageRequestDTO pageRequestDTO){

        PageResponseDTO<ReplyDTO> list = replyService.list(bno, pageRequestDTO);
        return list;
    }
}
