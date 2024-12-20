<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">

        <title>ICARD:Reg Form(Non Gaz)</title>
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
       <script type="text/javascript" src="./js/jquery.min.js"></script> 
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

        <div id="wrapper" >

            <!-- Navigation -->
            <nav class="navbar navbar-inverse navbar-fixed-top " role="navigation">
             <table  width="100%" border="0" style="background:#225b45;">
            <col width="20%"><col width="60%"><col width="20%">
            <tr>
            <td style="text-align:left;">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href="https://eastcoastrail.indianrailways.gov.in/" target="_blank"><img src="./images/IR_logo.png" width="100" height="110"><br>&nbsp;&nbsp;<span style="font-size:20px;font-weight:bold;color:#f1f1f1;"> East Coast Railway</span></a></td>
            <td style="text-align:center;"><span style="font-size:30px;font-weight:bold;color:#f0fdfd;">Online Form for I-cards</span></td>
            <td style="text-align:right;"><a href="http://10.180.18.249" target="_blank"><span style="font-size:20px;font-weight:bold;color:#f1fff1;;">Developed by IT Centre&nbsp;&nbsp;</span><br><img src="./images/itcentre_logo.png" width="100" height="100"></a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
            </tr>
            </table> 
                <div class="navbar-default sidebar " role="navigation">
                    <div class="sidebar-nav navbar-collapse">
                        <ul class="nav" id="side-menu">
                            <li>
                                <a href="index.jsp"  target="mainf"><i class="fa fa-home fa-fw"></i> Home</a>
                            </li>
                            <li>
                                <a href="regForm_NonGaz.jsp"  target="mainf"><i class="fa fa-home fa-edit"></i> Apply for new I-card (NG)</a>
                            </li>
                             <li>
                                <a href="regForm_Gaz.jsp"  target="mainf"><i class="fa fa-home fa-edit"></i> Apply for new I-card (GAZ)</a>
                            </li>
                            <li>
                                <a href="application_status.jsp"  target="mainf"><i class="fa fa-home fa-edit"></i> Application details & status </a>
                            </li>
                            
                        </ul>
                    </div>
                    <!-- /.sidebar-collapse -->
                </div>
                <!-- /.navbar-static-side -->
            </nav>

            <div id="page-wrapper"><br>
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-lg-12">
                            <h2 class="page-header">Employee Registration Form (Non Gazetted)</h2>
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->
                    <div class="row">
                        <div class="col-lg-12">
                            <div class="panel panel-primary">
                                <div class="panel-heading">
                                   <h4>Employee Registration Form (Non Gazetted)</h4>
                                    <h3><%=request.getParameter("ack")==null?"":request.getParameter("ack") %></h3>
                                </div>
                                <div class="panel-body">
                                    <div class="row">
                                       
          <form name="NonGazFormSubmit" id="NonGazFormSubmit" method="post" ENCTYPE="multipart/form-data" target="_self"   action="./NonGazFormSubmit"  >	
          		
		<TABLE  class="table" >
							   
			<TR class="success">
				<TD colspan="3">					 
                 <label style="color:#445ddd">Employee Details:</label>	                  
                 </TD>
				
	       </TR>
	          <TR class="success">
		        <TD>			
				<div class="form-group has-success">
                   <label>Employee Name:<code><i class="fa fa-asterisk"></i></code></label>
                     <input name="EMP_NAME" id="emp_name" type="text"  class="form-control" placeholder="Enter Employee Name" autofocus required />                                              
                </div>
				</TD>
	           	
				 <TD>			
				<div class="form-group has-success">
                   <label>Designation:<code><i class="fa fa-asterisk"></i></code></label>
                     <input name="DESIGNATION" type="text"  class="form-control" placeholder="Enter Designation" required />                                              
                </div>
				</TD>	
				<TD>			
				<div class="form-group has-success">
                   <label>Employee No:<code><i class="fa fa-asterisk"></i></code></label>
                     <input name="EMP_NO" type="text"  class="form-control" maxlength="11" placeholder="Enter Employee No." required />                                              
                </div>
				</TD>			 			 
	       </TR>
	       <TR class="success">
	       		<TD>			
				<div class="form-group has-success">
                   <label>Date Of Birth:<code><i class="fa fa-asterisk"></i></code></label>
                     <input name="EMPDOB" id="empdob" type="text"  class="form-control" placeholder="Enter Date Of Birth" readonly   onClick="javascript:NewCssCal('empdob','ddMMMyyyy')" required />                                              
                </div>
				</TD>
		        <TD>			
				<div class="form-group has-success">
                   <label>Department:<code><i class="fa fa-asterisk"></i></code></label>
                     <select name="DEPARTMENT" id="dept" type="text"  class="form-control"  required >   
                     <option> select</option>
                     </select>                                           
                </div>
				</TD>
	            <TD>			
				<div class="form-group has-success">
                   <label>Station:<code><i class="fa fa-asterisk"></i></code></label>
                     <input name="STATION" type="text" VALUE="BHUBANESWAR" class="form-control" placeholder="Enter Station" required />                                              
                </div>
				</TD>
							 			 
	       </TR>
	       <TR class="success">
				<TD>			
				<div class="form-group has-success">
                   <label>Bill Unit:<code><i class="fa fa-asterisk"></i></code></label>
                     <select name="BILL_UNIT" id="bill" type="text"  class="form-control"  required >   
                     <option> select</option>
                     </select>                                           
                </div>
				</TD>
		        <TD>			
				 <div class="form-group has-success">
                   <label> Residential Address:<code><i class="fa fa-asterisk"></i></code></label>
                   <textarea name="RES_ADDRESS" class="form-control" placeholder="Enter Residential Address" required ></textarea>
				   
                  </div>
				</TD>
	           <TD>			
				<div class="form-group has-success">
                   <label>Rly Contact Number:</label>
                     <input name="RLY_NO" type="text"  class="form-control" placeholder="Enter Rly Contact Number"  />                                              
                </div>
				</TD>
			</TR>
	       <TR class="success">		
				<TD>			
				<div class="form-group has-success">
                   <label>Mobile Number:<code><i class="fa fa-asterisk"></i></code></label>
                     <input name="MOBILE_NO" type="text"  class="form-control" placeholder="Enter Valid Mobile Number" required />
                                                                   
                </div>
				</TD>
				<TD>			
				<div class="form-group has-success">
                   <label>Reason for Application:<code><i class="fa fa-asterisk"></i></code></label>
                     <input id="REASON" name="REASON" type="text"  class="form-control" placeholder="Enter Reason" required />                                              
                </div>
				</TD>
				<TD></TD>
				 			 			 
	       </TR>
	       				   
		   
		   <TR class="danger">
				<TD colspan="3">					 
                 <table class="table" style="width:100%">
                 
                 <TR class="danger">
				<TD colspan="6">					 
                 <label style="color:#445ddd">Details of family members (As per pass rule):</label>	                  
                 </TD>
	            </TR>	            
                 <tr class="danger">
                 <th>Name</th><th>Blood Group</th><th>Relationship</th><th>DOB</th><th>Identification mark(s)</th><th></th>
                 </tr>
                 <tr class="danger">
                 <td><input id="NAME" type="text"  /></td>
                 <td><input id="BG" type="text"   /></td>
                 <td><input id="RELATIONSHIP" type="text" VALUE="SELF" /></td>
                 <td><input id="DOB"  type="text" readonly onClick="javascript:NewCssCal('DOB','ddMMMyyyy')" /> </td>
                 <td><input id="ID_MARKS" type="text" /></td>
                 <td> <input  name="add" type="button" id="addBtn" value="Add" class="btn btn-primary" ></td>
                 </tr>           
                 </table>
                 <table class="table table-striped table-bordered table-hover"  style="font-size:12px;" >
                                        <thead>
                                            <tr class="success">
                                               <th>Sl. No</th> <th>Name</th><th>Blood Group</th><th>Relationship</th><th>DOB</th><th>Identification mark(s)</th><th></th>
                                            </tr>                                            
                                        </thead>
                                        <tbody id="tbody">
							
                                         
                                       </tbody>
                                    </table>	                  
                 </TD>
	       </TR>
	       <TR class="info">
				<TD colspan="3">					 
                 <label style="color:#445ddd">Additional Details:</label><p style="color:red">NOTE: File name should be Employee's FirstName_photo, FirstName_sign (jpeg/jpg/png)</br>Upload well scanned Photo and Signature. For better visibility Avoid uploading Mobile scanned files and Selfie</p>	                  
                 </TD>
	        </TR>
	        <TR class="info">
		        <TD>			
				 <div class="form-group has-success">
                   <label>Emergency Contact Name:<code><i class="fa fa-asterisk"></i></code></label>
                   <input name=EMERGENCY_CONTACT_NAME type="text" class="form-control" placeholder="Enter Emergency Contact Name" required ></textarea>
				   
                  </div>
				</TD>
	           <TD>			
				<div class="form-group has-success">
                   <label>Emergency Contact Number:<code><i class="fa fa-asterisk"></i></code></label>
                     <input name="EMERGENCY_CONTACT_NO" type="text"  class="form-control" placeholder="Enter Emergency Contact Number" required />                                              
                </div>
				</TD>
				<TD>
							
				</TD>				
			</TR>
			<TR class="info">
				<TD>
				<div class="form-group has-success">
                  <label>Upload Photo:<code><i class="fa fa-asterisk"></i></code></label>
                     <input name="PHOTO" type="file"  class="form-control" id="file1" accept="image/gif, image/jpeg, image/png" onchange="loadPhoto(event)"  required />                                                
                </div>					
				</TD>
				<TD>
					<img id="photo" width="160px"  />				
				</TD>
				<TD>
												
				</TD>	 						 			 
	          </TR>
	       <TR class="info">
				<TD>
				<div class="form-group has-success">
                  <label>Upload Signature:<code><i class="fa fa-asterisk"></i></code></label>
                     <input name="SIGN" type="file"  class="form-control" id="file2" accept="image/gif, image/jpeg, image/png" onchange="loadImage(event)" required />                                                
                </div>					
				</TD>
				<TD>
						<img id="sign" width="160px"  />			
				</TD>
				<TD>
								
				</TD>	 			 
	       </TR>
	        <TR>
				<TD class="formheader" align="center" colspan="5" height="30"> 
						<INPUT name="submit" id="submit"  type="submit"  value="SUBMIT" class="btn btn-primary">
						&nbsp;&nbsp;
				 <INPUT name="clear" type="reset"  value=" CLEAR " class="btn btn-primary" onclick="javascript:clearData()"></TD>
			</TR>
		</TABLE>
		
