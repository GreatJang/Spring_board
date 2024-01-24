package com.encore.board.post.domain;

import com.encore.board.author.domain.Author;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
public class Post {
    @Builder
    public Post(String title, String contents, Author author) {
        this.title = title;
        this.contents = contents;
        this.author = author;
//        author객체의 posts를 초기화시켜준 후,
//        this.author.getPosts().add(this); // Author posts에 Setter를 사용하지 않고 사용하는 방법
    }
    public void updatePost(String title, String contents){
        this.title = title;
        this.contents = contents;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(nullable = false, length = 3000)
    private String contents;

//    author_id는 DB의 컬럼명, 별다른 옵션없을시 author의 pk에 fk가 설정
//    post객체 입장에서는 한사람이 여러개 글을 쓸수 있으므로 N:1 ManyToOne으로 설정
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
//    @JoinColumn(nullable=false, name="author_email", referenceColumnName="email")
    private Author author;

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedTime;

}
