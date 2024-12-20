<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <meta name="description" content="">
        <meta name="author" content="">

        <title>ICARD:Login</title>
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
                                <a href="application_status.jsp"  target="mainf"><i class="fa fa-home fa-edit"></i> Application details & status</a>
                            </li>
                            
                        </ul>
                    </div>
                    <!-- /.sidebar-collapse -->
                </div>
                <!-- /.navbar-static-side -->
            </nav>
</br>
            <div id="page-wrapper"><br>
            <div class="row" > <br><br>
             <div class="col-md-8 col-md-offset-4"></div>       
           <br> <div class="col-lg-2 col-md-offset-5" >			
				<div class="login-panel panel panel-primary">
					<div class="panel-heading">
						<h3 class="panel-title">I-Card Admin Login </h3>
					</div>
					<div class="panel-body">
						 
								<form action="./ICARDAdminLogin" method="post">
								<span class="text-danger"><%=request.getParameter("ack")==null?"":request.getParameter("ack")%> </span>
							<fieldset>
								<div class="form-group">
									<input class="form-control" placeholder="Enter USER ID" name="USERID" type="text" autofocus required>
								</div>
								<div class="form-group">
									<input class="form-control" placeholder="Password" 	name="PASSWORD" type="password" value="" required>
								</div>
								
								<input type="submit" name="submit" value="Login" 	class="btn btn-lg btn-success btn-block"> 
								
							</fieldset>
						</form>
							
					</div>
				</div>
				
			</div> 
					                       				
	  </div></br>
	 <center><img src="./images/G20 theme and logo.png" height="200" width="200"/></center>
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

        <!-- Bootstrap Core JavaScript -->
        <script src="./js/bootstrap.min.js"></script>

        <!-- Metis Menu Plugin JavaScript -->
        <script src="./js/metisMenu.min.js"></script>

        <!-- Custom Theme JavaScript -->
        <script src="./js/startmin.js"></script>

    </body>
</html>
