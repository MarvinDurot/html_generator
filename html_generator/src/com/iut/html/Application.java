package com.iut.html;

import java.io.File;
import java.io.IOException;
import com.iut.html.dom.DOMParser;
import com.iut.html.entity.EtudiantMap;
import org.xml.sax.SAXException;
import com.iut.html.server.HTTPServer;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Point d'entrée de l'application
 * @author durotm & dictelj
 *
 */
public class Application {

	// Configuration de l'application
	public final static String DATABASE = "etudiants.xml";
	public final static String HOST = "localhost";
	public final static int PORT = 8080;

	// Point d'entrée
	public static void main(String[] args) throws SAXException, IOException {
		new HTTPServer(Application.HOST, Application.PORT);
	}

	/**
	 * Chargement des étudiants
	 * @return
     */
	public static EtudiantMap getEtudiants()
	{
		File file = new File(Application.DATABASE);
		DOMParser domParser = new DOMParser();

		try {
			domParser.parse(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return domParser.getEtudiants();
	}

	/**
	 * Mise à jour des étudiants
	 */
	public static void updateEtudiants(EtudiantMap etudiants) throws TransformerConfigurationException, ParserConfigurationException, TransformerException
	{
		Transformer transformer = TransformerFactory.newInstance().newTransformer();
		Result output = new StreamResult(new File(Application.DATABASE));
		transformer.transform(new DOMSource(etudiants.toDocument()), output);
	}
}