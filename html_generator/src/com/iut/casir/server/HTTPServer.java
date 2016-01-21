package com.iut.casir.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Serveur HTTP
 * @author durotm & dichetlj
 *
 */
public class HTTPServer {

	private ServerSocket serverSocket;
	private final static int PORT = 80;
	private final static String HOST = "localhost";

	/**
	 * Constructeur
	 */
	public HTTPServer() {
		// Création du socket serveur
		try {
			this.serverSocket = new ServerSocket();
			InetAddress adr = InetAddress.getByName(HOST);
			InetSocketAddress sa = new InetSocketAddress(adr, PORT);
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

	public static void main(String[] args) {
		new HTTPServer();
	}
}