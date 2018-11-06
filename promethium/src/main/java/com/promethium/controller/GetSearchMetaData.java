package com.promethium.controller;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.promethium.helper.Constants;
import com.promethium.helper.Utils;
import com.promethium.models.ConnectionErrorResponse;
import com.promethium.models.Database;
import com.promethium.models.ResponseError;
import com.promethium.models.ResponseSuccess;
import com.promethium.models.Tables;
import com.promethium.sql.HiveCon;
import com.promethium.sql.MysqlCon;
import com.promethium.sql.OracleCon;
import com.google.gson.Gson;

@RestController
public class GetSearchMetaData {
	
	private ResponseError _errorResult = new ResponseError();
	
	private Constants constants = new Constants();
	
	private Gson gson = new Gson();
	
	private static boolean isError = false;
	
	private String dbName,dbPort,userName,password,host,keyword = "";	
	
	private static Connection sqlConnection = null;
	
	private static DatabaseMetaData metadata = null;
	
	private static List<Tables> table_list = new ArrayList<>();

	@SuppressWarnings("unchecked")
	@RequestMapping(path = "/api/searchtable", method={RequestMethod.POST})
	@ResponseStatus(value=HttpStatus.OK)
	public String SearchDb(@RequestParam("keyword") String _keyword) {
		
		dbName = "oracle_system";
		dbPort = "10000";
		userName = "hive";
		password = "Host@2018";
		host = "54.234.180.243";
		keyword = _keyword;
		
		if (!_keyword.isEmpty()) {
			
			
			isError = false;
						
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
			
			//return "Yes it's working";
			
		}
		
		else {
			
			isError = true;
			
			_errorResult.setIs_error(true);
			_errorResult.setError_msg(constants.EMPTY_SEARCH_KEYWORD);	
			
			String _errorResults = gson.toJson(_errorResult);
			return _errorResults;
			
		}
		
		
	
	}
	
	protected boolean connectRemoteDatabase() {
			
		boolean result = false;
		
		
		// connection type Hive
		if (!isError) {
			
			HiveCon con = new HiveCon();
			sqlConnection = con.makeconnection(dbName,dbPort,userName,password,host);
			
						
			if (sqlConnection != null) {
				
				try {
					metadata = sqlConnection.getMetaData();
					System.out.print("It's working..");
					
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
		
		ResultSet rs = null;
		
		try {
			
			String[] types = {"TABLE"};
            rs = metadata.getTables(null, null, "%", types);
           
			
			respone_success.setIs_error(false);
			respone_success.setIs_result(true);
			
//			Statement st = sqlConnection.createStatement();
//			//String sql = "SELECT * FROM drawings WHERE name LIKE ?";
//			
//			String[] types = {"TABLE"};
//            rs = metadata.getTables(null, null, "%", types);
//			
//			String sql ="SHOW TABLES"; 
//					
////			
//			//String sql = "CREATE TABLE IF NOT EXISTS employee ( eid int, name String)";
//			//String sql = "SELECT * FROM employee";
//			PreparedStatement preparedStatement = sqlConnection.prepareStatement(sql);
//			//preparedStatement.setString(1, "%" + keyword);
//			//preparedStatement.setString(1, "%" + dbName);
//			
//			
//			
//			ResultSet rs = preparedStatement.executeQuery();
			
			
			
			System.out.print("Here");
			
            while (rs.next()) {
            	
            	Tables table = new Tables();
	        	table.setTable_name(rs.getString("TABLE_NAME"));
	        	//table.setTable_coulmns(getColumnsMetadata(rs.getString("TABLE_NAME")));
	        	table_list.add(table);
                
            	System.out.println("table name :" + rs.getString(1));
                
                
            }
			
			Database database = new Database();
			
			/*String url = metadata.getURL();
			
			database.setDatabase_name(url.substring(url.lastIndexOf("/") + 1));
			database.setDatabase_version(metadata.getDatabaseProductVersion());
			database.setDatabase_jdbc_version(metadata.getDriverVersion());
			database.setProduct_name(metadata.getDatabaseProductName());
			database.setDriver_version(metadata.getDriverVersion());
			*/
			// Run the getDatabaseTableMetaData
			
		//	getDatabaseTableMetaData();
			
		//	database.setTables(table_list);
		//	database.setTotal_table(tableAvailable);
		//	database.setDisplay_table(tableCount);
			
			respone_success.setDatabase_details(database);
				
			
		}
		catch (SQLException e) {
        	
			respone_success.setIs_error(true);
			respone_success.setIs_result(false);
			System.out.print(String.valueOf(e));
			
			
        }
		
		return respone_success;
	}
	

	
}


