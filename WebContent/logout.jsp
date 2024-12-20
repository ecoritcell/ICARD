<!--Title:DRMDB:LOGOUT-->
<!--Author:P.Naresh Behera(JE-IT)-->
<!--Date:13-NOV-2017-->
<Html>
<head>
<script>
    history.forward();
</script>


<meta Http-Equiv="Cache-Control" Content="no-cache">
<meta Http-Equiv="Pragma" Content="no-cache">
<meta Http-Equiv="Expires" Content="0">
</head>
<body onload="">
	<%
  response.setHeader("Cache-Control","no-cache");
  response.setHeader("Cache-Control","no-store");
  response.setHeader("Pragma","no-cache");
  response.setDateHeader ("Expires", 0);
   session.invalidate();   
     response.sendRedirect("index.jsp");

  %>
</body>
</Html>