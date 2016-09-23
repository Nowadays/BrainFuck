package com.deodexed.brainfuck;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AchatDetailActivity extends AppCompatActivity implements ArticleFragment.FragmentItemClickCallBack, ArticleDetailFragment.FragmentEditItemCallBack, RemoteDatabaseListener, PostResultListener {

    @Override
    public void onPostRequestFinished(String jsondata) {

    }

    public final String FRAG_ARTICLE_RECYCLERVIEW = "FRAG_ARTICLE_RECYCLERVIEW";
    public final String FRAG_ARTICLE_DETAIL = "FRAG_ARTICLE_DETAIL";

    private ProgressDialog pd;

    private ArrayList<Article> listData;
    private FragmentManager manager;
    private int lastAdapterPosition = 0;

    private int id_achat;
    private String nom_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_achat_detail);
        listData = new ArrayList<>();





       /*Fragment detFrag = ArticleDetailFragment.newInstance(listData.get(0));


        transaction.replace(R.id.up_fragment,detFrag,FRAG_ARTICLE_DETAIL);
        transaction.commit();*/


        Bundle extras = getIntent().getBundleExtra(MainActivity.BUNDLE_EXTRAS);
        if (!extras.isEmpty()){
            pd = new ProgressDialog(this);
            pd.setTitle("Downloading...");
            pd.setMessage("Téléchargement des articles");
            pd.show();
            this.id_achat = extras.getInt(MainActivity.EXTRA_ID_ACHAT);
            this.nom_user = extras.getString(MainActivity.EXTRA_NOM_USER);
            HttpGetTask hgt = new HttpGetTask(this, "BrainFuckApp/getAllArticlesFromAchat/" + Integer.toString(this.id_achat), ScriptValues.GET_ALL_ARTICLE_FROM_ACHAT, pd);
            if (isNetworkAvailable()) {

                hgt.execute();
            }
        }




      /*  final View myView = findViewById(R.id.im_detail_image);
        myView.post(new Runnable(){

            @Override
            public void run()
            {
                //create your anim here
                // get the center for the clipping circle
                int cx = myView.getWidth() / 2;
                int cy = myView.getHeight() / 2;

// get the final radius for the clipping circle
                float finalRadius = (float) Math.hypot(cx, cy);

// create the animator for this view (the start radius is zero)
                Animator anim =
                        ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);
                anim.setDuration(1000);

// make the view visible and start the animation
                myView.setVisibility(View.VISIBLE);
                anim.start();
            }
        });*/


    }

    @Override
    public void onEditButtonClick(Article article) {
        Toast.makeText(this, "Edit button clicked", Toast.LENGTH_SHORT).show();
        int id_article = article.getId_article();
        int id_achat = article.getId_achat();


        ContentValues cv = new ContentValues();
        cv.put("id_article", id_article);
        cv.put("id_achat", id_achat);
        cv.put("libelle", article.getLibelle_article());
        cv.put("quantite", article.getQuantite_article());
        cv.put("prix", article.getPrix_article());

        HttpPostTask hp = new HttpPostTask(this, "BrainFuckApp/setArticle", cv);
        hp.execute();
        ArticleFragment af = (ArticleFragment) getSupportFragmentManager().findFragmentById(R.id.down_fragment);
        if (af.isDataListEmpty()) {
            af.addArticle(article);
        } else {
            af.setArticle(article, this.lastAdapterPosition);
        }


    }

    @Override
    public void onListItemClicked(int position) {
        ArticleDetailFragment det = (ArticleDetailFragment) getSupportFragmentManager().findFragmentById(R.id.up_fragment);
        det.setEditText(listData.get(position).getLibelle_article(), Integer.toString(listData.get(position).getQuantite_article()), Integer.toString(listData.get(position).getPrix_article()));
        det.setId(listData.get(position).getId_article(), listData.get(position).getId_achat(), listData.get(position).getNom_user());

        /*Fragment detailFrag = ArticleDetailFragment.newInstance(listData.get(position));
        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.up_fragment,detailFrag,FRAG_ARTICLE_DETAIL);
        transaction.commit();*/
        this.lastAdapterPosition = position;
    }

    public ArrayList<Article> getListData() {
        return this.listData;
    }


    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onExecSQLfinished(String jsondata, String scriptConstant) {
        switch (scriptConstant) {

            case (ScriptValues.GET_ALL_ARTICLE_FROM_ACHAT): {
                if (jsondata != null) {
                    int id_user;
                    int id_articles;
                    String nom_user;
                    String libelle_article;
                    String prenom_user;
                    String date;
                    int prix;
                    int quantite;
                    JSONArray array = null;
                    listData.clear();
                    try {
                        array = new JSONArray(jsondata);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject row = array.getJSONObject(i);
                            //nom_user = row.getString("nom") + " " + row.getString("prenom");
                            id_articles = row.getInt("id");
                            // id_achat = row.getInt("id_achat");
                            libelle_article = row.getString("libelle");
                            prix = row.getInt("prix");
                            quantite = row.getInt("quantite");
                            // date = row.getString("date");

                            this.listData.add(new Article(id_articles, this.id_achat, this.nom_user, libelle_article, prix, quantite));

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (!listData.isEmpty()) {
                        FragmentTransaction transaction;
                        manager = getSupportFragmentManager();
                        Fragment listFrag = ArticleFragment.newInstance(this.listData);
                        transaction = manager.beginTransaction();


                        transaction.replace(R.id.down_fragment, listFrag, FRAG_ARTICLE_RECYCLERVIEW);
                        transaction.commit();
                    } else {

                    }

                }
                break;
            }
        }
    }


    @Override
    public void onDataUpdated() {

    }
}
