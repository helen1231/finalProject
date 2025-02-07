<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>1:1 문의</title>
</head>
<body>
	<c:import url="/navbar"/>
	<section style="margin: 20px 10% 10% 10%">	
		<c:if test="${pnum == 0}" >
			<h1>1:1 문의 등록</h1>
			<form action="/help/qna/addQnaPro" name="addQna" 
				method="post" encType="multipart/form-data" onSubmit="return check()">
				<jsp:include page="/WEB-INF/views/board/addBoardForm.jsp" flush="false"/>
				<input type="hidden" name="board_type" value="3"/>
			</form>
			<input type="button" class="btn btn-primary" style="float:right" value="글 목록" onclick="window.location='/help/qna/qnaList?board_type=3' "/>
		</c:if>
		
		<c:if test="${pnum != 0}" >
			<h1>1:1 문의 답글</h1>
			<form action="/help/qna/addQnaPro" name="addQna" 
				method="post" encType="multipart/form-data" onSubmit="return check()">
				<h2>고객문의 원글</h2>
				<jsp:include page="/WEB-INF/views/board/boardContent.jsp" flush="false"/>
				<br/>
				<h2>답글 작성</h2>
				<jsp:include page="/WEB-INF/views/board/addBoardForm.jsp" flush="false"/>
				<input type="hidden" name="board_type" value="3"/>
				<input type="hidden" name="post_group" value="${board.post_group}">
				<input type="hidden" name="post_level" value="${board.post_level}">
			</form>
			<input type="button" class="btn btn-primary" style="float:right" value="글 목록" onclick="window.location='/help/qna/qnaList?board_type=3' "/>
		</c:if>
	</section>
	<jsp:include page="/WEB-INF/views/footer.jsp" />
</body>
</html>
