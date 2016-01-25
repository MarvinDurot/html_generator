package com.iut.html.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.xml.sax.SAXException;

/**
 * HTTP Thread
 * @author durotm & dichtelj
 *
 */
public class HTTPServerThread extends Thread {
	
	private static final String ERROR_MESSAGE =
	"<html><body><head><meta charset=\"utf-8\"></head><h1>Requête invalide !</h1></body></html>";
	
	private Socket clientSocket;

	/**
	 * Constructeur
	 * @param socket
	 */
	public HTTPServerThread(Socket socket) {
		super();
		this.clientSocket = socket;
	}
	
	/**
	 * Implémentation Runnable
	 */
	public void run() {
		try {
			// Récupération des flux d'entrée et de sortie
			InputStream is = this.clientSocket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			
			// Récupération de la requête du client
			String request = br.readLine();
			
			try {
				// Construction du handler
				HTTPRequestHandler httpHandler = new HTTPRequestHandler(request);
				// Envoi de la réponse au client
				this.clientSocket.getOutputStream().write(httpHandler.getResponse());
				
			} catch (HTTPBadRequestException e) {
				this.clientSocket.getOutputStream().write(HTTPServerThread.ERROR_MESSAGE.getBytes());
				System.out.println("Requête invalide!");
			} catch (TransformerException e) {}
			
			// Fermeture du buffer et du socket client
			this.clientSocket.close();
			br.close();
			
		} catch (IOException | SAXException | ParserConfigurationException e) {
			System.out.println("Le serveur n'arrive pas à fermer le flux du buffer!");
			System.exit(-1);
		}
		try {
			this.clientSocket.close();
		} catch (IOException e) {
			System.out.println("Le serveur n'arrive pas à fermer le socket!");
			System.exit(-1);
		}
	}
}
