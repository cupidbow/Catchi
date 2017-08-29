package com.envisability.catchi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.envisability.catchi.AdapterClickListener;
import com.envisability.catchi.R;
import com.envisability.catchi.models.GalleryModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Тарас on 07.06.2017.
 */
public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {

    ArrayList<GalleryModel.GalleryFile> files;
    Context context;
    GalleryModel gallery;
    AdapterClickListener clickListener;

    public void setClickListener(AdapterClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public FileAdapter(Context context, ArrayList<GalleryModel.GalleryFile> files, GalleryModel gallery) {
        this. files =  files;
        this.context = context;
        this.gallery = gallery;
    }

    public static class FileViewHolder extends RecyclerView.ViewHolder
    {
        String data;
        ImageView ivTitle;
        ImageView ivSelection;
        ImageView ivVideo;
        TextView tvSelection;
        View parent;
        public FileViewHolder(View view)
        {
            super(view);
            parent = view;
            ivTitle = (ImageView) view.findViewById(R.id.ivTitle);
            ivSelection = (ImageView) view.findViewById(R.id.ivSelection);
            tvSelection = (TextView) view.findViewById(R.id.tvSelection);
            ivVideo = (ImageView) view.findViewById(R.id.ivVideo);
        }

        public void updateSelection(GalleryModel gallery)
        {
            int index = gallery.getSelectedIndex(data);
            if(index != -1)
            {
                ivSelection.setVisibility(View.VISIBLE);
                tvSelection.setVisibility(View.VISIBLE);
                tvSelection.setText(""+index);
            }
            else
            {
                ivSelection.setVisibility(View.INVISIBLE);
                tvSelection.setVisibility(View.INVISIBLE);
            }
        }

    }

    @Override
    public FileViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_file, parent, false);

        return new FileViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FileViewHolder holder, final int position) {
        holder.data = files.get(position).getFullPath();
        Glide.with(context).load(files.get(position).getFullPath()).diskCacheStrategy(DiskCacheStrategy.RESULT).thumbnail( 0.1f ).into(holder.ivTitle);
        int index = gallery.getSelectedIndex(files.get(position).getFullPath());
        if(index != -1)
        {
            holder.ivSelection.setVisibility(View.VISIBLE);
            holder.tvSelection.setVisibility(View.VISIBLE);
            holder.tvSelection.setText(""+index);
        }
        else
        {
            holder.ivSelection.setVisibility(View.INVISIBLE);
            holder.tvSelection.setVisibility(View.INVISIBLE);
        }

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(clickListener!=null)
                {
                    clickListener.onClick(position, files.get(position).getFullPath());
                }
            }
        });

        if(files.get(position).isVideo())
            holder.ivVideo.setVisibility(View.VISIBLE);
        else
            holder.ivVideo.setVisibility(View.INVISIBLE);

    }


    @Override
    public int getItemCount()
    {
        return files.size();
    }

    @Override
    public void onBindViewHolder(FileViewHolder viewHolder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            // Perform a full update
            onBindViewHolder(viewHolder, position);
        } else {
            // Perform a partial update
            for (Object payload : payloads) {
                if (payload instanceof GalleryModel) {
                    viewHolder.updateSelection((GalleryModel) payload);
                }
            }
        }
    }
}
