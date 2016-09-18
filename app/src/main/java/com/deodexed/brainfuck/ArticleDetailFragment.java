package com.deodexed.brainfuck;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArticleDetailFragment extends Fragment {
    private static final String ARTICLE = "ARTICLE";
    private Article arguments;
    private  int id_article, id_achat;
    private  String nom_user;

    private FragmentEditItemCallBack callback;

    private EditText libelle_detail, quantite_detail, prix_detail;
    private Button validate;

    private View framgmentView;

    public ArticleDetailFragment() {
        // Required empty public constructor
    }

    public static ArticleDetailFragment newInstance(Article article){

        ArticleDetailFragment articleDetailFragment = new ArticleDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARTICLE,article);
        articleDetailFragment.setArguments(args);
        return articleDetailFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        if(getArguments() != null){
         this.arguments = getArguments().getParcelable(ARTICLE);
        }

    }

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

         framgmentView = inflater.inflate(R.layout.fragment_article_detail, container, false);


        libelle_detail = (EditText)framgmentView.findViewById(R.id.et_libelle_detail);
        quantite_detail = (EditText)framgmentView.findViewById(R.id.et_quantite_detail);
        prix_detail = (EditText)framgmentView.findViewById(R.id.et_prix_detail);
        validate = (Button)framgmentView.findViewById(R.id.btn_validate);
        validate.setOnClickListener(onClickListener);

        return framgmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentEditItemCallBack){
            callback = (FragmentEditItemCallBack)context;
        }else{
            throw new RuntimeException(context.toString() + "must implement callBack");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callback = null;
    }


    public interface FragmentEditItemCallBack{
        void onEditButtonClick(Article article);
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Log.i("TEST", "Prix : " + libelle_detail.getText().toString());
            prix_detail = (EditText)framgmentView.findViewById(R.id.et_prix_detail);
            libelle_detail = (EditText)framgmentView.findViewById(R.id.et_libelle_detail);
            quantite_detail = (EditText)framgmentView.findViewById(R.id.et_quantite_detail);
            callback.onEditButtonClick(new Article(id_article, id_achat, nom_user,libelle_detail.getText().toString(), Integer.parseInt(prix_detail.getText().toString()),Integer.parseInt(quantite_detail.getText().toString()) ));
        }
    };

    @Override
    public void onResume() {
        super.onResume();
        if (arguments != null){
            libelle_detail.setText(arguments.getLibelle_article());
            quantite_detail.setText(Integer.toString(arguments.getQuantite_article()));
            prix_detail.setText(Integer.toString(arguments.getPrix_article()));

        }
    }

    public void setEditText(String libelle, String quantite, String prix){
        libelle_detail.setText(libelle);
        quantite_detail.setText(quantite);
        prix_detail.setText(prix);
    }

    public  void setId(int id, int id_a,String nom_user){
        id_article = id;
        id_achat = id_a;
        this.nom_user = nom_user;
    }
}
