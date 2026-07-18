<%--
  Created by IntelliJ IDEA.
  User: data8320-04
  Date: 26. 7. 16.
  Time: 오후 4:43
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="kopo.poly.util.CmmUtil" %>
<%
    //Controller로부터 전달받은 데이터
    String res = CmmUtil.nvl((String) request.getAttribute("res"));

    res = res.replaceAll("\n", " "); // 줄바꿈(엔터)를 한칸 공백 " "으로 변경
    res = res.replaceAll("\"", " "); // "(큰 따옴표) 특수문자를 한칸 공백 " "으로 변경
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>이미지 파일로부터 인식된 문자열 읽어주기</title>
    <link rel="stylesheet" href="/css/table.css"/>
    <script type="text/javascript" src="/js/jquery-3.6.0.min.js"></script>

    <script>


        const myOcrText = "<%=res%>";


        $(document).ready(function () {

            $("#btnTextRead").on("click", function () {
                speak(myOcrText);
            })
        })


        function speak(text) {
            if (typeof SpeechSynthesisUtterance === "undefined" ||
                typeof window.speechSynthesis === "undefined") {
                alert("이 브라우저는 문자읽기 기능을 지원하지 않습니다.");
                return;
            }

            window.speechSynthesis.cancel()

            const speechMsg = new SpeechSynthesisUtterance()
            speechMsg.rate = 1;
            speechMsg.pitch = 1;
            speechMsg.lang = "ko-KR";
            speechMsg.text = text;


            window.speechSynthesis.speak(speechMsg);
        }
    </script>
</head>
<body>
<h2>이미지 파일로부터 인식된 문자열 읽어주기</h2>
<hr/>
<br/>
<div class="divTable minimalistBlack">
    <div class="divTableHeading">
        <div class="divTableRow">
            <div class="divTableHead">이미지로부터 텍스트 인식 결과</div>
        </div>
    </div>
    <div class="divTableBody">
        <div class="divTableRow">
            <div class="divTableCell"><%=res%>
            </div>
        </div>
    </div>
</div>
<div>
    <button id="btnTextRead" type="button">문자열 읽기</button>
</div>
</body>
</html>
