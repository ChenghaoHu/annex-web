<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
		<title>系统登录</title>
	</head>
	<style type="text/css">
	.subject-area { 
	  width: 100%;
	  height: 51rem;
	  background-image:url(${request.contextPath}/assets/img/background-img.jpg);
	  background-repeat:no-repeat;
	  background-size:100% 100%;
	  background-attachment: fixed;
	}
	.login-area { padding: 5rem 10rem;text-algin: center; }
	.text-style { padding: 0.75rem 2.25rem; }
	.subject-area .login-area form { 
	  display: grid;
	  float: right;
	  width: 23%;
	  height: 12rem;
	  padding: 3rem 1rem;
      background: #45517c;
      margin-top: 10rem;
      border-radius: 7px;
    }
    .subject-area .login-area form center {
      font-size: 1.5rem;
      letter-spacing: 14px;
      color: #fff;
    }
    .subject-area .login-area form input { 
      padding-left: 0.8rem;
      border-radius: 5px;
      margin-bottom: 5px;
    }
    .subject-area .login-area form button {
      border-radius: 5px;
    }
	</style>
	<body>
		<div class="subject-area">
			<div class="login-area">
				<form action="" method="post">
					<#-- ftl常用语法可参考网上教程 -->
					<#assign loginname="admin">
					<#assign loginpwd="1">
					<center>附件管理系统</center>
					<input type="text" class="" id="loginname" name="loginname" value="${loginname}" placeholder="请输入账号"/>
					<input type="password" class="" id="loginpwd" name="loginpwd" value="${loginpwd}" placeholder="请输入密码"/>
					<button type="submit" class=""><span style="text-style">登录</span></button>
				</form>
			</div>
		</div>
	</body>
</html>