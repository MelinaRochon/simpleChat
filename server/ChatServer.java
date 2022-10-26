package server;
//This file contains material supporting section 3.7 of the textbook:
//"Object Oriented Software Engineering" and is issued under the open-source
//license found at www.lloseng.com 


import java.io.IOException;

import common.ChatIF;
import ocsf.server.*;

/**
* This class overrides some of the methods in the abstract 
* superclass in order to give more functionality to the server.
*
* @author Dr Timothy C. Lethbridge
* @author Dr Robert Lagani&egrave;re
* @author Fran&ccedil;ois B&eacute;langer
* @author Paul Holden
* @version July 2000
*/
public class ChatServer extends AbstractServer {
	//Class variables *************************************************

	/**
	 * The default port to listen on.
	 */
	final public static int DEFAULT_PORT = 5555;
	
	ChatIF serverUI;
	//Constructors ****************************************************

	/**
	 * Constructs an instance of the echo server.
	 *
	 * @param port The port number to connect on.
	 */
	public ChatServer(int port) {
		super(port);
	}


	//Instance methods ************************************************

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg The message received from the client.
	 * @param client The connection from which the message originated.
	 */
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		String message = msg.toString();
		System.out.println("Message received: " + msg + " from " + client);
		this.sendToAllClients("> " + msg);
		
	}
 
	/**
	 * This method overrides the one in the superclass.  Called
	 * when the server starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println
		("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass.  Called
	 * when the server stops listening for connections.
	 */
	protected void serverStopped() {
		System.out.println ("Server has stopped listening for connections.");
	}

	/**
	 * Méthode qui est appelé chaque fois qu'un nouveau client se connecte au serveur 
	 */
	@Override
	public void clientConnected(ConnectionToClient client) {
		//System.out.println("Un nouveau client, " + client + ", vient de se connecter au serveur");
		this.sendToAllClients("Un nouveau client, " + client + ", vient de se connecter au serveur");
	}
	
	/**
	 * Méthode qui est appelée quand un client se déconnecte du serveur. 
	 */
	@Override
	public synchronized void clientDisconnected(ConnectionToClient client) {
			//System.out.println("Un client, " + client + ", vient de se déconnecter du serveur");
		this.sendToAllClients(client + " ses déconnecté.");
			
	}
	
	/**
	 * Méthode qui est appelée pour déconnecter un client du serveur
	 */
	@Override
	public void clientException(ConnectionToClient client, Throwable exception) {
		clientDisconnected(client); // déconnexion
		
	}
	

	/**
	 * This method handles all data coming from the UI      
	 * Cette méthode implémente des commandes spécifiques entrées par le client, commençant par 
	 * le symbole '#'. On regarde cas par cas la commande. Elles peuvent soit êtres les suivantes: 
	 * #quit,#logoff,#sethost, #setport, #login, #gethost ou #getport 
	 * @param command
	 */
	public void handleMessageFromServerConsole(String command) {
		if (command.startsWith("#")) {
			String[] argument = command.split(" ");
			String message = argument[0];
			switch (message) {
				case "#quit" :
					// provoque la fermeture du serveur
					try {
						this.close();
						System.out.println("quit");
					}catch (IOException e) {
						System.exit(1);
					}
					System.exit(1);
					break;
				case "#stop" :
					// provoque le serveur de cesser d'écouter aux nouveaux clients
					this.stopListening();
				case "#close" :
					// provoque le serveur d'arrêter d'écouter aux clients et de 
					// déconnecter tout les clients existants
					try {
						this.close();
					} catch (IOException e) {
						System.out.println("Erreur lors de le l'arrêt d'écoute du serveur");
					}
					break;
				case "#setport" :
					// appelle méthode setPort sur le serveur
					// commande seulement autorisée si le serveur est fermé
					if (!this.isListening()) {
						super.setPort(Integer.parseInt(argument[1]));
						System.out.println("Port initié à : " + Integer.parseInt(argument[1]));
					} else {
						System.out.println("Impossible d'exécuter cette commande pour le moment car le serveur est connecté.");
					}
					break;
				case "#start" :
					// provoque serveur de commencer à écouter pour nouveaux clients
					// commande uniquement valable si le serveur est arrêté
					if (!this.isListening()) {
						try {
							this.listen();
						} catch (IOException e) {
							System.out.println("Erreur " + e);
						}
					} else {
						System.out.println("Impossible d'exécuter cette commande car le serveur est déjà partis");
					}
					break;
				case "#getport" :
					// affiche numéro de port actuel
					System.out.println("Le port actuel est : " + this.getPort());
					break;
			} 
		} else {
			this.sendToAllClients("SERVER MSG> " + command); // envoie les messages aux clients connectés
		}
	}
	
	
	//Class methods ***************************************************

	/**
	 * This method is responsible for the creation of 
	 * the server instance (there is no UI in this phase).
	 *
	 * @param args[0] The port number to listen on.  Defaults to 5555 
	 *          if no argument is entered.
	 */
	public static void main(String[] args) {
		int port = 0; //Port to listen on

		try {
			port = Integer.parseInt(args[0]); //Get port from command line
		} catch(Throwable t) {
			port = DEFAULT_PORT; //Set port to 5555
		}
	
		ChatServer sv = new ChatServer(port);
 
		try {
			sv.listen(); //Start listening for connections
		} catch (Exception ex){
			System.out.println("ERROR - Could not listen for clients!");
		}
	}
}
//End of EchoServer class
