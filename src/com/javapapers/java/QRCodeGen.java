package com.javapapers.java;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.zxing.EncodeHintType;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import communication.DBConnect;

/**
 * Servlet implementation class QRCodeGen
 */
@WebServlet("/QRCodeGen")
public class QRCodeGen extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		String userid=request.getParameter("userid");
		String username=request.getParameter("username");
		String dob=request.getParameter("dob");
		System.out.println(userid);
		String qrCodeData = makeUrl(request).replace("QRCodeGen", "Attendance?id="+userid);
		String filePath = "./webapps/qrcode_images/"+userid+".png";
		String charset = "UTF-8"; // or "ISO-8859-1"
		Map<EncodeHintType, ErrorCorrectionLevel> hintMap = new HashMap<EncodeHintType, ErrorCorrectionLevel>();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
		System.out.println(makeUrl(request).replace("QRCodeGen", "login.jsp?"+userid));
		
		try {
		QRCode.createQRCode(qrCodeData, filePath, charset, hintMap, 200, 200);
		Connection con=DBConnect.getConnection();
		PreparedStatement ps=null;
		ps=con.prepareStatement("insert into users(userid,username,dob) values(?,?,?)");
		ps.setString(1,userid);	
		ps.setString(2,username);	
		ps.setString(3,dob);
		ps.executeUpdate();
		System.out.println("Saved Successfully");
		response.sendRedirect("qcgen.jsp?id="+userid);
		
		}catch(SQLException e) {
			System.out.println(e);			
			response.sendRedirect("qcgen.jsp?ack=Some SQL Error Occured, Please Try Later");	
			
			}
		catch(Exception e) {
			System.out.println(e);			
			response.sendRedirect("qcgen.jsp?ack=Some Error Occured, Please Try Later");	
			
			}
	}
	public static String makeUrl(HttpServletRequest request)
	{
	    return request.getRequestURL().toString();
	}

}
