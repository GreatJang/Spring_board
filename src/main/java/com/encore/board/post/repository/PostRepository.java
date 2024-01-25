package com.encore.board.post.repository;

import com.encore.board.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllByOrderByCreatedTimeDesc();

//  mysql : SELECT p.* from post p left join author a on p.author_id = a.id;
//    아래 jpql의 join문은 author객체를 통해 post를 스크리닝(걸러내는)하고 싶은 상황에 적합
    @Query("select p from Post p left join p.author") //jpql문
    List<Post> findAllJoin();
//  mysql : SELECT p.*, a.* from post p left join author a on p.author_id = a.id;
    @Query("select p from Post p left join fetch p.author") //jpql문 쿼리 1줄로 list전체 출력가능
    List<Post> findAllFetchJoin();
}
