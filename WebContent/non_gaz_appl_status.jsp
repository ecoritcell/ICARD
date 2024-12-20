<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">

        <title>ICARD:Non Gaz Application Status</title>
  <!-- Bootstrap Core CSS -->
        <link href="./css/bootstrap.min.css" rel="stylesheet">

        <!-- MetisMenu CSS -->
        <link href="./css/metisMenu.min.css" rel="stylesheet">

        <!-- Timeline CSS -->
        <link href="./css/timeline.css" rel="stylesheet">

        <!-- Custom CSS -->
        <link href="./css/startmin.css" rel="stylesheet">

        <!-- Morris Charts CSS -->
        <link href="./css/morris.css" rel="stylesheet">

        <!-- Custom Fonts -->
        <link href="./css/font-awesome.min.css" rel="stylesheet" type="text/css">
       <script type="text/javascript" src="../js/jquery.min.js"></script> 
       <script src="./js/datespecial.js"></script>
        <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
        <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
        <!--[if lt IE 9]>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/html5shiv/3.7.3/html5shiv.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/respond.js/1.4.2/respond.min.js"></script>
        <![endif]-->
<style>
@media print {
  a[href]:after {
    content: none !important;
  }
}


table, th, td {
  border-collapse: collapse;
  font-size:12px;
}

</style>
     
       
    </head>
    <body>

       
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="communication.*" %>


<%	

ResultSet rs=null,rs1=null;
Statement st=null,st1=null;
PreparedStatement ps=null;
String appid=request.getParameter("appid");
String dob=request.getParameter("dob");
System.out.println(dob);

DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
DateFormat tf = new SimpleDateFormat("dd-MM-yyyy");

