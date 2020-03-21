package com.example.bakingapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.databinding.FragmentIngredientsBinding;
import com.example.bakingapp.model.Ingredient;

public class IngredientsFragment extends Fragment {

    private FragmentIngredientsBinding mDataBinding;
    private Ingredient[] mIngredient;

    public void setmIngredient(Ingredient[] mIngredient) {
        this.mIngredient = mIngredient;
    }

    public IngredientsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mDataBinding = FragmentIngredientsBinding.inflate(inflater,container,false);
        mIngredient = RecipeDetailActivity.getRecipe().getIngredients();

        IngredientsAdapter adapter = new IngredientsAdapter(mIngredient);
        mDataBinding.rvIngredientDetail.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        manager.setReverseLayout(false);
        mDataBinding.rvIngredientDetail.setLayoutManager(manager);
        mDataBinding.rvIngredientDetail.setHasFixedSize(true);

        View rootView = mDataBinding.getRoot();

        return rootView;
    }
}
