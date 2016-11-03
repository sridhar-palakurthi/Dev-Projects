<%@page import="com.demo.social.GoogleAuthHelper"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%
GoogleAuthHelper authHelper = GoogleAuthHelper.getGoogleAuthHelper();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Java Google Login</title>
</head>
<body style="text-align: center; margin: 0 auto;">
	<div
		style="margin: 0 auto; height: 150px; width: 610px;">
		<a href="<%=authHelper.buildLoginUrl()%>"> 
		<img style="margin-top: 138px;" src="./img/googleplus-signin.gif" />
		</a>
	</div>
</body>
</html>