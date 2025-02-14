<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>자격증 상세일정 수정 - ${cnum }</title>
	<script>
		function dateFormat(obj){
			var len = obj.value.length;
			if(len == 4) obj.value += "-";
			if(len == 7) obj.value += "-";
			if(len == 10) obj.value += " ";
			if(len == 13) obj.value += ":";
		}
		
		function chkList(){
			window.open("/admin/certi/sameSchedule?datepk=${dto.datePK}",
						'변경되는 자격증 목록',
						'width=500,height=500');
		}
	</script>
</head>
<body>
	<jsp:include page="../adminNavBar.jsp"/>
	<section style="margin: 20px 10% 10% 10%">
		<h2>시험 일정</h2>
		<h3>자격증 번호 : ${cnum }</h3>
		<form action="/admin/certi/modDatePro" method="post">
			<table class="table table-hover">
				<tr>
					<td style="text-align: center">시행 연도</td>
					<td><input type="text" name="cyear" value="${dto.cyear }" class="form-control" style="width:10%"></td>
				</tr>
				<tr>
					<td style="text-align: center">자격증 회차</td>
					<td><input type="text" name="cround" value="${dto.cround }" class="form-control" style="width:10%"></td>
				</tr>
				<tr>
					<td style="text-align: center">필기 원서접수</td>
					<td>
						<input type="text" name="docRegStart1" value="${dto.docRegStart1 }" 
							onKeypress="dateFormat(this)" placeholder="yyyy-MM-dd HH:mm" maxlength="16">
						~ 
						<input type="text" name="docRegEnd1" value="${dto.docRegEnd1 }" 
							onKeypress="dateFormat(this)" placeholder="yyyy-MM-dd HH:mm" maxlength="16">
					</td>
				</tr>
				<tr>
					<td style="text-align: center">필기원서 - 추가접수</td>
					<td>
						<input type="text" name="docRegStart2" value="${dto.docRegStart2 }"
							onKeypress="dateFormat(this)" placeholder="yyyy-MM-dd HH:mm" maxlength="16">
						~ 
						<input type="text" name="docRegEnd2" value="${dto.docRegEnd2 }"
							onKeypress="dateFormat(this)" placeholder="yyyy-MM-dd HH:mm" maxlength="16">
					</td>
				</tr>
				<tr>
					<td style="text-align: center">필기시험</td>
					<td>
						<input type="text" name="docTestStart" value="${dto.docTestStart }"
							onKeypress="dateFormat(this)" placeholder="yyyy-MM-dd HH:mm" maxlength="16">
						~ 
						<input type="text" name="docTestEnd" value="${dto.docTestEnd }"
							onKeypress="dateFormat(this)" placeholder="yyyy-MM-dd HH:mm" maxlength="16">
					</td>
				</tr>
				<tr>
					<td style="text-align: center">필기-합격자발표</td>
					<td>
						<input type="text" name="docResultStart" value="${dto.docResultStart }"
							onKeypress="dateFormat(this)" placeholder="yyyy-MM-dd HH:mm" maxlength="16">
						~ 
						<input type="text" name="docResultEnd" value="${dto.docResultEnd }"
							onKeypress="dateFormat(this)" placeholder="yyyy-MM-dd HH:mm" maxlength="16">
					</td>
				</tr>
				<tr>
					<td style="text-align: center">응시자격 서류제출</td>
					<td>
						<input type="text" name="docSubmitStart" value="${dto.docSubmitStart }"
							onKeypress="dateFormat(this)" placeholder="yyyy-MM-dd HH:mm" maxlength="16">
						~ 
						<input type="text" name="docSubmitEnd" value="${dto.docSubmitEnd }"
							onKeypress="dateFormat(this)" placeholder="yyyy-MM-dd HH:mm" maxlength="16">
					</td>
				</tr>
				<tr>
					<td style="text-align: center">실기시험 원서 접수</td>
					<td>
						<input type="text" name="pracRegStart1" value="${dto.pracRegStart1 }"
							onKeypress="dateFormat(this)" placeholder="yyyy-MM-dd HH:mm" maxlength="16">
						~ 
						<input type="text" name="pracRegEnd1" value="${dto.pracRegEnd1 }"
							onKeypress="dateFormat(this)" placeholder="yyyy-MM-dd HH:mm" maxlength="16">
					</td>
				</tr>
				<tr>
					<td style="text-align: center">실기시험 추가 접수</td>
					<td>
						<input type="text" name="pracRegStart2" value="${dto.pracRegStart2 }"
							onKeypress="dateFormat(this)" placeholder="yyyy-MM-dd HH:mm" maxlength="16">
						~ 
						<input type="text" name="pracRegEnd2" value="${dto.pracRegEnd2 }"
							onKeypress="dateFormat(this)" placeholder="yyyy-MM-dd HH:mm" maxlength="16">
					</td>
				</tr>
				<tr>
					<td style="text-align: center">실기시험</td>
					<td>
						<input type="text" name="pracTestStart" value="${dto.pracTestStart }"
							onKeypress="dateFormat(this)" placeholder="yyyy-MM-dd HH:mm" maxlength="16">
						~ 
						<input type="text" name="pracTestEnd" value="${dto.pracTestEnd }"
							onKeypress="dateFormat(this)" placeholder="yyyy-MM-dd HH:mm" maxlength="16">
					</td>
				</tr>
				<tr>
					<td style="text-align: center">실기 합격자 발표</td>
					<td>
						<input type="text" name="pracResStart" value="${dto.pracResStart }"
							onKeypress="dateFormat(this)" placeholder="yyyy-MM-dd HH:mm" maxlength="16">
						~ 
						<input type="text" name="pracResEnd" value="${dto.pracResEnd }"
							onKeypress="dateFormat(this)" placeholder="yyyy-MM-dd HH:mm" maxlength="16">
					</td>
				</tr>
			</table>
			<input type="hidden" name="datePK" value="${dto.datePK }">		
			<input type="hidden" name="cnum" value="${cnum }">
			<div style="margin-top:20px; margin-bottom:20px">
				<c:if test="${fn:startsWith(cnum, 'N')}">
					<h5>상기일정을 수정하실 경우, [총 : ${count}개]의 자격증일정도 같이 변경됩니다.</h5>
					<input type="button" class="btn btn-warning" value="변경되는 자격증 리스트 확인하기" onclick="chkList();">
				</c:if>
			</div>	
			<input type="submit" class="btn btn-primary" value="수정" style="margin-left:45%">
		</form>
	</section>
	<jsp:include page="/WEB-INF/views/footer.jsp" />
</body>
</html>