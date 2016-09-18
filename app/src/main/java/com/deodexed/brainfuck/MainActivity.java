package com.deodexed.brainfuck;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Fade;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity implements AchatAdapter.ItemClickCallback, RemoteDatabaseListener {

    public static final String EXTRA_NOM_USER = "EXTRA_NOM_USER";
    public static final String EXTRA_DATE = "EXTRA_DATE";
    public static final String EXTRA_PRIX = "EXTRA_PRIX";
    public static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    public static final String EXTRA_ID_ACHAT = "EXTRA_ID_ACHAT";


    public static final String EXTRA_IMAGE_RES_ID = "EXTRA_IMAGE_RES_ID";
    public static final String ADD_TOKEN_TO_DATABASE = "Register/reg";
    public static final String GET_ACHAT = "BrainFuckApp/";


    private RecyclerView recyclerView;
    private AchatAdapter adapter;
    private TextView noAchat;
    private HttpGetTask hgt;
    private ContentValues cv;
    private FloatingActionButton fab;

    private ArrayList<Achat> listData;

    private ProgressDialog pd;

    @Override
    protected void onResume() {
        super.onResume();
        Animation hyperspaceJump = AnimationUtils.loadAnimation(this, R.anim.hyperspace_jump);
        fab.startAnimation(hyperspaceJump);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        FirebaseMessaging.getInstance().subscribeToTopic("BrainFuckTopic");
        String test = FirebaseInstanceId.getInstance().getToken();
        registerToken(test);

        listData = new ArrayList();
        pd = new ProgressDialog(this);
        pd.setTitle("Downloading...");
        pd.setMessage("Téléchargement des données en cours");

        pd.show();
        if (isNetworkAvailable()) {
            try {


                hgt = new HttpGetTask(this, "BrainFuckApp/getAllAchat", ScriptValues.GET_ALL_ACHAT, pd);
                hgt.execute();
                noAchat.setVisibility(View.GONE);

            } catch (Exception e) {

            }
        } else {

            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Internet indisponible");
            builder1.setCancelable(false);

            builder1.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
            noAchat = (TextView) findViewById(R.id.textView4);

            if (listData.isEmpty()) {
                noAchat.setVisibility(View.VISIBLE);
            }

        }


        recyclerView = (RecyclerView) findViewById(R.id.rec_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
        fab = (FloatingActionButton) findViewById(R.id.fab);
        Animation hyperspaceJump = AnimationUtils.loadAnimation(this, R.anim.hyperspace_jump);
        fab.startAnimation(hyperspaceJump);


    }

    @Override
    public void onItemClick(View v, int p) {
        Achat item = (Achat) listData.get(p);

        Intent i = new Intent(this, AchatDetailActivity.class);

        Bundle extras = new Bundle();
        extras.putString(EXTRA_NOM_USER, item.getUser());
        extras.putString(EXTRA_DATE, item.getDate());
        extras.putInt(EXTRA_ID_ACHAT, item.getId_achat());
        i.putExtra(BUNDLE_EXTRAS, extras);
        startActivity(i);

    }


    @Override
    public void onSecondaryIconClick(int p) {

    }

    private ItemTouchHelper.Callback createHelperCallBack() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback =
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                        ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                          RecyclerView.ViewHolder target) {
                        moveItem(viewHolder.getAdapterPosition(), target.getAdapterPosition());
                        return true;
                    }

                    @Override
                    public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        deleteItem(viewHolder.getAdapterPosition());
                    }
                };
        return simpleItemTouchCallback;
    }

    private void addItemToList() {
        /*ListItem item = DerpData.getRandomListItem();
        listData.add(item);
        adapter.notifyItemInserted(listData.indexOf(item));*/
    }

    private void moveItem(int oldPos, int newPos) {

        Achat item = (Achat) listData.get(oldPos);
        listData.remove(oldPos);
        listData.add(newPos, item);
        adapter.notifyItemMoved(oldPos, newPos);
    }

    private void deleteItem(final int position) {
        listData.remove(position);
        adapter.notifyItemRemoved(position);
    }

    public void onFabClick(View v) {
        if (isNetworkAvailable()) {
            try {
                cv = new ContentValues();

                hgt = new HttpGetTask(this, "BrainFuckApp/achat", ScriptValues.GET_ALL_ACHAT, pd);
                hgt.execute();
                noAchat.setVisibility(View.GONE);


            } catch (Exception e) {

            }
        } else {
            AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
            builder1.setMessage("Internet indisponible");
            builder1.setCancelable(false);

            builder1.setPositiveButton(
                    "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alert11 = builder1.create();
            alert11.show();
            noAchat = (TextView) findViewById(R.id.textView4);
            if (listData.isEmpty()) {
                noAchat.setVisibility(View.VISIBLE);
            }
        }
    }


    @Override
    public void onDataUpdated() {

    }


    public  boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void registerToken(String token) {
        cv = new ContentValues();
        cv.put("token", token);

        HttpPostTask hp = new HttpPostTask(this, "Register/reg", cv);
        hp.execute();

    }

    @Override
    public void onExecSQLfinished(String jsondata, String scriptConstant) {
        Log.i("TEST LOG", "test");
        switch (scriptConstant) {
            case (ScriptValues.GET_ALL_ACHAT): {
                if (jsondata != null){
                    int id_achat;
                    String user;
                    String date;
                    JSONArray array = null;
                    listData.clear();
                    try{
                        array = new JSONArray(jsondata);
                        for (int i = 0; i< array.length(); i++){
                            JSONObject row = array.getJSONObject(i);
                            id_achat = row.getInt("id");
                            user = row.getString("nom") + " " + row.getString("prenom");
                            date = row.getString("date");
                            listData.add(new Achat(id_achat,user,date));
                        }
                    }catch (Exception e){

                    }
                    adapter = new AchatAdapter(listData, this);
                    recyclerView.setAdapter(adapter);

                    ItemTouchHelper itemTouchHelper = new ItemTouchHelper(createHelperCallBack());
                    itemTouchHelper.attachToRecyclerView(recyclerView);
                    adapter.setItemClickCallback(this);
                    adapter.notifyDataSetChanged();
                }

            }

            break;
            case ScriptValues.GET_ALL_ARTICLE_FROM_ACHAT: {
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
                            id_user = row.getInt("id_user");
                            prenom_user = row.getString("prenom");
                            nom_user = row.getString("nom");
                            id_articles = row.getInt("id_achat");
                            libelle_article = row.getString("libelle");
                            prix = row.getInt("prix");
                            quantite = row.getInt("quantite");
                            date = row.getString("date");

                            //this.listData.add(new Article(id_articles, id_user, nom_user, prenom_user, libelle_article, date, prix, quantite));

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }
        }
    }
}
