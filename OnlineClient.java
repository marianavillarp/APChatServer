package COMP1549_G4;

import java.io.Serializable;

class OnlineClient implements Serializable{
	
	/**
	 * Serializable class to send online user lists through output stream
	 */
	
	private String name;
		
	public OnlineClient(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

}
