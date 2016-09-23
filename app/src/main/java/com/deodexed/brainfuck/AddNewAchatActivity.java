package com.deodexed.brainfuck;

import android.animation.Animator;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class AddNewAchatActivity extends AppCompatActivity {

    private RelativeLayout rootLayout;
    private float fab_postion_x;
    private float fab_postion_y;

    private Button btn_validate;
    private EditText et_libelle;
    private EditText et_prix;
    private Spinner spin_quantite;
    private EditText et_commentaire;

    private Integer[] items =  new Integer[]{1,2,3,4,5,6,7,8,9,10};

    private ArrayList<Achat> listData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_achat);

        Bundle extras = getIntent().getBundleExtra(MainActivity.BUNDLE_EXTRAS);
        if (extras != null){
            this.fab_postion_x = extras.getFloat(MainActivity.FAB_POSITION_X);
            this.fab_postion_y = extras.getFloat(MainActivity.FAB_POSITION_Y);
            listData = extras.getParcelableArrayList(MainActivity.LIST_ACHAT);
        }
                 rootLayout = (RelativeLayout)findViewById(R.id.root_layout);
        if (savedInstanceState == null) {
            rootLayout.setVisibility(View.INVISIBLE);

            ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        circularRevealActivity();
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            rootLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }
                });
            }
        }
        Spinner mspin = (Spinner)findViewById(R.id.spinner_add);


        ArrayAdapter<Integer> spinnerValue = new ArrayAdapter<Integer>(this,R.layout.support_simple_spinner_dropdown_item,items);
        mspin.setAdapter(spinnerValue);

        btn_validate = (Button)findViewById(R.id.btn_validate_add);
        et_commentaire = (EditText)findViewById(R.id.et_commentaire);
        et_libelle = (EditText)findViewById(R.id.et_libelle_add);
        et_prix = (EditText)findViewById(R.id.et_prix_add);
        spin_quantite = mspin;


    }
    private void circularRevealActivity() {

        int cx = rootLayout.getWidth() -50;
        int cy = rootLayout.getHeight();

        float finalRadius = Math.max(rootLayout.getWidth(), rootLayout.getHeight() +20);

        // create the animator for this view (the start radius is zero)
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, (int)fab_postion_x, (int)fab_postion_y, 0, finalRadius);
        circularReveal.setDuration(400);

        // make the view visible and start the animation
        rootLayout.setVisibility(View.VISIBLE);
        circularReveal.start();
    }

    public void onValidateBtnClick(View view){
        SimpleDateFormat sp = new SimpleDateFormat("YYYY-MM-dd HH:mm");
        Date today = new Date();
        String date = sp.format(today);

Bundle extras = new Bundle();
        extras.putString(MainActivity.EXTRA_DATE, date);
        extras.putInt(MainActivity.EXTRA_NOM_USER, 1);
        extras.putString(MainActivity.EXTRA_LIBELLE_NEW_ARTICLE, et_libelle.getText().toString());
        extras.putString(MainActivity.EXTRA_COMMENTAIRE, et_commentaire.getText().toString());
        extras.putInt(MainActivity.EXTRA_PRIX, Integer.parseInt(et_prix.getText().toString()));
        extras.putInt(MainActivity.EXTRA_QUANTITE, items[spin_quantite.getSelectedItemPosition()]);
        Intent resultIntent = new Intent();
        resultIntent.putExtra(MainActivity.BUNDLE_EXTRAS,extras);
        setResult(RESULT_OK,resultIntent);
        finish();
    }

}
