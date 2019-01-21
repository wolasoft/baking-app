package com.wolasoft.bakingapp.ui.adapters;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.wolasoft.bakingapp.R;

public class ImageBindingAdapter {

    @BindingAdapter({"app:imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        if (!imageUrl.equals("")) {
            Picasso.get()
                    .load(imageUrl)
                    .error(R.drawable.recipe)
                    .placeholder(R.drawable.recipe)
                    .into(view);
        } else {
            view.setImageDrawable(
                    view.getContext().getResources().getDrawable(R.drawable.art_work));

        }
    }
}
