package com.promethium.models;

public class ResponseError {

	private  boolean is_error = false;
	
	private  String error_msg = null;

	public boolean isIs_error() {
		return is_error;
	}


	public void setIs_error(boolean is_error) {
		this.is_error = is_error;
	}


	public String getError_msg() {
		return error_msg;
	}


	public void setError_msg(String error_msg) {
		this.error_msg = error_msg;
	}

	public ResponseError(){

    }

	
	public ResponseError(boolean is_error, String error_msg) {
        this.is_error = is_error;
        this.error_msg = error_msg;        
    }
	
}
