/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dgu.userapp;

import java.sql.*;

public class UserDao {
    private Connection conn; //자바와 데이터베이스를 연결
    private PreparedStatement pstmt; //쿼리문 대기 및 설정
    private ResultSet rs; //결과값 받아오기
    
    //기본 생성자
    //UserDao가 실행되면 자동으로 생성되는 부분
    //메소드마다 반복되는 코드를 이곳에 넣으면 코드가 간소화된다
    public UserDao(){
        try{
            Class.forName("oracle.jdbc.OracleDriver");
            String url="jdbc:oracle:thin:@dalma.dongguk.ac.kr:1521:stud1";
            String user ="ysh9693";
            String password = "1234";
            String sql2 = "select * from users where id=?";
            conn = DriverManager.getConnection(url,user,password);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    
	public int login(String userID, String userPassword) {
		String sql = "select userPassword from users where userID = ?";
		try {
			pstmt = conn.prepareStatement(sql); //sql쿼리문을 대기 시킨다
			pstmt.setString(1, userID); //첫번째 '?'에 매개변수로 받아온 'userID'를 대입
			rs = pstmt.executeQuery(); //쿼리를 실행한 결과를 rs에 저장
			if(rs.next()) {
				if(rs.getString(1).equals(userPassword)) {
					return 1; //로그인 성공
				}else
					return 0; //비밀번호 틀림
			}
			return -1; //아이디 없음
		}catch (Exception e) {
			e.printStackTrace();
		}
		return -2; //오류
	}
        
        public int join(UserBean user) {
            String sql = "insert into users values(?, ?, ?)";
            try {
                   pstmt = conn.prepareStatement(sql);
                   pstmt.setString(1, user.getUserID());
                   pstmt.setString(2, user.getUserPassword());
                   pstmt.setString(3, user.getUserName());
                   return pstmt.executeUpdate();
            }catch (Exception e) {
                e.printStackTrace();
            }
            return -1;
        }
}
