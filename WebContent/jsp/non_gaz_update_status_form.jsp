<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">

        <title>Non-Gaz Status Update</title>

        <!-- Bootstrap Core CSS -->
        <link href="../css/bootstrap.min.css" rel="stylesheet">

        <!-- MetisMenu CSS -->
        <link href="../css/metisMenu.min.css" rel="stylesheet">

        <!-- Timeline CSS -->
        <link href="../css/timeline.css" rel="stylesheet">

        <!-- Custom CSS -->
        <link href="../css/startmin.css" rel="stylesheet">

        <!-- Morris Charts CSS -->
        <link href="../css/morris.css" rel="stylesheet">

        <!-- Custom Fonts -->
        <link href="../css/font-awesome.min.css" rel="stylesheet" type="text/css">
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.0/jquery.min.js"></script>
<script>
function closeAndRefresh(){
	  opener.location.reload(); // or opener.location.href = opener.location.href;
	  window.close(); // or self.close();
	}
</script>
    </head>
    <body>
  <%@page import="java.sql.*"%>
<%@ page import="communication.*"%>
<%
String id_no=request.getParameter("ID_NO");    
String status=request.getParameter("STATUS")==null?"":request.getParameter("STATUS");
String deptserial=request.getParameter("DEPTSERIAL");
String remarks=request.getParameter("REMARKS");
String submit=request.getParameter("submit");
if(submit!=null){
        try
        {
          
            Connection con = DBConnect.getConnection();
           
            PreparedStatement ps=null;
            
           if(deptserial!=null & remarks !=null){
    			ps=con.prepareStatement("update non_gaz_master set status='"+status+"', dept_slno='"+deptserial+"', remarks='"+remarks+"' where id_no='"+id_no+"'");
           }else if(deptserial!=null){
        	   ps=con.prepareStatement("update non_gaz_master set status='"+status+"', dept_slno='"+deptserial+"' where id_no='"+id_no+"'");
           }else if(remarks!=null){
        	   ps=con.prepareStatement("update non_gaz_master set status='"+status+"', remarks='"+remarks+"' where id_no='"+id_no+"'");
           }
           else{
        	
        	   ps=con.prepareStatement("update non_gaz_master set status='"+status+"' where id_no='"+id_no+"'");
           }
           
    		ps.executeUpdate(); 
    		
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
catch(Exception e)
{
    e.printStackTrace();
}%>


            <div id="wrapper">
            <div class="col-lg-12">
            <br><br><br><br><br>
            <button class="form-control btn btn-success " onclick="closeAndRefresh();">Close</button>
            </div>
<% }else{%>           
                <div class="row" style="padding-left:30px;">
                    <div class="col-lg-12">
                       <h3 class="page-header">
                        
                        Update Task on Task ID=<%=request.getParameter("id_no") %>	</h3>			
                    </div>	
                 
                </div>
                <!-- /.row -->
         <form name="newtask"  method="post"  target="_self"   action="" >				
		<TABLE style="width:100%; align:center;" class="table table-striped table-bordered table-hover">
	        <TR>		
				<TD>	
				 <div>				 
                  <input type="hidden" name="ID_NO" value='<%=request.getParameter("id_no") %>' >
                 </div>  		
				 <div class="form-group">
                   <label>Status:</label>
                   <select id ="STATUS" name="STATUS" class="form-control" required>
						<option value="1"> Pending </option>
						<option value="2"> Printing (Draft)</option>
						<option value="3"> Printing (To be Sent)</option>
						<option value="4"> Printing (Sent)</option>
						<option value="5"> Closed </option>
						<option value="6"> Rejected </option>
					</select>
                  </div>
				</TD>
			</TR>
				<TR>	
				<TD  width="50%">					
				 <div class="form-group">				 
                   <label>Dept SL No:</label>
                   <input type="text" id="DEPTSERIAL" name="DEPTSERIAL" class="form-control" placeholder="Dept Serial No." <%=status.equals("2")?"required":"" %>>
                 </div>                         
				</TD>
				<TD width="50%">					
				 <div class="form-group">				 
                   <label>Remarks:</label>
                   <input type="text" id="REMARKS"  name="REMARKS" class="form-control" placeholder="Remarks" <%=status.equals("6")?"required":"" %>>
                 </div>                         
				</TD>
			</TR>
			
			<TR>
				<TD class="formheader" align="center" colspan="5" height="30"> 
						<INPUT name="submit" type="submit"  value="SUBMIT" class="btn btn-primary">
						&nbsp;&nbsp;
				 <INPUT name="clear" type="reset"  value=" CLEAR " class="btn btn-primary" onclick="javascript:clearData()"></TD>
			</TR>
		</TABLE>
</form>
            </div>
            <!-- /#page-wrapper -->
<%} %>
        <!-- /#wrapper -->

        <!-- jQuery -->
        <script src="../js/jquery.min.js"></script>
        <script  src="../java script/datespecial.js"></script>
        <!-- Bootstrap Core JavaScript -->
        <script src="../js/bootstrap.min.js"></script>

        <!-- Metis Menu Plugin JavaScript -->
        <script src="../js/metisMenu.min.js"></script>

        <!-- Morris Charts JavaScript -->
        <script src="../js/raphael.min.js"></script>
        <script src="../js/morris.min.js"></script>
        <script src="../js/morris-data.js"></script>

        <!-- Custom Theme JavaScript -->
        <script src="../js/startmin.js"></script>

    </body>
    <script type="text/javascript">
      
   $(document).ready(function(){
		loadDataWithStatus();
		
	})
		   
	
	
	function loadDataWithStatus(){

	   <%@page import="java.sql.*"%>
	   <%@ page import="communication.*"%>
	   <%
	   String status1 = "";		   
		String deptslno1 = ""; 
	 	String remarks1 = "";
	   try
       {         

          Connection con = DBConnect.getConnection();         
           PreparedStatement ps=null;
           ResultSet rs=null;
           String id_no1=request.getParameter("id_no");    
           System.out.println("id_no :" + id_no1);
   		ps=con.prepareStatement("SELECT status,dept_slno,remarks FROM non_gaz_master where id_no='"+id_no1+"'");
   		rs=ps.executeQuery();
   		 if(rs.next()){
   			
   			status1 = rs.getString("status");
   			if(status1 == null)
   				status1 = "";
   			deptslno1 = rs.getString("dept_slno");
   			if(deptslno1 == null)
   				deptslno1 = "";
   			remarks1 = rs.getString("remarks");
   			if(remarks1 == null)
   				remarks1 = "";
   			
   			System.out.println("status1 :" + status1);
   			System.out.println("deptslno1 :" + deptslno1);
   			System.out.println("remarks1 :" + remarks1);
   			
   		 }
   		
       }
       catch(SQLException e)
       {
           e.printStackTrace();
       }
	   %>
	   
   }

   
   var status ="<%=status1%>";
   var deptslno2 ="<%=deptslno1%>";
   var remarks ="<%=remarks1%>";
   
	$("#STATUS").val(status).attr("selected","selected");
   document.getElementById("DEPTSERIAL").value = deptslno2;
   document.getElementById("REMARKS").value = remarks;
   
	   
   </script>
</html>
