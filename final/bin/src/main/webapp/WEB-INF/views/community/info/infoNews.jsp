<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>취업톡톡</title>
</head>

<body>
	<c:import url="/navbar"/>
	<section style="margin-left:5%;margin-right:5%;margin-bottom:5%">
		<h1>취업톡톡</h1>
		<div>
			<c:forEach var="dto" items="${list}">
				<table class="table table-bordered">
					<tr>
						<td>제목</td>
						<td><a href="${dto.link}" target="_blank">
								${dto.subject}
							</a>
						</td>
						<td>${dto.date}</td>
					</tr>
					<tr>
						<td>요약</td>
						<td colspan="2">
							${dto.summary}
						</td>
					</tr>
				</table>
			</c:forEach>
		</div>
	</section>
	<jsp:include page="/WEB-INF/views/footer.jsp" />
</body>