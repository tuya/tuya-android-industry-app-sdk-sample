package com.tuya.dev.iotos.view;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Custom Divider Item Decoration which disable last divider
 *
 * @author 乾启 <a href="mailto:sunrw@tuya.com">Contact me.</a>
 * @since 2021/5/6 10:53 AM
 */
public class DividerDecoration extends RecyclerView.ItemDecoration {
    private Drawable mDivider;

    public DividerDecoration(Drawable divider) {
        mDivider = divider;
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
        int dividerLeft = parent.getPaddingLeft();
        int dividerRight = parent.getWidth() - parent.getPaddingRight();

        int childCount = parent.getChildCount();
        for (int i = 0; i <= childCount - 2; i++) {
            View child = parent.getChildAt(i);

            RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();

            int dividerTop = child.getBottom() + params.bottomMargin;
            int dividerBottom = dividerTop + mDivider.getIntrinsicHeight();

            mDivider.setBounds(dividerLeft, dividerTop, dividerRight, dividerBottom);
            mDivider.draw(canvas);
        }
    }
}

