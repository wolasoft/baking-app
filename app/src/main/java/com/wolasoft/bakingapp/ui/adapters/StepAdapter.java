package com.wolasoft.bakingapp.ui.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wolasoft.bakingapp.R;
import com.wolasoft.bakingapp.data.models.Step;
import com.wolasoft.bakingapp.databinding.StepListItemBinding;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.ViewHolder> {

    private final List<Step> steps;
    private final OnStepClickedListener listener;

    public StepAdapter(List<Step> steps, OnStepClickedListener listener) {
        this.steps = steps;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        StepListItemBinding dataBinding = DataBindingUtil.inflate(inflater, R.layout.step_list_item, viewGroup, false);
        return new ViewHolder(dataBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Step step = this.steps.get(position);
        viewHolder.bind(step);
    }

    @Override
    public int getItemCount() {
        return this.steps == null ? 0 : this.steps.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final StepListItemBinding dataBinding;

        ViewHolder(@NonNull StepListItemBinding dataBinding) {
            super(dataBinding.getRoot());
            this.dataBinding = dataBinding;

            itemView.setOnClickListener(this);
        }

        void bind(Step step) {
            dataBinding.setStep(step);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Step step = steps.get(position);
            listener.stepClicked(step);
        }
    }

    public interface OnStepClickedListener {
        void stepClicked(Step step);
    }
}
