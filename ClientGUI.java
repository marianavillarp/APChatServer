package test_client;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import java.awt.Label;
import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.awt.SystemColor;
import javax.swing.DropMode;
import java.awt.Panel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

public class ClientGUI extends JFrame {

	Scanner in;
    PrintWriter out;
    
	private JPanel contentPane;
	private JTextField textField;
	private JTextField portText;
	private JTextField IpText;
	private JTextArea inputMsg;
	JTextArea messageArea;
	Client c;

	/**
	 * Launch the application.
	 */
//	public static void main(String[] args) {
//		EventQueue.invokeLater(new Runnable() {
//			public void run() {
//				try {
//					ClientGUI frame = new ClientGUI(c);
//					frame.setVisible(true);
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		});
//	}

	/**
	 * Create the frame.
	 */
	public ClientGUI(Client c) {
		
		this.c = c;
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 903, 575);
		contentPane = new JPanel();
		contentPane.setBackground(Color.BLACK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JTextField userText = new JTextField();
		userText.setEditable(false);
		userText.setFont(new Font("Tahoma", Font.PLAIN, 14));
		userText.setText("maria");
		userText.setBounds(139, 216, 129, 22);
		contentPane.add(userText);
		
		portText = new JTextField();
		portText.setEditable(false);
		portText.setFont(new Font("Tahoma", Font.PLAIN, 14));
		portText.setText("7000");
		portText.setBounds(139, 173, 129, 19);
		contentPane.add(portText);
		portText.setColumns(10);
		
		IpText = new JTextField();
		IpText.setFont(new Font("Tahoma", Font.PLAIN, 15));
		IpText.setEditable(false);
		IpText.setText("127.0.0.1");
		IpText.setBounds(139, 270, 129, 19);
		contentPane.add(IpText);
		IpText.setColumns(10);
		
		
		JLabel lblNewLabel = new JLabel("Port");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("Century Gothic", Font.BOLD, 17));
		lblNewLabel.setBounds(28, 163, 83, 36);
		contentPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("ID");
		lblNewLabel_1.setForeground(Color.WHITE);
		lblNewLabel_1.setFont(new Font("Century Gothic", Font.BOLD, 17));
		lblNewLabel_1.setBounds(28, 208, 83, 36);
		contentPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("IP");
		lblNewLabel_2.setForeground(Color.WHITE);
		lblNewLabel_2.setFont(new Font("Century Gothic", Font.BOLD, 17));
		lblNewLabel_2.setBounds(28, 258, 83, 36);
		contentPane.add(lblNewLabel_2);
		
		inputMsg = new JTextArea();
		inputMsg.setBounds(293, 454, 355, 22);
		contentPane.add(inputMsg);
		inputMsg.setColumns(10);
		
		JButton btnSendMsg = new JButton("Send");
		btnSendMsg.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnSendMsg.setBounds(671, 451, 74, 28);
		btnSendMsg.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				sendMsg();
			}
		});
		contentPane.add(btnSendMsg);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(291, 44, 378, 378);
		contentPane.add(scrollPane);
		
		messageArea = new JTextArea();
		messageArea.setForeground(Color.WHITE);
		scrollPane.setViewportView(messageArea);
		messageArea.setBackground(Color.DARK_GRAY);
		messageArea.setEditable(false);
		
		JButton btnDisconnect = new JButton("Disconnect");
		btnDisconnect.setFont(new Font("Tahoma", Font.PLAIN, 15));
		btnDisconnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		btnDisconnect.setBounds(67, 30, 136, 22);
		contentPane.add(btnDisconnect);
		
		JTextArea txtrOnlineUsers = new JTextArea();
		txtrOnlineUsers.setFont(new Font("Monospaced", Font.BOLD, 20));
		txtrOnlineUsers.setText("online users");
		txtrOnlineUsers.setForeground(Color.WHITE);
		txtrOnlineUsers.setBackground(Color.BLACK);
		txtrOnlineUsers.setBounds(704, 45, 162, 378);
		contentPane.add(txtrOnlineUsers);
		
	}
	
//	public void submitData(String user, int port) {
//		c = new Client(user,port);
//		c.contactServer(messageArea);
//	}
	
	public void sendMsg() {
		c.sendMessage(inputMsg);
	}
}
