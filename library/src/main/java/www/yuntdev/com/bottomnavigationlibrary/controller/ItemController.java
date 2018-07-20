package www.yuntdev.com.bottomnavigationlibrary.controller;

import www.yuntdev.com.bottomnavigationlibrary.listener.OnTabItemSelectedListener;

public interface ItemController {
    void setSelect(int index);

    void addTabItemSelectedListener(OnTabItemSelectedListener listener);

    int getSelected();

    int getItemCount();

    String getItemTitle(int index);
}
