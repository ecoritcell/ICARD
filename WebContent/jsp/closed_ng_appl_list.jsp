<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">

        <title>ICARD:Applications</title>
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
       <script type="text/javascript" src="../js/jquery.min.js"></script> 
       <script src="../js/datespecial.js"></script>
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

        <div id="wrapper" >

            <!-- Navigation -->
            <nav class="navbar navbar-inverse navbar-fixed-top " role="navigation">
             <table  width="100%" border="0" style="background:#225b45;">
            <col width="20%"><col width="60%"><col width="20%">
            <tr>
            <td style="text-align:left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="https://eastcoastrail.indianrailways.gov.in/" target="_blank"><img src="../images/IR_logo.png" width="100" height="110"><br>&nbsp;&nbsp;<span style="font-size:20px;font-weight:bold;color:#f1f1f1;"> East Coast Railway</span></a></td>
            <td style="text-align:center;"><span style="font-size:30px;font-weight:bold;color:#f0fdfd;">Online Form for I-cards</span></td>
            <td style="text-align:right;"><a href="http://10.180.18.249" target="_blank"><span style="font-size:20px;font-weight:bold;color:#f1fff1;;">Developed by IT Centre&nbsp;&nbsp;</span><br><img src="../images/itcentre_logo.png" width="100" height="100"></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
            </tr>
            </table> 
                <div class="navbar-default sidebar " role="navigation">
                    <div class="sidebar-nav navbar-collapse">
                        <ul class="nav" id="side-menu">
                            <li>
                                <a href="index.jsp"  target="mainf"><i class="fa fa-home fa-fw"></i> Home</a>
                            </li>
                            <li>
                                <a href="non_gaz_appl_list.jsp"  target="mainf"><i class="fa fa-home fa-edit"></i>Pending Applications(NG)</a>
                            </li>
                             <li>
                                <a href="gaz_appl_list.jsp"  target="mainf"><i class="fa fa-home fa-edit"></i>Pending Applications(GAZ)</a>
                            </li>
                             <li>
                                <a href="closed_ng_appl_list.jsp"  target="mainf"><i class="fa fa-home fa-edit"></i>Closed Applications(NG)</a>
                            </li>
                             <li>
                                <a href="closed_gaz_appl_list.jsp"  target="mainf"><i class="fa fa-home fa-edit"></i>Closed Applications(GAZ)</a>
                            </li>
                            <li>
                                <a href="../logout.jsp"  target="mainf"><i class="fa fa-home fa-edit"></i> Logout</a>
                            </li>
                        </ul>
                    </div>
                    <!-- /.sidebar-collapse -->
                </div>
                <!-- /.navbar-static-side -->
            </nav>
<%@ page import="java.util.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.sql.*" %>
<%@ page import="communication.*" %>


<%	
String userid=(String)session.getAttribute("userid");
if(userid==null){
response.sendRedirect("../index.jsp?ack=Session Expired, Please Login again!");	
return;	
}
ResultSet rs=null,rs1=null;
Statement st=null,st1=null;
PreparedStatement ps=null;
String errmsg="";

DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
DateFormat tf = new SimpleDateFormat("dd-MMM-yyyy");