try{
	Connection con=DBConnect.getConnection();
    st=con.createStatement(); 
    st1=con.createStatement(); 
  
 
%>
       <div id="wrapper">
        &nbsp;&nbsp;  <img src="../images/print.jpg" id="print_img" heigth="40px" width="40px" onclick="printPage()"/>
        
         <div class="col-lg-12" id="myTable1">
       <%
int i=1;
String query="select ID_NO,EMPNO,EMPNAME,DESIGNATION,DOB,DEPARTMENT,STATION,RES_ADDRESS,RLY_NUMBER,MOBILE_NUMBER,EMERGENCY_CONTACT_NAME, EMERGENCY_CONTACT_NO,PHOTO,SIGNATURE,LAST_UPDATE,dept_name,qr_code,status,remarks from non_gaz_master ngm,department d where d.dept_code=ngm.department and id_no='"+appid+"' and dob='"+dob+"' order by id_no";
 rs=st.executeQuery(query);
 if(rs.next()){
 %> 
                        <div class="panel panel-primary" id="dataTable">
                            <div  class="btn btn-lg btn-primary btn-block">
                                ID No:<%=rs.getString("ID_NO") %> &nbsp;&nbsp;&nbsp;&nbsp; Status=<%=rs.getInt("STATUS")==1?"Pending":rs.getInt("STATUS")==2?"Printing (Draft)":rs.getInt("STATUS")==3?"Printing (To be Sent)":rs.getInt("STATUS")==4?"Printing (Sent)":rs.getInt("STATUS")==5?"Closed":"Rejected" %> <%=rs.getInt("STATUS")==6?"("+rs.getString("remarks")+")":"" %>
                               
                            </div>
                            <!-- /.panel-heading -->
                            <div class="panel-body" >
                                <div class="table-responsive">
 		
 									 <table class="table table-striped table-bordered table-hover"  style="font-size:12px;" >
 										   <tr>                                            
                                            <th>EMPNO</th>  <td><%=rs.getString("EMPNO") %></td>
                                            <th>EMPNAME</th> <td><%=rs.getString("EMPNAME") %></td>
                                            <th>DESIGNATION</th><td><%=rs.getString("DESIGNATION") %></td>
                                          </tr>
                                           <tr>
                                            <th>DOB</th><td><%=tf.format(df.parse(rs.getString("DOB"))) %></td>
   											<th>DEPARTMENT</th><td><%=rs.getString("dept_name") %></td>
   											<th>STATION</th><td><%=rs.getString("STATION") %></td>
                                           </tr>
                                           <tr>
   											<th>ADDRESS</th><td><%=rs.getString("RES_ADDRESS") %></td>
   											<th>RLY NUMBER</th><td><%=rs.getString("RLY_NUMBER")==null? "": rs.getString("RLY_NUMBER")%></td>
   											<th>MOBILE NUMBER</th><td><%=rs.getString("MOBILE_NUMBER") %></td>
                                           </tr>
                                           <tr>                                            
   											<th>EMERGENCY CONTACT NAME</th><td><%=rs.getString("EMERGENCY_CONTACT_NAME") %></td>
   											<th>EMERGENCY CONTACT NO</th><td><%=rs.getString("EMERGENCY_CONTACT_NO") %></td>
   											<th>APPLICATION DATE</th><td><%=tf.format(df.parse(rs.getString("LAST_UPDATE"))) %> </td>
                                           </tr>
                                           <tr>
   										    <th>QR Code</th><td><img src='/ICARD_QR_codes/<%=rs.getString("QR_CODE") %>' style="width:190px;"> </td>
                                            <th>PHOTO</th><td><img src="/ICARD_Attachments/<%=rs.getString("PHOTO") %>"  style="width:100px;" /></td>
   											<th>SIGNATURE</th><td><img src="/ICARD_Attachments/<%=rs.getString("SIGNATURE") %>"  style="width:100px;" /></td>
                                           </tr> 
                                       </table>
                                        <table class="table table-striped table-bordered table-hover"  style="font-size:12px;" >
                                        <tr class="danger">
                						 <th>Name</th><th>Blood Group</th><th>Relationship</th><th>DOB</th><th>Identification marks(s)</th>
                						</tr>                                     
    										
 <% 
 query="select FNAME,BLOOD_GROUP,RELATIONSHIP,DOB,ID_MARKS,slno from non_gaz_family  where id_no='"+rs.getString("ID_NO")+"'order by slno";
 rs1=st1.executeQuery(query);
 while(rs1.next()){
	 
 %> 							        <tr>
                                            <td><%=rs1.getString("FNAME") %></td>
                                            <td><%=rs1.getString("BLOOD_GROUP") %></td>
    										<td><%=rs1.getString("RELATIONSHIP") %></td>
    										<td><%=tf.format(df.parse(rs1.getString("DOB"))) %></td>
    										<td><%=rs1.getString("ID_MARKS") %></td>
                                         </tr>
                                         
<%}%>							
                                   	</table>
<%}else{ 
	out.println("Please Enter Valid data..");
}
	%>

                                </div>
                                <!-- /.table-responsive -->
                            </div>
                            <!-- /.panel-body -->
                        </div>
                        <!-- /.panel -->
                    </div>
                    <!-- /.col-lg-6 -->
            <footer style="font-size:20px;font-weight:bold;color:#fffff0;padding-bottom: 0px;"> 
	  		 <table style="width:100%;">
	  		  <tr>
                      
                       <td style="text-align:right;"><a href="http://10.180.18.249" target="_blank"><i><span style="font-size:20px;font-weight:bold;color:#f100f1;;">Designed, Developed and Maintained by IT Centre/ECoR/Bhubaneswar &nbsp;&nbsp;</span></i></a></td>
               </tr>       
           </table> 
	  		      
              </footer>
              
        </div>
        <!-- /#wrapper -->
<%
}

catch(SQLException e)
{
	out.println("SQL Error:"+e);
}
catch(Exception e)
{
	out.println("Error:"+e);
}
%>        <!-- /#wrapper -->
<script type="text/javascript">


function printPage(){
	//alert();
//document.getElementById("exp_excel").style.display="none";	
document.getElementById("print_img").style.display="none";
window.print();
//document.getElementById("exp_excel").style.display="";
document.getElementById("print_img").style.display="";
}

</script>
 <script src="../js/jquery.min.js"></script>

        <!-- Bootstrap Core JavaScript -->
        <script src="../js/bootstrap.min.js"></script>

        <!-- Metis Menu Plugin JavaScript -->
        <script src="../js/metisMenu.min.js"></script>

        <!-- Morris Charts JavaScript -->
        <script src="../js/dataTables/jquery.dataTables.min.js"></script>
        <script src="../js/dataTables/dataTables.bootstrap.min.js"></script>

        <!-- Custom Theme JavaScript -->
        <script src="../js/startmin.js"></script>
         <script>
            $(document).ready(function() {
                $('#myTable').DataTable({
                        responsive: true
                });
            });
        </script>


</body>
</html>
