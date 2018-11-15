package com.promethium.sql;

import java.sql.Connection;
import java.sql.DriverManager;

import com.promethium.models.ConnectionErrorResponse;

public class TeradataCon {

	private static Connection connection = null;

	public Connection makeconnection(String _dbName, String _dbPort, String _userName, String _password, String _host) {
		
		try{
			
			Class.forName("com.teradata.jdbc.TeraDriver");
			String connectionString = "jdbc:teradata://"+_host+"/"+_dbName+","+_dbPort;
	        String user = _userName;
	        String password = _password;
			connection = DriverManager.getConnection(connectionString, user, password);						
			
		}
		
		catch(Exception e){		
			
			ConnectionErrorResponse error_response = new ConnectionErrorResponse();	
			error_response.setError_msg(e.toString());
			
			System.out.println (e.toString());
			
			
		} 
		
		return connection;
		
	}  
	
	
}
