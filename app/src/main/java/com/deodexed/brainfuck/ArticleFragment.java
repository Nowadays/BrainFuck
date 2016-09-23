package com.deodexed.brainfuck;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import java.util.ArrayList;



public class ArticleFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String LIST_DATA = "LIST_DATA";


    private ArrayList<Article> listData;
    private FragmentItemClickCallBack callBack;
    private RecyclerView recyclerView;
    private ArticleAdapter adapter;


    public ArticleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment ArticleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArticleFragment newInstance(ArrayList<Article> listData) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(LIST_DATA, listData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.listData = getArguments().getParcelableArrayList(LIST_DATA);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v = inflater.inflate(R.layout.fragment_article, container, false);
        recyclerView = (RecyclerView)v.findViewById(R.id.rec_view_detail);

        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.listData = ((AchatDetailActivity)getActivity()).getListData();
         adapter = new ArticleAdapter(listData,getActivity());
        recyclerView.setAdapter(adapter);
        adapter.setItemClickCallBack(new ArticleAdapter.ItemClickCallBackArticle() {
            @Override
            public void onItemClick(int p) {
                callBack.onListItemClicked(p);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentItemClickCallBack) {
            callBack = (FragmentItemClickCallBack) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        callBack = null;
    }


    public interface FragmentItemClickCallBack{
        void onListItemClicked(int position);
    }

    public void setArticle(Article article, int position){
        listData.set(position,article);
        adapter.notifyItemChanged(position);

    }

    public void addArticle(Article article){
        this.listData.add(article);
        adapter.notifyDataSetChanged();
    }

    public boolean isDataListEmpty(){
        return this.listData.isEmpty();
    }

}
