<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<div id="errors_div" >
	<c:if test="${!empty errors}" >
		<ul id="errors_list" >
			<c:set var="counter" value="0"/>
			<c:forEach var="error" items="${errors}">
				<c:if test="${suppress_errors == null}" >
					<c:set var="counter" value="${counter + 1}"/>
					<li id="error_list_${counter}" style="color:red;" >${error}</li>
				</c:if>
				<c:if test="${suppress_errors != null}" >
					<li style="color:black;" >${error}</li>
				</c:if>
			</c:forEach>
		</ul>
	</c:if>
</div>
