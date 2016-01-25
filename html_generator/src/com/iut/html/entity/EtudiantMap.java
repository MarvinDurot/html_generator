package com.iut.html.entity;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.HashMap;

public class EtudiantMap extends HashMap<String, Etudiant> {

    /**
     * Retourne la collection sous forme de document DOM
     * @return Document
     */
	public Document toDocument() throws ParserConfigurationException {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document document = db.newDocument();

        // Création de l'élément étudiants
        Element etudiants = document.createElement("étudiants");

        // Parcours des étudiants de la collection
        for (EtudiantMap.Entry entry : this.entrySet()) {

            // Récupération des infos de l'étudiant
            Etudiant etudiant = (Etudiant) entry.getValue();

            // Création de l'élément étudiant
            Element etudiantDOM = document.createElement("étudiant");

            // Création de l'élément id
            Element idDOM = document.createElement("id");
            idDOM.setTextContent((String) entry.getKey());
            etudiantDOM.appendChild(idDOM);

            // Création de l'élément nom
            Element nomDOM = document.createElement("nom");
            nomDOM.setTextContent(etudiant.getNom());
            etudiantDOM.appendChild(nomDOM);

            // Création de l'élément prénom
            Element prenomDOM = document.createElement("prénom");
            prenomDOM.setTextContent(etudiant.getPrenom());
            etudiantDOM.appendChild(prenomDOM);

            // Création de l'élément groupe
            Element groupeDOM = document.createElement("groupe");
            groupeDOM.setTextContent(etudiant.getGroupe());
            etudiantDOM.appendChild(groupeDOM);

            // Ajout de l'étudiant à l'élément étudiants
            etudiants.appendChild(etudiantDOM);
        }

        // Ajout des étudiants à la racine du document
        document.appendChild(etudiants);
        return document;
    }
}
