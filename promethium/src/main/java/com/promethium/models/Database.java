package com.promethium.models;

import java.util.ArrayList;
import java.util.List;

public class Database {
	
	public String database_name = "";
	
	public String database_version = "";
	
	public String database_jdbc_version = "";
	
	public String product_name = "";
	
	public String driver_version = "";
	
	public List<Tables> tables = new ArrayList<Tables>();

	public String getDatabase_name() {
		return database_name;
	}

	public void setDatabase_name(String database_name) {
		this.database_name = database_name;
	}

	public String getDatabase_version() {
		return database_version;
	}

	public void setDatabase_version(String database_version) {
		this.database_version = database_version;
	}

	public String getDatabase_jdbc_version() {
		return database_jdbc_version;
	}

	public void setDatabase_jdbc_version(String database_jdbc_version) {
		this.database_jdbc_version = database_jdbc_version;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getDriver_version() {
		return driver_version;
	}

	public void setDriver_version(String driver_version) {
		this.driver_version = driver_version;
	}

	public List<Tables> getTables() {
		return tables;
	}

	public void setTables(List<Tables> tables) {
		this.tables = tables;
	}

	

}
