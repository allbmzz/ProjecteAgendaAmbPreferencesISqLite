package com.example.projecteloggerambsql;

import java.io.Serializable;
import java.util.Date;

public class Contacte implements Serializable {

    private long id;
    private String nom;
    private String cognom;
    private String adreca;
    private Integer telefon;
    private String mail;
    private Date dataNaixement;

    public Contacte(long id, String nom, String cognom, String adreca, Integer telefon, String mail, Date dataNaixement){
        this.id = id;
        this.nom = nom;
        this.cognom = cognom;
        this.adreca = adreca;
        this.telefon = telefon;
        this.mail = mail;
        this.dataNaixement = dataNaixement;
    }

    public Contacte(String nom, String cognom, String adreca, Integer telefon, String mail, Date dataNaixement){
        this(-1, nom, cognom, adreca, telefon, mail, dataNaixement);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCognom() {
        return cognom;
    }

    public void setCognom(String cognom) {
        this.cognom = cognom;
    }

    public String getAdreca() {
        return adreca;
    }

    public void setAdreca(String adreca) {
        this.adreca = adreca;
    }

    public Integer getTelefon() {
        return telefon;
    }

    public void setTelefon(Integer telefon) {
        this.telefon = telefon;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Date getDataNaixement() {
        return dataNaixement;
    }

    public void setDataNaixement(Date dataNaixement) {
        this.dataNaixement = dataNaixement;
    }

    public String getNomComplet() {
        return (nom + " " + cognom).trim();
    }
}
