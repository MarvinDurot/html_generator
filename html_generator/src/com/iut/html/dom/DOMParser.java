package com.iut.html.dom;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.iut.html.entity.Etudiant;
import com.iut.html.entity.EtudiantMap;

/**
 * Lit et enregistre les étudiants d'un fichier XML sous forme d'ArrayList
 * @author durotm & dichtelj
 *
 */
public class DOMParser {

	private static EtudiantMap etudiants;
	private Document document;

	/**
	 * Constructeur
	 */
	public DOMParser() {
		this.etudiants = new EtudiantMap();
	}
	
	/**
	 * Parse un fichier pour en récupérer des étudiants sous forme de collection
	 * @param file
	 * @throws IOException
	 */
	public void parse(File file) throws IOException {
		// Création de la factory pour obtenir le document
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		
		try {
			final DocumentBuilder builder = factory.newDocumentBuilder();
			this.document = builder.parse(file);

		} catch (final ParserConfigurationException e) {
			e.printStackTrace();
		} catch (final SAXException e) {
			e.printStackTrace();
		}
		
		// Récupération de la racine
		final Element racine = document.getDocumentElement();
		// Si racine valide
		if (racine.getNodeName().equals("étudiants")) {
			// Récupération des noeuds fils
			final NodeList noeuds = racine.getChildNodes();
			// Pour chaque étudiant
			for (int i = 0; i < noeuds.getLength(); i++) {
				// Si élément valide
				if (noeuds.item(i).getNodeType() == Node.ELEMENT_NODE) {
					// Récupération du noeud de l'étudiant
					final Element noeud = (Element) noeuds.item(i);
					// Récupération des informations de l'étudiant
					Element id = (Element) noeud.getElementsByTagName("id").item(0);
					Element nom = (Element) noeud.getElementsByTagName("nom").item(0);
					Element prenom = (Element) noeud.getElementsByTagName("prénom").item(0);
					Element groupe = (Element) noeud.getElementsByTagName("groupe").item(0);
					// Création de l'objet étudiant
					Etudiant etudiant = new Etudiant(nom.getTextContent(), prenom.getTextContent(), groupe.getTextContent());
					// Ajout de l'étudiant à la collection
					this.etudiants.put(id.getTextContent(), etudiant);
				}
			}
		}
	}
	
	/**
	 * Getter
	 * @return
	 */
	public EtudiantMap getEtudiants() {
		return etudiants;
	}

	@Override
	public String toString() {
		return "DOMParser [etudiants=" + etudiants + "]";
	}
}
