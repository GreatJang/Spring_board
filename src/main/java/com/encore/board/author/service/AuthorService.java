package com.encore.board.author.service;

import com.encore.board.author.domain.Author;
import com.encore.board.author.domain.Role;
import com.encore.board.author.dto.AuthorDetailResDto;
import com.encore.board.author.dto.AuthorListResDto;
import com.encore.board.author.dto.AuthorSaveReqDto;
import com.encore.board.author.dto.AuthorUpdateReqDto;
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
        Role role = null;
        if(authorSaveReqDto.getRole() == null || authorSaveReqDto.getRole().equals("user")){ //분기처리
            role = Role.USER;
        }else {
            role = Role.ADMIN;
        }
        Author author = new Author(authorSaveReqDto.getName(),
                                   authorSaveReqDto.getEmail(),
                                   authorSaveReqDto.getPassword(),
                                   role);
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

    public Author findById(Long id) throws EntityNotFoundException {
        Author author = authorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("검색하신 ID의 회원이 없습니다."));
        return author;
    }

    public AuthorDetailResDto findAuthorDetail(Long id) throws EntityNotFoundException {
        Author author = authorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("검색하신 ID의 회원이 없습니다."));
        String role = null; // role 출력시 분기처리해서 한글로 출력
        if(author.getRole() == null || author.getRole().equals(Role.USER)){
            role = "일반유저";
        }else {
            role = "관리자";
        }

        AuthorDetailResDto authorDetailResDto = new AuthorDetailResDto();
        authorDetailResDto.setId(author.getId());
        authorDetailResDto.setName(author.getName());
        authorDetailResDto.setEmail(author.getEmail());
        authorDetailResDto.setPassword(author.getPassword());
        authorDetailResDto.setRole(role);
        authorDetailResDto.setCreatedTime(author.getCreatedTime());
        return authorDetailResDto;
    }

    public void update(Long id, AuthorUpdateReqDto authorUpdateReqDto) {// throws EntityNotFoundException{
        Author author = this.findById(id);
//        Author author = authorRepository.findById(authorUpdateReqDto.getId()).orElseThrow(EntityNotFoundException::new);
        author.updateMember(authorUpdateReqDto.getName(),authorUpdateReqDto.getPassword());
        authorRepository.save(author);
    }

    public void delete(Long id) {//throws EntityNotFoundException{
//        authorRepository.delete(authorRepository.findById(id).orElseThrow(EntityNotFoundException::new));
        authorRepository.deleteById(id);
//        findById = Optional타입, delete = Entity타입
//        findById를 Entity타입으로 변경해주어야 하기때문에 orElseThrow를 사용해서 Entity로 변경해주고 예외처리까지 해주어서
//        orElseThrow 예외처리값을 throws로 Controller에 던저주고 try,catch로 예외를 잡아준다.
    }
}
