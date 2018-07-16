package www.yuntdev.com.library.referview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import java.util.ArrayList;
import java.util.List;

import www.yuntdev.com.library.R;
import www.yuntdev.com.library.controller.BottomLayoutController;
import www.yuntdev.com.library.controller.NavigationController;
import www.yuntdev.com.library.internal.CustomItemLayout;
import www.yuntdev.com.library.item.BaseTabItem;
import www.yuntdev.com.library.listener.OnTabItemSelectedListener;

public class PageBottomTabLayout extends ViewGroup {
    private int mTabPaddingTop;
    private int mTabPaddingBottom;
    private NavigationController mNavigationController;
    private ViewPagerPageChangeListener mPageChangeListener;
    private ViewPager mViewPager;
    private OnTabItemSelectedListener mTabItemListener = new OnTabItemSelectedListener() {
        @Override
        public void onSelected(int index, int old) {
            if (mViewPager != null) {
                mViewPager.setCurrentItem(index, false);
            }
        }

        @Override
        public void onRepeat(int index) {
        }
    };

    public PageBottomTabLayout(Context context) {
        this(context, null);
    }

    public PageBottomTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PageBottomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setPadding(0, 0, 0, 0);
        final TypedArray a = context.obtainStyledAttributes(
                attrs, R.styleable.PageBottomTabLayout);
        if (a.hasValue(R.styleable.PageBottomTabLayout_tabPaddingTop)) {
            mTabPaddingTop = a.getDimensionPixelSize(R.styleable.PageBottomTabLayout_tabPaddingTop, 0);
        }
        if (a.hasValue(R.styleable.PageBottomTabLayout_tabPaddingBottom)) {
            mTabPaddingBottom = a.getDimensionPixelSize(R.styleable.PageBottomTabLayout_tabPaddingBottom, 0);
        }
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int count = getChildCount();
        final int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(
                getMeasuredWidth(), MeasureSpec.EXACTLY);
        final int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(
                getMeasuredHeight(), MeasureSpec.EXACTLY);
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        final int width = r - l;
        final int height = b - t;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);
            if (child.getVisibility() == GONE) {
                continue;
            }
            child.layout(0, 0, width, height);
        }
    }

    public CustomBuilder custom() {
        return new CustomBuilder();
    }

    public class CustomBuilder {
        List<BaseTabItem> items;

        CustomBuilder() {
            items = new ArrayList<>();
        }

        public NavigationController build() {
            if (items.size() == 0) {
                return null;
            }
            CustomItemLayout customItemLayout = new CustomItemLayout(getContext());
            customItemLayout.initialize(items);
            customItemLayout.setPadding(0, mTabPaddingTop, 0, mTabPaddingBottom);
            PageBottomTabLayout.this.removeAllViews();
            PageBottomTabLayout.this.addView(customItemLayout);
            mNavigationController = new NavigationController(new Controller(), customItemLayout);
            mNavigationController.addTabItemSelectedListener(mTabItemListener);
            return mNavigationController;
        }

        public CustomBuilder addItem(BaseTabItem baseTabItem) {
            items.add(baseTabItem);
            return CustomBuilder.this;
        }
    }

    private class Controller implements BottomLayoutController {
        private ObjectAnimator animator;
        private boolean hide = false;

        @Override
        public void setupWithViewPager(ViewPager viewPager) {
            if (viewPager == null) {
                return;
            }
            mViewPager = viewPager;
            mViewPager.setOffscreenPageLimit(5);
            if (mPageChangeListener != null) {
                mViewPager.removeOnPageChangeListener(mPageChangeListener);
            } else {
                mPageChangeListener = new ViewPagerPageChangeListener();
            }
            if (mNavigationController != null) {
                int n = mViewPager.getCurrentItem();
                if (mNavigationController.getSelected() != n) {
                    mNavigationController.setSelect(n);
                }
                mViewPager.addOnPageChangeListener(mPageChangeListener);
            }
        }

        private ObjectAnimator getAnimator() {
            if (animator == null) {
                animator = ObjectAnimator.ofFloat(
                        PageBottomTabLayout.this, "translationY", 0, PageBottomTabLayout.this.getHeight());
                animator.setDuration(300);
                animator.setInterpolator(new AccelerateDecelerateInterpolator());
            }
            return animator;
        }
    }

    private class ViewPagerPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            if (mNavigationController != null && mNavigationController.getSelected() != position) {
                mNavigationController.setSelect(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }

    private static final String INSTANCE_STATUS = "INSTANCE_STATUS";
    private final String STATUS_SELECTED = "STATUS_SELECTED";

    @Override
    protected Parcelable onSaveInstanceState() {
        if (mNavigationController == null) {
            return super.onSaveInstanceState();
        }
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATUS, super.onSaveInstanceState());
        bundle.putInt(STATUS_SELECTED, mNavigationController.getSelected());
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            int selected = bundle.getInt(STATUS_SELECTED, 0);
            super.onRestoreInstanceState(bundle.getParcelable(INSTANCE_STATUS));

            if (selected != 0 && mNavigationController != null) {
                mNavigationController.setSelect(selected);
            }
            return;
        }
        super.onRestoreInstanceState(state);
    }
}
