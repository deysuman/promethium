package com.promethium.sql;

import java.sql.Connection;
import java.sql.DriverManager;

import com.promethium.models.ConnectionErrorResponse;

public class OracleCon {

	private static Connection connection = null;

	public Connection makeconnection(String _dbName, String _dbPort, String _userName, String _password, String _host) {
		
		try{
			
			Class.forName("oracle.jdbc.driver.OracleDriver");  
			connection = DriverManager.getConnection(  
					"jdbc:oracle:thin:@"+ _host +":"+ _dbPort +":"+_dbName , _userName, _password);						
			
		}
		
		catch(Exception e){		
			
			ConnectionErrorResponse error_response = new ConnectionErrorResponse();	
			error_response.setError_msg(e.toString());
			
			System.out.println (e.toString());
			
			
		} 
		
		return connection;
		
	}  
	
}
