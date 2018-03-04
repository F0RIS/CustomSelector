package customdropdownselector.f0ris.com.customdropdownselector;

import android.animation.IntEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;

public class CustomSelector extends LinearLayout {

    private TextView tvTitle;
    private ImageView trianglePointer;
    private LinearLayout itemsContainer;
    private boolean itemsVisibility = false;
    private boolean firstShow = true;

    //    private LabelAdapter adapter;
    private OnClickListener clickListener;
    private ArrayList<String> arrayList = new ArrayList<>();


    private static final Interpolator interpolator = new OvershootInterpolator();
    private static final int ANIMATION_DURATION = 250; //ms
    private ViewGroup.LayoutParams containerParams;
//    private String title = "";
//    private BaseAdapter adapter;

    public interface ItemClickAction {
        void onAction(Object object, int position);
    }

    public CustomSelector(Context context) {
        this(context, null);
    }

    public CustomSelector(final Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.chart_inteval_layout, this, true);
        itemsContainer = (LinearLayout) findViewById(R.id.items_container);
        containerParams = itemsContainer.getLayoutParams();
        containerParams.height = 0;

        tvTitle = (TextView) findViewById(R.id.tv_duration);

        tvTitle.setBackgroundResource(R.drawable.selector_title_bg_open);
        tvTitle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switchSelectorsVisibility();
            }
        });

        trianglePointer = (ImageView) findViewById(R.id.triangle_pointer);
    }

    public CustomSelector(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    private void addItems() {
//        Context context = getContext();
//
//        arrayList = new ArrayList<>();
//        arrayList.add("Tick");
//        arrayList.add("1m");
//        arrayList.add("5m");
//        arrayList.add("1h");
//        arrayList.add("1d");
//
//        itemsContainer.removeAllViews();
//
//        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
//                LayoutParams.MATCH_PARENT,
//                LayoutParams.MATCH_PARENT, 1.0f); //create items with same weight, need for overstretching
//
//        for (int i = 0; i < arrayList.size(); i++) {
//            View view = View.inflate(context, R.layout.item_chart_interval, null);
//            view.setLayoutParams(param);
//
//            String label = arrayList.get(i);
//            ((TextView) view.findViewById(R.id.text)).setText(label);
//            view.setTag(i);
//            view.setOnClickListener(clickListener);
//            itemsContainer.addView(view);
//        }
//        firstShow = false;

        Context context = (Context) getContext();
        itemsContainer.removeAllViews();

        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT, 1.0f); //create items with same weight, need for overstretching


        for (int i = 0; i < arrayList.size(); i++) {
            View view = View.inflate(context, R.layout.item_chart_interval, null);
            view.setLayoutParams(param);
//
//            if (i == adapter.getCount() - 1) {
//                ((FrameLayout) view).getChildAt(0).setBackgroundResource(R.drawable.game_tab_bottom_empty);
//            }
//            String label = ((ILabelForList) adapter.getItem(i)).getLabel(context.getOpteckString(R.string.label_minutes));
            String label = arrayList.get(i);
            ((TextView) view.findViewById(R.id.text)).setText(label);
            view.setTag(i);
            view.setOnClickListener(clickListener);
            itemsContainer.addView(view);
        }
        firstShow = false;
    }

    public void setValues(String[] items) {
        arrayList.clear();
        arrayList.addAll(Arrays.asList(items));
    }

    public void setValues(ArrayList<String> items) {
        arrayList.clear();
        arrayList.addAll(items);
    }

//    public void setActionListener() {
//        clickListener = new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                action.onAction((ILabelForList) adapter.getItem((Integer) v.getTag()));
//                tvTitle.setText(arrayList.get((Integer) v.getTag()));
//                switchSelectorsVisibility();
//            }
//        };
//    }

    public void setActionListener(final ItemClickAction action) {
        clickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer pos = (Integer) v.getTag();
                tvTitle.setText(arrayList.get((Integer) v.getTag()));
                switchSelectorsVisibility();
                action.onAction(arrayList.get(pos), pos);
            }
        };

    }

    private void switchSelectorsVisibility() {
        if (firstShow) {
//            setActionListener();
            addItems();
        }

        itemsVisibility = !itemsVisibility;

        int initHeight = 0;
        int finalHeight = (int) getResources().getDimension(R.dimen.selector_item_height) * arrayList.size();

        if (!itemsVisibility) {
            if (containerParams.height == 0){ //already closed (click outside selector)
                return;
            }
            initHeight = finalHeight; //reversing animation
            finalHeight = 0;
        }

        ValueAnimator heightAnim = ValueAnimator.ofObject(new IntEvaluator(), initHeight, finalHeight);
        heightAnim.setDuration(ANIMATION_DURATION);
        heightAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int animatedValue = (int) animation.getAnimatedValue();

                containerParams.height = animatedValue;
                itemsContainer.requestLayout();

                if (animatedValue == 0 && !itemsVisibility){ //set bg with rounding in the end of closing animation
                    tvTitle.setBackgroundResource(R.drawable.selector_title_bg_open);
                    trianglePointer.setVisibility(View.VISIBLE);
                }
//                System.out.println("value: " + animation.getAnimatedValue());
            }
        });

        if (itemsVisibility) {//apply interpolator only for opening to avoid negative values during closing animation
            heightAnim.setInterpolator(interpolator);
            tvTitle.setBackgroundResource(R.drawable.selector_title_bg);
            trianglePointer.setVisibility(View.INVISIBLE);
        }
        heightAnim.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (isOutOfBounds(event)) {
            this.hide();
        }
        return super.onTouchEvent(event);
    }

    private boolean isOutOfBounds(MotionEvent event) {
        Rect viewRect = new Rect();
        getGlobalVisibleRect(viewRect);
        return !viewRect.contains((int) event.getRawX(), (int) event.getRawY());
    }

    public void hide() {
        itemsVisibility = true;
        switchSelectorsVisibility();
    }

    public void setTitle(String title) {
//        this.title = title;
        tvTitle.setText(title);
    }
}
