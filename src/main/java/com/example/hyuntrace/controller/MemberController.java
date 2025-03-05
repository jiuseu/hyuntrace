package com.example.hyuntrace.controller;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/member")
@Log4j2
public class MemberController {

    @GetMapping("/login")
    public void loginGet(String logout){

        if(logout != null){
            log.info("Logout......");
        }

    }

    @GetMapping("/join")
    public void joinGet(){
    }

    @GetMapping("/info")
    public void infoGet(){

    }

    @GetMapping("/modinfo")
    public void modInfoGet(){

    }
}
