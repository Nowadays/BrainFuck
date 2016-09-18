package com.deodexed.brainfuck;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Deode on 16/09/2016.
 */
public class DataHolder extends Application  {
    private ArrayList<Article> articlesList;

    private Context context;


    public ArrayList<Article> getArticlesList(){
        return articlesList;
    }

    public void setArticlesList(ArrayList<Article> articlesList){
        this.articlesList = articlesList;
    }

    public void setContext(Context context){
        this.context = context;
    }

    public void init(){

      /*  HttpGetTask hgt;
        hgt = new HttpGetTask(this,"BrainFuckApp/getAllAchat",ScriptValues.GET_ALL_ACHAT);
        hgt.execute("getAllAchat","");*/
    }

    public void deleteArticle(int position){
        this.articlesList.remove(position);
    }

    public void addArticle(Article article){
        this.articlesList.add(article);
    }

    public void setArticle(Article article, int position){
        this.articlesList.add(position,article);
    }




}
