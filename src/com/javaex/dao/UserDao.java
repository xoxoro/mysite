package com.javaex.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.javaex.vo.UserVo;

public class UserDao {
	
	//필드
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "webdb";
	private String pw = "webdb";
	
	//생성자
	
	//메소드gs
	
	//메소드일반
	
	//연결하기
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
	
	//저장 메소드(회원가입)
	public int insert(UserVo userVo) {
		
		int count = 0;
		
		getConnection();
		
		try {
			//3. sql문 준비 / 바인딩 / 실행
			//문자열
			String query = "";
			query += " insert into users ";
			query += " values(seq_users_no.nextval, ?, ?, ?, ? ) ";
			System.out.println("db받기성공");
			
			//쿼리문
			pstmt = conn.prepareStatement(query);
			
			//바인딩
			pstmt.setString(1, userVo.getId()); //1번째 물음표
			pstmt.setString(2, userVo.getPassword()); //2번째 물음표
			pstmt.setString(3, userVo.getName()); //3번째 물음표
			pstmt.setString(4, userVo.getGender()); //4번째 물음표
			
			//실행
			count = pstmt.executeUpdate();
			
			//4. 결과처리
			System.out.println(count + "건이 등록되었습니다(UserDao)");
			
		} catch (SQLException e) {
			System.out.println("error: " + e);
		}

		close();
		
		return count;
		
	}
	
	//회원정보 1명 가져오기(로그인용)
	public UserVo getUser(String id, String password) {
		
		UserVo userVo = null;//초기값이 없어서 진행이 안될수있으니 null값 넣어줌
		getConnection();
		
		try {
		
			//3. SQL문 준비 / 바인딩 / 실행
			//문자열
			String query = "";
			query += " select no, ";
			query += "       name ";
			query += " from users  ";
			query += " where id = ? ";
			query += " and password = ? ";
			
			//쿼리문
			pstmt = conn.prepareStatement(query);
			
			//바인딩
			pstmt.setString(1, id); //1번째 물음표
			pstmt.setString(2, password); //2번째 물음표
			
			//실행
			rs = pstmt.executeQuery();//select문이니까 쿼리
			
			//결과처리
			while(rs.next()) {
				int no = rs.getInt("no");
				String name = rs.getString("name");
				
				userVo = new UserVo();
				//생성자에 안쓰고 여기서만 쓸거같으면 set을 만들어줌
				userVo.setNo(no);
				userVo.setName(name);
			}	
				
		} catch (SQLException e) {
				System.out.println("error: " + e);
		}
		close();
		return userVo;
	}
	
}
