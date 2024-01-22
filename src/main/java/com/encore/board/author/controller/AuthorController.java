package com.encore.board.author.controller;

import com.encore.board.author.dto.AuthorDetailResDto;
import com.encore.board.author.dto.AuthorListResDto;
import com.encore.board.author.dto.AuthorSaveReqDto;
import com.encore.board.author.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class AuthorController {
    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @PostMapping("/author/save")
    @ResponseBody
    public String authorSave(AuthorSaveReqDto authorSaveReqDto){ // 웹 입력값을 RequestBody 어노테이션 authorSaveReqDto으로 받음
        authorService.save(authorSaveReqDto); // save로 넘김
        System.out.println(authorSaveReqDto);
        return "OK";
    }

    @PostMapping("/author/list")
    @ResponseBody
    public List<AuthorListResDto> authorList(){
        return authorService.findAll();
    }

    @PostMapping("/author/detail/{id}")
    @ResponseBody
    public AuthorDetailResDto authorDetail(@PathVariable Long id){
        return authorService.findById(id);
    }
}
