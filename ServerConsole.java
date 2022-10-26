// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import common.*;
import server.ChatServer;
import server.UserServer;

/**
 * This class constructs the UI for a chat client.  It implements the
 * chat interface in order to activate the display() method.
 * Warning: Some of the code here is cloned in ClientConsole 
 *
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Dr Timothy C. Lethbridge  
 * @author Dr Robert Lagani&egrave;re
 * @version September 2020
 */
public class ServerConsole implements ChatIF {
	
	//Class variables *************************************************
	  
	/**
	 * The default port to connect on.
	 */
	final public static int DEFAULT_PORT = 5555;
	  
	//UserServer server;
	ChatServer server;
	public ServerConsole (int port) {
		server = new ChatServer(port);
		try {
			server.listen();
		} catch(IOException e) {
			System.out.println("ERREUR. Impossible d'ecouter des clients");
		}
	}
	
	public void accept() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String msg;
			while ((msg = br.readLine()) != null) {
				server.handleMessageFromServerConsole(msg);
				//server.handleMessageFromServerUI(msg);
				this.display(msg);
				
			}
		} catch (Exception e) {
			System.out.println("Erreur imprévue en lisant la console.");
		}
	}

	@Override
	public void display(String message) {
		// TODO Auto-generated method stub
		if (message.startsWith("#")) {
			return;
		}
		System.out.println("SERVER MSG> " + message);
		//System.out.println(message);
		
	}
	
	public static void main(String[] args) {
		int port = 0;
		try {
			port = Integer.parseInt(args[0]);
		} catch (Throwable t) {
			port = DEFAULT_PORT;
		}
		
		ServerConsole server_console = new ServerConsole(port);
		server_console.accept();
	}
}
