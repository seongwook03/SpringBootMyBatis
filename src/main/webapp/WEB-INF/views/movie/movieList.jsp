<%--
  Created by IntelliJ IDEA.
  User: data8320-04
  Date: 26. 7. 12.
  Time: 오후 8:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="kopo.poly.dto.MovieDTO" %>
<%@ page import="java.util.List" %>
<%@ page import="kopo.poly.util.CmmUtil" %>
<%@ page import="kopo.poly.dto.FoodDTO" %>
<%
    List<MovieDTO> rList = (List<MovieDTO>) request.getAttribute("rList");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>영화 순위 결과</title>
    <link rel="stylesheet" href="/css/table.css"/>
</head>
<body>
<h2>영화 순위 정보</h2>
<hr/>
<br/>
<div class="divTable minimalistBlack">
    <div class="divTableHeading">
        <div class="divTableRow">
            <div class="divTableHead">순위</div>
            <div class="divTableHead">제목</div>
            <!-- <div class="divTableHead">평점</div> -->
            <div class="divTableHead">개봉일</div>
        </div>
    </div>
    <div class="divTableBody">
        <%
            for (MovieDTO rDTO : rList) {
        %>
        <div class="divTableRow">
            <div class="divTableCell"><%=CmmUtil.nvl(rDTO.getMovieRank())%>
            </div>
            <div class="divTableCell"><%=CmmUtil.nvl(rDTO.getMovieNm())%>
            </div>
            <!-- <div class="divTableCell"><%=CmmUtil.nvl(rDTO.getScore())%>
            </div> -->
            <div class="divTableCell"><%=CmmUtil.nvl(rDTO.getOpenDay())%>
            </div>
        </div>
        <%
            }
        %>
    </div>
</div>
</body>
</html>
