<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>

<script>
	function getCnum(){
		var cname = $('#certiSearch').val();
		var cn = $('#searchList option').filter(function(){
			return this.value == cname;
		}).data('value');
		
		if(typeof cn !== 'undefined'){
			document.frm.cnum.value = cn;
		}else{
			document.frm.cnum.value = "";
		}
		
	}
</script>

<h1>보유자격증 추가</h1>
<form name="frm" action="/mypage/addMemberCertiPro" onsubmit="getCnum()" method="post">

	<table border="1">
		<tr>
			<th>자격증 이름</th>
			<td>
				<input type="text" id="certiSearch" list="searchList" name="cert_name"/>
				<datalist id="searchList" >
					<c:forEach var="dto" items="${list }">
						<c:if test="${dto.cnum.charAt(0) == 80 }">
							<option data-value="${dto.cnum }">${dto.cname.concat(dto.clevel) }</option>
						</c:if>
						<c:if test="${dto.cnum.charAt(0) != 80 }">
							<option data-value="${dto.cnum }">${dto.cname}</option>
						</c:if>
					</c:forEach>
				</datalist>
				<br/>
				목록에 없는경우 자격증이름 직접입력
			</td>
		</tr>
		<tr>
			<th>만료일자</th>
			<td>
				<input type="date" name="expirydate"/>
			</td>
		</tr>
	</table>
	<input type="hidden" name="cnum" />
	<input type="hidden" name="memid" value="${sessionScope.sid}"/>
	<input type="submit" value="입력" />
</form>