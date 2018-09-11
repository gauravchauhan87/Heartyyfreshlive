package com.heartyy.heartyyfresh.utils;

import android.content.ClipData;
import android.content.ClipDescription;
import android.view.View;

/**
 * Created by Vijay on 3/1/2016.
 */
public class MyClickListener implements View.OnLongClickListener {
    @Override
    public boolean onLongClick(View view) {
        ClipData.Item item = new ClipData.Item((CharSequence)view.getTag());

        String[] mimeTypes = { ClipDescription.MIMETYPE_TEXT_PLAIN };
        ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

        view.startDrag( data, //data to be dragged
                shadowBuilder, //drag shadow
                view, //local data about the drag and drop operation
                0   //no needed flags
        );


        view.setVisibility(View.INVISIBLE);
        return true;
    }
}
