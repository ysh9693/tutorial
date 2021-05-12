package com.dgu.userapp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TourDao {

    private Connection conn;
    private PreparedStatement pstmt = null;
    private ResultSet rs;

    public TourDao() {

        try {
            Class.forName("oracle.jdbc.OracleDriver");
            String url = "jdbc:oracle:thin:@dalma.dongguk.ac.kr:1521:stud1";
            String user = "ysh9693";
            String password = "1234";
            conn = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException e) {
            System.out.println("오류 Message : " + e.getMessage());
            e.printStackTrace(System.out);
        } catch (SQLException e) {
            System.out.println("오류 SQLException : " + e.getSQLState());
            System.out.println("오류 Message : " + e.getErrorCode() + " - " + e.getMessage());
            e.printStackTrace(System.out);
        } finally {
            DBC.close(conn);
        }
    }

    //게시글 번호 부여 메소드
    public int getNext() {
        String sql = "select idx from tour order by idx desc";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
            return 1; //첫 번째 게시물인 경우
        } catch (SQLException e) {
            System.out.println("오류 SQLException : " + e.getSQLState());
            System.out.println("오류 Message : " + e.getErrorCode() + " - " + e.getMessage());
            e.printStackTrace(System.out);
        } finally {
            DBC.close(conn, pstmt, rs);
        }
        return -1; //데이터베이스 오류
    }

    public static void main(String[] args) {
        int pageNumber = 1;
        TourDao dao = new TourDao();
        ArrayList<TourBean> list = dao.getList(pageNumber);
    }

    public ArrayList<TourBean> getList(int pageNumber) {
        String sql = "select * from tour where rownum <= ?";
        ArrayList<TourBean> list = new ArrayList<TourBean>();
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, getNext() - (pageNumber - 1) * 10);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                TourBean tb = new TourBean();
                tb.setCity(rs.getString(1));
                tb.setYear(rs.getInt(2));
                tb.setBranch(rs.getInt(3));
                tb.setSum(rs.getInt(4));
                tb.setCitizen(rs.getInt(5));
                tb.setForeigner(rs.getInt(6));
                list.add(tb);
            }
        } catch (SQLException e) {
            System.out.println("오류 SQLException : " + e.getSQLState());
            System.out.println("오류 Message : " + e.getErrorCode() + " - " + e.getMessage());
            e.printStackTrace(System.out);
        } finally {
            DBC.close(conn, pstmt, rs);
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
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println("오류 SQLException : " + e.getSQLState());
            System.out.println("오류 Message : " + e.getErrorCode() + " - " + e.getMessage());
            e.printStackTrace(System.out);
        } finally {
            DBC.close(conn, pstmt, rs);
        }
        return false;
    }
}
