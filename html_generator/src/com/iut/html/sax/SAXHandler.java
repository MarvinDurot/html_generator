package com.iut.html.sax;

import java.io.File;
import java.io.IOException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.iut.html.dom.DOMParser;
import com.iut.html.entity.Etudiant;
import com.iut.html.entity.EtudiantMap;

/**
 * Classe de callbacks utilisées par le parseur SAX
 * @author durotm & dichtelj
 *
 */
public class SAXHandler extends DefaultHandler {

	public final static String MAPAGE = "mapage";
	public final static String ENTETE = "entete";
	public final static String RETOUR = "retour";
	public final static String LISTE = "liste";
	public final static String CARTE = "carte";
	public final static String ETUDIANT = "etudiant";
	
	private String htmlContent;

	@Override
	public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException
	{
		// Analyse balise
		switch (qName) {
		case SAXHandler.MAPAGE:
			this.htmlContent += "<head><title>" + atts.getValue("titre")
			+ "</title><meta charset=\"utf-8\"></head><body>";
			break;
		case SAXHandler.ENTETE:
			this.htmlContent += "<h1>";
			break;
		case SAXHandler.RETOUR:
			this.htmlContent += "<br/>";
			break;
		case SAXHandler.LISTE:
			File file = new File(atts.getValue("source"));
			this.htmlContent += "<table>";
			this.generateList(file);
			break;
		case SAXHandler.CARTE:
			this.htmlContent += "<form method=\"GET\" action=\"\" >";
			break;
		case SAXHandler.ETUDIANT:
			// TODO
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
		EtudiantMap etudiants = domParser.getEtudiants();
		
		// Parcour des étudiants
		for (EtudiantMap.Entry etudiant : etudiants.entrySet()) {
			this.htmlContent += "<tr ><td>" + ((Etudiant) etudiant.getValue()).getNom()
					+ "</td><td>" + ((Etudiant) etudiant.getValue()).getPrenom() + "</td><td>"
					+ ((Etudiant) etudiant.getValue()).getGroupe()
					+ "</td><td><a href=\"/details.toto?id="+ etudiant.getKey() + "\">Afficher détail</a></td></tr>";
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
