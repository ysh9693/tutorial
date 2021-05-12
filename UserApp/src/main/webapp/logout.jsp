<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>관광객 현황 통계 시스템</title>
    </head>
    <body>
        <%
            session.invalidate();
        %>
        <script>
            alert('로그아웃 되었습니다');
            location.href="main.jsp";
        </script>
        
    </body>
</html>
