package com.javaex.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.UserDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.UserVo;


@WebServlet("/user")
public class UserController extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("/user");
		
				//자바의 액션					//파라미터의 액션
		String action = request.getParameter("action");
		
		if("joinForm".equals(action)) {
			System.out.println("user>joinForm");
			
			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/user/joinForm.jsp");
		
		}else if("join".equals(action)) {
			System.out.println("user > join");
			
			//겟파라미터 꺼내기
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			String name = request.getParameter("name");
			String gender = request.getParameter("gender");
			//파라미터-->vo로 묶어주기
			UserVo userVo = new UserVo(id, password, name, gender);
			//테스트해보고 이상없으면 다오 저장하기
			//System.out.println(userVo);
			
			//UserDao 의 insert() 로 저장하기(회원가입하기)
			UserDao userDao = new UserDao();
			int count = userDao.insert(userVo);
			
			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/user/joinOk.jsp");
			
		}else if("loginForm".equals(action)) {
			System.out.println("user > loginForm");
			
			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/user/loginForm.jsp");
			
		}else if("login".equals(action)) {
			System.out.println("user > login");
			
			//파라미터 보냈으니 꺼내기
			String id = request.getParameter("id");
			String password = request.getParameter("password");
			
			//System.out.println(id);
			//System.out.println(password);
			
			UserDao userDao = new UserDao();
			//getUser를 만들어서 아이디와 비밀번호 넣어줌
			UserVo authVo = userDao.getUser(id,password);
			
			//System.out.println(authVo);
			
			//성공시 메인으로 리다이렉트, 실패시 리다이렉트 /user?action=loginform&result=fail
			//데이터있으면 세션영역에 값 넣는다(로그인성공)
			//데이타가 없으면 세션에 아무것도 안한다(로그인실패)
			if(authVo == null) { //로그인실패
				System.out.println("로그인실패");
				
				WebUtil.redirect(request, response, "/mysite/user?action=loginForm&result=fail");
			}else { //로그인성공
				System.out.println("로그인성공");

				//HttpSession은 접근할 세션의 주소값을 알려준다(http의 jsessionid)
				HttpSession session = request.getSession();
				//어트리뷰트는 그냥 저장공간으로 이해하자 왼쪽은 키, 오른쪽은 값
				session.setAttribute("authUser", authVo);
				
				WebUtil.redirect(request, response, "/mysite/main");
			}
			
			
		}else if("logout".equals(action)) {
				System.out.println("user > logout");
				
				//HttpSession은 접근할 세션의 주소값을 알려준다(http의 jsessionid)
				HttpSession session = request.getSession();
				//어트리뷰트는 그냥 저장공간으로 이해하자 왼쪽은 키, 오른쪽은 값
				session.removeAttribute("authUser");
				session.invalidate();
				
				WebUtil.redirect(request, response, "/mysite/main");
		}
			
	}
	

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
