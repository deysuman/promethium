package com.promethium.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.promethium.client.ElasticClient;
import com.promethium.models.ConnectionErrorResponse;
import com.promethium.models.ResponseSuccess;

import java.util.List;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.xcontent.XContentType;

@RestController
public class ElasticSearch {
	
	public static Client client;
	
	
	// Prepare the client
	protected void getClient() {
		
		client = new ElasticClient().client();
		
	}
	
	
	public void givenJsonString_whenJavaObject_thenIndexDocument() {
	    String jsonObject = "{\"age\":10,\"dateOfBirth\":1471466076564,"
	      +"\"fullName\":\"John Doe\"}";
	    IndexResponse response = client.prepareIndex("people", "Doe")
	      .setSource(jsonObject, XContentType.JSON).get();
	       
	    String id = response.getId();
	    String index = response.getIndex();
	    String type = response.getType();
	    long version = response.getVersion();
	    
	    System.out.print(response.getResult());
//	        
//	    assertEquals(Result.CREATED, response.getResult());
//	    assertEquals(0, version);
//	    assertEquals("people", index);
//	    assertEquals("Doe", type);
	}

	
	public void getJson() {
		
//		SearchResponse response = client.prepareSearch().execute().actionGet();
//		List<SearchHit> searchHits = Arrays.asList(response.getHits().getHits());
//		List<Person> results = new ArrayList<Person>();
	}
	
	@SuppressWarnings("unchecked")
	@RequestMapping(path = "/api/elasticseach", method={RequestMethod.POST})
	@ResponseStatus(value=HttpStatus.OK)
	public String requestElastic(@RequestParam("keyword") String _keyword) {
		
		
		// first make the client
		
		getClient();
		
		//givenJsonString_whenJavaObject_thenIndexDocument();
		
		return "deatails";
	
	}
	
}
