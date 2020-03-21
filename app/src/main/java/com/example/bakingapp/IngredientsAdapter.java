package com.example.bakingapp;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bakingapp.databinding.IngredientsItemBinding;
import com.example.bakingapp.model.Ingredient;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientHolder>{

    private Ingredient[] mIngredients;

    public IngredientsAdapter(Ingredient[] mIngredients) {
        this.mIngredients = mIngredients;
    }

    public Ingredient[] getmIngredients() {
        return mIngredients;
    }

    public void setmIngredients(Ingredient[] mIngredients) {
        this.mIngredients = mIngredients;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        IngredientsItemBinding dataBinding = IngredientsItemBinding.inflate(inflater, parent, false);
        return new IngredientHolder(dataBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull IngredientHolder holder, int position) {
        holder.bindData(position);
    }

    @Override
    public int getItemCount() {
       if(mIngredients == null)
           return 0;
       else
           return mIngredients.length;
    }


    class IngredientHolder extends RecyclerView.ViewHolder{

        private final IngredientsItemBinding mBinding;

        public IngredientHolder(@NonNull IngredientsItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        void bindData(int position){
            Ingredient ingredient = mIngredients[position];
            mBinding.tvIngredientName.setText(mBinding.getRoot().getContext().getString(
                    R.string.ingredients_description,
                    ingredient.getIngredient(),
                    String.valueOf(ingredient.getQuantity()),
                    ingredient.getMeasure()));


        }
    }
}
