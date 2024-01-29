package com.encore.board.author.service;

import com.encore.board.author.domain.Author;
import com.encore.board.author.domain.Role;
import com.encore.board.author.dto.AuthorDetailResDto;
import com.encore.board.author.dto.AuthorListResDto;
import com.encore.board.author.dto.AuthorSaveReqDto;
import com.encore.board.author.dto.AuthorUpdateReqDto;
import com.encore.board.author.repository.AuthorRepository;
import com.encore.board.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AuthorService {
    private final AuthorRepository authorRepository;
    private final PostRepository postRepository;

    @Autowired // 생성자가 하나이면 자동으로 주입이 되지만 생성자가 많아지면 꼭 써주어야하기때문에 일단 써놓는게 좋다.
    public AuthorService(AuthorRepository authorRepository, PostRepository postRepository) {
        this.authorRepository = authorRepository;
        this.postRepository = postRepository;
    }

    public void save(AuthorSaveReqDto authorSaveReqDto) throws IllegalArgumentException{
        if(authorRepository.findByEmail(authorSaveReqDto.getEmail()).isPresent()) throw new IllegalArgumentException("중복이메일");

        Role role = null;
        if(authorSaveReqDto.getRole() == null || authorSaveReqDto.getRole().equals("user")){ //분기처리
            role = Role.USER;
        }else {
            role = Role.ADMIN;
        }
//        일반 생성자 방식
//        Author author = new Author(authorSaveReqDto.getName(),
//                                   authorSaveReqDto.getEmail(),
//                                   authorSaveReqDto.getPassword(),
//                                   role);
//        빌더패턴 // 순서상관없음
        Author author = Author.builder()
                .email(authorSaveReqDto.getEmail())
                .name(authorSaveReqDto.getName())
                .password(authorSaveReqDto.getPassword())
                .role(role)
                .build(); //최종적으로 완성시키는 단계 .build()
        authorRepository.save(author);

//        cascade.persist 테스트
//        부모테이블을 통해 자식테이블에 객체를 동시에 생성
//        List<Post> posts = new ArrayList<>();
//        Post post = Post.builder()
//                .title("안녕하세요. " + author.getName()+ "입니다.")
//                .contents("반갑습니다. cascade테스트 중입니다.")
//                .author(author)
//                .build();
//        posts.add(post);
//        author.setPosts(posts);
//        authorRepository.save(author);
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
        Author author = authorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("검색하신 ID의 회원이 없습니다. author not found"));
        return author;
    }

    public AuthorDetailResDto findAuthorDetail(Long id) throws EntityNotFoundException {
        Author author = authorRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("검색하신 ID의 회원이 없습니다. author not found"));
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
        authorDetailResDto.setCounts(author.getPosts().size()); // DetailResDto에 정의된 counts변수 사용
//        author에 posts가 정의되어 있으므로 get으로 가져옴
        authorDetailResDto.setCreatedTime(author.getCreatedTime());
        return authorDetailResDto;
    }

    public void update(Long id, AuthorUpdateReqDto authorUpdateReqDto) {// throws EntityNotFoundException{
        Author author = this.findById(id);
//        Author author = authorRepository.findById(authorUpdateReqDto.getId()).orElseThrow(EntityNotFoundException::new);
        author.updateMember(authorUpdateReqDto.getName(),authorUpdateReqDto.getPassword());

//        명시적으로 save를 하지 않더라도, jpa의 영속성컨텍스트를 통해, 객체에 변경이 감지(dirtychecking)되면,
//        트랜잭션이 완료되는 시점에 save동작.
//        authorRepository.save(author);
    }

    public void delete(Long id) {//throws EntityNotFoundException{
//        authorRepository.delete(authorRepository.findById(id).orElseThrow(EntityNotFoundException::new));
        authorRepository.deleteById(id);
//        findById = Optional타입, delete = Entity타입
//        findById를 Entity타입으로 변경해주어야 하기때문에 orElseThrow를 사용해서 Entity로 변경해주고 예외처리까지 해주어서
//        orElseThrow 예외처리값을 throws로 Controller에 던저주고 try,catch로 예외를 잡아준다.
    }
}
