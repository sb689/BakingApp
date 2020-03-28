package com.example.bakingapp;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.bakingapp.databinding.FragmentMasterListBinding;
import com.example.bakingapp.model.Recipe;


/**
 * A simple {@link Fragment} subclass.
 */
public class MasterListFragment extends Fragment implements StepsAdapter.StepClickListener{

    private static final String TAG = MasterListFragment.class.getName();

    private Recipe mRecipe;
    private IngredientsClickedListener mIngredientListener;
    private StepClickedListenerForward mStepListenerForward;

    public Recipe getmRecipe() {
        return mRecipe;
    }

    public void setmRecipe(Recipe recipe) {
        this.mRecipe = recipe;
    }

    public MasterListFragment() {
        // Required empty public constructor
    }



    public interface IngredientsClickedListener{
        void ingredientsClicked();
    }

    public interface StepClickedListenerForward{
        void stepClickedForward(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{

            mIngredientListener = (IngredientsClickedListener) context;
            mStepListenerForward = (StepClickedListenerForward) context;

        }catch (ClassCastException ex){
            throw new ClassCastException(" Listeners not implemented");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        FragmentMasterListBinding binding = FragmentMasterListBinding.inflate(inflater, container, false);

        mRecipe = RecipeDetailActivity.getRecipe();

        StepsAdapter stepsAdapter = new StepsAdapter(mRecipe.getSteps(), this, getContext());

        binding.rvIngredientSteps.setAdapter(stepsAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        manager.setReverseLayout(false);
        manager.setOrientation(RecyclerView.VERTICAL);
        binding.rvIngredientSteps.setLayoutManager(manager);
        binding.rvIngredientSteps.setHasFixedSize(true);

        binding.tvIngredientsTitle.setText(getString(R.string.ingredients_title));
        binding.tvIngredientsTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIngredientListener.ingredientsClicked();
            }
        });

        stepsAdapter.setmSteps(mRecipe.getSteps());
        View rootView = binding.getRoot();
        return rootView;
    }

    @Override
    public void stepClicked(int position) {
        mStepListenerForward.stepClickedForward(position);
    }
}
