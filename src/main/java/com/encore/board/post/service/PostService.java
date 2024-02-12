package com.encore.board.post.service;

import com.encore.board.author.domain.Author;
import com.encore.board.author.repository.AuthorRepository;
import com.encore.board.post.domain.Post;
import com.encore.board.post.dto.PostDetailResDto;
import com.encore.board.post.dto.PostListResDto;
import com.encore.board.post.dto.PostSaveReqDto;
import com.encore.board.post.dto.PostUpdateReqDto;
import com.encore.board.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public PostService(PostRepository postRepository, AuthorRepository authorRepository) {
        this.postRepository = postRepository;
        this.authorRepository = authorRepository;
    }

    public void save(PostSaveReqDto postSaveReqDto, String email) throws IllegalArgumentException{
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String email = authentication.getName();
        Author author = authorRepository.findByEmail(email).orElse(null);
        LocalDateTime localDateTime = null;
        String appointment = null;
        if(postSaveReqDto.getAppointment().equals("Y") // 예약 글쓰기 Y
                && !postSaveReqDto.getAppointmentTime().isEmpty()){ // 시간설정 되어있을 시
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"); // 날짜 형식 설정
             localDateTime = LocalDateTime.parse(postSaveReqDto.getAppointmentTime(), dateTimeFormatter); // 입력받은 시간 정한형식으로 localDateTime으로 받기
            LocalDateTime now = LocalDateTime.now(); // 현재 시간 설정
            if(localDateTime.isBefore(now)){ // 현재시간보다 더 이전으로 설정할경우 예외터트리기
                throw new IllegalArgumentException("시간정보 잘못입력");
            }
            appointment = "Y";
        }

        Post post = Post.builder()
                .title(postSaveReqDto.getTitle())
                .contents(postSaveReqDto.getContents())
                .author(author)
                .appointment(appointment)
                .appointmentTime(localDateTime)
                .build();

//        더티체킹 테스트
//        author.updateMember("dirty checking test", "1234");
        postRepository.save(post);
    }

    public List<PostListResDto> findAll() {
        List<Post> posts = postRepository.findAll(); // select * from post
        List<PostListResDto> postListResDtos = new ArrayList<>();
        for(Post post : posts){
            PostListResDto postListResDto = new PostListResDto();
            postListResDto.setId(post.getId());
            postListResDto.setTitle(post.getTitle());
            postListResDto.setAuthor_email(post.getAuthor()==null? "익명유저" : post.getAuthor().getEmail());
            postListResDtos.add(postListResDto);
        }
        return postListResDtos;
    }
    public Page<PostListResDto> findByAppointment(Pageable pageable) {
//        Page객체 안에서 Map 지원.
        Page<Post> posts = postRepository.findByAppointment(null, pageable); // select * from post // null값만 조회
        Page<PostListResDto> postListResDtos
                = posts.map(p -> new PostListResDto(p.getId(), p.getTitle(), p.getAuthor()==null? "익명유저" : p.getAuthor().getEmail()));
        return postListResDtos;
    }

    public Page<PostListResDto> findAllPaging(Pageable pageable) {
//        Page객체 안에서 Map 지원.
        Page<Post> posts = postRepository.findAll(pageable); // select * from post
        Page<PostListResDto> postListResDtos
                = posts.map(p -> new PostListResDto(p.getId(), p.getTitle(), p.getAuthor()==null? "익명유저" : p.getAuthor().getEmail()));
        return postListResDtos;
    }

    public Post findById(Long id) throws EntityNotFoundException {
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("검색하신 ID의 회원이 없습니다."));
        return post;
    }

    public PostDetailResDto findPostDetail(Long id) throws EntityNotFoundException {
        Post post = postRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("검색하신 ID의 회원이 없습니다."));

        PostDetailResDto postDetailResDto = new PostDetailResDto();
        postDetailResDto.setId(post.getId());
        postDetailResDto.setTitle(post.getTitle());
        postDetailResDto.setContents(post.getContents());
        postDetailResDto.setCreatedTime(post.getCreatedAt());
        return postDetailResDto;
    }

    public void update(Long id, PostUpdateReqDto postUpdateReqDto) {
        Post post = this.findById(id);
        post.updatePost(postUpdateReqDto.getTitle(),postUpdateReqDto.getContents());
        postRepository.save(post);
    }

    public void delete(Long id) {
        postRepository.deleteById(id);
    }

}
