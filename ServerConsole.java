// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;
import java.util.Scanner;

import common.*;
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

	final public static int DEFAULT_PORT = 5555;
	
	UserServer user;
	
	// Constructeur de la classe
	public ServerConsole(int port) {
		try {
			user = new UserServer(port);
			user.listen();
		} catch(IOException ex) {
			System.out.println("Error: Can't setup connection!"
	                + " Terminating client.");
			System.exit(1);
		} 
	}
	
	/**
	* Cette méthode attend pour des entrées de la console. Une fois 
	* reçus, ça l'envoie au "client's message handler"
	*/
	public void accept() {
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			String msg;
			while (true) {
				msg = br.readLine();
				user.handleMessageFromServerUI(msg);
			}
		} catch (Exception e) {
			System.out.println ("Une erreur inattendu ses produite en lisant la console.");
		}
	}
	
	@Override
	/**
	* Cette méthode fait un "override" de la méthode dans l'interface ChatIF.
	* Il montre un message sur l'écran. 
	* @param message Le string a être visualisé.
	*/
	public void display(String message) {
		// TODO Auto-generated method stub
		System.out.println(message);
	}

	/**
	 * Cette méthode est responsable pour la création du ServerConsole.
	 * 
	 * @param args[0] Le port à connecter.
	*/
	public static void main(String[] args) {
		ServerConsole server_console;
		try {
			// Entre le numéro de port dans la ligne de commande
			server_console = new ServerConsole(Integer.parseInt(args[0]));
		} catch (NumberFormatException exception) {
			System.out.println("Valeur entrée n'est pas bon. Utilise la valeur de port : " + DEFAULT_PORT);
			server_console = new ServerConsole(DEFAULT_PORT);
		} catch (ArrayIndexOutOfBoundsException exception) {
			server_console = new ServerConsole(DEFAULT_PORT);
		}
		server_console.accept();
	}
}
