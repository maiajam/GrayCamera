package com.maiajam.camera2app.Helper;

import android.content.Context;
import android.view.MenuItem;
import android.view.View;

import com.maiajam.camera2app.R;

public class PopupMenuEventHandler implements MenuItem.OnMenuItemClickListener  {

    private final Context context;

    public PopupMenuEventHandler(Context context) {
        this.context = context ;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        switch (item.getItemId()){
            case R.id.id_Missing:
               break;
            case R.id.id_bandaged:
                break;
            case R.id.id_sixthFinger:
                break;
        }
        return false;
    }


}
