package com.javaex.dao;
import com.javaex.vo.UserVo;

public class TestDao {
	//insert다오 테스트
	public static void main(String[] args) {
		
		UserVo userVo = new UserVo("ccc", "1234", "최재호", "male");
		
		UserDao userDao = new UserDao();
		userDao.insert(userVo);

	}

}