</form>
                                        
                                    </div>
                                    <!-- /.row (nested) -->
                                </div>
                                <!-- /.panel-body -->
                            </div>
                            <!-- /.panel -->
                        </div>
                        <!-- /.col-lg-12 -->
                    </div>
                    <!-- /.row -->
                </div>
                <!-- /.container-fluid -->
            </div>
            <!-- /#page-wrapper -->
            <footer style="font-size:20px;font-weight:bold;color:#fffff0;padding-bottom: 0px;"> 
	  		 <table style="width:100%;">
	  		  <tr>
                      
                       <td style="text-align:right;"><a href="http://10.180.18.249" target="_blank"><i><span style="font-size:20px;font-weight:bold;color:#f1fff1;;">Designed,Developed and Maintained by IT Centre/ECoR/Bhubaneswar &nbsp;&nbsp;</span></i></a></td>
               </tr>       
           </table> 
	  		      
              </footer>
        </div>
        <!-- /#wrapper -->
<script>
var loadPhoto = function(event) {
	var image = document.getElementById('photo');
	image.src = URL.createObjectURL(event.target.files[0]);
};
var loadImage = function(event) {
	var image = document.getElementById('sign');
	image.src = URL.createObjectURL(event.target.files[0]);
};
</script>
<script>

$(document).ready(function(){
	//alert();
            $.ajax({
                url:"./jsp/dept_list.jsp",               
                success:function(data)
                {
                	//alert(data);
                    $("#dept").html(data)
                } 
            });
            
            $.ajax({
                url:"./jsp/billunit_list.jsp",               
                success:function(data)
                {
                	//alert(data);
                    $("#bill").html(data)
                } 
            });
     
});   
var rowIdx = 1;
// jQuery button click event to add a row 
$("#addBtn").on('click', function () { 
	
if($("#NAME").val()==null||$("#NAME").val()==""){
alert("Please enter name");
return false;
}

if($("#BG").val()==null||$("#BG").val()==""){
alert("Please Enter blood group");
return false;
}

if($("#RELATIONSHIP").val()==null||$("#RELATIONSHIP").val()==""){
alert("Please enter relationship");
return false;
}

if($("#DOB").val()==null||$("#DOB").val()==""){
alert("Please Enter date of birth");
return false;
}
if($("#ID_MARKS").val()==null||$("#ID_MARKS").val()==""){
	alert("Please Enter identification marks");
	return false;
	}	
	
	// Adding a row inside the tbody. 
	$("#tbody").append("<tr><td><input type='text' name='FSLNO' id='fslno' size='1'  value="+ rowIdx++ +" readonly ></td><td><input type='text'  name='FNAME' value='"+$("#NAME").val()+"' readonly /></td> <td><input type='text'  name='FBG'   value='"+$("#BG").val()+"' readonly /></td> <td><input type='text'  name='FRELATIONSHIP' value='"+$("#RELATIONSHIP").val()+"' readonly /> <td><input type='text' name='FDOB' value='"+$("#DOB").val()+"' readonly /></td> <td><input type='text' name='FID_MARKS'  value='"+$("#ID_MARKS").val()+"' readonly /></td><td class='text-center'> <button class='btn btn-danger remove' type='button'>Remove</button> </td> </tr>"); 
	$("#NAME").val("");$("#BG").val("");$("#RELATIONSHIP").val("");$("#DOB").val("");$("#ID_MARKS").val("");
});


