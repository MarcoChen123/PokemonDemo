package com.example.pokemonkotlindemo.base.widget;

import android.util.SparseArray;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class BaseViewHolder extends RecyclerView.ViewHolder {

    private final View mContentView;
    private final SparseArray<View> mChildViews;

    public BaseViewHolder(View itemView) {
        super(itemView);
        mContentView = itemView;
        mChildViews = new SparseArray<>();
    }

    public static BaseViewHolder createViewHolder(View itemView){
        return new BaseViewHolder(itemView);
    }

    public <T extends View> T findView(int viewId) {
        View view = mChildViews.get(viewId);
        if (view == null) {
            view = mContentView.findViewById(viewId);
            mChildViews.put(viewId, view);
        }
        return (T) view;
    }
}
