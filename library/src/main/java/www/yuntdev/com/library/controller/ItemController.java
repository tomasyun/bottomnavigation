package www.yuntdev.com.library.controller;

import www.yuntdev.com.library.listener.OnTabItemSelectedListener;

public interface ItemController {
    void setSelect(int index);

    void addTabItemSelectedListener(OnTabItemSelectedListener listener);

    int getSelected();

    int getItemCount();

    String getItemTitle(int index);
}
