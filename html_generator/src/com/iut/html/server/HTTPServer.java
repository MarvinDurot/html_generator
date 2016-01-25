package com.iut.html.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Serveur HTTP 1.0
 * @author durotm & dichetlj
 *
 */
public class HTTPServer {

	private ServerSocket serverSocket;

	/**
	 * Constructeur
	 */
	public HTTPServer(String hostname, int port) {
		// Création du socket serveur
		try {
			this.serverSocket = new ServerSocket();
			InetAddress adr = InetAddress.getByName(hostname);
			InetSocketAddress sa = new InetSocketAddress(adr, port);
			this.serverSocket.bind(sa);
		} catch (IOException e) {
			System.out.println("Le serveur n'arrive pas à créer le socket!");
			System.exit(-1);
		}

		System.out.println("Le serveur a bien démarré!");

		// Boucle d'écoute
		while (true) {
			try {
				// On attend une connexion (bloquant)
				Socket socket = serverSocket.accept();
				System.out.println("Un client s'est connecté!");
				// On crée un thread par client connecté
				HTTPServerThread httpThread = new HTTPServerThread(socket);
				httpThread.start();
			} catch (IOException e) {
				System.out.println("Le client n'a pas pu se connecter!");
			}
		}
	}
}