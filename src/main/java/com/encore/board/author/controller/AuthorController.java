package com.encore.board.author.controller;

import com.encore.board.author.domain.Author;
import com.encore.board.author.dto.AuthorDetailResDto;
import com.encore.board.author.dto.AuthorSaveReqDto;
import com.encore.board.author.dto.AuthorUpdateReqDto;
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

//    @GetMapping("/author/create")
//    @ResponseBody
//    public String authorSave(){
//        return "OK";
//    }

    @GetMapping("/author/create")
    public String authorCreate(){
        return "author/author-create";
    }

    @PostMapping("/author/create")
    public String authorSave(Model model, AuthorSaveReqDto authorSaveReqDto){ // 웹 입력값을 RequestBody 어노테이션 authorSaveReqDto으로 받음
        try {
            authorService.save(authorSaveReqDto);
            return "redirect:/author/list";
        }catch (IllegalArgumentException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "author/author-create"; //model을 사용하니까 화면을 주어야함
        }
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
        model.addAttribute("author", authorService.findAuthorDetail(id));
        return "author/author-detail";
    }

    @PostMapping("/author/{id}/update")
    public String authorUpdate(@PathVariable Long id, AuthorUpdateReqDto authorUpdateReqDto){
        authorService.update(id, authorUpdateReqDto);
        return "redirect:/author/detail/"+ id;
//        return "redirect:/member/member-detail?id="+memberRequestDto.getId();
    }

    @GetMapping("/author/delete/{id}")
    public String authorDelete(@PathVariable Long id){
        authorService.delete(id);
        return "redirect:/author/list";
    }

//    엔티티 순환참조 이슈 테스트
//    순환참조 발생
    @GetMapping("/author/{id}/circle/entity")
    @ResponseBody
//    연관관계가 있는 Author엔티티를 json으로 직렬화를 하게 될 경우
//    순환참조 이슈 발생하므로, dto 사용필요
    public Author circleIssueTest1(@PathVariable Long id){
        return authorService.findById(id);
    }

//    Dto사용으로 순환참조 발생 X
    @GetMapping("/author/{id}/circle/dto")
    @ResponseBody
    public AuthorDetailResDto circleIssueTest2(@PathVariable Long id){
        return authorService.findAuthorDetail(id);
    }
}
