<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@page import="java.sql.*"%>
<%@ page import="communication.*"%> 
<option value=""> [SELECT BILL UNIT]  </option>
<%
    

        try
        {
          
            Connection con = DBConnect.getConnection();
           
            PreparedStatement ps=null; //create statement
            
            ps=con.prepareStatement("SELECT billunit_code, billunit_name FROM ICARD_billunit order by billunit_code ");            
            ResultSet rs=ps.executeQuery(); 
            
           while(rs.next())               
            {  
                %>
               <option value="<%=rs.getString(1)%>"> <%=rs.getString(1)%>  </option>
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