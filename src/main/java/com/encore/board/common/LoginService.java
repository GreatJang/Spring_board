package com.encore.board.common;

import com.encore.board.author.domain.Author;
import com.encore.board.author.service.AuthorService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class LoginService implements UserDetailsService {
    private final AuthorService authorService;

    public LoginService(AuthorService authorService) {
        this.authorService = authorService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Author author = authorService.findByEmail(username);
        List<GrantedAuthority> authorities = new ArrayList<>();
////        ROLE_권한 이 패턴으로 스프링에서 기본적으로 권한체크
        authorities.add(new SimpleGrantedAuthority("ROLE_" + author.getRole()));

////        매개변수 : userEmail, userPassWord, 권한(authorities)
////        해당 메서드에서 return되는 User객체는 session 메모리 저장소에 저장되어, 계속 사용 가능
        return new User(author.getEmail(), author.getPassword(), authorities);
    }
}
