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
    public ArrayList<TourDao> getList(int pageNumber){
	String sql = "select * from tourist";
    	ArrayList<TourDao> list = new ArrayList<TourDao>();
	try {
		PreparedStatement pstmt = conn.prepareStatement(sql);
		rs = pstmt.executeQuery();
		while(rs.next()) {
			TourBean tb = new TourBean();
			tb.setCity(rs.getString(1));
			tb.setYear(rs.getInt(2));
			tb.setBranch(rs.getInt(3));
			tb.setSum(rs.getInt(4));
			tb.setCitizen(rs.getInt(5));
			tb.setForeigner(rs.getInt(6));
		}
	}catch (Exception e) {
		e.printStackTrace();
	}
	return list;
    }
}
