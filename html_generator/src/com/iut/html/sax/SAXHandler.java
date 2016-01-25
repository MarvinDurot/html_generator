package com.iut.html.sax;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
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
	public final static String ETUDIANT = "etudiant";
	
	private String htmlContent;
	private HashMap<String, String> parameters;

	/**
	 * Constructeur
	 * @param parameters
     */
	public SAXHandler(HashMap<String, String> parameters) {
		this.htmlContent = "";
		this.parameters = parameters;
	}

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
			this.htmlContent += "<table>";
			this.generateList(new File(atts.getValue("source")));
			break;
		case SAXHandler.ETUDIANT:
			this.htmlContent += "<form method=\"GET\" action=\"#\" >";
			this.generateForm(new File(atts.getValue("source")));
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
		case SAXHandler.MAPAGE:
			this.htmlContent += "</body>";
			break;
		case SAXHandler.ENTETE:
			this.htmlContent += "</h1>";
			break;
		case SAXHandler.LISTE:
			this.htmlContent += "</table>";
			break;
		case SAXHandler.ETUDIANT:
			this.htmlContent += "</form>";
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
	 * Récupère les étudiants
	 * @param file
	 * @return EtudiantMap
     */
	private EtudiantMap loadEtudiants(File file)
	{
		DOMParser domParser = new DOMParser();

		try {
			domParser.parse(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return domParser.getEtudiants();
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
		// Génération de l'entête du tableau
		this.htmlContent += "<thead><tr><th>Nom</th><th>Prénom</th><th>Groupe</th></tr></thead>";
		this.htmlContent += "<tbody>";
		
		// Récupération des étudiants
		EtudiantMap etudiants = this.loadEtudiants(file);
		
		// Parcours des étudiants
		for (EtudiantMap.Entry etudiant : etudiants.entrySet()) {
			this.htmlContent += "<tr ><td>" + ((Etudiant) etudiant.getValue()).getNom();
			this.htmlContent += "</td><td>" + ((Etudiant) etudiant.getValue()).getPrenom();
			this.htmlContent += "</td><td>"	+ ((Etudiant) etudiant.getValue()).getGroupe();
			this.htmlContent += "</td><td><a href=\"/details.toto?action=view&id=" + etudiant.getKey() + "\">Afficher détail</a></td></tr>";
		}

		// Fin du corps du tableau
		this.htmlContent += "</tbody>";
	}

	private void generateForm(File file)
	{
		// Récupération des étudiants
		EtudiantMap etudiants = this.loadEtudiants(file);
		Etudiant etudiant = etudiants.get(this.parameters.get("id"));

		// Mode édition
		if (etudiant != null) {
			this.htmlContent += "<input type=\"hidden\" name=\"id\" value=\""+ this.parameters.get("id") + "\">";
			this.htmlContent += "<input type=\"text\" name=\"nom\" value=\""+ etudiant.getNom() + "\">";
			this.htmlContent += "<input type=\"text\" name=\"prenom\" value=\""+ etudiant.getPrenom() + "\">";
			this.htmlContent += "<input type=\"text\" name=\"groupe\" value=\""+ etudiant.getGroupe() + "\">";
			this.htmlContent += "<input type=\"submit\" name=\"action\" value=\"update\">";
			this.htmlContent += "<input type=\"submit\" name=\"action\" value=\"delete\">";
			// Mode création
		} else {
			this.htmlContent += "<input type=\"text\" name=\"nom\" value=\"Entrez un nom...\">";
			this.htmlContent += "<input type=\"text\" name=\"prenom\" value=\"Entrez un prénom...\">";
			this.htmlContent += "<input type=\"text\" name=\"groupe\" value=\"Entrez un groupe...\">";
			this.htmlContent += "<input type=\"submit\" name=\"action\" value=\"create\">";
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
