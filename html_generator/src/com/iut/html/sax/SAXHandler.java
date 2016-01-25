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

	// Balises reconnues
	public final static String MAPAGE = "mapage";
	public final static String ENTETE = "entete";
	public final static String LIEN = "lien";
	public final static String LISTE = "liste";
	public final static String ETUDIANT = "etudiant";
	
	private String htmlContent;
	private HashMap<String, String> parameters;
	private EtudiantMap etudiants;

	/**
	 * Constructeur
	 * @param parameters
	 * @param etudiants
     */
	public SAXHandler(HashMap<String, String> parameters, EtudiantMap etudiants) {
		this.htmlContent = "";
		this.parameters = parameters;
		this.etudiants = etudiants;
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
			this.htmlContent += "<h1 style=\"text-align: center;\">";
			break;
		case SAXHandler.LIEN:
			this.htmlContent += "<a href=\"" + atts.getValue("destination") + "\" style=\"display: block; text-align: center; margin-top: 20px;\">";
			break;
		case SAXHandler.LISTE:
			this.htmlContent += "<table style=\"padding-top: 10px; margin: auto; width: 600px; text-align: center;\">";
			this.generateList(atts.getValue("lien"));
			break;
		case SAXHandler.ETUDIANT:
			this.htmlContent += "<form method=\"GET\" action=\"#\" style=\"width: 170px; margin: auto;\">";
			this.generateForm();
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
		case SAXHandler.LIEN:
			this.htmlContent += "</a>";
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
	 * @param link
	 */
	private void generateList(String link)
	{
		// Génération de l'entête du tableau
		this.htmlContent += "<thead><tr><th>Nom</th><th>Prénom</th><th>Groupe</th></tr></thead>";
		this.htmlContent += "<tbody>";
		
		// Parcours des étudiants
		for (EtudiantMap.Entry etudiant : this.etudiants.entrySet()) {
			this.htmlContent += "<tr ><td>" + ((Etudiant) etudiant.getValue()).getNom();
			this.htmlContent += "</td><td>" + ((Etudiant) etudiant.getValue()).getPrenom();
			this.htmlContent += "</td><td>"	+ ((Etudiant) etudiant.getValue()).getGroupe();
			this.htmlContent += "</td><td><a href=\"" + link + "?action=view&id=" + etudiant.getKey() + "\">Afficher détail</a></td></tr>";
		}

		// Fin du corps du tableau
		this.htmlContent += "</tbody>";
	}

	/**
	 * Génère le formulaire d'édition d'un étudiant
	 */
	private void generateForm()
	{
		// Récupération de l'étudiant
		Etudiant etudiant = this.etudiants.get(this.parameters.get("id"));

		// Mode édition
		if (etudiant != null) {
			this.htmlContent += "<input type=\"hidden\" name=\"id\" value=\""+ this.parameters.get("id") + "\">";
			this.htmlContent += "<label>Nom : </label><input type=\"text\" name=\"nom\" value=\""+ etudiant.getNom() + "\" required>";
			this.htmlContent += "<label>Prénom : </label><input type=\"text\" name=\"prenom\" value=\""+ etudiant.getPrenom() + "\" required>";
			this.htmlContent += "<label>Groupe : </label><input type=\"text\" name=\"groupe\" value=\""+ etudiant.getGroupe() + "\" required>";
			this.htmlContent += "<button type=\"submit\" name=\"action\" value=\"update\" style=\"margin-top: 10px;\">Sauvegarder</button>";
			this.htmlContent += "<button type=\"submit\" name=\"action\" value=\"delete\" style=\"margin-top: 5px;\">Supprimer</button>";
			// Mode création
		} else {
			this.htmlContent += "<label>Nom : </label><input type=\"text\" name=\"nom\" placeholder=\"Entrez un nom...\" required>";
			this.htmlContent += "<label>Prénom : </label><input type=\"text\" name=\"prenom\" placeholder=\"Entrez un prénom...\" required>";
			this.htmlContent += "<label>Groupe : </label><input type=\"text\" name=\"groupe\" placeholder=\"Entrez un groupe...\" required>";
			this.htmlContent += "<button type=\"submit\" name=\"action\" value=\"create\" style=\"margin-top: 10px;\">Créer</button>";
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