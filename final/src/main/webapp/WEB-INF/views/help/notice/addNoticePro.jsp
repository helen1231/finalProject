<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항</title>
</head>
<body>
<c:if test="${result == 0 }">
	<script>
	  alert("공지사항 등록에 실패했습니다. 다시 입력해주세요!");
	  history.go(-1);
	</script>
</c:if>
<c:if test="${result == 1 }">
	<script>
		alert("공지사항 등록이 완료되었습니다.");
		window.location="/help/notice/noticeList?board_type=1";
	</script>
</c:if>
</body>
</html>