package com.encore.board.common;


import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//로그인이 성공했을때 실행할 로직 정의
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        HttpSession httpSession = request.getSession();
//        httpSession 저장소에 값 저장
//        authentication객체 안에는 User객체가 들어가 있고, 여기서 getName은 email을 의미.
        httpSession.setAttribute("email", authentication.getName());
        response.sendRedirect("/"); // 홈화면으로 리다이렉트
    }
}
