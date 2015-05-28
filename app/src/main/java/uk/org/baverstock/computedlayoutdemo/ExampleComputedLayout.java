package uk.org.baverstock.computedlayoutdemo;

import android.graphics.Color;
import android.widget.AnalogClock;
import android.widget.ImageView;
import android.widget.TextView;

import static uk.org.baverstock.computedlayoutdemo.ComputedLayout.Placer.place;

public class ExampleComputedLayout extends ComputedLayout {

    private final TextView textView;
    private final ImageView imageView;
    private final AnalogClock analogClock;

    public ExampleComputedLayout(DemoActivity context) {
        super(context);

        textView = new TextView(context);
        textView.setText("Your guess is\nas good as mine");
        textView.setBackgroundColor(Color.YELLOW);

        imageView = new ImageView(context);
        imageView.setImageResource(R.drawable.ic_launcher);

        analogClock = new AnalogClock(context);
        analogClock.setBackgroundColor(Color.CYAN);

        addViews(textView, imageView, analogClock);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        measureChild(textView, exactly(getMeasuredWidth()), unspecified());
        measureChild(imageView, unspecified(), exactly(textView.getMeasuredHeight()));
        measureChild(analogClock, exactly(textView.getMeasuredHeight()), exactly(textView.getMeasuredHeight()));

        setMeasuredDimension(getMeasuredWidth(), measuredHeights(textView, analogClock));
    }

    @Override
    public void onLayout(boolean viewGroupChanged, int l, int u, int r, int b) {
        place(textView).layout();
        place(imageView).insideRight(textView).insideTop(textView).layout();
        place(analogClock).below(textView).centreHorizontal(textView).layout();
    }
}
