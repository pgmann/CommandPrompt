package com.pgmann.commandprompt;

import java.io.*;

public class RunCommand extends Thread {
	String cmd;
	Main ref;
	PrintWriter writer;
	
	public RunCommand(String cmd, Main ref) {
		this.cmd=cmd;
		this.ref=ref;
	}
	
	public void run() {

		String s = null;
		
		try {
			// log cmd
			ref.log.setText(ref.log.getText()+cmd+"\n");
			ref.updateDisplay();
			
			// run the command
			Process p = Runtime.getRuntime().exec(cmd);

			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			this.writer = new PrintWriter(p.getOutputStream());

			while (p.isAlive()) {
				// read the output from the command
				while ((s = stdInput.readLine()) != null) {
					ref.log.setText(ref.log.getText()+s+"\n");
					ref.updateDisplay();
				}

				// read any errors from the attempted command
				while ((s = stdError.readLine()) != null) {
					ref.log.setText(ref.log.getText()+s+"\n");
					ref.updateDisplay();
				}
			}
		}
		catch (IOException e) {
			ref.log.setText(ref.log.getText()+"Error: "+e.getMessage()+"\n");
			ref.updateDisplay();
		}
		
		ref.log.setText(ref.log.getText()+ref.workingDir+">");
		ref.updateDisplay();
	}
}
