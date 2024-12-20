<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
asdsAF sd fgsd fgsd
<%
response.setContentType("application/pdf");
response.setHeader("content-disposition","attachment; filename="+"Filename.pdf");
//What ever u write inside this JSP file will be exported as pdf file when you send request and response to this page
%>
</body>
</html>