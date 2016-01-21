package com.iut.html.sax;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import com.iut.html.dom.DOMParser;
import com.iut.html.entity.Etudiant;

/**
 * Classe de callbacks utilisées par le parseur SAX
 * @author durotm & dichtelj
 *
 */
public class SAXHandler extends DefaultHandler {

	private String htmlContent;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException
	{
		// Analyse balise
		switch (qName) {
		case "mapage":
			this.htmlContent += "<head><title>" + atts.getValue("titre")
			+ "</title><meta charset=\"utf-8\"></head><body>";
			break;
		case "entete":
			this.htmlContent += "<h1>";
			break;
		case "retour":
			this.htmlContent += "<br/>";
			break;
		case "liste":
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
			this.htmlContent += "</body>";
			break;
		case "entete":
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
	public void characters(char[] ch, int start, int length)
	        throws SAXException {
	    this.htmlContent += new String(ch, start, length);
	}

	@Override
	public void startDocument() throws SAXException {
		this.htmlContent = "<!DOCTYPE html><html>";
	}

	@Override
	public void endDocument() throws SAXException {
		this.htmlContent += "</html>";
	}

	/**
	 * Génère la liste des étudiants sous forme de tableau HTML,
	 * le résultat est ajouté au contenu HTML courant
	 * 
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

		// Génération de l'entête du tableau
		this.htmlContent += "<thead><tr><th>Nom</th><th>Prénom</th><th>Groupe</th></tr></thead>";
		this.htmlContent += "<tbody>";
		
		// Récupération des étudiants
		ArrayList<Etudiant> etudiants = domParser.getEtudiants();

		// Parcours de étudiants
		for (int i = 0 ; i < etudiants.size() ; i++ )
		{
			this.htmlContent += "<tr><td>" + etudiants.get(i).getNom()
					+ "</td><td>" + etudiants.get(i).getPrenom() + "</td><td>"
					+ etudiants.get(i).getGroupe() + "</td></tr>";
		}
		
		// Fin du corps du tableau
		this.htmlContent += "</tbody>";
	}

	/**
	 * Getter
	 * @return
	 */
	public String getHtmlContent() {
		return htmlContent;
	}

}
