package servlets;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import communication.DBConnect;

/**
 * Servlet implementation class ICARDAdminLogin
 */
@WebServlet("/ICARDAdminLogin")
public class ICARDAdminLogin extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		System.out.println("Admin Login");
		HttpSession session=request.getSession();
		String password=request.getParameter("PASSWORD");
		String admin_id=request.getParameter("USERID");
		String ipAddress =  request.getRemoteAddr();
		try {
		Connection con=DBConnect.getConnection();
		PreparedStatement ps=con.prepareStatement("SELECT admin_id FROM icard_admins WHERE admin_id LIKE ? AND  password LIKE ?");
		ps.setString(1,admin_id);
		ps.setString(2,password);
		ResultSet rs=ps.executeQuery();
		System.out.println("exec query");
		if(rs.next()) {
		    ps=con.prepareStatement("insert into icard_log(admin_id,password,ip_address,log_time) values(?,?,?,sysdate)");
			ps.setString(1,admin_id);
			ps.setString(2, password);
			ps.setString(3, ipAddress);
		    ps.executeUpdate();
			System.out.println("Logged in Successfully"+rs.getString(1));
			session.setAttribute("userid", rs.getString(1));			
			response.sendRedirect("./jsp/index.jsp");	
			
			
		}else {
			response.sendRedirect("index.jsp?ack=Invalid Credentials,Try again!");	
		}
		}catch(SQLException e) {
			System.out.println(e);			
			request.setAttribute("ack", "Some Error Occured, Please Try Later");
			request.getRequestDispatcher("index.jsp").forward(request, response);	
			
			}
		catch(Exception e) {
			System.out.println(e);			
			request.setAttribute("ack", "Some Error Occured, Please Try Later");
			request.getRequestDispatcher("index.jsp").forward(request, response);	
			
			}
	}

}
