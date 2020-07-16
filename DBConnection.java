import java.sql.*;

public class DBConnection{
	private static String url = "jdbc:mysql://mysql05.domainhotelli.fi/yuobdrlv_orderkoistine";
	private static String driver = "com.mysql.cj.jdbc.Driver";
	private static String username = "yuobdrlv_koistineadmin";
	private static String password = "pena123123";
	private static Connection con;
	
	public static Connection getConnection(){
		try{
			Class.forName(driver);
			try{
				con = DriverManager.getConnection(url,username,password);
				System.out.println("Connected!");
			}catch(SQLException e){
				System.out.println("Unable to connect: " + e);
			}
		}catch(ClassNotFoundException e){
			System.out.println("Bad driver: " + e);
		}
		return con;
		
	}
}