package com.wolasoft.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wolasoft.bakingapp.R;
import com.wolasoft.bakingapp.data.models.Step;

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

        View view = inflater.inflate(R.layout.step_list_item, viewGroup, false);
        return new ViewHolder(view);
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

        private final Context context;
        private final ImageView thumbnail;
        private final TextView shortDescriptionTV;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            thumbnail = itemView.findViewById(R.id.thumbnail);
            shortDescriptionTV = itemView.findViewById(R.id.shortDescriptionTV);

            itemView.setOnClickListener(this);
        }

        void bind(Step step) {
            if (step.getThumbnailURL().isEmpty()) {
                thumbnail.setImageDrawable(
                        this.context.getResources().getDrawable(R.drawable.step));
            } else {
                Picasso.get()
                        .load(step.getThumbnailURL())
                        .error(R.drawable.recipe)
                        .placeholder(R.drawable.recipe)
                        .into(thumbnail);
            }
            this.shortDescriptionTV.setText(step.getShortDescription());
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
