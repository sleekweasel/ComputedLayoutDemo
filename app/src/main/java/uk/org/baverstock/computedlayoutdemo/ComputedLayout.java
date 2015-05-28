package uk.org.baverstock.computedlayoutdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

public abstract class ComputedLayout extends ViewGroup {
    public ComputedLayout(Context context) {
        super(context);
    }

    public ComputedLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ComputedLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    Map<Integer, View> resourceToView = new HashMap<Integer, View>();

    protected static int measuredHeights(View... view) {
        int height = view[0].getMeasuredHeight();
        for (int ix = 1; ix < view.length; ++ix) {
            height += view[ix].getMeasuredHeight();
        }
        return height;
    }

    protected static int measuredWidths(View... view) {
        int width = view[0].getMeasuredWidth();
        for (int ix = 1; ix < view.length; ++ix) {
            width += view[ix].getMeasuredWidth();
        }
        return width;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int childCount = getChildCount();
        for (int ix = 0; ix < childCount; ix++) {
            View view = getChildAt(ix);
            resourceToView.put(view.getId(), view);
        }
    }

    public static int unspecified() {
        return MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
    }

    public static int exactly(int dimension) {
        return MeasureSpec.makeMeasureSpec(dimension, MeasureSpec.EXACTLY);
    }

    public static int atMost(int dimension) {
        return MeasureSpec.makeMeasureSpec(dimension, MeasureSpec.AT_MOST);
    }

    protected void addViews(View... views) {
        for (View view : views) {
            addView(view);
        }
    }

    public static class Placer {
        private static Placer PLACER = new Placer();
        private View view;
        private int left;
        private int top;
        private int width;
        private int height;

        public static Placer place(View view) {
            if (PLACER.view != null) {
                throw new RuntimeException("Previous view unplaced: " + PLACER.view);
            }
            PLACER.view = view;
            PLACER.top = 0;
            PLACER.left = 0;
            PLACER.width = view.getMeasuredWidth();
            PLACER.height = view.getMeasuredHeight();
            return PLACER;
        }

        public void layout() {
            view.layout(left, top, left + width, top + height);
            view = null;
        }

        public Placer insideRight(View view) {
            left = view.getRight() - width;
            return this;
        }

        public Placer insideLeft(View view) {
            left = view.getLeft();
            return this;
        }

        public Placer insideTop(View view) {
            top = view.getTop();
            return this;
        }

        public Placer insideBottom(View view) {
            top = view.getBottom() - height;
            return this;
        }

        public Placer below(View view) {
            top = view.getBottom();
            return this;
        }

        public Placer above(View view) {
            top = view.getTop() - height;
            return this;
        }

        public Placer leftOf(View view) {
            left = view.getLeft() - width;
            return this;
        }

        public Placer rightOf(View view) {
            left = view.getRight();
            return this;
        }

        public Placer centreHorizontal(View view) {
            left = (view.getLeft() + view.getRight() - width)/2;
            return this;
        }

        public Placer centreVertical(View view) {
            top = (view.getTop() + view.getBottom() - height)/2;
            return this;
        }

        public Placer topAt(int c) {
            top = c;
            return this;
        }
    }
}
