package com.iut.html.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.regex.PatternSyntaxException;

import javax.management.BadAttributeValueExpException;
import javax.print.AttributeException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.iut.html.sax.SAXHandler;

/**
 * Classe de callbacks utilisées par le serveur HTTP
 * pour traiter les requêtes des clients
 * 
 * @author durotm & dichtelj
 *
 */
public class HTTPRequestHandler
{

	private File file;
	private HashMap<String, String> parameters;
	private SAXParserFactory factory;


	/**
	 * Constructor
	 * @param request
	 * @throws HTTPBadRequestException 
	 */
	public HTTPRequestHandler(String request) throws HTTPBadRequestException
	{
		this.factory = SAXParserFactory.newInstance();

		// Log
		System.out.println("Log requête : " + request);

		// Teste la validité de la requête
		if (request == null || ! request.startsWith("GET "))
			throw new HTTPBadRequestException();
		else
			request = request.substring(5, request.indexOf("HTTP"));

		// Récupération du fichier demandé et des paramètres de la requête
		if(request.indexOf("?") != -1) {
			this.file = new File(request.substring(0, request.indexOf("?")));
			this.parameters = this.parseParameters(request.substring(request.indexOf("?") + 1));
		} else {
			this.file = new File(request);
			this.parameters = new HashMap<String, String>();
		}

		// Log
		System.out.println("Fichier demandé : " + file.getAbsolutePath());

		// Teste si le fichier demandé existe
		if (! this.file.isFile())
			throw new HTTPBadRequestException();
	}

	/**
	 * Retourne la réponse à la requête du client
	 * 
	 * @return byte[]
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public byte[] getResponse() throws ParserConfigurationException, SAXException, IOException
	{
		// Lecture du fichier
		InputStream fileInputStream = new FileInputStream(this.file);

		// Parsage du fichier
		SAXParser saxParser = this.factory.newSAXParser();
		SAXHandler saxHandler = new SAXHandler();
		saxParser.parse(fileInputStream, saxHandler);

		// Fermeture du fichier
		fileInputStream.close();

		return saxHandler.getHtmlContent().getBytes();
	}

	/**
	 * Transforme une chaine de paramètres en une HashMap
	 * @param rawParams
	 * @return HashMap
	 */
	private HashMap<String, String> parseParameters(String rawParams)
	{
		HashMap<String, String> mapParams = new HashMap<String, String>();
		String[] arrayParams;

		try {
			arrayParams = rawParams.split("&");
			// Parcours des paramètres
			for (int i = 0; i < arrayParams.length; i++) {
				String[] field = arrayParams[i].split("=");			
				mapParams.put(field[0], field[1]);
			}
		} catch (PatternSyntaxException e)
		{
			String[] field = rawParams.split("=");	
			mapParams.put(field[0], field[1]);	
		}

		return mapParams;
	}

}
