<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>꿀팁, 후기</title>
</head>
<body>
	<c:import url="/navbar"/>
	<section style="margin: 20px 10% 10% 10%">
		<c:if test="${pnum == 0}" >
			<h1>꿀팁, 후기 등록</h1>
			<form action="/community/review/addReviewPro" name="addReview" 
				method="post" encType="multipart/form-data" onSubmit="return check()">
				<jsp:include page="/WEB-INF/views/board/addBoardForm.jsp" flush="false"/>
				<input type="hidden" name="board_type" value="4"/>
			</form>
			<input type="button" class="btn btn-primary" style="float:right" value="글 목록" onclick="window.location='/community/review/reviewList?board_type=4' "/>
		</c:if>
		
		<c:if test="${pnum != 0}" >
			<h1>꿀팁, 후기 답글</h1>
			<form action="/community/review/addReviewPro" name="addReview" 
				method="post" encType="multipart/form-data" onSubmit="return check()">
				<jsp:include page="/WEB-INF/views/board/addBoardForm.jsp" flush="false"/>
				<input type="hidden" name="board_type" value="4"/>
				<input type="hidden" name="post_group" value="${board.post_group}">
				<input type="hidden" name="post_level" value="${board.post_level}">
			</form>
			<input type="button" class="btn btn-primary" style="float:right" value="글 목록" onclick="window.location='/community/review/reviewList?board_type=4' "/>
		</c:if>
	</section>
	<jsp:include page="/WEB-INF/views/footer.jsp" />
</body>
</html>
