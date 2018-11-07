package com.promethium.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import com.promethium.helper.Constants;
import com.promethium.helper.Utils;
import com.promethium.models.ColumnSchema;
import com.promethium.models.ConnectionErrorResponse;
import com.promethium.models.Database;
import com.promethium.models.ResponseError;
import com.promethium.models.ResponseSuccess;
import com.promethium.models.Tables;
import com.promethium.sql.MysqlCon;
import com.promethium.sql.OracleCon;

@RestController
public class Getdatabasetable {

	
	private static boolean isRemote = true;
	
	private static int typeofDb = 0;
	// Import utils call and define globaly
	@SuppressWarnings("unused")
	private Utils utils = new Utils();
	
	private static int tableLimit = -1;
	
	private static int tableCount = 0;
	
	private static int tableAvailable = 0;
	
	private boolean isError = false;
	
	private ResponseError _errorResult = new ResponseError();
		
	private Constants constants = new Constants();
	
	private static Connection sqlConnection = null;
	
	private static DatabaseMetaData metadata = null;
	
	private Gson gson = new Gson();
	
	private static List<Tables> table_list = new ArrayList<>();
	
	@SuppressWarnings("unused")
	private static int error_point = 0;
	
	String dbName,dbPort,userName,password,host = "";
		
	
	@SuppressWarnings("unchecked")
	@RequestMapping(path = "/api/getdbtable", method={RequestMethod.POST})
	@ResponseStatus(value=HttpStatus.OK)
	public String Getdbtable(@RequestParam("dbname") String _dbName,@RequestParam("dbport") String dbport,@RequestParam("username") String _userName,@RequestParam("password") String _password,@RequestParam("host") String _host,@RequestParam("dbtype") String dbtype,@RequestParam(value = "limit", required = false) String tablelimit) {	
		
		
		
		
		
		dbName = _dbName;
		dbPort = dbport;
		userName = _userName;
		password = _password;
		host = _host.toLowerCase();
		
		typeofDb = 0;
		
		if(Utils.isNumeric(dbtype)) {
			
			typeofDb = Integer.parseInt(dbtype);
			
		}
		
		// Check is table limit parameter is have a value & is a numeric data type
		
		if (!tablelimit.isEmpty() && Utils.isNumeric(tablelimit)) {
			
			// We successfully find table limit parameter is have a value & is a numeric data type
			
			tableLimit = Integer.parseInt(tablelimit);								
			
		}
		
		boolean validateDb = validateCredential(typeofDb,_dbName,dbport,_userName,_password,_host);
		
		if(!validateDb && isError) {
			
			
			String _errorResults = gson.toJson(_errorResult);			
										
			return _errorResults;
		}
		
		else {
			
			boolean isConnected = connectRemoteDatabase();
			
			if(isConnected) {
				
				ResponseSuccess databaseMetaData = getDatabaseMetaData();
				String sucessResults = gson.toJson(databaseMetaData); 
				
				return String.valueOf(sucessResults);
				
				
			}
			
			else {
				
				//***ConnectionErrorResponse connectionError = new ConnectionErrorResponse();
				
				_errorResult.setIs_error(true);
				_errorResult.setError_msg(new ConnectionErrorResponse().getError_msg());
				
				String _errorResults = gson.toJson(_errorResult);	
				
				return _errorResults;
				
			}
			
			
		}
	}
	
	

