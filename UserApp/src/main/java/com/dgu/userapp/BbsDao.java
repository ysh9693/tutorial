package com.dgu.userapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.util.ArrayList;

public class BbsDao {
    private Connection conn;
    private ResultSet rs;
    
    //기본생성자
    public BbsDao(){
        try{
            Class.forName("oracle.jdbc.OracleDriver");
            String url="jdbc:oracle:thin:@dalma.dongguk.ac.kr:1521:stud1";
            String user ="ysh9693";
            String password = "1234";
            conn = DriverManager.getConnection(url,user,password);
            
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    //작성일자 메소드
    public String getDate(){
        String sql = "select now()";
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if(rs.next()){
                return rs.getString(1);
            }
        }catch( Exception e){
            e.printStackTrace();
        }
        return "";
    }
    //게시글 번호 부여 메소드
    public int getNext(){
        //현재 게시글을 내림차순으로 조회하여 가장 마지막 글의 번호를 구한다
        String sql = "select now()";
        try{
            PreparedStatement pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if(rs.next()){
                return rs.getInt(1) + 1 ;
            }
            return 1; //첫번째 게시물인 경우
        }catch( Exception e){
            e.printStackTrace();
        }
        return -1; //데이터베이스 오류
    }
    //글쓰기 메소드
    public int write(String bbsTitle, String userID, String bbsContent) {
	String sql = "insert into bbs values(?, ?, ?, ?, ?, ?)";
	try {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, getNext());
		pstmt.setString(2, bbsTitle);
		pstmt.setString(3, userID);
		pstmt.setString(4, getDate());
		pstmt.setString(5, bbsContent);
		pstmt.setInt(6, 1); //글의 유효번호
		return pstmt.executeUpdate();
	}catch (Exception e) {
		e.printStackTrace();
	}
	return -1; //데이터베이스 오류
	}
    //게시글 리스트 메소드
    public ArrayList<BbsBean> getList(int pageNumber){
	String sql = "select * from bbs where bbsID < ? and bbsAvailable = 1 order by bbsID desc limit 10";
    	ArrayList<BbsBean> list = new ArrayList<BbsBean>();
	try {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
		rs = pstmt.executeQuery();
		while(rs.next()) {
			BbsBean bbs = new BbsBean();
			bbs.setBbsID(rs.getInt(1));
			bbs.setBbsTitle(rs.getString(2));
			bbs.setUserID(rs.getString(3));
			bbs.setBbsDate(rs.getString(4));
			bbs.setBbsContent(rs.getString(5));
			bbs.setBbsAvailable(rs.getInt(6));
			list.add(bbs);
		}
	}catch (Exception e) {
		e.printStackTrace();
	}
	return list;
    }
	
    //페이징 처리 메소드
    public boolean nextPage(int pageNumber) {
	String sql = "select * from bbs where bbsID < ? and bbsAvailable = 1";
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
