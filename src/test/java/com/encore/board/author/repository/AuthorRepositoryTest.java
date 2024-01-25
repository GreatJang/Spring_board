package com.encore.board.author.repository;

import com.encore.board.author.domain.Author;
import com.encore.board.author.domain.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

//DataJpaTest 어노테이션을 사용하면 매 테스트가 종료되면 자동으로 DB 원상복구
//모든 스프링빈을 생성하지 않고, DB테스트 특화 어노테이션
@DataJpaTest
//replace = AutoConfigureTestDatabase.Replace.ANY : H2DB(spring 내장 인메모리)가 기본설정
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
//@SpringBootTest어노테이션은 자동롤백기능은 지원하지 않고, 별도로 롤백 코드 또는 어노테이션
//@SpringBootTest어노테이션은 실제 스프링 실행과 동일하게 스프링빈 생성 및 주입
//@SpringBootTest
//@Transactional // 롤백
@ActiveProfiles("test") // application-test.yml 파일을 찾아 설정값 세팅
public class AuthorRepositoryTest {

    @Autowired
    private AuthorRepository authorRepository; // 검증 대상 repository
//    save 와 findByEmail 기능검증

    @Test
    public void authorSaveTest(){
//        객체를 만들어서 save -> 재조회해서 -> 만든 객체와 비교
//        1. 준비(given, prepare)
        Author author = Author.builder()
                .email("leekang333@naver.com")
                .name("leekang")
                .password("lee1234")
                .role(Role.ADMIN)
                .build();
//        2. 실행(when, execute)
        authorRepository.save(author);
        Author authorDb = authorRepository.findByEmail("leekang333@naver.com").orElse(null);
//        3. 검증(then)
//        Assertions클래스의 기능을 통해 오류의 원인파악, null처리, 가시적으로 성공/실패 여부확인
//        assertEquals : 삽입한 값과 DB에 들어간 값이 같으면 통과 다르면 에러
        Assertions.assertEquals(author.getEmail(), authorDb.getEmail());
    }

}
