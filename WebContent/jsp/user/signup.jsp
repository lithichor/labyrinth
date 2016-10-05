<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Create an account</title>
</head>
<body OnLoad="document.getElementById('signup_firstname').focus();" >

	<jsp:include page="../includes/errors.jsp" />

	<form id="signup_form" method=POST action="signup" >
		<input type="hidden" name="action" value="submit" >
		First Name:<input id="signup_firstname" type="text" title="firstname" name="firstname" value="${firstname}" ><br/>
		Last Name:<input id="signup_lastname" type="text" title="lastname" name="lastname" value="${lastname}" ><br/>
		Email:<input id="signup_email" type="text" title="email" name="email" value="${email}" ><br/>
		Password:<input id="signup_password" type="password" title="password" name="password" value="${password}" ><br/>
		Password Confirm:<input id="signup_confirm" type="password" title="confirm" name="confirm" value="${confirm}" ><br/>
		<input id="signup_submit" type="submit" title="submit" name="submit" value="Signup" ><br/>
	</form>

</body>
</html>