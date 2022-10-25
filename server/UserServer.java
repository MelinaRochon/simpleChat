package server;

import java.io.*;

import client.ChatClient;
import ocsf.server.*;

public class UserServer extends AbstractServer {
	
	ChatClient user;
	// Constructeur
	public UserServer(int port) {
		super(port);
	}
	
	// Méthodes d'instances	
	@Override
	/**
	 * Cette méthode envoie un message au serveur que le message à été reçus
	 */
	protected void handleMessageFromClient(Object msg, ConnectionToClient client) {
		// TODO Auto-generated method stub
		System.out.println("Message received: " + msg + " from " + client);
		this.sendToAllClients(msg);
	}
	
	/**
	 * Cette méthode regarde si ce que l'utilisateur à écris commence avec un '#',
	 * si oui, alors on effectue le code switch(controle), sinon on indique que le 
	 * message est un SERVER MSG pour tout les clients
	 * @param msg
	 */
	public void handleMessageFromServerUI(Object msg) {
		String ligne = "";
		if (!((String) msg).toString().startsWith("#") && getNumberOfClients() > 0) {
			this.sendToAllClients("SERVER MSG> " + msg);
			return;
		} else {
			//user.handleMessageFromTheClientConsole(ligne);
			String[] argument = msg.toString().split(" ");
			String controle = argument[0];
			switch (controle) {
			  
			  	// provoque fermeture normal du serveur
			  	case "#quit" :
			  		try {
			  			close(); 
			  			System.exit(0);
			  		} catch (IOException exception) {
			  			System.exit(1);
			  		}
			  		System.exit(0);
			  	
			  	// provoque serveur de cesser d'écouter aux nouveaux clients
			  	case "#stop" :
			  		stopListening();
			  	
			  	// provoque serveur de cesser d'écouter aux nouveaux clients et de
			  	// déconnecter tous les clients existants
			  	case "#close" :
			  		try {
			  			close();
			  		} catch (IOException exception) {
			  			System.out.println("Erreur " + exception.getMessage());
			  		}
			  					  	
			  	// appelle méthode setPort() sur le serveur. Commande autorisé seulement si le 
			  	// serveur est fermé
			  	case "#setport" :
			  		if (isListening()) {
			  			System.out.println("Erreur. Déjà en écoute.");
			  			return;
			  		}
			  		int port = 0;	
			  		// cherche la valeur donnée du port
			  		try {
			  			port = Integer.parseInt(ligne.split(" ")[1]);
			  		} catch (ArrayIndexOutOfBoundsException exception) {
			  			System.out.println("Valeur du port entré est invalide");
			  			return;
			  		}
			  		setPort(port);
			  	
			  	// provoque serveur de commencer à écouter pour des nouveaux clients. Commande seulement
			  	// valable si serveur est arrêté
			  	case "#start" :
			  		if (isListening()) {
			  			System.out.println("Erreur. Déjà en écoute.");
			  			return;
			  		}
			  		try {
			  			listen();
			  		} catch (IOException exception) {
			  			System.out.println("Erreur " + exception.getMessage());
			  		}

			  	// affiche numéro de port actuel
			  	case "#getport" :
			  		System.out.println("Le numéro de port actuel est : " + this.getPort());
			  		break;
			  		
			  } 
		}
	}
	
	/** 
	 * This method overrides the one in the superclass. Called
	 * when the server starts listening for connections.
	 * Méthode pris de la classe <code>EchoServer</code>
	 */
	protected void serverStarted() {
		System.out.println
		("Server listening for connections on port " + getPort());
	}
	
	/**
	 * This method overrides the one in the superclass.  Called
	 * when the server stops listening for connections.
	 * Méthode pris de la classe <code>EchoServer</code>
	 */
	protected void serverStopped() {
		System.out.println ("Server has stopped listening for connections.");
	}
}
