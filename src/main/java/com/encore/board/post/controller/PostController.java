package com.encore.board.post.controller;

import com.encore.board.post.dto.PostDetailResDto;
import com.encore.board.post.dto.PostListResDto;
import com.encore.board.post.dto.PostSaveReqDto;
import com.encore.board.post.dto.PostUpdateReqDto;
import com.encore.board.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
public class PostController {
    private final PostService postService;
    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }


    @GetMapping("/post/create")
    public String postCreate(){
        return "post/post-create";
    }

    @PostMapping("/post/create")
    public String postSave(Model model, PostSaveReqDto postSaveReqDto, HttpSession httpSession){
        try {
//            HttpServletRequest req를 매개변수에 주입한 뒤에
//            HttpSession session = req.getsession(); 세션값을 꺼내어 getAttribute("email")
//            HttpSession session = req.getSession();
//            System.out.println(session.getAttribute("email"));

                    postService.save(postSaveReqDto, httpSession.getAttribute("email").toString());
            return "redirect:/post/list";
        }catch (IllegalArgumentException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "post/post-create";
        }
    }

    @GetMapping("/post/list")
//    localhost:8080/post/list?size=xx&page=xx&sort=xx,desc
    public String postList(Model model, @PageableDefault(size = 5, sort = "createdTime", direction = Sort.Direction.DESC) Pageable pageable){
        Page<PostListResDto> postListResDtos = postService.findByAppointment(pageable);
        model.addAttribute("postList", postListResDtos);
        return "post/post-list";
    }
//    @GetMapping("/json/post/list")
////    localhost:8080/post/list?size=xx&page=xx&sort=xx,desc
//    @ResponseBody
//    public Page<PostListResDto> postList(Pageable pageable){
//        Page<PostListResDto> postListResDtos = postService.findAllPaging(pageable);
//        return postListResDtos;
//    }

    @GetMapping("/post/detail/{id}")
    public String postDetail(@PathVariable Long id, Model model){
        PostDetailResDto postDetailResDto = postService.findPostDetail(id);
        model.addAttribute("post", postDetailResDto);
        return "post/post-detail";
    }

    @PostMapping("/post/{id}/update")
    public String postUpdate(@PathVariable Long id, PostUpdateReqDto postUpdateReqDto){
        postService.update(id, postUpdateReqDto);
        return "redirect:/post/detail/"+ id;
    }

    @GetMapping("/post/delete/{id}")
    public String postDelete(@PathVariable Long id){
        postService.delete(id);
        return "redirect:/post/list";
    }
}
