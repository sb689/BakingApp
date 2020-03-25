package com.example.bakingapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bakingapp.databinding.StepItemBinding;
import com.example.bakingapp.model.Step;


public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder>{

    private static final String TAG = StepsAdapter.class.getName();
    private Step[] mSteps;
    private StepClickListener mStepListener;
    private Context mContext;

    public Step[] getmSteps() {
        return mSteps;
    }

    public void setmSteps(Step[] mSteps) {
        this.mSteps = mSteps;
        notifyDataSetChanged();
    }

    public StepsAdapter(Step[] steps, StepClickListener listener, Context context ) {
        mSteps = steps;
        mStepListener = listener;
        mContext = context;
    }

    public interface StepClickListener{
        void stepClicked(int position);
    }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        StepItemBinding dataBinding = StepItemBinding.inflate(inflater, parent, false);
        return new StepsViewHolder(dataBinding);

    }


    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder holder, int position) {
        holder.bindData(position);
    }


    @Override
    public int getItemCount() {
        if(mSteps == null){
            return  0;
        }else{
            return mSteps.length;
        }
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final StepItemBinding mDataBinding;

         StepsViewHolder(@NonNull StepItemBinding binding) {
            super(binding.getRoot());
            mDataBinding = binding;
            mDataBinding.tvStepShortDesc.setOnClickListener(this);
        }

        void bindData(int position){
             String text;
             if(position == 0){
                 text = mSteps[position].getDescription();
             }else {
                 text = mDataBinding.getRoot().getContext().getString(R.string.step_description, position, mSteps[position].getShortDescription());
             }
             mDataBinding.tvStepShortDesc.setText(text);
         }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mStepListener.stepClicked(position);
        }
    }
}
