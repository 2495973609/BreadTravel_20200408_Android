package com.example.breadtravel_20200408.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.breadtravel_20200408.entity.WorksComment;


import java.util.List;

public class CommentAdapter extends BaseAdapter {
    List<WorksComment> list;

    public CommentAdapter(List<WorksComment> list) {
        this.list=list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
