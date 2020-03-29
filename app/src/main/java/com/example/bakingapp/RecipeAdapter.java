package com.example.bakingapp;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.databinding.ReceipeCardItemBinding;
import com.example.bakingapp.model.Recipe;
import com.example.bakingapp.utiils.DisplayUtils;


import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder>{

    private static final String TAG = RecipeAdapter.class.getSimpleName();
    private static final int CARD_HEIGHT_TABLET = 350;

    private List<Recipe> mRecipeList;
    private RecipeAdapterClickListener mListener;
    private boolean mTabletView;
    private Activity mActivity;

    public List<Recipe> getmRecipeList() {
        return mRecipeList;
    }

    public RecipeAdapter(RecipeAdapterClickListener listener, boolean isTabletView, Activity activity){
        mListener = listener;
        mTabletView = isTabletView;
        mActivity = activity;
    }

    public void setmRecipeList(List<Recipe> mRecipeList) {
        this.mRecipeList = mRecipeList;
        notifyDataSetChanged();
    }

    public interface RecipeAdapterClickListener{
        void RecipeClicked(int position);
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ReceipeCardItemBinding itemBinding = ReceipeCardItemBinding.inflate(layoutInflater,parent,false);
        return new RecipeHolder(itemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {

        if(mRecipeList == null)
            return 0;
        else
            return  mRecipeList.size();
    }

    private int calculateCardWidth(){
        DisplayUtils.getScreenSize(mActivity);
        int cardWidth = DisplayUtils.mScreenWidth / MainActivity.GRID_SPAN_COUNT;
        return cardWidth;
    }


    class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final ReceipeCardItemBinding mDataBinding;

        RecipeHolder(@NonNull ReceipeCardItemBinding itemBinding) {
            super(itemBinding.getRoot());
            mDataBinding = itemBinding;
            itemBinding.getRoot().setOnClickListener(this);
            if(mTabletView){
                mDataBinding.cardViewItem.getLayoutParams().width = calculateCardWidth();
                mDataBinding.cardViewItem.getLayoutParams().height = CARD_HEIGHT_TABLET;
            }
        }

        void bindData(final int position){
            Recipe recipe = mRecipeList.get(position);
            mDataBinding.cvTvRecipeName.setText(recipe.getName());
            mDataBinding.cvTvServings.setText(  String.valueOf(recipe.getServings()));
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mListener.RecipeClicked(position);
        }
    }
}
