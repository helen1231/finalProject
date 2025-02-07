<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<head>
	<meta charset="UTF-8">
	<title>전체회원 목록</title>
</head>
	<jsp:include page="../adminNavBar.jsp"/>
	<section style="margin-left:100px;margin-right:100px">
		<h1>회원목록 [총:${count}명]</h1>
		<form action="/admin/member/search" method="post">
			<div class="row">
				<select name="search" class="form-control" style="max-width:8%">
					<option value="memid">ID</option>
					<option value="mem_name">이름</option>
					<option value="mobile">전화번호</option>
					<option value="email">이메일</option>
				</select>
				<input type="text" class="form-control" name="keyword" style="max-width:8%">
				<input type="submit" class="btn btn-primary" value="검색">
			</div>
		</form>
		<table class="table table-hover">
			<thead>
				<tr>
					<th>ID</th>	
					<th>이름</th>	
					<th>이메일</th>	
					<th>우편번호</th>
					<th>주소</th>	
					<th>핸드폰번호</th>	
					<th>
						<select class="form-control" name="status" onchange="location.href=this.value">
							<c:if test="${status == null }">
								<option value="list">==전체회원==</option>
							</c:if>
							<c:if test="${status != null }">
								<option>==${status_name }==</option>
							</c:if>
							<option value="list">전체회원</option>
							<option value="list?status=0">일반</option>
							<option value="list?status=2">활동중지</option>
							<option value="list?status=3">탈퇴</option>
							<option value="list?status=4">강제탈퇴</option>
							<option value="list?status=5">휴면</option>
						</select>
					</th>	
					<th>포인트</th>	
					<th>가입일</th>		
				</tr>
			</thead>
			<tbody>
				<c:forEach var="dto" items="${list }">
					<tr>
						<td><a href="/admin/member/info?memid=${dto.memid }">${dto.memid }</a></td>
						<td>${dto.mem_name }</td>
						<td>${dto.email }</td>
						<td>${dto.postalCode }</td>
						<td>${dto.address }</td>
						<td>${dto.mobile }</td>
						<td>
							${dto.status_name }
							(ref:<fmt:formatDate pattern="yyyy-MM-dd" value="${dto.ref_date }"/>)
						</td>
						<td>${dto.mem_point }</td>
						<td>
							<fmt:formatDate pattern="yyyy-MM-dd" value="${dto.regdate }"/>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
		<c:if test="${count > 0}">
			<c:set var="pageCount" value="${count / page.pageSize + ( count % page.pageSize == 0 ? 0 : 1)}"/>
			
			<!-- int만 나올 수 있도록 fmt 통해서 포맷팅해줌 -->
			<fmt:parseNumber var="result" value="${page.currentPage/10}" integerOnly="true"/>
			<c:set var="startPage" value="${result*10+1}"/>
			
			<c:set var="pageBlock" value="${10}"/>
			<c:set var="endPage" value="${startPage + pageBlock-1}"/>
			
			<c:if test="${endPage > pageCount}">
				<c:set var="endPage" value="${pageCount}" />
			</c:if>
			
	        <ul class="pagination justify-content-center">
		        <c:if test="${startPage > 10 }">
		        	<li class="page-item">
		        		<a class="page-link" href="/admin/member/list?pageNum=${startPage-10}&status=${status}">이전</a>
					</li>
		        </c:if>
		        
		        <c:forEach var="i" begin="${startPage}" end="${endPage}" step="1" >
		        	<li class="page-item">
		        		<a class="page-link" href="/admin/member/list?pageNum=${i}&status=${status}">${i}</a>
					</li>
				</c:forEach>
				
				<c:if test="${endPage < pageCount}">
					<li class="page-item">
		        		<a class="page-link" href="/admin/member/list?pageNum=${startPage + 10}&status=${status}">다음</a>
					</li>
				</c:if>
			</ul>
	    </c:if>
	</section>