package com.example.hyuntrace.controller;

import com.example.hyuntrace.dto.BoardDTO;
import com.example.hyuntrace.dto.PageRequestDTO;
import com.example.hyuntrace.service.BoardService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.jar.Attributes;

@Controller
@RequestMapping("/board")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    @GetMapping("/list")
    public void listGet(PageRequestDTO pageRequestDTO, Model model){
        log.info("Board List Get...........................");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            System.out.println("Current user role: " + authority.getAuthority());
        }
        model.addAttribute("pageRequestDTO",pageRequestDTO);
    }

    @GetMapping("/read")
    public void readGet(Long bno,Model model){
       model.addAttribute("bno",bno);
    }

    @GetMapping("/modify")
    public void modifyGet(Long bno,Model model){
        model.addAttribute("bno", bno);
    }

    @GetMapping("/register")
    public void registerGet(){
    }

}
