package communication;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnect {
	private static Connection con=null;
	public static Connection getConnection()
	{
		try
		{
			if(con==null)
			{
		Class.forName("oracle.jdbc.OracleDriver");
		con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","icard","icard");
		
			}
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		return con;
	}
}
