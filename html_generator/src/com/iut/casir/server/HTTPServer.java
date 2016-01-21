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
		// Cr�ation du socket serveur
		try {
			this.serverSocket = new ServerSocket();
			InetAddress adr = InetAddress.getByName(HOST);
			InetSocketAddress sa = new InetSocketAddress(adr, PORT);
			this.serverSocket.bind(sa);
		} catch (IOException e) {
			System.out.println("Le serveur n'arrive pas � cr�er le socket!");
			System.exit(-1);
		}

		System.out.println("Le serveur a bien d�marr�!");

		// Boucle d'�coute
		while (true) {
			try {
				// On attend une connexion (bloquant)
				Socket socket = serverSocket.accept();
				System.out.println("Un client s'est connect�!");
				// On cr�e un thread par client connect�
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