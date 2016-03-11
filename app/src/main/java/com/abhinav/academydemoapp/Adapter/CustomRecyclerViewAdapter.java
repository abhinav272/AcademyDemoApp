package com.abhinav.academydemoapp.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.abhinav.academydemoapp.Model.AcademyItem;
import com.abhinav.academydemoapp.R;

import java.util.ArrayList;

/**
 * Created by abhinavsharma on 10-03-2016.
 */
public class CustomRecyclerViewAdapter extends RecyclerView.Adapter<CustomRecyclerViewAdapter.MyViewHolder> {

    public interface OnItemClickListener {
        public void onItemClick(View view , int position);
    }

    private LayoutInflater inflater;
    private ArrayList<AcademyItem> academyItems;
    private OnItemClickListener mOnItemClickListener;



    public CustomRecyclerViewAdapter(Context mContext, ArrayList<AcademyItem> academyItems){
        this.academyItems = academyItems;
        inflater = LayoutInflater.from(mContext);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.single_list_item,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.academyName.setText(academyItems.get(position).getAcademyName());
        holder.location.setText(academyItems.get(position).getLocation());
    }

    @Override
    public int getItemCount() {
        return academyItems.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView academyName, location;
        public MyViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            academyName = (TextView) itemView.findViewById(R.id.academy_name);
            location = (TextView) itemView.findViewById(R.id.location);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mOnItemClickListener!=null){
                mOnItemClickListener.onItemClick(v,getAdapterPosition());
            }

        }
    }

    public void SetOnItemClickListener(final OnItemClickListener mOnItemClickListener){
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
