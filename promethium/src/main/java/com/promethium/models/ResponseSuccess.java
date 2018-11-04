package com.promethium.models;

public class ResponseSuccess {
	
	public boolean is_error = false;
	
	public boolean is_result = false;
	
	public Database database_details = new Database();

	
	public boolean isIs_error() {
		return is_error;
	}


	public void setIs_error(boolean is_error) {
		this.is_error = is_error;
	}


	public boolean isIs_result() {
		return is_result;
	}


	public void setIs_result(boolean is_result) {
		this.is_result = is_result;
	}


	public Database getDatabase_details() {
		return database_details;
	}


	public void setDatabase_details(Database database_details) {
		this.database_details = database_details;
	}
	

}
