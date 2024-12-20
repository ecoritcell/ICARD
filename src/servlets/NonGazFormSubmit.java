package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import communication.DBConnect;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.javapapers.java.QRCode;
import com.oreilly.servlet.MultipartRequest;

import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;
/**
 * Servlet implementation class NonGazFormSubmit
 */
@WebServlet("/NonGazFormSubmit")
public class NonGazFormSubmit extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest mrequest, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		System.out.println("NonGazFormSubmit Called");
		MultipartRequest mrequest = new MultipartRequest(request,"./webapps/ICARD_Attachments",5*1024*1024,new DefaultFileRenamePolicy( ));
		String empno=mrequest.getParameter("EMP_NO");		
		String empname=mrequest.getParameter("EMP_NAME");
		String designation=mrequest.getParameter("DESIGNATION");
		String dob=mrequest.getParameter("EMPDOB");
		String dept=mrequest.getParameter("DEPARTMENT");
		String station=mrequest.getParameter("STATION");
		String bill_unit=mrequest.getParameter("BILL_UNIT");
		String reason=mrequest.getParameter("REASON");
		String res_address=mrequest.getParameter("RES_ADDRESS");
		String rly_number=mrequest.getParameter("RLY_NO");
		String mobile_no=mrequest.getParameter("MOBILE_NO");
		String emergency_contact_name=mrequest.getParameter("EMERGENCY_CONTACT_NAME");
		String emergency_contact_no=mrequest.getParameter("EMERGENCY_CONTACT_NO");	
		String photo=mrequest.getFilesystemName("PHOTO");
		String sign=mrequest.getFilesystemName("SIGN");
		String dept_name="";
		String[] slno=mrequest.getParameterValues("FSLNO");
		String[] fname=mrequest.getParameterValues("FNAME");
		String[] bg=mrequest.getParameterValues("FBG");
		String[] frelationship=mrequest.getParameterValues("FRELATIONSHIP");
		String[] fdob=mrequest.getParameterValues("FDOB");
		String[] fid_marks=mrequest.getParameterValues("FID_MARKS");
		int id_slno=0;
		ResultSet rs=null;
		Statement st=null;
		//String userid=mrequest.getParameter("userid");
		System.out.println("begin"+photo);
		try {
			Connection con=DBConnect.getConnection();
			st=con.createStatement();
			 String query="select dept_name from department where dept_code='"+dept+"'";
			 rs=st.executeQuery(query);
			 if(rs.next()){
				dept_name=rs.getString(1); 
			 }
			 query="select max(id_no) from non_gaz_master";
			 rs=st.executeQuery(query);
			 if(rs.next()){
				 id_slno=rs.getInt(1)+1; 
			 }
			
			String qrCodeData = "Emp No: "+empno+"\nEmpname: "+empname+"\nDesignation: "+designation+"\nDOB: "+dob+"\nDept: "+dept_name+"\nStation: "+station+"\nResidential Address: "+res_address+"\nRly Number: "+rly_number+"\nMobile_no: "+mobile_no+"\nEmergency contact name: "+emergency_contact_name+"\nEmergency contact no: "+emergency_contact_no;
			String filePath =  "./webapps/ICARD_QR_codes/"+empno+".png";
			String charset = "UTF-8"; // or "ISO-8859-1"
			Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
			hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
			QRCode.createQRCode(qrCodeData, filePath, charset, hintMap, 200, 200);
		
		PreparedStatement ps=null;
		ps=con.prepareStatement("insert into non_gaz_master(EMPNO,EMPNAME,DESIGNATION,DOB,DEPARTMENT,STATION,RES_ADDRESS,RLY_NUMBER,MOBILE_NUMBER,EMERGENCY_CONTACT_NAME,EMERGENCY_CONTACT_NO,PHOTO,SIGNATURE,QR_CODE,ID_NO,BILLUNIT,REASON,STATUS,LAST_UPDATE) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,'1',sysdate)");
		ps.setString(1,empno);
		ps.setString(2,empname);		
		ps.setString(3,designation);
		ps.setString(4,dob);
		ps.setString(5,dept);
		ps.setString(6,station);
		ps.setString(7,res_address);
		ps.setString(8,rly_number);		
		ps.setString(9,mobile_no);		
		ps.setString(10,emergency_contact_name);
		ps.setString(11,emergency_contact_no);
		ps.setString(12,photo);
		ps.setString(13,sign);
		ps.setString(14,empno+".png");
		ps.setInt(15,id_slno);
		ps.setString(16,bill_unit);
		ps.setString(17,reason);
		ps.executeUpdate();
		System.out.println("1st table");
		
	for(int i=0;i<slno.length;i++){
		System.out.println(fname[i]);
		System.out.println(bg[i]);
		System.out.println(frelationship[i]);
		System.out.println(fdob[i]);
		System.out.println(slno[i]);
		ps=con.prepareStatement("insert into non_gaz_family(ID_NO,FNAME,BLOOD_GROUP,RELATIONSHIP,DOB,ID_MARKS,SLNO) values(?,?,?,?,?,?,?)");
			
		ps.setInt(1,id_slno);
		ps.setString(2,fname[i]);
		ps.setString(3,bg[i]);
		ps.setString(4,frelationship[i]);
		ps.setString(5,fdob[i]);
		ps.setString(6,fid_marks[i]);
		ps.setString(7,slno[i]);
		ps.executeUpdate();
		System.out.println("2nd table");
	}
		System.out.println("Saved Successfully");
	       response.sendRedirect("regForm_NonGaz.jsp?ack=Saved Successfully. Your application ID="+id_slno+", keep it for future use.");
		

		}catch(SQLException e) {
			System.out.println(e);			
			response.sendRedirect("regForm_NonGaz.jsp?ack=Some Error Occured, Please Try Later"+e);	
			
			}
		catch(Exception e) {
			System.out.println(e);			
			response.sendRedirect("regForm_NonGaz.jsp?ack=Some Error Occured, Please Try Later"+e);	
			
			}
	}

}
