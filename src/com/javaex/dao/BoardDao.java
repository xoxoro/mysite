package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.BoardVo;

public class BoardDao {

	//필드
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "webdb";
	private String pw = "webdb";
	
	//메소드 일반
	private void getConnection() {
		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			Class.forName(driver);

			// 2. Connection 얻어오기
			conn = DriverManager.getConnection(url, id, pw);
			// System.out.println("접속성공");

		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	public void close() {
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	public List<BoardVo> getList() {
		List<BoardVo> boardList = new ArrayList<BoardVo>();
		getConnection();
		
		try {
		
		String query = "";
		query += " select  us.no ";
		query += "         ,title ";
		query += "         ,content ";
		query += "         ,hit ";
		query += "         ,to_char(reg_date, 'yyyy-mm-dd AMHH24:MI:SS') reg_date ";
		query += "         ,bo.user_no ";
		query += "         ,name ";
		query += " from board bo, users us ";
		query += " where bo.user_no = us.no ";
		
		pstmt = conn.prepareStatement(query);
		rs = pstmt.executeQuery();
		
	
		
		while(rs.next()) {
			
			int no = rs.getInt("bo.user_no");
			String name = rs.getString("name");
			String title = rs. getString("title");
			String content = rs.getString("content");
			String regDate = rs.getString("reg_date");
			int hit = rs.getInt("hit");
			int userno = rs.getInt("bo.user_no");
			
			BoardVo boardVo = new BoardVo(no, title, content, hit, regDate, userno, name);
			
			boardList.add(boardVo);
			}
		}	catch (SQLException e) {
			System.out.println("error:" + e);
			}		
			
			
			close();
			return boardList;
	}
	
	//리스트 중 하나 (회원 no받고 출력)
	public BoardVo getList(int index) {
		
		BoardVo boardVo = null;
		getConnection();
		
		try {
			
			String query = "";
			query += " select  bo.no no ";
			query += "         ,title ";
			query += "         ,content ";
			query += "         ,hit ";
			query += "         ,to_char(reg_date, 'yyyy-mm-dd AMHH:MI:SS') reg_date ";
			query += "         ,bo.user_no user_no ";
			query += "         ,name ";
			query += " from board bo, users us ";
			query += " where bo.user_no = us.no ";
			query += " and	   bo.no = ? ";
			
			pstmt = conn.prepareStatement(query);
				
			pstmt.setInt(1, index);
			
			rs = pstmt.executeQuery();
			
			
		while(rs.next()) {
			int no = rs.getInt("no");
			String title = rs.getString("title");
			String content = rs. getString("content");
			int hit = rs.getInt("hit");
			String regDate = rs.getString("reg_date");
			int userno = rs.getInt("user_no");
			String name = rs.getString("name");
			
			
			boardVo = new BoardVo(no, title, content, hit, regDate, userno, name);
			
			System.out.println(boardVo);
			
		}
		}catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return boardVo;
	}

	
	//등록
	public int boardInsert(BoardVo boardVo) {
		int count = 0;
		getConnection();
		
		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " insert into board  ";
			query += " values (seq_board_no.nextval,?,?,?,sysdate,?) ";
			
			//쿼리문
			pstmt = conn.prepareStatement(query);
			//바인딩
			pstmt.setString(1, boardVo.getTitle());
			pstmt.setString(2, boardVo.getContent());
			pstmt.setInt(3, boardVo.getHit());
			pstmt.setInt(4, boardVo.getUserNo());
			
			count = pstmt.executeUpdate();
			
			} catch (SQLException e) {
				System.out.println("error:" + e);
			} 
		close();
		return count;
	}
	
	
	//삭제
	public int boardDelete(int i) {
		int count = 0;
		getConnection();
		
		try {
			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " delete from board ";
			query += " where no = ? ";
		
			pstmt = conn.prepareStatement(query); // 쿼리로 만들기
		
			pstmt.setInt(1, i); //첫번째 물음표
			count = pstmt.executeUpdate(); 
			System.out.println(count + "건 삭제되었습니다.(GuestDao)");
			
		}catch (SQLException e) {
		    System.out.println("error:" + e);
		}
		close();
		return count;
	}
	
	
	//게시판수정(modify)
	public int boardUpdate(BoardVo boardVo) {
		int count = 0;
		getConnection();
		
		
		try {
			
			String query = "";
			query += " update board ";
			query += " set title = ? ";
			query += "     ,content = ? ";
			query += " where no = ? ";
			
			pstmt = conn.prepareStatement(query); // 쿼리로 만들기
			
			pstmt.setString(1, boardVo.getTitle()); //첫번째 물음표
			pstmt.setString(2, boardVo.getContent()); //두번째 물음표
			pstmt.setInt(3, boardVo.getNo()); //세번째 물음표
			
			count = pstmt.executeUpdate();
			System.out.println(count + "건 삭제되었습니다.(GuestDao)");
			
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		close();
		return count;
	}
	
	//조회수올리기
	public int hitUpdate(int index) {
		int count = 0;
		getConnection();
		
		try {
			String query = "";
			query += " update board ";
			query += " set hit = NVL(hit, 0) + 1 ";
			query += " where no = ? ";
			
			pstmt = conn.prepareStatement(query); 
			
			pstmt.setInt(1, index); //1번째 물음표
			
			count = pstmt.executeUpdate(); 
			
		}catch (SQLException e) {
			System.out.println("error:" + e);
		}
		
		close();
		return count;
	}
	
}

		