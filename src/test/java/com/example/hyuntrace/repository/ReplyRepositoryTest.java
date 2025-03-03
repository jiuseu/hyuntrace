package com.example.hyuntrace.repository;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Log4j2
public class ReplyRepositoryTest {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void replyListTest(){

    }
}
