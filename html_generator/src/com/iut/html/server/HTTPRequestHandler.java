package com.iut.html.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
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

    // Extensions autorisés
    private static Pattern fileExtnPtrn = Pattern.compile("([^\\s]+(\\.(?i)(toto))$)");

	/**
	 * Constructor
	 * @param request
	 * @throws HTTPBadRequestException
	 */
	public HTTPRequestHandler(String request) throws HTTPBadRequestException
	{
		this.factory = SAXParserFactory.newInstance();

		// Teste la validité de la requête
		if (request == null || ! request.startsWith("GET "))
            throw new HTTPBadRequestException();
        else
            request = request.substring(5, request.indexOf("HTTP")).trim();

		// Récupération du fichier demandé et des paramètres de la requête
		if(request.indexOf("?") != -1) {
			this.file = new File(request.substring(0, request.indexOf("?")));
			this.parameters = this.parseParameters(request.substring(request.indexOf("?") + 1));
		} else {
			this.file = new File(request);
			this.parameters = new HashMap<String, String>();
		}

		// Log
        System.out.println("Fichier demandé : " + this.file.getPath());
        System.out.println("Paramètres : " + this.parameters.toString());

        // Vérification de l'extension
        Matcher mtch = fileExtnPtrn.matcher(this.file.getName());

		// Teste si le fichier demandé est valide
		if (! (this.file.isFile() && mtch.matches()))
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
        // On transmet les paramètres au SAXHandler
		SAXHandler saxHandler = new SAXHandler(this.parameters);
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
