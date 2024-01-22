package com.encore.board.author.service;

import com.encore.board.author.domain.Author;
import com.encore.board.author.domain.AuthorDetailResDto;
import com.encore.board.author.domain.AuthorListResDto;
import com.encore.board.author.domain.AuthorSaveReqDto;
import com.encore.board.author.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired // 생성자가 하나이면 자동으로 주입이 되지만 생성자가 많아지면 꼭 써주어야하기때문에 일단 써놓는게 좋다.
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public void save(AuthorSaveReqDto authorSaveReqDto) {
        Author author = new Author(authorSaveReqDto.getName(),
                                   authorSaveReqDto.getEmail(),
                                   authorSaveReqDto.getPassword());
        authorRepository.save(author);
    }

    public List<AuthorListResDto> findAll() {
        List<Author> authors = authorRepository.findAll();
        List<AuthorListResDto> authorListResDtos = new ArrayList<>();
        for(Author author : authors){
            AuthorListResDto authorListResDto = new AuthorListResDto();
            authorListResDto.setId(author.getId());
            authorListResDto.setName(author.getName());
            authorListResDto.setEmail(author.getEmail());
            authorListResDtos.add(authorListResDto);
        }
        return authorListResDtos;
    }


    public AuthorDetailResDto findById(Long id) throws EntityNotFoundException {
        Author author = authorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("검색하신 ID의 회원이 없습니다."));
        AuthorDetailResDto authorDetailResDto = new AuthorDetailResDto();
        authorDetailResDto.setId(author.getId());
        authorDetailResDto.setName(author.getName());
        authorDetailResDto.setEmail(author.getEmail());
        authorDetailResDto.setPassword(author.getPassword());
        authorDetailResDto.setCreatedTime(author.getCreatedTime());
        return authorDetailResDto;
    }
}
