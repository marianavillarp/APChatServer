package test_client;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextPane;
import javax.swing.JButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

public class ServerGUI extends JFrame {

	Scanner in;
    PrintWriter out;
	
	private JPanel contentPane;
	JTextArea messageChat;
	Server server;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerGUI frame = new ServerGUI();
					frame.setVisible(true);
					frame.setTitle("Server");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ServerGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 599, 378);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		Server server = new Server();
		
		JButton btnConnect = new JButton("Start");
		btnConnect.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnConnect.setBounds(127, 10, 97, 21);
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					server.startServer(messageChat);
				} catch (IOException e1) {
					System.out.println("Error server");
				}
			}
			
		});
		contentPane.add(btnConnect);
		
		JButton btnDisconnect = new JButton("Disconnect");
		btnDisconnect.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnDisconnect.setBounds(345, 10, 110, 21);
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					server.closeServer(messageChat);
				} catch (IOException e1) {
					System.out.println("Error end");
				}
			}
		});
		contentPane.add(btnDisconnect);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(54, 56, 457, 260);
		contentPane.add(scrollPane);
		
		messageChat = new JTextArea();
		messageChat.setForeground(Color.WHITE);
		messageChat.setBackground(Color.DARK_GRAY);
		scrollPane.setViewportView(messageChat);
		messageChat.setEditable(false);
	}
}

