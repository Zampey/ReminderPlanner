package com.example.planner.schedule;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.planner.R;

public class DividerItemDecoration extends RecyclerView.ItemDecoration {
    private Drawable divider;

    // Konstruktor, kde načteme drawable resource pro čáru
    public DividerItemDecoration(Context context) {
        divider = context.getResources().getDrawable(R.drawable.divider);
    }

    @Override
    public void onDrawOver(@NonNull Canvas canvas, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
            int top = child.getBottom() + params.bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();

            // Pokud je položka poslední, nekreslíme čáru pod ní
            if (i < childCount - 1) {
                divider.setBounds(parent.getPaddingLeft(), top, parent.getWidth() - parent.getPaddingRight(), bottom);
                divider.draw(canvas);
            }
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.set(0, 0, 0, divider.getIntrinsicHeight());
    }
}
