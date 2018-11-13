package com.promethium.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.promethium.models.ConnectionErrorResponse;
import com.promethium.models.ResponseSuccess;
import java.io.BufferedReader;
import java.io.InputStreamReader;

@RestController
public class CommandExecutor {
	
	@SuppressWarnings("unchecked")
	@RequestMapping(path = "/api/executecmd", method={RequestMethod.POST})
	@ResponseStatus(value=HttpStatus.OK)
	public String execute_cmd() {
		
		String cmd = execute();
	
		return cmd;
			
	}
	
	
	protected String execute() {
		

		String domainName = "google.com";
		
		//in mac oxs
		String command = "ping -c 3 " + domainName;
		
		//in windows
		//String command = "ping -n 3 " + domainName;
		
		command = "java -cp \"Promethium-ingestion-0.0.1-SNAPSHOT.jar:/home/ubuntu/Promethium2/lib/*\" com.cbnits.drivers.RDBMSIngestionDriver /home/ubuntu/Promethium2/config/TableConnection.csv /home/ubuntu/Promethium2/config/Queryfileca.txt http://52.205.181.28:9200";
		
		String output = executeCommand(command);
		
		return output;
		
	}
	
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
