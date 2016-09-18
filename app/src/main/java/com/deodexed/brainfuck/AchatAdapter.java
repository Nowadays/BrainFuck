package com.deodexed.brainfuck;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Deode on 10/09/2016.
 */
public class AchatAdapter extends RecyclerView.Adapter<AchatAdapter.AchatHolder> {

    private static int i = 0;
    private List<Achat> listeAchat;
    private LayoutInflater layoutInflater;
    private ItemClickCallback itemClickCallback;

    public AchatAdapter(List<Achat> listeAchat, Context c) {
        this.listeAchat = listeAchat;
        layoutInflater = LayoutInflater.from(c);

    }

    public interface ItemClickCallback {
        void onItemClick(View v,int p);
        void onSecondaryIconClick(int p);
    }


    @Override
    public AchatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.achat_item_card, parent, false);
        return new AchatHolder(view);
    }

    @Override
    public void onBindViewHolder(AchatHolder holder, int position) {
        Achat item = listeAchat.get(position);
        holder.nom_user.setText(item.getUser());
        holder.date.setText(item.getDate());


    }

    @Override
    public int getItemCount() {
        return this.listeAchat.size();
    }

    public void setItemClickCallback(final ItemClickCallback itemClickCallback) {
        this.itemClickCallback = itemClickCallback;
    }

    class AchatHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView nom_user;
        private TextView date;
        private TextView prix;
        private ImageView image;
        private View container;

        @Override
        public void onClick(View view) {
            if (view.getId() == R.id.cont_item_root){
                itemClickCallback.onItemClick(view,getAdapterPosition());
            }
        }


        public AchatHolder(View itemView) {
            super(itemView);
            nom_user = (TextView) itemView.findViewById(R.id.lbl_nom);
            date = (TextView) itemView.findViewById(R.id.date_achat);
            image = (ImageView) itemView.findViewById(R.id.imageView);
            container = itemView.findViewById(R.id.cont_item_root);
            container.setOnClickListener(this);



        }
    }
}
