package com.example.breadtravel_20200408.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.breadtravel_20200408.R;
import com.example.breadtravel_20200408.entity.WorksContent;
import com.example.breadtravel_20200408.util.BaseApplication;
import java.util.List;

public class ContentAdapter extends BaseAdapter {

    List<WorksContent> list;

    static class ViewHolder {
        ImageView photo;
        TextView contentText;
        TextView time;
        TextView location;
    }

    public ContentAdapter(List<WorksContent> list) {
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
        ViewHolder viewHolder;
        if (view == null) {
            view = View.inflate(BaseApplication.getmContext(), R.layout.release_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.photo=view.findViewById(R.id.photo);
            viewHolder.contentText=view.findViewById(R.id.contentText);
            viewHolder.time=view.findViewById(R.id.time);
            viewHolder.location=view.findViewById(R.id.location);
            view.setTag(viewHolder);
        }else {
            viewHolder= (ViewHolder) view.getTag();
        }
        Glide.with(BaseApplication.getmContext()).load(list.get(i).getPhoto()).apply(new RequestOptions().placeholder(R.drawable.logo_no_bg)).into(viewHolder.photo);
        viewHolder.contentText.setText(list.get(i).getContentText());
        viewHolder.time.setText(list.get(i).getDate() + " " + list.get(i).getTime());
        viewHolder.location.setText(list.get(i).getLocation());
        return view;
    }
}
