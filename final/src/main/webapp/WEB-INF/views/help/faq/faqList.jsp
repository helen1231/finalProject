<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>자주하는 질문</title>
</head>
<body>
	<c:import url="/navbar"/>
	<section style="margin-left:5%;margin-right:5%;margin-bottom:5%">
		<h1>자주하는 질문 목록(전체 글:${count})</h1>
		<c:if test="${memberStatus == 1}">
			<button class="btn btn-primary" style="float: right" onclick="window.location='/help/faq/addFaq'">글쓰기</button>
		</c:if>
		<c:if test="${count == 0}">
			<table class="table table-hover">
				<thead>
					<tr>
						<td><h2 style="text-align: center">공지사항이 없습니다.</h2></td>
					</tr>
				</thead>
			</table>
		</c:if>
		
		<table class="table table-hover">
			<thead>
				<c:if test="${count > 0}">
					<tr>
						<th>글번호</th>
						<th>제 목</th>
						<th>작성자</th>
						<th>작성일</th>
						<th>조회수</th>
					</tr>
				</c:if>
			</thead>
			<tbody>
				<c:forEach var="board" items="${boardList}">
					<tr>
						<td>${board.pnum}</td>
						<td><a href="/help/faq/faqContent?pnum=${board.pnum}&pageNum=${currentPage}">${board.subject}</a></td> 
						<td>${board.writer}</td>
						<td><fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${board.reg}"/></td>
						<td>${board.readCnt}</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		
		<form action="/help/faq/searchList" method="get">
			<div class="row">
				<select name="search" class="form-control" style="max-width:8%">
					<option value="">==검색==</option>
					<option value="subject">제목</option>
					<option value="post_content">내용</option>
				</select>
				<input type="text" name="keyword" class="form-control" style="max-width:10%"/>
				<input type="submit" value="검색" class="btn btn-primary"/>
			</div>
			<input type="hidden" name="board_type" value="2"/>
		</form>
		
		<c:if test="${count > 0}">
			<c:set var="pageCount" value="${count / pageSize + (count % pageSize == 0 ? 0 : 1)}"/>
			<fmt:parseNumber var="result" value="${(currentPage/10)}" integerOnly="true" />
			<c:set var="startPage" value="${result*10+1}" />
			<c:set var="pageBlock" value="${10}" />	
			<c:set var="endPage" value="${startPage + pageBlock - 1}" />
			
			<c:if test="${endPage > pageCount}">
				<c:set var="endPage" value="${pageCount}" />
			</c:if>
			
			<ul class="pagination justify-content-center">
				<c:if test="${startPage > 10}" >
					<li class="page-item">
			        	<a class="page-link" href="/help/faq/faqList?board_type=2&pageNum=${startPage - 10}">이전</a>
			    	</li>
			    </c:if>
			    
			    <c:forEach var="i" begin="${startPage}" end="${endPage}" step="1">    
			        <li class="page-item">
			        	<a class="page-link" href="/help/faq/faqList?board_type=2&pageNum=${i}">${i}</a>
			    	</li>
			    </c:forEach>
			    
			    <c:if test="${endPage < pageCount}" >
			    	<li class="page-item">
			       		<a class="page-link" href="/help/faq/faqList?board_type=2&pageNum=${startPage + 10 }">다음</a>
			    	</li>
			    </c:if>
			 </ul>
		</c:if>
	</section>
	<jsp:include page="/WEB-INF/views/footer.jsp" />
</body>
</html>