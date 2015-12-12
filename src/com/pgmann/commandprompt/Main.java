package com.pgmann.commandprompt;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class Main {
	boolean running=true;
	Scanner scanner;
	JFrame frame;
	JTextArea log;
	JTextField input;
	JScrollPane scroll;
	static Main ref;
	int prevMax;
	Font normal = new Font("Consolas", Font.PLAIN, 16);
	String workingDir = System.getProperty("user.dir");
	
	public static void main(String[] args) {
		ref=new Main();
	}

	Main() {
		
		frame = new JFrame("Command Prompt");
		frame.getContentPane().setLayout(null);
		frame.setMinimumSize(new Dimension(700, 700));
		frame.setSize(new Dimension(700, 700));
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setBackground(Color.BLACK);
		frame.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("cmd.png")));
		// Add a window listener for close button
		frame.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		log = new JTextArea("Command Prompt [Version 1.0]\n\u00A9 2015 pgmann. All rights reserved.\n\n"+workingDir+">");
		log.setEditable(false);
		log.setBackground(Color.BLACK);
		log.setForeground(Color.WHITE);
		log.setFont(normal);
		log.setLineWrap(true);
		
		scroll = new JScrollPane(log);
	    scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		scroll.setLocation(10, 10);
		scroll.setSize(new Dimension(660, 640));
		scroll.setBorder(null);
		scroll.setCursor(new Cursor(Cursor.TEXT_CURSOR));
		scroll.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
		    public void adjustmentValueChanged(AdjustmentEvent e) {
		    	if(e.getAdjustable().getMaximum() != prevMax) {
		    		e.getAdjustable().setValue(e.getAdjustable().getMaximum());
		    		prevMax=e.getAdjustable().getMaximum();
		    	}
		    }
		});
		
		input = new JTextField("");
		input.setLocation(10, 70/*620*/);
		input.setSize(660, 30);
		input.setForeground(Color.WHITE);
		input.setBackground(Color.BLACK);
		input.setFont(normal);
		input.setBorder(null);
		input.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String command = input.getText();
				if(command.length() != 0) {
					if (command.equalsIgnoreCase("exit")) {
						System.exit(0);
					} else if (command.length() >= 5 && command.substring(0,5).equalsIgnoreCase("title")) {
						frame.setTitle(command.substring(5));
					} else {
						// Run the command
						new RunCommand(command, ref).start();
						System.out.println(command);
					}
					input.setText("");
					updateDisplay();
				}
			}
			
		});
		frame.getContentPane().add(input);
		frame.getContentPane().add(scroll);
		frame.pack();
		frame.setVisible(true);

		updateDisplay();
	}
	void updateDisplay() {
		
		// FontMetrics: work out the position for the text input field
		FontMetrics fm = frame.getGraphics().getFontMetrics(normal);
		int width = fm.stringWidth(workingDir+">");
		int line = log.getLineCount();
		if(line>4) line=44;
		
		// Set the position
		input.setLocation(10+width, 10+line*14/*620*/);
		input.setSize(700-20-width-50, 30);
		
		// Focus input field
		input.requestFocusInWindow();
		input.requestFocus();
	}
}
