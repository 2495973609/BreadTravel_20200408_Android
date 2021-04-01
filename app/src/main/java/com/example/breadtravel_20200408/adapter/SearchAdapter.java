package com.example.breadtravel_20200408.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.help.Tip;
import com.example.breadtravel_20200408.R;
import com.example.breadtravel_20200408.entity.Address;
import com.example.breadtravel_20200408.util.BaseApplication;
import com.example.breadtravel_20200408.util.HeadImageView;

import java.util.List;

public class SearchAdapter extends BaseAdapter {

    private List<Tip> list;

    public SearchAdapter(List<Tip> list){
        this.list=list;
    }

    static class ViewHolder {
        TextView name;
        TextView district;
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
            view = View.inflate(BaseApplication.getmContext(), R.layout.location_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.name=view.findViewById(R.id.name);
            viewHolder.district=view.findViewById(R.id.district);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.name.setText(list.get(i).getName());
        viewHolder.district.setText(list.get(i).getDistrict()+list.get(i).getAddress());
        return view;
    }
}
