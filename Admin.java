package test_client;

public class Admin extends Client {
    
	Client client_admin;
	Server current_server;
	
	public Admin(String name, int port, Server server) {
		super(name, port);
		this.current_server = server;
		// TODO Auto-generated constructor stub
	}


	public void getOnlineUsers() {
		
	}
	
	public void removeUser() {
		// set client instance to null
	}
	
	public static void main(String[] args) {
		
	}
	
	
}