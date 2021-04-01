package com.example.breadtravel_20200408.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.breadtravel_20200408.R;
import com.example.breadtravel_20200408.activity.WorksContentActivity;
import com.example.breadtravel_20200408.entity.WorksPreview;
import com.example.breadtravel_20200408.util.BaseApplication;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.CityHolder> {

    private List<WorksPreview> list;
    private LatLng latLng1;
    private FragmentActivity fragmentActivity;
    protected static final String HOST = "http://10.70.48.215:8080/BreadTravel_20200408_war_exploded/";

    public RecyclerViewAdapter(List<WorksPreview> list, LatLng latLng1, FragmentActivity activity){
        Collections.reverse(list);
        this.list=list;
        this.latLng1=latLng1;
        this.fragmentActivity=activity;
    }

    //创建ViewHolder
    @NonNull
    @Override
    public CityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_item, parent, false);
        CityHolder holder = new CityHolder(view);
        return holder;
    }

    //填充视图
    @Override
    public void onBindViewHolder(@NonNull CityHolder holder, final int position) {
        final WorksPreview worksPreview=list.get(position);
        holder.bindData(worksPreview);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(fragmentActivity, WorksContentActivity.class);
                intent.putExtra("previewData", worksPreview);
                fragmentActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class  CityHolder extends RecyclerView.ViewHolder{

        public ImageView coverImg,headImg;
        public TextView title,nickName,distance;

        public CityHolder(@NonNull View itemView) {
            super(itemView);
            coverImg=itemView.findViewById(R.id.coverImg);
            headImg=itemView.findViewById(R.id.headImg);
            title=itemView.findViewById(R.id.title);
            nickName=itemView.findViewById(R.id.nickName);
            distance=itemView.findViewById(R.id.distance);
        }

        public void bindData(WorksPreview worksPreview) {
            RequestOptions options = new RequestOptions().error(R.drawable.img_error).placeholder(R.drawable.logo).bitmapTransform(new RoundedCorners(20));
            Glide.with(BaseApplication.getmContext()).load(HOST + worksPreview.getUserName() + "/" + worksPreview.getCoverImg()).apply(options).into(coverImg);
            String hdImgUrl;
            if (worksPreview.getHeadImg().equals("logo.jpg")) {
                hdImgUrl = HOST + "/logo.jpg";
            } else {
                hdImgUrl = HOST + worksPreview.getUserName() + "/" + worksPreview.getHeadImg();
            }
            Glide.with(BaseApplication.getmContext()).load(hdImgUrl).apply(options).into(headImg);
            title.setText(worksPreview.getTitle());
            nickName.setText(worksPreview.getNickName());
            String local=worksPreview.getRegion();
            String str=local.substring(local.indexOf("("),local.indexOf(")")+1);
            LatLng latLng2=new LatLng(Double.valueOf(str.substring(1,str.indexOf(","))),Double.valueOf(str.substring(str.indexOf(",")+1,str.lastIndexOf(")"))));
            float dis= AMapUtils.calculateLineDistance(latLng1,latLng2)/1000;
            if (dis<1){
                distance.setText("<1km");
            }else {
                distance.setText(String.format("%.1f",dis)+"km");
            }
        }
    }
}
