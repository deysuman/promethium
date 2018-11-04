package com.promethium.models;

import java.util.ArrayList;
import java.util.List;

public class Tables {
	
	public String table_name = "";
	
	public List<ColumnSchema> table_coulmns = new ArrayList<>();

	public String getTable_name() {
		return table_name;
	}

	public void setTable_name(String table_name) {
		this.table_name = table_name;
	}

	public List<ColumnSchema> getTable_coulmns() {
		return table_coulmns;
	}

	public void setTable_coulmns(List<ColumnSchema> table_coulmns) {
		this.table_coulmns = table_coulmns;
	}

	
	
	
}