$("#submit").on('click', function () {
	
	var x=$("#fslno").val();
	if (typeof x === "undefined") {
		alert("Please add details of atleast one family member.");
		return false;
	}
	else{
		var x=$("#REASON").val();
		if(x.length > 50){
			
			alert("Reason length should not be more than 50 characters.");		
			return false;
		}
		else{
			return true;
		}
			
	}
	return true;
	//alert("hello"+x);
	
});
$("#emp_name").on('blur', function () {
	var x=$("#fslno").val();
	if (typeof x === "undefined") {
	$("#NAME").val($("#emp_name").val());
	}
});
$("#empdob").on('blur', function () {
	var x=$("#fslno").val();
	if (typeof x === "undefined") {
	$("#DOB").val($("#empdob").val());	
	}
});

$('#tbody').on('click', '.remove', function () { 

	// Getting all the rows next to the row 
	// containing the clicked button 
	var child = $(this).closest('tr').nextAll(); 

	// Iterating across all the rows 
	// obtained to change the index 
	child.each(function () { 

	// Getting <tr> id. 
	var id = $(this).attr('id'); 

	// Getting the <p> inside the .row-index class. 
	var idx = $(this).children('.row-index').children('p'); 

	// Gets the row number from <tr> id. 
	var dig = parseInt(id.substring(1)); 

	// Modifying row index. 
	idx.html(`Row ${dig - 1}`); 

	// Modifying row id. 
	$(this).attr('id', `R${dig - 1}`); 
	}); 

	// Removing the current row. 
	$(this).closest('tr').remove(); 

	// Decreasing total number of rows by 1. 
	rowIdx--; 
}); 

$('body').on('keydown', 'input, select', function(e) {
    if (e.key === "Enter") {
        var self = $(this), form = self.parents('form:eq(0)'), focusable, next;
        focusable = form.find('input,a,select,button,textarea').filter(':visible');
        next = focusable.eq(focusable.index(this)+1);
        if (next.length) {
            next.focus();
        } else {
            form.submit();
        }
        return false;
    }
});
</script>
        <!-- Bootstrap Core JavaScript -->
        <script src="./js/bootstrap.min.js"></script>

        <!-- Metis Menu Plugin JavaScript -->
        <script src="./js/metisMenu.min.js"></script>

        <!-- Custom Theme JavaScript -->
        <script src="./js/startmin.js"></script>

    </body>
</html>
