package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

	private static final String dbType = "mysql";
	private static final String hostName = "localhost";
	private static final String port = "3306";
	private static final String databaseName = "DEMO_TEST";
	private static final String userName = "root";
	private static final String password = "insync";
	
	public static Connection getConnection(){
		String result="";
		Connection conn = null;
		String driverClass = "";
		String DB_URL = "";
		//TODO: read String dbType,String hostName,String port,String databaseName,String userName,String password from properties file
		if ("mysql".equalsIgnoreCase(dbType)) {
			if(databaseName!=null && databaseName.trim().length() > 0)
				DB_URL = "jdbc:mysql://" + hostName + ":" + port + "/"+ databaseName;
			else
				DB_URL = "jdbc:mysql://" + hostName + ":" + port;
			driverClass = "org.gjt.mm.mysql.Driver";
			  //driverClass = "com.mysql.jdbc.Driver";
		} else if ("sqlserver".equalsIgnoreCase(dbType)) {
			if(databaseName!=null && databaseName.trim().length() > 0)
				DB_URL = "jdbc:sqlserver://" + hostName + ":" + port + ";DatabaseName=" + databaseName;
			else 
				DB_URL = "jdbc:sqlserver://" + hostName + ":" + port;
			driverClass = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
		} else if ("oracle".equalsIgnoreCase(dbType)) {
			DB_URL = "jdbc:oracle:thin:@" + hostName + ":" + port + ":"	+ databaseName;
			driverClass = "oracle.jdbc.driver.OracleDriver";
		}

		try {
			// STEP 2: Register JDBC driver
			Class.forName(driverClass);
			// STEP 3: Open a connection
			conn = DriverManager.getConnection(DB_URL, userName, password);
		} catch (Throwable t) {
			
		} 
		return conn;
	}
}
