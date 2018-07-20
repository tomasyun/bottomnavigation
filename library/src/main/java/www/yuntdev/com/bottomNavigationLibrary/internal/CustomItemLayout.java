package www.yuntdev.com.bottomNavigationLibrary.internal;


import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import www.yuntdev.com.bottomNavigationLibrary.R;
import www.yuntdev.com.bottomNavigationLibrary.controller.ItemController;
import www.yuntdev.com.bottomNavigationLibrary.item.BaseTabItem;
import www.yuntdev.com.bottomNavigationLibrary.listener.OnTabItemSelectedListener;

public class CustomItemLayout extends ViewGroup implements ItemController {
    private final int BOTTOM_NAVIGATION_ITEM_HEIGHT;
    private List<BaseTabItem> mItems;
    private List<OnTabItemSelectedListener> mListeners = new ArrayList<>();
    private int mSelected = -1;

    public CustomItemLayout(Context context) {
        this(context, null);
    }

    public CustomItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        BOTTOM_NAVIGATION_ITEM_HEIGHT = getResources().getDimensionPixelSize(R.dimen.material_bottom_navigation_height);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int n = getChildCount();
        int childWidth = getMeasuredWidth() / n;
        final int heightSpec = MeasureSpec.makeMeasureSpec(BOTTOM_NAVIGATION_ITEM_HEIGHT, MeasureSpec.EXACTLY);
        final int widthSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
        for (int i = 0; i < n; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            child.measure(widthSpec, heightSpec);
            child.getLayoutParams().width = child.getMeasuredWidth();
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        final int count = getChildCount();
        final int width = right - left;
        final int height = bottom - top;
        final int padding_top = getPaddingTop();
        final int padding_bottom = getPaddingBottom();
        int used = 0;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            if (ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                child.layout(width - used - child.getMeasuredWidth(), padding_top, width - used, height - padding_bottom);
            } else {
                child.layout(used, padding_top, child.getMeasuredWidth() + used, height - padding_bottom);
            }
            used += child.getMeasuredWidth();
        }
    }

    public void initialize(List<BaseTabItem> items) {
        mItems = items;
        int n = mItems.size();
        for (int i = 0; i < n; i++) {
            BaseTabItem v = mItems.get(i);
            v.setChecked(false);
            this.addView(v);

            final int finali = i;
            v.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    setSelect(finali);
                }
            });
        }
        mSelected = 0;
        mItems.get(0).setChecked(true);
    }

    @Override
    public void setSelect(int index) {
        if (index == mSelected) {
            for (OnTabItemSelectedListener listener : mListeners) {
                listener.onRepeat(mSelected);
            }
            return;
        }
        int oldSelected = mSelected;
        mSelected = index;

        if (oldSelected >= 0) {
            mItems.get(oldSelected).setChecked(false);
        }
        mItems.get(mSelected).setChecked(true);
        for (OnTabItemSelectedListener listener : mListeners) {
            listener.onSelected(mSelected, oldSelected);
        }
    }

    @Override
    public void addTabItemSelectedListener(OnTabItemSelectedListener listener) {
        mListeners.add(listener);
    }

    @Override
    public int getSelected() {
        return mSelected;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public String getItemTitle(int index) {
        return mItems.get(index).getTitle();
    }


}
