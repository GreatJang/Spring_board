package com.encore.board.author.domain;


import com.encore.board.post.domain.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
//@Builder
//@AllArgsConstructor
//위와같이 모든 매개변수가 있는 생성자를 생성하는 어노테이션과 Builder를 클래스에 붙여
//모든 매개변수가 있는 생성자 위에 Builder어노테이션을 붙인것과 같은 효과가 있음.
//NoArgsConstructor는 따로 생성자를 생성할 필요가 없음.
public class Author {

//    builder어노테이션을 통해 빌더패턴으로 객체생성
//    매개변수의 세팅순서, 매개변수의 개수 등을 유연하게 세팅
    @Builder
    public Author(String name, String email, String password, Role role, List<Post> posts){
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.posts = posts;
    }
    public void updateMember(String name, String password){
        this.name = name;
        this.password = password;
    }

    @Id // pk
    @GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 20, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

//    author를 조회할 때 post객체가 필요할시에 선언
//    1:N관계이기 때문에 List설정
//    mappedBy에 연관관계의 주인을 명시하고, fk를 관리하는 변수명을 명시 // fk를 관리하는 쪽이 fk의 주인이다
    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @Setter // cascade.persist를 위한 테스트
    private List<Post> posts;

    @CreationTimestamp
    @Column(columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdTime;

    @UpdateTimestamp
    @Column(columnDefinition = "TIMESTAMP ON UPDATE CURRENT_TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime updatedTime;
}

