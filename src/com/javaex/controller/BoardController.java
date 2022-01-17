package com.javaex.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.javaex.dao.BoardDao;
import com.javaex.util.WebUtil;
import com.javaex.vo.BoardVo;
import com.javaex.vo.UserVo;

@WebServlet("/board")
public class BoardController extends HttpServlet {

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		String act = request.getParameter("action");
		
		if("list".equals(act)) {
			
			List<BoardVo> boardList = new BoardDao().getList();

			request.setAttribute("boardList", boardList);

			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/board/list.jsp");
			 
			System.out.println(boardList);
		
		} else if("read".equals(act)) {
			
			int no = Integer.parseInt(request.getParameter("no"));
			
			BoardVo boardVo = new BoardDao().getList(no);
			BoardDao boardDao = new BoardDao();
			
			request.setAttribute("boardVo", boardVo);
			boardDao.hitUpdate(no);
			
			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/board/read.jsp");
		
			System.out.println("게시판에 쓴글 읽기");
		
		} else if("modifyForm".equals(act)) {
			
			int no = Integer.parseInt(request.getParameter("no"));

			BoardVo boardVo = new BoardDao().getList(no);
			request.setAttribute("boardVo", boardVo);
			
			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/board/modifyForm.jsp");
			
			System.out.println("modifyForm");
		
		
		} else if("modify".equals(act)) {
			
			System.out.println("modify");
			
			request.setCharacterEncoding("UTF-8");
			
			int no = Integer.parseInt(request.getParameter("id"));
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			
			
			BoardVo boardVo = new BoardVo();
			boardVo.setNo(no);
			boardVo.setContent(content);
			boardVo.setTitle(title);
			
			BoardDao boardDao = new BoardDao();
			
			boardDao.boardUpdate(boardVo);
			
			//리다이렉트
			WebUtil.redirect(request, response, "/mysite/board?action=list");
			
		} else if("writeForm".equals(act)) {
			//포워드
			WebUtil.forward(request, response, "/WEB-INF/views/board/writeForm.jsp");
		
			System.out.println("writeForm");
			
		} else if("boardWrite".equals(act)) {
			
			System.out.println("boardWrite");
			
			
			HttpSession session = request.getSession();
			UserVo authUser = (UserVo) session.getAttribute("authUser");
			
			String title = request.getParameter("title");
			String content = request.getParameter("content");
			int userNo = authUser.getNo();
			
			BoardVo boardVo = new BoardVo();
			boardVo.setTitle(title);
			boardVo.setContent(content);
			boardVo.setUserNo(userNo);
			
			BoardDao boardDao = new BoardDao();
			boardDao.boardInsert(boardVo);
			
			WebUtil.redirect(request, response, "/mysite/board?action=list");		
		
		} else if("delete".equals(act)) {
			
			System.out.println("삭제중...");
			
			int no = Integer.parseInt(request.getParameter("no"));
			
			BoardDao boardDao = new BoardDao();
			boardDao.boardDelete(no);
			
			//리다이렉트
			WebUtil.redirect(request, response, "/mysite/board?action=list");
		}
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		doGet(request, response);
	}

}