try{
	Connection con=DBConnect.getConnection();
    st=con.createStatement(); 
    st1=con.createStatement(); 
  
 
%>
       <div id="page-wrapper"><br><br><br><br><br><br>
         <div class="col-lg-12">
                        <div class="panel panel-primary" id="dataTable">
                            <div  class="btn btn-lg btn-primary btn-block">
                                Non Gazetted I-Card Application List 
                                &nbsp;&nbsp;  <img src="../images/print.jpg" id="print_img" heigth="40px" width="40px" onclick="printPage()"/>
                                 &nbsp;&nbsp; <img src="../images/excel.png" id="exp_excel" heigth="40px" width="40px" onclick="tableToExcel('myTable', 'I-card application list')"  />
                            </div>
                            <!-- /.panel-heading -->
                            <div class="panel-body">
                                <div class="table-responsive">
                                    <table class="table table-striped table-bordered table-hover"  style="font-size:12px;" id="myTable">
                                        <thead>
                                            <tr class="success">
                                            	<th>Sl No</th>
                                                <th>EMPNO</th>
                                                <th>EMPNAME</th>
   											    <th>DESIGNATION</th>
                                                <th>DOB</th>
   											    <th>DEPARTMENT</th>
                                                <th>STATION</th>
   											    <th>ADDRESS</th>
   											    <th>RLY NUMBER</th>
                                                <th>MOBILE NUMBER</th>
   											    <th>EMERGENCY CONTACT NAME</th>
   											    <th>EMERGENCY CONTACT NO</th>
   											    <th>Application Date</th>
   											    <th>PHOTO</th>
   											    <th>SIGNATURE</th>
   											    <th>Name1</th>
   											    <th>BG1</th>
   											    <th>Rel1</th>
   											    <th>DOB1</th>
   											    <th>Identity1</th>
   											    <th>Name2</th>
   											    <th>BG2</th>
   											    <th>Rel2</th>
   											    <th>DOB2</th>
   											    <th>Identity2</th>
   											    <th>Name3</th>
   											    <th>BG3</th>
   											    <th>Rel3</th>
   											    <th>DOB3</th>
   											    <th>Identity3</th>
   											    <th>Name4</th>
   											    <th>BG4</th>
   											    <th>Rel4</th>
   											    <th>DOB4</th>
   											    <th>Identity4</th>
   											    <th>Name5</th>
   											    <th>BG5</th>
   											    <th>Rel5</th>
   											    <th>DOB5</th>
   											    <th>Identity5</th>
   											    <th>Name6</th>
   											    <th>BG6</th>
   											    <th>Rel6</th>
   											    <th>DOB6</th>
   											    <th>Identity6</th>
                                            </tr>
                                        </thead>
                                        <tbody>
<%
int i=1;
String query="select ID_NO,EMPNO,EMPNAME,DESIGNATION,DOB,DEPARTMENT,STATION,RES_ADDRESS,RLY_NUMBER,MOBILE_NUMBER,EMERGENCY_CONTACT_NAME,EMERGENCY_CONTACT_NO,PHOTO,SIGNATURE,LAST_UPDATE,dept_name from non_gaz_master ngm,department d where d.dept_code=ngm.department and status='C' order by last_update";
 rs=st.executeQuery(query);
 while(rs.next()){
 %>  		
										 <tr class="info">
										    <td><%=i++ %></td>
                                            <td><%=rs.getString("EMPNO") %></td>
    										<td><%=rs.getString("EMPNAME") %></td>
    										<td><%=rs.getString("DESIGNATION") %></td>
    										<td><%=rs.getString("DOB").substring(0,11) %></td>
    										<td><%=rs.getString("dept_name") %></td>
    										<td><%=rs.getString("STATION") %></td>
    										<td><%=rs.getString("RES_ADDRESS") %></td>
    										<td><%=rs.getString("RLY_NUMBER") %></td>
    										<td><%=rs.getString("MOBILE_NUMBER") %></td>
    										<td><%=rs.getString("EMERGENCY_CONTACT_NAME") %></td>
    										<td><%=rs.getString("EMERGENCY_CONTACT_NO") %></td>
    										<td><%=rs.getString("LAST_UPDATE").substring(0,11) %></td>
    										<td><img src="/ICARD_Attachments/<%=rs.getString("PHOTO") %>"  style="width:100px;" /></td>
    										<td><img src="/ICARD_Attachments/<%=rs.getString("SIGNATURE") %>"  style="width:100px;" /></td>
 <% int fcount=0;
 query="select FNAME,BLOOD_GROUP,RELATIONSHIP,DOB,ID_MARKS,slno from non_gaz_family  where id_no='"+rs.getString("ID_NO")+"'order by slno";
 rs1=st1.executeQuery(query);
 while(rs1.next()){
	fcount++; 
 %> 				
                                            <td><%=rs1.getString("FNAME") %></td>
                                            <td><%=rs1.getString("BLOOD_GROUP") %></td>
    										<td><%=rs1.getString("RELATIONSHIP") %></td>
    										<td><%=rs1.getString("DOB").substring(0,11) %></td>
    										<td><%=rs1.getString("ID_MARKS") %></td>
 <%}
 for(int j=fcount*5;j<30;j+=5){
 
 %>                        
 											<td></td><td></td><td></td><td></td><td></td>
 <%} %>					                						
                                         </tr>
 <%} %>							
                                         
                                       </tbody>
                                    </table>
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
                      
                       <td style="text-align:right;"><a href="http://10.180.18.249" target="_blank"><i><span style="font-size:20px;font-weight:bold;color:#f1fff1;;">Designed,Developed and Maintained by IT Centre/ECoR/Bhubaneswar &nbsp;&nbsp;</span></i></a></td>
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
var tableToExcel = (function() {
  var uri = 'data:application/vnd.ms-excel;base64,'
    , template = '<html xmlns:o="urn:schemas-microsoft-com:office:office" xmlns:x="urn:schemas-microsoft-com:office:excel" xmlns="http://www.w3.org/TR/REC-html40"><head><!--[if gte mso 9]><xml><x:ExcelWorkbook><x:ExcelWorksheets><x:ExcelWorksheet><x:Name>{worksheet}</x:Name><x:WorksheetOptions><x:DisplayGridlines/></x:WorksheetOptions></x:ExcelWorksheet></x:ExcelWorksheets></x:ExcelWorkbook></xml><![endif]--><meta http-equiv="content-type" content="text/plain; charset=UTF-8"/></head><body><table>{table}</table></body></html>'
    , base64 = function(s) { return window.btoa(unescape(encodeURIComponent(s))) }
    , format = function(s, c) { return s.replace(/{(\w+)}/g, function(m, p) { return c[p]; }) }
  return function(table, name) {
    if (!table.nodeType) table = document.getElementById(table)
    var ctx = {worksheet: name || 'Worksheet', table: table.innerHTML}
    window.location.href = uri + base64(format(template, ctx))
  }
})()



function printPage(){
	//alert();
document.getElementById("exp_excel").style.display="none";	
document.getElementById("print_img").style.display="none";
window.print();
document.getElementById("exp_excel").style.display="";
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
