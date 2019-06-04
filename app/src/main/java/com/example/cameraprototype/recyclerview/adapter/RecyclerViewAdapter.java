package com.example.cameraprototype.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cameraprototype.R;
import com.example.cameraprototype.model.Library;

import java.util.Collections;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {

    private Context context;
    private List<Library> list;
    private OnItemClickListener onItemClickListener;

    public RecyclerViewAdapter(Context context, List<Library> list) {
        this.context = context;
        this.list = list;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.cardview_library, viewGroup, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int i) {
        Library library = list.get(i);
        holder.bindInformation(library);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void remove(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView itemName;
        private final ImageView itemImage;
        private Library lib;


        public MyViewHolder(final View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.item_name);
            itemImage = itemView.findViewById(R.id.image_view_item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(lib, getAdapterPosition());
                }
            });
        }

        public void bindInformation(Library lib) {
            this.lib = lib;
            itemName.setText(lib.getItem());
            itemImage.setImageBitmap(lib.getImage());
        }
    }
}
