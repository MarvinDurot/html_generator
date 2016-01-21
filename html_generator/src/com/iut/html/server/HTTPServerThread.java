package com.iut.html.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.iut.html.entity.Etudiant;
import com.iut.html.sax.SAXHandler;

/**
 * HTTP Thread
 * @author durotm & dichtelj
 *
 */
public class HTTPServerThread extends Thread {
	
	private static final String ERROR_MESSAGE =
	"<html><body><head><meta charset=\"utf-8\"></head><h1>Fichier non trouvé!</h1></body></html>";
	
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
	 * Implémentation Runnable
	 */
	public void run() {
		try {
			// On récupère les flux d'entrée et de sortie
			InputStream is = this.clientSocket.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
			
			// On récupére la requête du client
			String request = br.readLine();
			
			// Si c'est une requête HTTP GET
			if ((request != null) && (request.startsWith("GET "))) {
				
				// Récupère le fichier demandé s'il existe
				File file = new File(request.substring(5, request.indexOf("HTTP")));
				
				// TODO : récupérer et traiter les paramètres s'il y en a...
				
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
					this.clientSocket.getOutputStream().write(HTTPServerThread.ERROR_MESSAGE.getBytes());
					System.out.println("Fichier non trouvé!");
				}
			}
			
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
