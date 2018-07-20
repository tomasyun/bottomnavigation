package www.yuntdev.com.bottomnavigationlibrary.item;


import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

public abstract class BaseTabItem extends FrameLayout {
    public BaseTabItem(@NonNull Context context) {
        super(context);
    }

    public BaseTabItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseTabItem(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    abstract public void setChecked(boolean checked);

    abstract public String getTitle();

}
