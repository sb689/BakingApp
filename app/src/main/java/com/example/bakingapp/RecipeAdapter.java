package com.example.bakingapp;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.databinding.ReceipeCardItemBinding;
import com.example.bakingapp.model.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder>{

    private static final String TAG = RecipeAdapter.class.getSimpleName();
    private List<Recipe> mRecipeList;

    private RecipeAdapterClickListener mListener;

    public List<Recipe> getmRecipeList() {
        return mRecipeList;
    }

    public RecipeAdapter(RecipeAdapterClickListener listener){
        mListener = listener;
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


    class RecipeHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final ReceipeCardItemBinding mDataBinding;

        RecipeHolder(@NonNull ReceipeCardItemBinding itemBinding) {
            super(itemBinding.getRoot());
            mDataBinding = itemBinding;
            itemBinding.getRoot().setOnClickListener(this);
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