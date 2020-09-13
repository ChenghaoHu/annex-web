<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>系统登录</title>
	</head>
	<style type="text/css">
	body{ margin:0 auto; }
	.subject-area { 
	  width: 100%;
	  min-height: calc(100vh - 2px);
	  heigth: calc(100%);
	  background-image:url(${request.contextPath}/assets/img/background-img.jpg);
	  background-repeat:no-repeat;
	  background-size:100% 100%;
	  background-attachment: fixed;
	}
	.login-area { 
	  position: sticky;
	  top: 30%;
	  text-align: center;
	  <#-- padding: 15% 10%;mix-blend-mode: hard-light; -->
	}
	.text-style { 
	  padding: 0.75rem 2.25rem; 
	}
	.subject-area .login-area form { 
	  display: inline-grid;
	  padding: 1.5rem 1.2rem 2.5rem;
      background: #45517c;
      border-radius: 7px;
      height: calc(50%);
      min-height: calc(30vh - 2px);
      min-width: 22%;
      max-width: 100%;
    }
    .subject-area .login-area form center {
      font: 400 21.3333px Arial;
      letter-spacing: 6px;
      color: #fff;
      padding: 0.5rem 1rem;
      background: #4e597f;
      align-self: center;
      border-radius: 5px;
    }
    .subject-area .login-area form input { 
      border-radius: 5px;
      text-indent: 7px;
      margin-top: 5px;
    }
    .subject-area .login-area form button {
      border-radius: 5px;
      margin-top: 5px;
      background: deepskyblue;
    }
	</style>
	<body>
		<div class="subject-area">
			<div class="login-area">
				<form action="${request.contextPath}/tmpfile/list" method="post">
					<#-- ftl页面脚本的相关语法可参照官网教程 -->
					<#assign loginname="admin">
					<#assign loginpwd="1">
					<center>附件管理系统</center>
					<input type="text" class="" id="loginname" name="loginname" value="${loginname}" placeholder="请输入登录名称"/>
					<input type="password" class="" id="loginpwd" name="loginpwd" value="${loginpwd}" placeholder="请输入登录密码"/>
					<button type="submit" class="">登录</button>
				</form>
			</div>
		</div>
	</body>
</html>