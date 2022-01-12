package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.javaex.dao.GuestbookDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.GuestbookVo;


@WebServlet("/guest")
public class GuestbookController extends HttpServlet {
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		System.out.println("/guestbook");
		//자바의 액션					//파라미터의 액션
		String action = request.getParameter("action");
	
		//방명록 삭제폼
		//방명록 등록폼
		if ("addList".equals(action)) {
			System.out.println("action > addList");

			//다오에 올린다
			GuestbookDao guestbookDao = new GuestbookDao();
			List<GuestbookVo> guestbookList = guestbookDao.gbList();
			
			//포워드 전 어트리뷰트 영역에 guestbookList를 gList라는 별명으로 넣어준다
			request.setAttribute("gList", guestbookList);
			
			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/guestbook/addList.jsp");
			System.out.println("forwarding");
		
		//방명록 등록자체
		} else if ("add".equals(action)) {
			System.out.println("action > add");

			String name = request.getParameter("name");
			String password = request.getParameter("password");
			String content = request.getParameter("content");
			
			//다오에 올린다
			GuestbookVo guestbookVo = new GuestbookVo(name, password, content);
			
			GuestbookDao guestbookDao = new GuestbookDao();
			guestbookDao.insert(guestbookVo);
		
			//리다이렉트
			WebUtil.redirect(request, response, "/mysite/guest?action=addList");
		
		} else if("deleteForm".equals(action)) {
			System.out.println("action > deleteForm");
			
			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/guestbook/deleteForm.jsp");
		
		} else if("delete".equals(action)) {
			System.out.println("action > delete");
			//파라미터 가져옴
			int no = Integer.parseInt(request.getParameter("no"));
			String password = request.getParameter("password");
			
			//여기서만 쓰게 셋터
			GuestbookVo vo = new GuestbookVo();
			vo.setNo(no);
			vo.setPassword(password);
			//다오에 올린다
			GuestbookDao dao = new GuestbookDao();
			dao.delete(vo);
			//리다이렉트
			response.sendRedirect("/mysite/guest?action=addList");
			System.out.println("삭제완료");
			
		} else {
			System.out.println("파라미터 없음");
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}

}
