package com.promethium.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.json.*;
import org.json.simple.JSONObject;

import com.google.gson.Gson;
import java.sql.*;
import com.promethium.helper.ConnectionErrorResponse;
import com.promethium.helper.Constants;
import com.promethium.helper.ResponseError;
import com.promethium.helper.Utils;
import com.promethium.sql.MysqlCon;

@RestController
public class Getdatabasetable {

	
	private static boolean isRemote = true;
	
	private static int typeofDb = 0;
	// Import utils call and define globaly
	@SuppressWarnings("unused")
	private Utils utils = new Utils();
	
	
	private boolean isError = false;
	
	private ResponseError _errorResult = new ResponseError();
		
	private Constants constants = new Constants();
	
	private static Connection sqlConnection = null;
	
	private static DatabaseMetaData metadata = null;
	
	private Gson gson = new Gson();
	
	@SuppressWarnings("unused")
	private static int error_point = 0;
	
	String dbName,dbPort,userName,password,host = "";
	
	@SuppressWarnings("unchecked")
	@RequestMapping(path = "/api/getdbtable", method={RequestMethod.POST})
	@ResponseStatus(value=HttpStatus.OK)
	public String Getdbtable(@RequestParam("dbname") String _dbName,@RequestParam("dbport") String dbport,@RequestParam("username") String _userName,@RequestParam("password") String _password,@RequestParam("host") String _host) {	
		
		dbName = _dbName;
		dbPort = dbport;
		userName = _userName;
		password = _password;
		host = _host.toLowerCase();
		
		boolean validateDb = validateCredential("0",_dbName,dbport,_userName,_password,_host);
		
		if(!validateDb && isError) {
			
			
			String _errorResults = gson.toJson(_errorResult);			
										
			return _errorResults;
		}
		
		else {
			
			boolean isConnected = connectRemoteDatabase();
			
			if(isConnected) {
				
				
				getDatabaseMetaData();
				
			}
			
			else {
				
				//ConnectionErrorResponse connectionError = new ConnectionErrorResponse();
				
				_errorResult.setIs_error(true);
				_errorResult.setError_msg(new ConnectionErrorResponse().getError_msg());
				
				String _errorResults = gson.toJson(_errorResult);	
				
				return _errorResults;
				
			}
			
			return "Getting datas "  + typeofDb;
			
		}
	}
	
	

	protected boolean validateCredential(String _typeOfDb,String _dbName, String _dbPort, String _userName, String _password, String _host) {
			
		
		// Validate first the host address if host is ip address
		
		
		boolean isIpv4 = Utils.validateIpv4(_host);
		
		
		// Validate database name
		
		boolean validdbName = Utils.validatedbName(_dbName);
		
		
		// Validate database port
		
		boolean validPort = Utils.validatePort(_dbPort);
		
		
		// Validate first the host address if host is url
		
		boolean validUrl = Utils.validUrl(_host.toLowerCase());
		
		// Getting any error of valid ip or valid url
		if ((!isIpv4 && !validUrl && !_host.toLowerCase().equals("localhost"))) {			
			_errorResult.setIs_error(true);
			_errorResult.setError_msg(constants.INVALID_HOST);	
			isError = true;
			error_point = 1;		
		}
		
		// Getting any error of validdbName
		else if(!validdbName && !isError) {						
			_errorResult.setIs_error(true);
			_errorResult.setError_msg(constants.INVALID_DBNAME);
			isError = true;
			error_point = 2;						
		}
		
		// Getting any error of validPort
		else if(!validPort && !isError) {								
			_errorResult.setIs_error(true);
			_errorResult.setError_msg(constants.INVALID_DBPORT);
			isError = true;
			error_point = 3;		
			
		}		
		
		else {			
			_errorResult.setIs_error(false);
			_errorResult.setError_msg("No erros are gretings");
			isError = false;
			error_point = 0;		
			
		}
				
		return isError ? false : true;
		
	}
	
	
	protected boolean connectRemoteDatabase() {
		
		boolean result = false;
		
		
		// connection type Mysql
		if (typeofDb == 0) {
			
			MysqlCon con = new MysqlCon();
			sqlConnection = con.makeconnection(dbName,dbPort,userName,password,host);
			
						
			if (sqlConnection != null) {
				
				result = true;
			}
				
			
		}
			
		
		return result;
		
	}
	
	protected void getDatabaseMetaData()
    {
        try {

        	metadata = sqlConnection.getMetaData();
            String[] types = {"TABLE"};
            ResultSet rs = metadata.getTables(null, null, "%", types);
            while (rs.next()) {
                System.out.println(rs.getString("TABLE_NAME"));
            }
        } 
            catch (SQLException e) {
            	e.printStackTrace();
            }
    }
	
	
	
}
