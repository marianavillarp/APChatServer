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
import java.io.IOException;
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
import javax.swing.JOptionPane;

import java.awt.Canvas;
import java.awt.ScrollPane;

public class LoginGUI extends JFrame {

	Scanner in;
    PrintWriter out;
    
	private JPanel loginPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField portText;
	private JTextField IpText;
	Client c;
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
		
		Label label = new Label("\r\nWelcome!\r\n");
		label.setBounds(119, 29, 161, 47);
		label.setForeground(new Color(255, 255, 255));
		label.setFont(new Font("Tahoma", Font.BOLD, 33));
		loginPane.add(label);
		
		JTextField userText = new JTextField();
		userText.setText("mar");
		userText.setBackground(new Color(128, 128, 128));
		userText.setBounds(160, 122, 209, 30);
		loginPane.add(userText);
		
		portText = new JTextField();
		portText.setText("7000");
		portText.setBackground(new Color(128, 128, 128));
		portText.setBounds(160, 203, 209, 30);
		loginPane.add(portText);
		portText.setColumns(10);
		
		IpText = new JTextField();
		IpText.setText("127.0.0.1");
		IpText.setBackground(new Color(128, 128, 128));
		IpText.setBounds(160, 163, 209, 30);
		loginPane.add(IpText);
		IpText.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Enter User ID:");
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setFont(new Font("Century Gothic", Font.BOLD, 17));
		lblNewLabel.setBounds(10, 115, 140, 36);
		loginPane.add(lblNewLabel);
		
		JLabel lblNewLabel_1 = new JLabel("Enter IP Address:");
		lblNewLabel_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1.setFont(new Font("Century Gothic", Font.BOLD, 17));
		lblNewLabel_1.setBounds(10, 159, 155, 36);
		loginPane.add(lblNewLabel_1);
		
		JLabel lblNewLabel_2 = new JLabel("Enter Port ID:");
		lblNewLabel_2.setForeground(new Color(255, 255, 255));
		lblNewLabel_2.setFont(new Font("Century Gothic", Font.BOLD, 17));
		lblNewLabel_2.setBounds(10, 200, 136, 36);
		loginPane.add(lblNewLabel_2);
		
		JButton btnNewButton = new JButton("LOGIN");
		btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 18));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int port = Integer.parseInt(portText.getText());
				submitData(userText.getText(),port);
			}
		});
		btnNewButton.setBounds(119, 289, 155, 36);
		loginPane.add(btnNewButton);
		
		JButton exit_btn = new JButton("Exit");
		exit_btn.setFont(new Font("Tahoma", Font.PLAIN, 12));
		exit_btn.setBounds(154, 335, 85, 21);
		exit_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		loginPane.add(exit_btn);
		
	}
	
	public void submitData(String user, int port) {
		
		try{
			c = new Client(user,port);
			ClientGUI frame2 = new ClientGUI(c);
			c.contactServer(frame2.messageArea,frame2);
			frame2.setVisible(true);
			frame.setVisible(false);
			frame2.setTitle("Chat server [" + user + "]");
		}
        catch (IOException ioe) {
        	c = null;
        	JOptionPane.showMessageDialog(frame, "Server unavailable");
		}
	}
}
