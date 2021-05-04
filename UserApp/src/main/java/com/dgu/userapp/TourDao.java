package com.dgu.userapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class TourDao {
    private Connection conn;
    private ResultSet rs;
    
    public TourDao(){
    
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
    //게시글 번호 부여 메소드
    public int getNext() {
	//현재 게시글을 내림차순으로 조회하여 가장 마지막 글의 번호를 구한다
	String sql = "select city from tour order by city desc";
	try {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();
		if(rs.next()) {
			return rs.getInt(1) + 1;
		}
		return 1; //첫 번째 게시물인 경우
	}catch (Exception e) {
		e.printStackTrace();
	}
	return -1; //데이터베이스 오류
    }
    
    public ArrayList<TourBean> getList(int pageNumber){
	String sql = "select * from tourist where rownum <=10";
    	ArrayList<TourBean> list = new ArrayList<TourBean>();
	try {
		PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
		rs = pstmt.executeQuery();
		while(rs.next()) {
			TourBean tb = new TourBean();
			tb.setCity(rs.getString(1));
			tb.setYear(rs.getInt(2));
			tb.setBranch(rs.getInt(3));
			tb.setSum(rs.getInt(4));
			tb.setCitizen(rs.getInt(5));
			tb.setForeigner(rs.getInt(6));
                        list.add(tb);
		}
	}catch (Exception e) {
		e.printStackTrace();
	}
	return list;
    }
    //페이징 처리 메소드
    public boolean nextPage(int pageNumber) {
	String sql = "select * from tour";
	try {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
		rs = pstmt.executeQuery();
		if(rs.next()) {
			return true;
		}
	}catch (Exception e) {
		e.printStackTrace();
	}
	return false;
    }
}
