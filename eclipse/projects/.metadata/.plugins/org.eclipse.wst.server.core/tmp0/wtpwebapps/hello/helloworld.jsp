<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import = "hello.HelloWorld" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>Hello world incredible</title>
	</head>
	<body>
	
		<%-- This is a JSP Comment before JSP Scriplet --%>
		<%
			/*
			//Prints out to console
			System.out.println("Hello World in Console!");
		 
			//Prints out to HTML page
			out.println("<h1>Hello World!</h1>");*/
		%>
		  
		<% 
			HelloWorld t = new HelloWorld();
			
			out.println (t.printTitle("La ostia de titulo..."));
			out.println (t.printText("HELLO WORLD!!!"));
			//t.test("Joy");
		%>
		
	</body>
</html>