	protected boolean validateCredential(Integer _typeOfDb,String _dbName, String _dbPort, String _userName, String _password, String _host) {
			
		
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
		
		
		// Getting any error of validPort
		else if(!(_typeOfDb >= 1 && _typeOfDb <= 2) && !isError) {								
			_errorResult.setIs_error(true);
			_errorResult.setError_msg(constants.INVALID_DATASOURCE);
			isError = true;
			error_point = 3;		
			
		}		
		
		
		else {			
			_errorResult.setIs_error(false);
			_errorResult.setError_msg("No erros are gretings");
			isError = false;
			error_point = 0;		
			
		}
		System.out.print("Values");
		System.out.print(String.valueOf(_typeOfDb));
				
		return isError ? false : true;
		
	}
	
	
	protected boolean connectRemoteDatabase() {
		
		boolean result = false;
		
		
		// connection type Mysql
		if (typeofDb == 1) {
			
			MysqlCon con = new MysqlCon();
			sqlConnection = con.makeconnection(dbName,dbPort,userName,password,host);
			
						
			if (sqlConnection != null) {
				
				try {
					metadata = sqlConnection.getMetaData();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				result = true;
			}
				
			
		}
		
		else if (typeofDb == 2) {
					
			OracleCon con = new OracleCon();
			sqlConnection = con.makeconnection(dbName,dbPort,userName,password,host);
			
						
			if (sqlConnection != null) {
				
				try {
					metadata = sqlConnection.getMetaData();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				result = true;
			}
				
			
		}
	
		
		return result;
		
	}
	
	
	/**
	 * Prints in the console the general metadata.
	 * 
	 * @throws SQLException
	 */
	protected ResponseSuccess getDatabaseMetaData()  {
		
		ResponseSuccess respone_success = new ResponseSuccess();
		
		try {
			respone_success.setIs_error(false);
			respone_success.setIs_result(true);
			
			
			
			Database database = new Database();
			String url = metadata.getURL();
			database.setDatabase_name(dbName);
			database.setDatabase_version(metadata.getDatabaseProductVersion());
			database.setDatabase_jdbc_version(metadata.getDriverVersion());
			database.setProduct_name(metadata.getDatabaseProductName());
			database.setDriver_version(metadata.getDriverVersion());
			
			// Run the getDatabaseTableMetaData
			
			getDatabaseTableMetaData2();
			
			database.setTables(table_list);
			database.setTotal_table(tableAvailable);
			database.setDisplay_table(tableCount);
			
			respone_success.setDatabase_details(database);
				
			
		}
		catch (SQLException e) {
        	
			respone_success.setIs_error(true);
			respone_success.setIs_result(false);
			
        }
		
		return respone_success;
	}

	/**
	 * 
	 * @return null
	 * @throws SQLException
	 */
	
	protected void getDatabaseTableMetaData2()
    {		
		table_list.clear();
		tableCount = 0;
		
		ResultSet rs = null;
		
		String sql = "";
		
		
        try {  
        	        	
        	List<String> tableNames = new ArrayList<String>();
        	
        	if(typeofDb == 1) {
        		
        		sql = "SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = ?";
        		
        		if (tableLimit != -1) {
        			
        			sql += " LIMIT ?";
        			
        		}
        	}

        	else if(typeofDb == 2) {
        		
        		sql = "SELECT OBJECT_NAME FROM USER_OBJECTS WHERE OBJECT_TYPE = ?";
        		
        		if (tableLimit != -1) {
        			
        			sql += " AND ROWNUM <= ?";
        			
        		}
        		
        	}
        	
			PreparedStatement preparedStatement = sqlConnection.prepareStatement(sql);
			preparedStatement.setString(1,  dbName);	
			if (tableLimit != -1) {
				preparedStatement.setInt(2, tableLimit);	
			}
			
			rs = preparedStatement.executeQuery();         
            while (rs.next()) {
            	 
            	System.out.print(rs.getString(1)); 
            	Tables table = new Tables();
	        	table.setTable_name(rs.getString("TABLE_NAME"));
	        	table.setTable_coulmns(getColumnsMetadata(rs.getString("TABLE_NAME")));
	        	table_list.add(table);
	        	tableCount++;
	        	tableAvailable++;

            	
            }
        } 
        
        
            catch (SQLException e) {
            	e.printStackTrace();
            }
        
        finally {
	        try { rs.close(); } catch (Exception ignore) { }
	    }
    }
	
	
	/**
	 * 
	 * @return null
	 * @throws SQLException
	 */
	
	protected void getDatabaseTableMetaData()
    {		
		table_list.clear();
		tableCount = 0;
		
		ResultSet rs = null;
		
        try {        	
             String[] types = {"TABLE"};
             rs = metadata.getTables(null, null, "%", types);
             
             System.out.print("Demo");
            
             while (rs.next()) {
            	
            	 tableAvailable++;
            	 
            	// If we don't get any table limit 
            	 
            	if (tableLimit == -1) { 
            	
		        	Tables table = new Tables();
		        	table.setTable_name(rs.getString("TABLE_NAME"));
		        	table.setTable_coulmns(getColumnsMetadata(rs.getString("TABLE_NAME")));
		        	table_list.add(table);
            	
            	}
            	
            	// If we gate any table name
            	
            	else {
            		
            		if (tableCount <= tableLimit) {
            			
            			Tables table = new Tables();
    		        	table.setTable_name(rs.getString("TABLE_NAME"));
    		        	table.setTable_coulmns(getColumnsMetadata(rs.getString("TABLE_NAME")));
    		        	table_list.add(table);
    		        	
    		        	tableCount++;
            			
            		}
            		
            		else {
            			
            			break;
            			
            		}
            		
            	}
            	
            	
            }
        } 
        
        
            catch (SQLException e) {
            	e.printStackTrace();
            }
        
        finally {
	        try { rs.close(); } catch (Exception ignore) { }
	    }
    }
	
	/**
	 * Prints in the console the columns metadata, based in the Arraylist of
	 * tables passed as parameter.
	 * 
	 * @param table_name
	 * @throws SQLException
	 * @return Arraylist with the columns
	 */
	
	public static List<ColumnSchema> getColumnsMetadata(String actualTable) {
		
		ResultSet rs = null;
		
		List<ColumnSchema> column_list = new ArrayList<>();
		
		List<String> column_schema = new ArrayList<>();
		
		
		try {
			
			rs = metadata.getColumns(null, null, actualTable, null);
			
			ResultSetMetaData rsmd = rs.getMetaData();
			
			int columnCount = rsmd.getColumnCount();
			
			for (int i = 1; i <= columnCount; i++ ) {
				
				  String name = rsmd.getColumnName(i);
				  
				  column_schema.add(name);
				  
				  System.out.print(name + "\n");
				  // Do stuff with name
			}
			
			while (rs.next()) {
				
				ColumnSchema columns_schema = new ColumnSchema();
				HashMap<String, String> newMap = new HashMap<String, String>();
								
				for (String schema_name : column_schema) {
					
					newMap.put(schema_name.toLowerCase(), rs.getString(schema_name));		
							
				}
				
				columns_schema.setCloumn_schema(newMap);
				
				column_list.add(columns_schema);
				
			}
		
		}
		
		catch (SQLException e) {
         	e.printStackTrace();
         }
		
		finally {
	        try { rs.close(); } catch (Exception ignore) { }
	    }
		
		
		return column_list;
		

	}
	
	
	
}
