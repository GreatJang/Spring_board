package com.encore.board.author.service;

import com.encore.board.author.domain.Author;
import com.encore.board.author.dto.AuthorDetailResDto;
import com.encore.board.author.dto.AuthorUpdateReqDto;
import com.encore.board.author.repository.AuthorRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class AuthorServiceTest {
    @Autowired
    private AuthorService authorService; // 진짜객체

//    가짜객체를 만드는 작업을 목킹이라 한다.
    @MockBean
    private AuthorRepository authorRepository; // 가짜 객체

    @Test
    void updateTest(){
        Long authorId = 1L;
        Author author = Author.builder()
                .name("sonson")
                .email("son@gmail.com")
                .password("son1212")
                .build();
        Mockito.when(authorRepository.findById(authorId)).thenReturn(Optional.of(author));
        AuthorDetailResDto authorDetailResDto = authorService.findAuthorDetail(authorId);
        Assertions.assertEquals(author.getName(), authorDetailResDto.getName());
        Assertions.assertEquals(author.getPosts().size(), authorDetailResDto.getCounts());
        Assertions.assertEquals("일반유저", authorDetailResDto.getRole());


        AuthorUpdateReqDto authorUpdateReqDto = new AuthorUpdateReqDto();
        authorUpdateReqDto.setName("sonson333");
        authorUpdateReqDto.setPassword("son3333");
        authorService.update(authorId, authorUpdateReqDto);


    }

    @Test
    void findAllTest(){
//        Mock repository 기능 구현 // 가짜 DB
        List<Author> authors = new ArrayList<>();
        authors.add(new Author());
        authors.add(new Author());
        Mockito.when(authorRepository.findAll()).thenReturn(authors);
//        authorRepository에 findAll 정의 // findAll 호출 시 항상 authors 호출
        for (Author author : authorRepository.findAll()){
            System.out.println(author.getId());
        }

//        검증
        Assertions.assertEquals(2, authorService.findAll().size());

    }
}
