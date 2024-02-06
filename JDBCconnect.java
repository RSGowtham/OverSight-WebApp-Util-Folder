//$Id$
package DBconnection;
import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCconnect {

	public static Connection connect()
	{
		Connection con=null;
		try
		{
			Class.forName("org.postgresql.Driver");
			String url="jdbc:postgresql://localhost:5432/test";
			String password="";
			String username="GTS";
		
			con=DriverManager.getConnection(url,username,password);
		}
		catch(Exception e)
		{
			System.err.println(e);
		}
		
		return con;
	}
	
}
