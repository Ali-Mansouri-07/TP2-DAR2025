package common;

import java.io.Serializable;

public class Operation implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private double operande1;
    private String operateur;
    private double operande2;
    private double resultat;
    private String erreur;

    // Constructeur
    public Operation(double operande1, String operateur, double operande2) {
        this.operande1 = operande1;
        this.operateur = operateur;
        this.operande2 = operande2;
    }

    // Getters et Setters
    public double getOperande1() {
        return operande1;
    }

    public void setOperande1(double operande1) {
        this.operande1 = operande1;
    }

    public String getOperateur() {
        return operateur;
    }

    public void setOperateur(String operateur) {
        this.operateur = operateur;
    }

    public double getOperande2() {
        return operande2;
    }

    public void setOperande2(double operande2) {
        this.operande2 = operande2;
    }

    public double getResultat() {
        return resultat;
    }

    public void setResultat(double resultat) {
        this.resultat = resultat;
    }

    public String getErreur() {
        return erreur;
    }

    public void setErreur(String erreur) {
        this.erreur = erreur;
    }

    @Override
    public String toString() {
        if (erreur != null) {
            return operande1 + " " + operateur + " " + operande2 + " = ERREUR: " + erreur;
        }
        return operande1 + " " + operateur + " " + operande2 + " = " + resultat;
    }
}