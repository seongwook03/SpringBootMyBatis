<%--
  Created by IntelliJ IDEA.
  User: data8320-04
  Date: 26. 7. 12.
  Time: 오후 8:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="kopo.poly.util.CmmUtil" %>
<%
    String msg = CmmUtil.nvl((String) request.getAttribute("msg"));
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>CGV 영화 수집 결과</title>
<body>
<%=msg%>
</body>
</html>