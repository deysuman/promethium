package com.promethium.sql;

import java.sql.Connection;
import java.sql.DriverManager;

import com.promethium.models.ConnectionErrorResponse;

public class HiveCon {
	
	private static Connection connection = null;

	public Connection makeconnection(String _dbName, String _dbPort, String _userName, String _password, String _host) {
		
		try{
			
			Class.forName("org.apache.hive.jdbc.HiveDriver");  
			connection = DriverManager.getConnection("jdbc:hive2://"+_host+":"+_dbPort+"/"+_dbName, _userName, _password);
			
			
			
		    
			
		}
		
		catch(Exception e){		
			
			ConnectionErrorResponse error_response = new ConnectionErrorResponse();	
			error_response.setError_msg(e.toString());
			
			System.out.println (e.toString());
			
			
		} 
		
		return connection;
		
	}  

}
