package com.iut.html.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

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
	 * @throws HTTPUnknownMethod 
	 */
	public HTTPRequestHandler(String request) throws HTTPBadRequestException
	{
		this.factory = SAXParserFactory.newInstance();
		
		// Teste la validité de la requête
		if (request == null || ! request.startsWith("GET "))
			throw new HTTPBadRequestException();
		else
			request = request.substring(5, request.indexOf("HTTP"));
		
		// Récupère le fichier demandé
		File file = new File(request.split("?")[0]);
		
		// Teste si le fichier demandé existe
		if (file.isFile())
			this.file = file;
		else
			throw new HTTPBadRequestException();
		
		// Récupération des paramètres de la requête
		if(request.split("?").length > 1) {
            this.parameters = this.parseParameters(request.split("?")[1]);
        } else {
            this.parameters = new HashMap<String, String>();           
        }
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
        
        return saxHandler.getHtmlContent().getBytes();
	}
	
	/**
	 * Transforme une chaine de paramètres en une HashMap
	 * @param parameters
	 * @return HashMap
	 */
	private HashMap<String, String> parseParameters(String rawParams)
	{
		String[] arrayParams = rawParams.split("&");
		HashMap<String, String> mapParams = new HashMap<String, String>(); 
		
		for (int i = 0; i < arrayParams.length; i++) {
			String[] field = arrayParams[i].split("=");			
			mapParams.put(field[0], field[1]);
		}
		
		return mapParams;
	}
	
}
