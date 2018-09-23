package com.android.parthjoshi.bakester.view.adapters;

import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.parthjoshi.bakester.R;
import com.android.parthjoshi.bakester.data.model.Recipe.Steps;
import com.android.parthjoshi.bakester.databinding.RecipeMasterItemBinding;

import java.util.List;

import javax.inject.Inject;

public class RecipeMasterAdapter extends RecyclerView.Adapter<RecipeMasterAdapter.MasterViewHolder> {

    private LayoutInflater inflater;
    private List<Steps> steps;
    private onRecipeStepClickListener listener;
    private boolean twoPane;
    private int selectedPosition = -1;

    @Inject
    public RecipeMasterAdapter(){}

    public void setData(List<Steps> steps, onRecipeStepClickListener listener, boolean twoPane){

        if(steps != null){
            this.steps = steps;
            notifyDataSetChanged();
        }

        this.listener = listener;
        this.twoPane = twoPane;
    }

    @NonNull
    @Override
    public MasterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(inflater == null)
            inflater = LayoutInflater.from(parent.getContext());

        RecipeMasterItemBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.recipe_master_item, parent, false);

        return new MasterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MasterViewHolder holder, int position) {
        String stepDescription = steps.get(position).getShortDescription();
        String stepItem;
        if(position == 0){
            stepItem = stepDescription;
        } else {
            String stepNumber = "Step " + String.valueOf(steps.get(position).getStepId());
            stepItem = stepNumber + ": " + stepDescription;
        }

        holder.binding.recipeMasterItem.setText(stepItem);
        if (twoPane) {
            if(selectedPosition == position)
                holder.binding.recipeMasterItem.setBackgroundColor(Color.parseColor("#FFCDD2"));
            else
                holder.binding.recipeMasterItem.setBackgroundColor(Color.parseColor("#BDBDBD"));
        }
    }

    @Override
    public int getItemCount() {
        if(steps == null) return 0;
        return steps.size();
    }

    public class MasterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        RecipeMasterItemBinding binding;

        public MasterViewHolder(RecipeMasterItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.recipeMasterItem.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onRecipeStepClick(steps.get(getAdapterPosition()), getAdapterPosition(), steps.size());
            selectedPosition = getAdapterPosition();
            notifyDataSetChanged();
        }
    }

    public interface onRecipeStepClickListener{
        void onRecipeStepClick(Steps step, int position, int count);
    }
}
