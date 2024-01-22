package com.encore.board.author.controller;

import com.encore.board.author.dto.AuthorSaveReqDto;
import com.encore.board.author.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class AuthorController {
    private final AuthorService authorService;

    @Autowired
    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping("/author/save")
    @ResponseBody
    public String authorSave(){
        return "OK";
    }

    @PostMapping("/author/save")
    @ResponseBody
    public String authorSave(AuthorSaveReqDto authorSaveReqDto){ // 웹 입력값을 RequestBody 어노테이션 authorSaveReqDto으로 받음
        authorService.save(authorSaveReqDto); // save로 넘김
        System.out.println(authorSaveReqDto);
        return "OK";
    }

//    @PostMapping("/author/list")
//    @ResponseBody
//    public List<AuthorListResDto> authorList(){
//        return authorService.findAll();
//    }
    @GetMapping("/author/list")
    public String authorList(Model model){
//        List<AuthorListResDto> authorListResDtos = authorService.findAll();
        model.addAttribute("authorList", authorService.findAll());
        return "author/author-list";
    }

//    @PostMapping("/author/detail/{id}")
//    @ResponseBody
//    public AuthorDetailResDto authorDetail(@PathVariable Long id){
//        return authorService.findById(id);
//    }
    @GetMapping("/author/detail/{id}")
    public String authorDetail(@PathVariable Long id, Model model){
//        AuthorDetailResDto authorDetailResDto = authorService.findById(id);
        model.addAttribute("author", authorService.findById(id));
        return "author/author-detail";
    }
}
