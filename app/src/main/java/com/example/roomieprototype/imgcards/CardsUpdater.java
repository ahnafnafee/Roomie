package com.example.roomieprototype.imgcards;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.view.ViewCompat;

import com.ramotion.cardslider.DefaultViewUpdater;

public class CardsUpdater extends DefaultViewUpdater {

    @Override
    public void updateView(@NonNull View view, float position) {
        super.updateView(view, position);

        final CardView card = ((CardView) view);
        final View alphaView = card.getChildAt(1);
        final View imageView = card.getChildAt(0);

        if (position < 0) {
            final float alpha = ViewCompat.getAlpha(view);
            ViewCompat.setAlpha(view, 1f);
            ViewCompat.setAlpha(alphaView, 0.9f - alpha);
            ViewCompat.setAlpha(imageView, 0.3f + alpha);
        } else {
            ViewCompat.setAlpha(alphaView, 0f);
            ViewCompat.setAlpha(imageView, 1f);
        }

    }

}
