package com.iut.casir.sax;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.iut.casir.dom.DOMParser;
import com.iut.casir.entity.Etudiant;


public class SAXHandler extends DefaultHandler {

	private String htmlContent;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException
	{
		// Analyse balise
		switch (qName) {
			case "mapage":
				this.htmlContent += "<h1>" + atts.getValue("titre");
				break;
			case "liste":
				// TODO : c'est ici qu'il y a un souci!
				File file = new File(atts.getValue("source"));
				this.htmlContent += "<table>";
				this.generateList(file);
				break;
			default:
				break;
		}
	}
	
	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException
	{
		// Analyse balise
				switch (qName) {
					case "mapage":
						this.htmlContent += "</h1>";
						break;
					case "liste":
						this.htmlContent += "</table>";
						break;
					default:
						break;
				}
	}

	@Override
	public void startDocument() throws SAXException {
		this.htmlContent = "<html><body>";
	}
	
	@Override
	public void endDocument() throws SAXException {
		this.htmlContent += "</body></html>";
	}
	
	/**
	 * Génère la liste des étudiants
	 * @param file
	 * @return
	 */
	private void generateList(File file)
	{
		DOMParser domParser = new DOMParser();
		
		try {
			domParser.parser(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Récupération des étudiants
		ArrayList<Etudiant> etudiants = domParser.getEtudiants();
		
		// Parcours de étudiants
		for (int i = 0 ; i < etudiants.size() ; i++ )
		{
			this.htmlContent += "<tr><td>" + etudiants.get(i).getNom()
					+ "</td><td>" + etudiants.get(i).getPrenom() + "</td><td>"
					+ etudiants.get(i).getGroupe() + "</td></tr>";
		}
	}

	/**
	 * Getter
	 * @return
	 */
	public String getHtmlContent() {
		return htmlContent;
	}

}
