package com.iut.casir.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.iut.casir.sax.SAXHandler;

/**
 * HTTP Thread
 * @author durotm & dichtelj
 *
 */
public class HTTPServerThread extends Thread {
	
	private static final String ERROR_MESSAGE = "<html><body><h1>Fichier non trouv�!</h1></body></html>";
	private Socket clientSocket;
	private SAXParserFactory factory;

	/**
	 * Constructeur
	 * @param socket
	 */
	public HTTPServerThread(Socket socket) {
		super();
		this.factory = SAXParserFactory.newInstance();
		this.clientSocket = socket;
	}

	/**
	 * Impl�mentation Runnable
	 */
	public void run() {
		try {
			// On r�cup�re les flux d'entr�e et de sortie
			InputStream is = this.clientSocket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			
			// On r�cup�re la requ�te du client
			String request = br.readLine();
			
			// Si c'est une requ�te HTTP GET
			if ((request != null) && (request.startsWith("GET "))) {
				
				// R�cup�re le fichier si il existe
				File file = new File(request.substring(5, request.indexOf("HTTP")));
				
				if (file.isFile()) {
					// Lecture du fichier				
					InputStream fileInputStream = new FileInputStream(file);
					// Parsage du fichier
		            SAXParser saxParser = this.factory.newSAXParser();
		            SAXHandler handler = new SAXHandler();
		            saxParser.parse(fileInputStream, handler);
					
					// On envoie le contenu du fichier vers le client
					this.clientSocket.getOutputStream().write(handler.getHtmlContent().getBytes());
				} else {
					this.clientSocket.getOutputStream().write(this.ERROR_MESSAGE.getBytes());
					System.out.println("Fichier non trouv�!");
				}
			}
			
			// Fermeture du buffer et du socket client
			this.clientSocket.close();
			br.close();
			
		} catch (IOException | SAXException | ParserConfigurationException e) {
			System.out.println("Le serveur n'arrive pas � fermer le flux du buffer!");
			System.exit(-1);
		}
		try {
			this.clientSocket.close();
		} catch (IOException e) {
			System.out.println("Le serveur n'arrive � fermer le socket!");
			System.exit(-1);
		}
	}
}
