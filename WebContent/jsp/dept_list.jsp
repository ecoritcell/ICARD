<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@page import="java.sql.*"%>
<%@ page import="communication.*"%> 
<option value=""> [SELECT DEPARTMENT]  </option>
<%
    

        try
        {
          
            Connection con = DBConnect.getConnection();
           
            PreparedStatement ps=null; //create statement
            
            ps=con.prepareStatement("SELECT dept_code,dept_name FROM department order by dept_name ");            
            ResultSet rs=ps.executeQuery(); 
            
           while(rs.next())               
            {  
                %>
               <option value="<%=rs.getString(1)%>"> <%=rs.getString(2)%>  </option>
                <%
            }
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
catch(Exception e)
{
    e.printStackTrace();
}
   
%>