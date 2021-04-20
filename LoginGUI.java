package COMP1549_G4;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class LoginGUI extends JFrame {

	Scanner in;
    PrintWriter out;
    
	private JPanel loginPane;
	JTextField userText;
	private JTextField portText;
	private JTextField IpText;
	String user;
	public Client c;
	static LoginGUI frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					frame = new LoginGUI();
					frame.setVisible(true);
					frame.setTitle("Contact Server");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public LoginGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 436, 419);
		loginPane = new JPanel();
		loginPane.setBackground(new Color(0, 0, 0));
		loginPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(loginPane);
		loginPane.setLayout(null);
		
		Label welcomeLbl = new Label("\r\nWelcome!\r\n");
		welcomeLbl.setBounds(119, 29, 161, 47);
		welcomeLbl.setForeground(new Color(255, 255, 255));
		welcomeLbl.setFont(new Font("Tahoma", Font.BOLD, 33));
		loginPane.add(welcomeLbl);
		
		userText = new JTextField();
		userText.setBackground(new Color(128, 128, 128));
		userText.setBounds(160, 122, 209, 30);
		loginPane.add(userText);
		
		portText = new JTextField();
		portText.setBackground(new Color(128, 128, 128));
		portText.setBounds(160, 203, 209, 30);
		loginPane.add(portText);
		portText.setColumns(10);
		
		IpText = new JTextField();
		IpText.setBackground(new Color(128, 128, 128));
		IpText.setBounds(160, 163, 209, 30);
		loginPane.add(IpText);
		IpText.setColumns(10);
		
		JLabel idLbl = new JLabel("Enter User ID:");
		idLbl.setForeground(new Color(255, 255, 255));
		idLbl.setFont(new Font("Century Gothic", Font.BOLD, 17));
		idLbl.setBounds(10, 115, 140, 36);
		loginPane.add(idLbl);
		
		JLabel iPlbl = new JLabel("Enter IP Address:");
		iPlbl.setForeground(new Color(255, 255, 255));
		iPlbl.setFont(new Font("Century Gothic", Font.BOLD, 17));
		iPlbl.setBounds(10, 159, 155, 36);
		loginPane.add(iPlbl);
		
		JLabel portLbl = new JLabel("Enter Port ID:");
		portLbl.setForeground(new Color(255, 255, 255));
		portLbl.setFont(new Font("Century Gothic", Font.BOLD, 17));
		portLbl.setBounds(10, 200, 136, 36);
		loginPane.add(portLbl);
		
		JButton connectBtn = new JButton("CONNECT");
		connectBtn.setFont(new Font("Tahoma", Font.PLAIN, 18));
		user = userText.getText();
		connectBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (!userText.getText().equals("") && !portText.getText().equals("") && !IpText.getText().equals("")) {
					int port = Integer.parseInt(portText.getText());
					submitData(userText.getText(),IpText.getText(),port);
				}
				else {
					JOptionPane.showMessageDialog(null, "Please, enter valid user details", "Error", JOptionPane.ERROR_MESSAGE);
				}
				
			}
		});
		connectBtn.setBounds(119, 289, 155, 36);
		loginPane.add(connectBtn);
		
		JButton exit_btn = new JButton("Exit"); // Provides option for user to exit the login screen
		exit_btn.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exit_btn.setBounds(154, 335, 85, 21);
		exit_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		loginPane.add(exit_btn);
		
		JLabel lblNewLabel_3 = new JLabel("Server Port - 7000");
		lblNewLabel_3.setForeground(Color.WHITE);
		lblNewLabel_3.setFont(new Font("Century Gothic", Font.BOLD, 17));
		lblNewLabel_3.setBounds(182, 244, 165, 21);
		loginPane.add(lblNewLabel_3);
		
		
	}
	
	/*
	 * Send details to start connection
	 */
	public void submitData(String user, String ip, int port) {
		if (!ip.equals("127.0.0.1")) {
			JOptionPane.showMessageDialog(frame, "Wrong IP address");
		}
		else {
			try{
				c = new Client(user,ip,port);
				c.contactServer();
				c.clientFrame.setVisible(true);
				frame.setVisible(false);
				c.clientFrame.setTitle("Chat server [" + user + "]");
			}
	        catch (IOException ioe) {
	        	c = null;
	        	JOptionPane.showMessageDialog(frame, "Server unavailable");
			}
		}
	}
}
