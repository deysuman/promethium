package com.promethium.sql;

import java.sql.*;


import com.promethium.helper.ConnectionErrorResponse;  


public class MysqlCon {	

	
	private static Connection connection = null;

	public Connection makeconnection(String _dbName, String _dbPort, String _userName, String _password, String _host) {
		
		try{
			
			Class.forName("com.mysql.jdbc.Driver");  
			connection = DriverManager.getConnection(  
			"jdbc:mysql://"+_host+":"+_dbPort+"/"+_dbName,_userName,_password);						
			
		}
		
		catch(Exception e){		
			
			ConnectionErrorResponse error_response = new ConnectionErrorResponse();	
			error_response.setError_msg(e.toString());
			
			System.out.println (e.toString());
			
			
		} 
		
		return connection;
		
	}  
			
}
