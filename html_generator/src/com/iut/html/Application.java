package com.iut.html;

import java.io.IOException;
import org.xml.sax.SAXException;
import com.iut.html.server.HTTPServer;

/**
 * Point d'entrée de l'application
 * @author durotm & dictelj
 *
 */
public class Application {

	public static void main(String[] args) throws SAXException, IOException {
		new HTTPServer("localhost", 8080);
	}
}