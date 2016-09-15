<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome to The Labyrinth</title>
</head>
<body OnLoad="document.getElementById('login_username').focus();" >

	<h1>The Labyrinth</h1>
	
	<jsp:include page="jsp/includes/errors.jsp" />

	<c:if test="${user == null}" >
		<a id="signup_link" href="signup" >Join</a><br/><br/>
		<form id="login_form" method = POST action="login" ><br/>
			<input type="hidden" name="action" value="login" >
			<input id="login_username" type="text" title="username" name="username" ><br/>
			<input id="login_password" type="password" title="password" name="password" ><br/>
			<input id="login_submit" type="submit" title="submit" name="submit" value="Login" ><br/>
		</form>
	</c:if>
	
	<!-- Not sure if I'll use this -->
	<c:if test="${user != null}" >
		<jsp:include page="jsp/includes/logout_link.jsp" />
		<h2>Welcome to The Labyrinth. You are logged in as ${user.firstName}</h2>
		<jsp:include page="/jsp/user/hello.jsp"></jsp:include>
	</c:if>
</body>
</html>