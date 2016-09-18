package com.deodexed.brainfuck;

/**
 * Created by Deode on 10/09/2016.
 */
public class Achat {
    private int id_achat;
    private String date;
    private String user;



    public Achat(int id_achat, String user, String date) {
        this.id_achat = id_achat;
        this.date = date;
        this.user = user;

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
}
