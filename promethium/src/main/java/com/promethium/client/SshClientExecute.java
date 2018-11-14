package com.promethium.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.common.io.CharStreams;
import com.jcraft.jsch.*;

public class SshClientExecute {
	
	
	/**
	 * Executes a given command. It opens exec channel for the command and closes
	 * the channel when it's done.
	 *
	 * @param session ssh connection to a remote server
	 * @param command command to execute
	 * @return command output string if the command succeeds, or null
	 */
	public static String executeCommand(Session session, String command) {
	    if (session == null || !session.isConnected()) {
	    	
	    	
	    	System.out.print("Something problem here");
	    	
	        return null;
	    }

	    //log.trace("Execute command {} to {}", command, session.getHost());

	    try {
	    	
	    	
	        Channel channel = session.openChannel("exec");
	        ((ChannelExec) channel).setCommand(command);
	        channel.setInputStream(null);
	        InputStream output = channel.getInputStream();

	        channel.connect();
	        String result = CharStreams.toString(new InputStreamReader(output));
	        channel.disconnect();

	        //log.trace("Result of command {} on {}: {}", command, session.getHost(), result);

	        return result;
	        
	        
	    } catch (JSchException | IOException e) {
	        //log.error("Failed to execute command {} on {} due to {}", command, session.getHost(), e.toString());
	        return null;
	    }
	}

}
