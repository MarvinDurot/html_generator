package com.iut.casir.com;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

import com.iut.casir.sax.SAXHandler;


public class Application {

	public static void main(String[] args) throws SAXException, IOException {
//		DOMParser domParser = new DOMParser();
//		domParser.parser(new File("G:/casir/java/mini_projet/etudiants.xml"));
//		System.out.println(domParser.getEtudiants());
		
		SAXParserFactory factory = SAXParserFactory.newInstance();
        try {
            InputStream xmlInput = new FileInputStream("G:/casir/java/mini_projet/structure.xml");

            SAXParser saxParser = factory.newSAXParser();
            SAXHandler handler = new SAXHandler();
            saxParser.parse(xmlInput, handler);

        } catch (Throwable err) {
            err.printStackTrace ();
        }
	}
}