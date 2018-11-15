package com.promethium.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.promethium.client.SshClient;
import com.promethium.client.SshClientExecute;
import com.promethium.helper.Config;
import com.promethium.helper.Constants;
import com.promethium.models.ConnectionErrorResponse;
import com.promethium.models.ResponseError;
import com.promethium.models.ResponseSuccess;
import com.promethium.models.SuccessResult;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@RestController
public class CommandExecutor {
	
	public Session session = null;
	
	private ResponseError _errorResult = new ResponseError();
	
	private SuccessResult _successResult = new SuccessResult();
	
	private Gson gson = new Gson();

	public static String command = "";
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(path = "/api/executecmd", method={RequestMethod.POST})
	@ResponseStatus(value=HttpStatus.OK)
	public String execute_cmd(@RequestParam("cmd") String cmd) {
		
		
		if (cmd.isEmpty() || cmd.isBlank()) {
			
			_errorResult.setIs_error(true);
			_errorResult.setError_msg(Constants.EMPTY_CMD);
			
			String json_result = gson.toJson(_errorResult);
			
			return json_result;
			
		}
		
		else {
		
			// First connect the ssh
			
			connectSsh();
			
			command = cmd;
			
			
			// Execute the cmd
			
			String execute_cmd = executeClient();
			
			if(!new ConnectionErrorResponse().getError_msg().isBlank() || !new ConnectionErrorResponse().getError_msg().isEmpty()) {
				
				_successResult.setIs_error(true);
				_successResult.setMassage(new ConnectionErrorResponse().getError_msg());
				
				
			}
			
			else {
				
				_successResult.setIs_error(true);
				_successResult.setMassage(execute_cmd.toString());
				
				
			}
			
			String json_result = gson.toJson(_successResult);
			
			
			
			// Getting result
		
			return json_result;
			
		}
			
	}
	
	
	protected void connectSsh() {
		
		try {
			
			session = new SshClient().getSession();
			
			
		} catch (JSchException e) {
			
			ConnectionErrorResponse error_response = new ConnectionErrorResponse();	
			error_response.setError_msg(e.toString());
			
			System.out.print(e.toString());
			
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	protected String executeClient() {
		
		
		String runCmd = new SshClientExecute().executeCommand(session,command.toString());
		
		return runCmd;
		
	}
	
	
//	protected String execute() {
//		
//
//		String domainName = "google.com";
//		
//		//in mac oxs
//		String command = "ping -c 3 " + domainName;
//		
//		//in windows
//		//String command = "ping -n 3 " + domainName;
//		
//		command = "java -cp \"Promethium-ingestion-0.0.1-SNAPSHOT.jar:/home/ubuntu/Promethium2/lib/*\" com.cbnits.drivers.RDBMSIngestionDriver /home/ubuntu/Promethium2/config/TableConnection.csv /home/ubuntu/Promethium2/config/Queryfileca.txt http://52.205.181.28:9200";
//		
//		String output = executeCommand(command);
//		
//		return output;
//		
//	}
	
	private String executeCommand(String command) {

		StringBuffer output = new StringBuffer();

		Process p;
		try {
			p = Runtime.getRuntime().exec(command);
			p.waitFor();
			BufferedReader reader = 
                            new BufferedReader(new InputStreamReader(p.getInputStream()));

                        String line = "";			
			while ((line = reader.readLine())!= null) {
				output.append(line + "\n");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return output.toString();

	}
	
}
