<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="../include/header.jsp"  %>

<div id="wrap" align="center" class="row">
	<h1>영화정보 등록</h1>
	<form role="form" name="frm" method="post" action="/movie/register">
		<table style="width:640px;">
				<tr>
					<th style="text-align:center;width:60px;">제  목</th>
					<td><input type="text" name="title" size="60"></td>
				</tr>
				<tr>
					<th style="text-align:center;">가  격</th>
					<td><input type="text" name="price" size="60"> 원</td>
				</tr>
				<tr>
					<th style="text-align:center;">감  독</th>
					<td><input type="text" name="director" size="80"></td>
				</tr>
				<tr>
					<th style="text-align:center;">배  우</th>
					<td><input type="text" name="actor" size="60"></td>
				</tr>
				<tr>
					<th style="text-align:center;">설  명</th>
					<td><textarea rows="10" cols="70" name="synopsis"></textarea></td>
				</tr>
				<tr>
					<th style="text-align:center;">사  진</th>
					<td>
					<div class="panel-body">
	        			<div class="form-group uploadDiv">
							<input type="file" name="uploadFile"><br>
						</div>
						<div class='uploadResult'> 
		          			<ul>
		          
		          			</ul>
	        			</div>
					</div>
					</td>
				</tr>
			</table>
		<br>
		<button type="submit" class="btn btn-default">확인</button>
		<button type="reset" class="btn btn-default">취소</button>
		<button type="submit" class="btn btn-default" data-oper='list'>목록</button>
	</form>
</div>

<script type="text/javascript">
	$(document).ready(function(e){
		var formObj = $("form[role='form']");
		
		$("button[type='submit']").on("click", function(e){
			e.preventDefault();
			console.log("submit clicked");
			
			var str = "";
			
			$(".uploadResult ul li").each(function(i, obj){
				var jobj = $(obj);
				
				console.log(jobj.data("type"));
				
				str += "<input type='hidden' name='attachList["+i+"].fileName' value='"+jobj.data("filename")+"'>";
				str += "<input type='hidden' name='attachList["+i+"].uuid' value='"+jobj.data("uuid")+"'>";
				str += "<input type='hidden' name='attachList["+i+"].uploadPath' value='"+jobj.data("path")+"'>";
				str += "<input type='hidden' name='attachList["+i+"].fileType' value='"+jobj.data("type")+"'>";
			});
			
			formObj.append(str).submit();
		});
		
		var regex = new RegExp("(.*?)\.(exe|sh|zip|alz)$");
		var maxSize = 5242880; //5MB
		
		function checkExtendsion(fileName, fileSize){
			if(fileSize >= maxSize){
				alert("파일 사이즈 초과");
				return false;
			}
			
			if(regex.test(fileName)){
				alert("해당 종류의 파일은 업로드할 수 없습니다.");
				return false;
			}
			
			return true;
		}
		
		$("input[type='file']").change(function(e){
			var formData = new FormData();
			var inputFile = $("input[name='uploadFile']");
			var files = inputFile[0].files;
			
			
			for(var i=0; i<files.length; i++){
				if(!checkExtendsion(files[i].name, files[i].size)){
					return false;
				}
				
				formData.append("uploadFile", files[i]);
			}
			
			$.ajax({
				url: '/uploadAjaxAction',
				processData: false,
				contentType: false,
				data: formData,
				type:'POST',
				dataType: 'json',
				success: function(result){
					console.log(result);
					showUploadFile(result);
				}
			});
			
		});
		
		function showUploadFile(uploadResultArr){
			if(!uploadResultArr || uploadResultArr.length ==0)
			{
				return;	
			}
			
			var uploadURL = $(".uploadResult ul");
			var str = "";
			
			// 6월 3일 이 부분부터 생각해야 함.
			$(uploadResultArr).each(function(i, obj){
				var fileCallPath =  encodeURIComponent("/s_"+obj.uuid +"_"+obj.fileName);
				str += "<li data-path='"+obj.uploadPath+"'";
				str +=" data-uuid='"+obj.uuid+"' data-filename='"+obj.fileName+"' data-type='"+obj.image+"'><div>";
				str += "<span> "+ obj.fileName+"</span>";
				str += "<button type='button' data-file=\'"+fileCallPath+"\' "
				str += "data-type='image' class='btn btn-warning btn-circle'><i class='fa fa-times'></i></button><br>";
				str += "<img src='/display?fileName="+fileCallPath+"'>";
				str += "</div>";
				str +"</li>";
			});
			
			uploadURL.append(str);
		}
		
		$(".uploadResult").on("click", "button", function(e){
			var targetFile = $(this).data("file");
			var type = $(this).data("type");
			
			var targetLi = $(this).closest("li");
			
			$.ajax({
				url: '/deleteFile',
				data: {fileName: targetFile, type:type},
				dataType:'text',
				type:'POST',
				success: function(result){
					alert(result);
					targetLi.remove();
				}
			});
		});
		
		$("button[data-oper='list']").on("click", function(e){
			formObj.attr("action", "/movie/list").attr("method", "get");
			formObj.submit();
		});	
	});
</script>
<%@ include file="../include/footer.jsp"  %>
