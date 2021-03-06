package com.iut.html.entity;

/**
 * Représentation d'un étudiant
 * @author durotm & dichtelj
 */
public class Etudiant {
	
	private String nom;
	private String prenom;
	private String groupe;
	
	/**
	 * Constructeur
	 * @param nom
	 * @param prenom
	 * @param groupe
	 */
	public Etudiant(String nom, String prenom, String groupe)
	{
		this.nom = nom;
		this.prenom = prenom;
		this.groupe = groupe;
	}

	/**
	 * Génère un hash en fonction du nom et du prénom
	 * @return String
     */
	public String generateHash()
	{
		return String.valueOf((this.nom + this.prenom).hashCode());
	}
	
	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public String getPrenom() {
		return prenom;
	}
	
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	public String getGroupe() {
		return groupe;
	}
	
	public void setGroupe(String groupe) {
		this.groupe = groupe;
	}
	
	/**
	 * ToString
	 */
	public String toString() {
		return "Etudiant [nom=" + nom + ", prenom=" + prenom + ", groupe="
				+ groupe + "]";
	}
}
