package com.encore.board.author.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
//Slf4j어노테이션(롬복)을 통해 쉽게 logback 로그라이브러리 사용가능
@Slf4j
public class LogTestController {
//    Slf4j어노테이션 사용하지 않고, 직접 라이브러리 import하여 로거 생성가능
//    private static final Logger logger = LoggerFactory.getLogger(LogTestController.class);

    @GetMapping("log/test1")
    public String testMethod1(){
        log.debug("디버그 로그입니다.");
        log.info("인포 로그입니다.");
        log.error("에러 로그입니다.");
        return "OK";

    }
}
