package com.envisability.catchi.adapters;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.envisability.catchi.AdapterClickListener;
import com.envisability.catchi.R;
import com.envisability.catchi.models.GalleryModel;

import java.util.ArrayList;

/**
 * Created by Тарас on 07.06.2017.
 */
public class DirectoryAdapter extends RecyclerView.Adapter<DirectoryAdapter.DirectoryViewHolder> {

    ArrayList<GalleryModel.GalleryDirectory> directories;
    Context context;
    GalleryModel gallery;

    AdapterClickListener clickListener;


    public void setClickListener(AdapterClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public DirectoryAdapter(Context context, ArrayList<GalleryModel.GalleryDirectory> directories, GalleryModel gallery) {
        this. directories =  directories;
        this.context = context;
        this.gallery = gallery;
    }

    public static class DirectoryViewHolder extends RecyclerView.ViewHolder
    {
        ImageView ivTitle;
        TextView tvTitle;
        ImageView ivSelection;
        View parent;
        public DirectoryViewHolder(View view)
        {
            super(view);
            parent = view;
            ivTitle = (ImageView) view.findViewById(R.id.ivTitle);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            ivSelection = (ImageView) view.findViewById(R.id.ivSelection);
        }

    }

    @Override
    public DirectoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_directory, parent, false);

        return new DirectoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final DirectoryViewHolder holder, final int position) {

        holder.tvTitle.setText(directories.get(position).getName());
        Glide.with(context).load(directories.get(position).getFiles().get(0).getFullPath()).thumbnail( 0.1f ).into(holder.ivTitle);
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickListener!=null)
                    clickListener.onClick(position, directories.get(position).getName());
            }
        });

        if(directories.get(position).getName().equals(gallery.selectedDirectory))
            holder.ivSelection.setVisibility(View.VISIBLE);
        else
            holder.ivSelection.setVisibility(View.INVISIBLE);

    }


    @Override
    public int getItemCount()
    {
        return directories.size();
    }


}
