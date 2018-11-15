package com.promethium.client;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.annotation.Resource;

import org.springframework.core.io.ClassPathResource;

import com.jcraft.jsch.*;
import com.promethium.helper.Config;
import com.promethium.models.ConnectionErrorResponse;

public class SshClient {
	
	public static JSch jsch = null;
		
	//public Channel channel = null;
	
	public String command = "whoami;hostname";
	
	public static Session session = null;
	
	public static String publicKey = null;

	
	protected void getServerPem() {
		
		final ClassPathResource resource = new ClassPathResource("cloudera_promethium.pem");
		
		try {
			
		     publicKey = String.valueOf(resource.getFile().toPath());
		     
		     
		     
		     
		} catch (IOException e) {
			
		     e.printStackTrace();
		     
		}
		
	}
	
	protected void configureSsh() 
	{
		
		// Using pem for connect ssh protocol
		
		try {
		
			jsch = new JSch();
			jsch.addIdentity(publicKey);
			
			System.out.print(publicKey);
			
			//jsch.setConfig("StrictHostKeyChecking", "no");
		
		}
		
		catch (JSchException e) {
			
			System.out.print("Jch login failed");
			
			System.out.print(e.toString());
			
			ConnectionErrorResponse error_response = new ConnectionErrorResponse();	
			error_response.setError_msg(e.toString());
			
			e.printStackTrace();
			
		}
		
		
	}
	
	protected void createSession()
	{
		
		//enter your own EC2 instance IP here
		
		try {
		
			session = jsch.getSession(Config.ssh_login_user, Config.ec2_ip, Config.sshPort);
			java.util.Properties config = new java.util.Properties();
	        config.put("StrictHostKeyChecking", "no");
	        session.setConfig(config);
			session.connect();
		
		}
		
		catch (JSchException e) {
			
			System.out.print("Ssh login failed");
			
			System.out.print(e.toString());
			
			e.printStackTrace();
			
			ConnectionErrorResponse error_response = new ConnectionErrorResponse();	
			error_response.setError_msg(e.toString());
			
		}
		
		
	}
	
	public Session getSession() throws JSchException 
	{
		
		SshClient sshClient = new SshClient();
		sshClient.getServerPem();
		sshClient.configureSsh();
		sshClient.createSession();
		
		return session;
		
	}
	
	
	
	
	
	

}
