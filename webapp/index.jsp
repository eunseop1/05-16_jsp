<%@page import="kr.human.memo.vo.PagingVO"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="kr.human.memo.vo.CommentVO"%>
<%@page import="java.util.List"%>
<%@page import="kr.human.service.CommentServiceImpl"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
//여기서 현재 페이지 번호/ 페이지당 글수/ 페이지 리스트 개수를 받아야 한다
	int currentPage = 1;
	try{
		currentPage = Integer.parseInt(request.getParameter("p"));
	}catch(Exception e){
		;
	}
	int pageSize = 10;
	try{
		pageSize = Integer.parseInt(request.getParameter("s"));
	}catch(Exception e){
		;
	}
	int blockSize = 10;
	try{
		blockSize = Integer.parseInt(request.getParameter("b"));
	}catch(Exception e){
		;
	}

//DB에서 글 목록을 읽어온다
	PagingVO<CommentVO> pagingVO = CommentServiceImpl.getInstance().selectList(currentPage, pageSize, blockSize);

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>1줄 메모장</title>
<link href="${pageContext.request.servletContext.contextPath }/webjars/bootstrap/5.1.3/css/bootstrap.min.css" rel="stylesheet">
<script type="text/javascript" src="${pageContext.request.servletContext.contextPath }/webjars/jquery/3.6.0/jquery.min.js" ></script>
<script type="text/javascript" src="${pageContext.request.servletContext.contextPath }/webjars/bootstrap/5.1.3/js/bootstrap.min.js" ></script>
<script type="text/javascript">

	$(function(){
		$("#cancelBtn").css('display','none');
	});
	function updateComment(idx, name, content){//수정하기 누르면 작동
		$("#cancelBtn").css('display','inline');// inline하면 옆으로 붙는다
		//취소버튼 보이고
		//alert(idx + "\n" + name + "\n" + content); //수치가 넘어오는게 확인되었다
		$("#idx").val(idx); //수정창에 글번호 넣고
		$("#name").val(name); //이름 넣고
		$("#content").val(content);//내용넣고
		$("#submitBtn").val("수정");//버튼의 이름을 수정으로 변경한다
		$("#mode").val("update"); //mode태그의 버튼을 업데이트로 바꾸고
		$("#password").focus(); //비밀번호 입력칸에 커서 보내기
	}
	function deleteComment(idx, name, content){//삭제하기 누르면 작동
		$("#cancelBtn").css('display','inline');
		$("#idx").val(idx); //수정창에 글번호 넣고
		$("#name").val(name); //이름 넣고
		$("#content").val(content);//내용넣고
		$("#submitBtn").val("삭제");//버튼의 이름을 삭제로 변경한다
		$("#mode").val("delete"); //mode태그의 버튼을 딜리트로 바꾸고
		$("#password").focus();
	}
	function resetComment(idx, name, content){//취소하기 누르면 작동
		$("#cancelBtn").css('display','none');
		$("#idx").val(0); //수정창에 글번호 없애고
		$("#name").val(""); //이름창을 비우고
		$("#content").val("");//내용 비우고
		$("#submitBtn").val("저장");//버튼의 이름을 삭제로 변경한다
		$("#mode").val("insert"); //mode태그의 버튼을 딜리트로 바꾸고
		$("#name").focus(); //커서를 이름 입력자리로 보낸다
	}
</script>
<style type="text/css">
	table{ width: 900px; margin: auto; border:none; padding: 5px;}
	td{padding: 5px; border: 1px solid gray;}
	th{padding: 5px; border: 1px solid gray; background-color: silver; text-align: center;}
	.title{font-size: 20pt; border: none; text-align: center;}
</style>

</head>
<body>
	<table>
		<tr>
			<td colspan="5" class="title">1줄 메모장</td>
		</tr>
		<tr>
			<td colspan="5" align="right" style="border: none;"><%=pagingVO.getPageInfo()%></td>
		</tr>
		<tr>
			<th width = "5%">No</th>
			<th>작성자</th>
			<th width="10%">내용</th>
			<th>작성일</th>
			<th>IP</th>
		</tr>
		<%
			if(pagingVO.getTotalCount() == 0){
		%>
		<tr>
			<td colspan="5" align="center">등록된 글이 없습니다</td>
		</tr>
		<% }else{
			for(CommentVO vo : pagingVO.getList()){
		%>
		<tr align="center">
			<td><%= vo.getIdx() %></td>
			<td><%= vo.getName() %></td>
			<td title = "<%= vo.getContent() %>" align="left">
			<%
			String content = vo.getContent();
			if(content.length() > 30){
				content = content.substring(0,30) + "..."; // 입력된 글이 40자 넘으면 ...으로 처리
				
			}
			content = content.replaceAll("<", "&lt;"); // <를 특수 문자로 만든다 -> 안하면 입력할때 태그로 인식해버린다
			out.println(content);
			%>
			<!-- 글번호는 숫자라서 따음표가 필요없으나 이름이랑 글자는 따음표가 있어야 한다 -->
			<input type="button" onclick="updateComment(<%=vo.getIdx() %>,'<%=vo.getName() %>','<%=vo.getContent() %>')" value="수정" class="btn btn-outline-success btn-sm" />
			<input type="button" onclick="deleteComment(<%=vo.getIdx() %>,'<%=vo.getName() %>','<%=vo.getContent() %>')"  value="삭제" class="btn btn-outline-success btn-sm" />
			</td>
			
			<td>
			<%
			Date date = vo.getRegDate();
			SimpleDateFormat sdf = new SimpleDateFormat("yy-MM-dd hh:mm");
			out.println(sdf.format(date));
			%>
			</td>
			<td><%= vo.getIp() %></td>
		</tr>
		<% 
			}
		}
		%>
		<tr>
			<td colspan="5" style="border: none;" align="center">
				<%-- 페이지 리스트를 찍자 --%>
				<%=pagingVO.getPageList() %>
			</td>
		</tr>
		<tr>
						<td colspan="5" style="border: none;" align="center">
				<%-- 여기에 폼을 달자!!!! --%>
				<form action="updateOk.jsp" method="post">
					<input type="hidden" name="idx" id="idx" value="0">
					<input type="hidden" name="p" id="p" value="<%=pagingVO.getCurrentPage()%>">
					<input type="hidden" name="s" id="s" value="<%=pagingVO.getPageSize()%>">
					<input type="hidden" name="b" id="b" value="<%=pagingVO.getBlockSize()%>">
					
					<%--저장/수정/삭제/ 표시 (우리 눈에 보일 필요는 없다) --%>
					<input type="hidden" name="mode" id="mode" value="insert"> 
					<input type="text" name="name" id="name" required="required" size="5"/>
					<input type="password" name="password" id="password" required="required" size="5"/>
					<input type="text" name="content" id="content" required="required" size="75"/>
					<input type="submit" id="submitBtn" value="저장" class="btn btn-outline-success btn-sm" />
					<input type="button" id="cancelBtn" value="취소" class="btn btn-outline-success btn-sm" />
					<!-- 취소버튼에도 온 클릭으로 리셋시킨다 -->
					<!-- update, delete, reset 할 기능이 필요하다 -->
				</form>
			</td>
		</tr>
	</table>
</body>
</html>