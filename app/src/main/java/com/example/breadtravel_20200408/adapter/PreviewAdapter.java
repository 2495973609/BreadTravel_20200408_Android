package com.example.breadtravel_20200408.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.breadtravel_20200408.R;
import com.example.breadtravel_20200408.entity.WorksPreview;
import com.example.breadtravel_20200408.util.BaseApplication;
import com.example.breadtravel_20200408.util.HeadImageView;

import java.util.Collections;
import java.util.List;

public class PreviewAdapter extends BaseAdapter {

    private List<WorksPreview> list;
    protected static final String HOST = "http://10.70.48.215:8080/BreadTravel_20200408_war_exploded/";

    public PreviewAdapter(List<WorksPreview> list) {
        Collections.reverse(list);
        this.list=list;
    }

    static class ViewHolder {
        ImageView coverImg;
        TextView title;
        TextView date;
        TextView day;
        TextView browse;
        TextView region;
        HeadImageView headImg;
        TextView nickName;
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
            view = View.inflate(BaseApplication.getmContext(), R.layout.preview_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.coverImg = view.findViewById(R.id.coverImg);
            viewHolder.title = view.findViewById(R.id.title);
            viewHolder.date = view.findViewById(R.id.date);
            viewHolder.day = view.findViewById(R.id.day);
            viewHolder.browse = view.findViewById(R.id.browse);
            viewHolder.region = view.findViewById(R.id.region);
            viewHolder.headImg = view.findViewById(R.id.headImg);
            viewHolder.nickName = view.findViewById(R.id.nickName);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.title.setText(list.get(i).getTitle());
        viewHolder.date.setText(list.get(i).getDate());
        viewHolder.day.setText(list.get(i).getDay() + "天");
        viewHolder.browse.setText(list.get(i).getSkim() + "次浏览");
        viewHolder.region.setText(list.get(i).getRegion().substring(0,list.get(i).getRegion().indexOf(" ")));
        String url1;
        if (list.get(i).getHeadImg().equals("logo.jpg")) {
            url1 = HOST + "/logo.jpg";
        } else {
            url1 = HOST + list.get(i).getUserName() + "/" + list.get(i).getHeadImg();
        }
        String url2 = HOST + list.get(i).getUserName() + "/" + list.get(i).getCoverImg();
        RequestOptions options = new RequestOptions().error(R.drawable.img_error).placeholder(R.drawable.logo).bitmapTransform(new RoundedCorners(20));
        Glide.with(BaseApplication.getmContext()).load(url1).into(viewHolder.headImg);
        Glide.with(BaseApplication.getmContext()).load(url2).apply(options).into(viewHolder.coverImg);
        viewHolder.nickName.setText("by " + list.get(i).getNickName());
        return view;

    }
}
