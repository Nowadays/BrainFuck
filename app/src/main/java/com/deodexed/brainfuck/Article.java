package com.deodexed.brainfuck;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Deode on 15/09/2016.
 */
public class Article implements Parcelable {
    private String libelle_article;
    private int quantite_article;
    private int prix_article;

    private int id_achat, id_user, id_article;
    private String prenom_user, nom_user;

    private String date;





    public Article(String libelle_article, int quantite_article, int prix_article) {
        this.libelle_article = libelle_article;
        this.quantite_article = quantite_article;
        this.prix_article = prix_article;
    }

    public Article(int id_article, int id_achat, String nom_user, String libelle_article, int prix_article, int quantite_article) {
        this.id_article = id_article;
        this.id_achat = id_achat;
        //this.id_user = id_user;
        this.nom_user = nom_user;
       // this.prenom_user = prenom_user;
        this.libelle_article = libelle_article;
       // this.date = date;
        this.prix_article = prix_article;
        this.quantite_article = quantite_article;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Article> CREATOR = new Parcelable.Creator<Article>()
    {
        @Override
        public Article createFromParcel(Parcel source)
        {
            return new Article(source);
        }

        @Override
        public Article[] newArray(int size)
        {
            return new Article[size];
        }
    };

    public int getId_article() {
        return id_article;
    }

    public void setId_article(int id_article) {
        this.id_article = id_article;
    }

    public static Creator<Article> getCREATOR() {
        return CREATOR;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id_article);
        parcel.writeInt(this.id_user);
        parcel.writeInt(this.id_achat);
        parcel.writeString(this.nom_user);
        //parcel.writeString(this.prenom_user);
        parcel.writeString(this.libelle_article);
        //parcel.writeString(this.date);
        parcel.writeInt(this.prix_article);
        parcel.writeInt(this.quantite_article);
    }
    protected Article(Parcel in) {
        this.id_article = in.readInt();
        this.id_user = in.readInt();
        this.id_achat = in.readInt();
        this.nom_user = in.readString();
       // this.prenom_user = in.readString();
        this.libelle_article = in.readString();
       // this.date = in.readString();
        this.prix_article = in.readInt();
        this.quantite_article = in.readInt();
    }

    public String getLibelle_article() {
        return libelle_article;
    }

    public void setLibelle_article(String libelle_article) {
        this.libelle_article = libelle_article;
    }

    public int getQuantite_article() {
        return quantite_article;
    }

    public void setQuantite_article(int quantite_article) {
        this.quantite_article = quantite_article;
    }

    public int getPrix_article() {
        return prix_article;
    }

    public void setPrix_article(int prix_article) {
        this.prix_article = prix_article;
    }

    public int getId_achat() {
        return id_achat;
    }

    public void setId_achat(int id_achat) {
        this.id_achat = id_achat;
    }

    public int getId_user() {
        return id_user;
    }

    public void setId_user(int id_user) {
        this.id_user = id_user;
    }

    public String getPrenom_user() {
        return prenom_user;
    }

    public void setPrenom_user(String prenom_user) {
        this.prenom_user = prenom_user;
    }

    public String getNom_user() {
        return nom_user;
    }

    public void setNom_user(String nom_user) {
        this.nom_user = nom_user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
