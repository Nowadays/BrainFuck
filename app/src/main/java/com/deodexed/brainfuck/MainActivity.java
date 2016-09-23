package com.deodexed.brainfuck;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements AchatAdapter.ItemClickCallback, RemoteDatabaseListener, PostResultListener {


    public static final String EXTRA_NOM_USER = "EXTRA_NOM_USER";
    public static final String EXTRA_DATE = "EXTRA_DATE";
    public static final String EXTRA_PRIX = "EXTRA_PRIX";
    public static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    public static final String EXTRA_ID_ACHAT = "EXTRA_ID_ACHAT";
    public static final String EXTRA_LIBELLE_NEW_ARTICLE = "EXTRA_LIBELLE_NEW_ARTICLE";
    public static final String EXTRA_COMMENTAIRE = "EXTRA_COMMENTAIRE";
    public static final String EXTRA_QUANTITE = "EXTRA_QUANTITE";


    public static final String EXTRA_IMAGE_RES_ID = "EXTRA_IMAGE_RES_ID";
    public static final String ADD_TOKEN_TO_DATABASE = "Register/reg";
    public static final String GET_ACHAT = "BrainFuckApp/";
    public static final String FAB_POSITION_X = "FAB_POSITION_X";
    public static final String FAB_POSITION_Y = "FAB_POSTION_Y";
    public static final String LIST_ACHAT = "LIST_ACHAT";

    static final int ADD_NEW_ACHAT_REQUEST = 1;  // The request code


    private RecyclerView recyclerView;
    private AchatAdapter adapter;
    private TextView noAchat;
    private HttpGetTask hgt;
    private ContentValues cv;
    private FloatingActionButton fab;
    private RelativeLayout yourParentView;

    private ArrayList<Achat> listData;

    private ProgressDialog pd;

    private static AnimationSet getAnimation(int x, int y) {

        AnimationSet animSet = new AnimationSet(false);
        animSet.addAnimation(new TranslateAnimation(0, 0, x, y));
        animSet.setDuration(1100);
        return animSet;
    }

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

        noAchat = (TextView)findViewById(R.id.no_achat);

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
                Toast.makeText(this, "Exception :" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
            noAchat = (TextView) findViewById(R.id.no_achat);

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

        yourParentView = (RelativeLayout) findViewById(R.id.root_layout_mainActivity);


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

        new AlertDialog.Builder(this)
                .setTitle("Confirmation de supression")
                .setMessage("Voulez vous vraiment supprimer cette achat ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ContentValues cv = new ContentValues();
                                cv.put("id_achat", listData.get(position).getId_achat());
                                CoordinatorLayout coord = (CoordinatorLayout) findViewById(R.id.coord_layout);
                                HttpPostTask hp = new HttpPostTask(MainActivity.this, "BrainFuckApp/deleteAchat", cv);
                                hp.execute();
                                listData.remove(position);
                                adapter.notifyItemRemoved(position);
                                Snackbar sb = Snackbar.make(coord, "Achat supprimé !", Snackbar.LENGTH_SHORT);
                                View snackBarView = sb.getView();
                                snackBarView.setBackgroundColor(getColor(R.color.colorAccent));
                                sb.show();

                            }
                        }
                )
                .setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .show();


    }

    public void onFabClick(View v) {


        SimpleDateFormat sp = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        Date today = new Date();
        String date = sp.format(today);

        String commentaire = "";

        ContentValues cv = new ContentValues();
        cv.put("id_user", 1);
        cv.put("date", date);
        cv.put("commentaire", commentaire);
        HttpPostTask hp = new HttpPostTask(this, "BrainFuckApp/addAchat", cv);
        hp.execute();


        /*Bundle extras = new Bundle();
        extras.putFloat(FAB_POSITION_X, fab.getX());
        extras.putFloat(FAB_POSITION_Y, fab.getY());
        extras.putParcelableArrayList(LIST_ACHAT, listData);
        Intent intent = new Intent(MainActivity.this, AddNewAchatActivity.class);
        intent.putExtra(BUNDLE_EXTRAS, extras);
        startActivityForResult(intent, ADD_NEW_ACHAT_REQUEST);  */


    }


    @Override
    public void onDataUpdated() {

    }


    public boolean isNetworkAvailable() {
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
                if (jsondata != null) {
                    int id_achat;
                    String user;
                    String date;
                    String commentaire;
                    JSONArray array = null;
                    listData.clear();
                    try {
                        array = new JSONArray(jsondata);
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject row = array.getJSONObject(i);
                            id_achat = row.getInt("id");
                            user = row.getString("nom") + " " + row.getString("prenom");
                            date = row.getString("date");
                            commentaire = row.getString("commentaire");
                            listData.add(new Achat(id_achat, user, date, commentaire));
                        }
                    } catch (Exception e) {

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK: {
                if (data != null) {
                    Bundle extras = data.getBundleExtra(BUNDLE_EXTRAS);

                    ContentValues cv = new ContentValues();
                    cv.put("id_user", extras.getInt(EXTRA_NOM_USER));
                    cv.put("date", extras.getString(EXTRA_DATE));
                    cv.put("libelle", extras.getString(EXTRA_LIBELLE_NEW_ARTICLE));
                    cv.put("prix", extras.getInt(EXTRA_PRIX));
                    cv.put("quantite", extras.getInt(EXTRA_QUANTITE));
                    cv.put("commentaire", extras.getString(EXTRA_COMMENTAIRE));
                    HttpPostTask hp = new HttpPostTask(this, "BrainFuckApp/addArticle", cv);
                    hp.execute();
                }


            }
            break;
        }


    }

    @Override
    public void onPostRequestFinished(String jsondata) {
        JSONArray array = null;
        JSONObject jsonObject = null;
        try {
            JSONObject row = new JSONObject(jsondata);
            Achat achat = new Achat(row.getInt("id"), row.getString("nom") + " " + row.getString("prenom"), row.getString("date"), row.getString("commentaire"));
            this.listData.add(0, achat);
            adapter.notifyItemInserted(0);
            CoordinatorLayout coord = (CoordinatorLayout) findViewById(R.id.coord_layout);
            Snackbar sb = Snackbar.make(coord, "Nouvel achat créé", Snackbar.LENGTH_INDEFINITE);
            View v = sb.getView();
            v.setBackgroundColor(getColor(R.color.colorAccent));

            sb.setAction("Voir", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scrollMyListViewToBottom();
                }
            });
            sb.setActionTextColor(getColor(R.color.colorPrimaryDark));
            sb.show();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //this.listData.add(new Article(id_articles, id_user, nom_user, prenom_user, libelle_article, date, prix, quantite));


    }

    private void scrollMyListViewToBottom() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                recyclerView.scrollToPosition(0);
            }
        });
    }
}
