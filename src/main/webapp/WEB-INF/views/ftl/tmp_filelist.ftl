<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>临时附件列表</title>
	</head>
	<style type="text/css">
	.subject-area .folder-area .folder-ul {
	  margin: 2rem 1rem;
      list-style: decimal;
	}
	.subject-area .folder-area .folder-ul .folder-li {
	  padding: 0.3rem 0rem;
	}
	.subject-area .folder-area .folder-ul .folder-li:hover {
	  background: #eaf6ff;
	}
	.subject-area .folder-area .folder-ul span {
	  font-size: 12px;
	  color: #999;
	  float: right;
      padding: 0.3rem 0rem;
	}
	</style>
	<script src="${request.contextPath}/assets/jquery/js/jquery-2.1.0.min.js"></script>
	<script type="text/javascript">
		function preview(filepath, filetype){
			$.ajax({
	    		type: "post",
	    		url: "${request.contextPath}/file/getfilepage",
	    		data: { "filepath": filepath, "filetype":filetype },
	    		success: function (data) {
	    			if(data && data!=""){
	   		 			myWindow=window.open(window.location.href);
	    				myWindow.document.write(data);
	    				myWindow.focus();
	    			}
	    		},
			});
		}
	</script>
	<body>
		<div class="subject-area">
			<div class="folder-area">
				<#if history_back?exists && history_back!="">
					<button type="button" onclick="window.history.back(-1);">返回上一步</button>
				</#if>
				<h2>文件所在磁盘位置：${nowFolder}</h2>
				<ul class="folder-ul">
					<#list fileList as item>
						<#if item.type?exists && item.type=="folder">
							<li class="folder-li">
							  <a href="${request.contextPath}/tmpfile/list?tmpfilepath=${item.content}">${item.content}</a>
							  <span>修改日期：${item.update}</span>
							</li>
						<#elseif item.type?exists && item.type=="file">
							<li class="folder-li">
							  <a href="${request.contextPath}/tmpfile/getfile?tmpfilepath=${item.content}">${item.content}</a>
							  <button type="button" class="btn btn-sm" onclick="preview('${item.content}','')">在线预览</button>
							  <span>修改日期：${item.update}</span>
							</li>
						</#if>
					</#list>
				</ul>
			</div>
		</div>
		<div>
			<if
		</div>
	</body>
</html>