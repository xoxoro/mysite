package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javaex.vo.GuestbookVo;

public class GuestbookDao {
		
		// 필드
		private Connection conn = null;
		private PreparedStatement pstmt = null;
		private ResultSet rs = null;

		private String driver = "oracle.jdbc.driver.OracleDriver";
		private String url = "jdbc:oracle:thin:@localhost:1521:xe";
		private String id = "webdb";
		private String pw = "webdb";

		// 메소드 일반
		private void getConnection() {
			try {
				Class.forName(driver);
				conn = DriverManager.getConnection(url, id, pw);

			} catch (ClassNotFoundException e) {
				System.out.println("error: 드라이버 로딩실패 - " + e);
			} catch (SQLException e) {
				System.out.println("error: " + e);
			}
		}

		private void close() {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				System.out.println("error." + e);
			}
		}
		
		// 리스트 가져오기
			public List<GuestbookVo> gbList() {
				List<GuestbookVo> getList = new ArrayList<GuestbookVo>();
				getConnection();

				try {
					String query = "";
					query += "select  no, ";
					query += "        name, ";
					query += "        content,";
					query += "        to_char(reg_date, 'YYYY-MM-DD AMHH24:MI:SS') reg_date ";
					query += " from guestbook ";
					query += " order by reg_date desc";

					pstmt = conn.prepareStatement(query);
					rs = pstmt.executeQuery();

					while (rs.next()) {
						int no = rs.getInt("no");
						String name = rs.getString("name");
						String content = rs.getString("content");
						String regDate = rs.getString("reg_date");

						GuestbookVo vo = new GuestbookVo();
						vo.setNo(no);
						vo.setName(name);
						vo.setContent(content);
						vo.setRegDate(regDate);
					  //GuestbookVo vo = new GuestbookVo(no, name, password, content, regDate);
						
						getList.add(vo);
					}
				} catch (SQLException e) {
					System.out.println("error: " + e);
				}

				close();
				return getList;
			}
			
			
		// 등록
		public int insert(GuestbookVo vo) {
			
			int count = 0;
			
			getConnection();

			try {
				String query = "";
				query += " insert into guestbook ";
				query += " values(seq_guestbook_no.nextval, ?, ?, ?, sysdate)";
				System.out.println(query);
				
				pstmt = conn.prepareStatement(query);
				pstmt.setString(1, vo.getName()); //1번째 물음표
				pstmt.setString(2, vo.getPassword()); //2번째 물음표
				pstmt.setString(3, vo.getContent()); //3번째 물음표

				//실행
				count = pstmt.executeUpdate();
				
				//4. 결과처리
				System.out.println(count + "건이 등록되었습니다(GuestbookDao)");
				
			} catch (SQLException e) {
				System.out.println("error: " + e);
			}

			close();
			return count;
		}

		// 삭제
		// 넘버와 비밀번호가 일치
		public int delete(GuestbookVo vo) {
			
			int count = 0;

			getConnection();

			try {

				
				String query = "";
				query += "delete from guestbook ";
				query += " where no = ?";	
				query += " and password = ?";
				
				pstmt = conn.prepareStatement(query);
				pstmt.setInt(1, vo.getNo()); //1번째 물음표
				pstmt.setString(2, vo.getPassword()); //2번째 물음표
				
				count = pstmt.executeUpdate();
				System.out.println(count + "건 삭제되었습니다.(GuestDao)");
				
			} catch (SQLException e) {
				System.out.println("error: " + e);
			}

			close();
			return count;
		}
}
