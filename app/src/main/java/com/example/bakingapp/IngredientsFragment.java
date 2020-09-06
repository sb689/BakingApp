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
import com.example.bakingapp.model.Recipe;

public class IngredientsFragment extends Fragment {

    private static final String RECIPE_KEY = "recipe_key";
    private FragmentIngredientsBinding mDataBinding;

    public IngredientsFragment() {

    }

    public static IngredientsFragment newInstance(Recipe recipe){
        IngredientsFragment fragment = new IngredientsFragment();
        Bundle args = new Bundle();
        args.putParcelable(RECIPE_KEY, recipe);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mDataBinding = FragmentIngredientsBinding.inflate(inflater,container,false);
        Ingredient[] ingredients = null;
        if(getArguments().containsKey(RECIPE_KEY))
        {
            Recipe recipe = getArguments().getParcelable(RECIPE_KEY);
            ingredients = recipe.getIngredients();
        }
        IngredientsAdapter adapter = new IngredientsAdapter(ingredients);
        mDataBinding.rvIngredientDetail.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setOrientation(RecyclerView.VERTICAL);
        manager.setReverseLayout(false);
        mDataBinding.rvIngredientDetail.setLayoutManager(manager);
        mDataBinding.rvIngredientDetail.setHasFixedSize(true);

       return mDataBinding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDataBinding.rvIngredientDetail.setAdapter(null);
        mDataBinding = null;

    }

}
