package com.deodexed.brainfuck;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Deode on 10/09/2016.
 */
public class Achat implements Parcelable {
    private int id_achat;
    private String date;
    private String user;
    private String commentaire;


    public String getCommentaire() {
        return commentaire;
    }

    public void setCommentaire(String commentaire) {
        this.commentaire = commentaire;
    }

    public Achat(int id_achat, String user, String date, String commentaire) {
        this.id_achat = id_achat;
        this.date = date;
        this.user = user;
        this.commentaire = commentaire;


    }

    public int getId_achat() {
        return id_achat;
    }

    public void setId_achat(int id_achat) {
        this.id_achat = id_achat;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id_achat);
        dest.writeString(this.user);
        dest.writeString(this.date);
        dest.writeString(this.commentaire);
    }

    public static final Parcelable.Creator<Achat> CREATOR
            = new Parcelable.Creator<Achat>() {
        public Achat createFromParcel(Parcel in) {
            return new Achat(in);
        }

        public Achat[] newArray(int size) {
            return new Achat[size];
        }
    };

    private Achat(Parcel in) {
        this.id_achat = in.readInt();
        this.user = in.readString();
        this.date = in.readString();
        this.commentaire = in.readString();
    }
}
