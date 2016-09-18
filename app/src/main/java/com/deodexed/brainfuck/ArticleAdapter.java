package com.deodexed.brainfuck;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Deode on 15/09/2016.
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleHolder> {

    private List<Article> listArticle;
    private LayoutInflater layoutInflater;
    private ItemClickCallBackArticle callBackArticle;


    public ArticleAdapter(List<Article> listArticle, Context c) {
        this.listArticle = listArticle;
        this.layoutInflater = LayoutInflater.from(c);
    }

    @Override
    public ArticleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.article_item,parent,false);
        return new ArticleHolder(v);
    }

    @Override
    public void onBindViewHolder(ArticleHolder holder, int position) {
        Article item = listArticle.get(position);
        holder.libelle_article.setText(item.getLibelle_article());
        holder.quantite_article.setText(Integer.toString(item.getQuantite_article()));
        holder.prix_article.setText(Integer.toString(item.getPrix_article()));
    }

    @Override
    public int getItemCount() {
        return this.listArticle.size();
    }

    class ArticleHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView libelle_article;
        private TextView quantite_article;
        private TextView prix_article;
        private View container;

        public ArticleHolder(View v){
            super(v);
            this.libelle_article = (TextView)v.findViewById(R.id.lbl_libelle_article);
            this.quantite_article = (TextView)v.findViewById(R.id.quantite_article);
            this.prix_article = (TextView)v.findViewById(R.id.prix_article);
            this.container = (View)v.findViewById(R.id.article_root_container);
            this.container.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            callBackArticle.onItemClick(getAdapterPosition());
        }
    }

    public interface ItemClickCallBackArticle{
        void onItemClick(int p);

    }

    public void setItemClickCallBack(ItemClickCallBackArticle itemClickCallBack){
        this.callBackArticle = itemClickCallBack;
    }

}

