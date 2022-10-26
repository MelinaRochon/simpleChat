// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient {
	//Instance variables **********************************************
  
	/**
	 * The interface type variable.  It allows the implementation of 
	 * the display method in the client.
	 */
	ChatIF clientUI; 

  
	//Constructors ****************************************************
  
	/**
	 * Constructs an instance of the chat client.
	 *
	 * @param host The server to connect to.
	 * @param port The port number to connect on.
	 * @param clientUI The interface type variable.
	 */
	  
	public ChatClient(String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); //Call the superclass constructor
		this.clientUI = clientUI;
		openConnection();
	}

  
	//Instance methods ************************************************
    
	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	public void handleMessageFromServer(Object msg) {
		clientUI.display(msg.toString());
	}

	/**
	 * This method handles all data coming from the UI            
	 *
	 * @param message The message from the UI.    
	 */
	public void handleMessageFromClientUI(String message){

		try {
			if (message.startsWith("#")) {
				handleCommand(message);
			} else {
				sendToServer(message);
			}
		} catch (IOException e) {
			clientUI.display("Could not send message to server.  Terminating client.");
			quit();
		}
	}
  
 
  
	/**
	 * Cette méthode implémente des commandes spécifiques entrées par le client, commençant par 
	 * le symbole '#'. On regarde cas par cas la commande. Elles peuvent soit êtres les suivantes: 
	 * #quit,#logoff,#sethost, #setport, #login, #gethost ou #getport 
	 * @param msg
	 */
	public void handleCommand(String msg) {
		String[] argument = msg.split(" ");
		if (msg.equals("#quit")) {
			// provoque arrêt normal du client et arrêt du programme
			clientUI.display("Le client va quitter.");
			quit();
		} else if (msg.equals("#logoff")) {
			// provoque déconnexion du client du serveur, mais pas la fermeture
			try {
				if (this.isConnected()) {
					this.closeConnection();
				} else {
					clientUI.display("Le client est déjà déconnecté.");
				}
			} catch (IOException e) {
				clientUI.display("Erreur avec la déconnexion du client");
			}
		} else if (msg.equals("#sethost")) {
			// appelle méthode sethost(). Commande autorisé seulement si client est
		  	// déconnecté, sinon message d'erreur
			if (this.isConnected()) {
				clientUI.display("Commande non autorisé car vous êtes déjà connecté.");
			} else {
	  			this.setHost(argument[1]);
	  		}
		} else if (msg.equals("#setport")) {
			// appelle méthode setPort(). Commande autorisé seulement si client est
		  	// déconnecté, sinon message d'erreur
			if (this.isConnected()) {
				clientUI.display("Commande non autorisé car vous êtes déjà connecté.");
			} else {
	  			this.setPort(Integer.parseInt(argument[1]));
	  		}
		} else if (msg.equals("#login")) {
			// connecte client au serveur. Autorisé seulement si client n'est pas
		  	// connecté, sinon message d'erreur
			try {
				if (!this.isConnected()) {
					this.openConnection();
				} else {
					clientUI.display("Le client est déjà connecté.");
				}
			} catch (IOException e) {
				clientUI.display("Erreur avec la connexion du client");
			}
		} else if (msg.equals("#gethost")) {
			// affiche nom de l`hôte actuel
			clientUI.display("Le nom de l'hôte actuel est : " + this.getHost());
		} else if (msg.equals("#getport")) {
			// affiche numéro de port actuel
			clientUI.display("Le numéro du port actuel est : " + this.getPort());
		} else {
			clientUI.display("Veuillez entrez une commande qui est valide.");
	  }
	  
	  /**if (msg.startsWith("#")) {
		  String[] argument = msg.split(" ");
		  String controle = argument[0];
		  switch (controle) {
		   
		  	// provoque arrêt normal du client et arrêt du programme
		  	case "#quit" :
		  		quit();
		  		break;
		  	
		  	// provoque déconnexion du client du serveur, mais pas la fermeture
		  	case "#logoff" :
		  		try {
		  			closeConnection();
		  		} catch(IOException exception) {
		  			System.out.println("Erreur en fermant la connexion.");
		  		}
		  	
		  	// appelle méthode sethost(). Commande autorisé seulement si client est
		  	// déconnecté, sinon message d'erreur
		  	case "#sethost" :
		  		if (this.isConnected()) {
		  			System.out.println("Commande non autorisé car vous êtes déjà connecté.");
		  		} else {
		  			this.setHost(argument[1]);
		  		}
		  		break;
		  	
		  	// appelle méthode setPort(). Commande autorisé seulement si client est
		  	// déconnecté, sinon message d'erreur
		  	case "#setport" :
		  		if (this.isConnected()) {
		  			System.out.println("Commande non autorisé car vous êtes déjà connecté.");
		  		} else {
		  			this.setPort(Integer.parseInt(argument[1]));
		  		}
		  		break;
		  	
		  	// connecte client au serveur. Autorisé seulement si client n'est pas
		  	// connecté, sinon message d'erreur
		  	case "#login" :
		  		if (this.isConnected()) {
		  			System.out.println("Commande non autorisé car vous êtes déjà connecté.");
		  		} else {
		  			try {
		  				this.openConnection(); // ouvre connexion
		  			} catch (IOException exception) {
		  				System.out.println("Pas capable d'ouvrir une connexion avec le serveur.");
		  			}
		  		}
		  		break;
		  		
		  	// affiche nom de l`hôte actuel
		  	case "#gethost" :
		  		System.out.println("Le nom de l'hôte actuel est : " + this.getHost());
		  		break;
		  	
		  	// affiche numéro de port actuel
		  	case "#getport" :
		  		System.out.println("Le numéro de port actuel est : " + this.getPort());
		  		break;
		  		
		  } 
	  //} else {
		  //handleMessageFromClientUI(msg);
	  //}**/
	}
  
	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try{
			closeConnection();
    }catch(IOException e) {}
		System.exit(0);
	}
  
	/**
	 * Cette méthode répond à l'arrêt du serveur 
	 */
	@Override
	public void connectionClosed() {
		//System.out.println("Connection avec le serveur fermé");
		//System.exit(0);
		clientUI.display("La connexion est terminé");
	}
  
	/**
	 * Cette méthode lance une exception. 
	 */
	@Override
	public void connectionException(Exception e) {
		//System.out.println("Connection avec le serveur fermé. \nException de connection : " + e.getMessage());
		//System.exit(0);
		clientUI.display("La connexion avec le serveur est fermé.");
		System.exit(0);
	}
}//End of ChatClient class
