package com.envisability.catchi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.envisability.catchi.AdapterClickListener;
import com.envisability.catchi.CatchiAdapterClickListener;
import com.envisability.catchi.R;
import com.envisability.catchi.models.Catchi;
import com.envisability.catchi.utils.CircleTransform;

import java.util.ArrayList;

/**
 * Created by Тарас on 08.06.2017.
 */
public class CatchiAdapter extends RecyclerView.Adapter<CatchiAdapter.CatchiViewHolder> {

    Context context;
    ArrayList<Catchi> catchis;
    CatchiAdapterClickListener listener;

    public CatchiAdapter(Context context, ArrayList<Catchi> catchis, CatchiAdapterClickListener listener)
    {
        this.listener = listener;
        this.catchis = catchis;
        this.context = context;
    }

    public static class CatchiViewHolder extends RecyclerView.ViewHolder
    {
        ImageView ivAvatar;
        ImageView ivOnline;
        TextView tvNickname;
        View parent;

        public CatchiViewHolder(View v)
        {
            super(v);
            parent = v;
            ivAvatar = (ImageView)v.findViewById(R.id.ivAvatar);
            ivOnline = (ImageView)v.findViewById(R.id.ivOnline);
            tvNickname = (TextView)v.findViewById(R.id.tvNickname);
        }

    }

    @Override
    public CatchiViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_catchi, parent, false);
        return new CatchiViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return catchis.size();
    }

    @Override
    public void onBindViewHolder(final CatchiViewHolder holder, final int position) {
        if(catchis.get(position).isOnline())
            holder.ivOnline.setVisibility(View.VISIBLE);
        else
            holder.ivOnline.setVisibility(View.INVISIBLE);
        holder.tvNickname.setText(catchis.get(position).getName());
        int padding = catchis.get(position).isSelected() ? (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,2.5f,context.getResources().getDisplayMetrics()): 0;
        holder.ivAvatar.setPadding(padding, padding, padding, padding);

        Glide.with(context).load(catchis.get(position).getAvatarURL()).transform(new CircleTransform(context)).into(holder.ivAvatar);
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(listener!=null)
                    listener.onClick(position, catchis.get(position),holder.ivAvatar.getDrawable());
            }
        });
    }
}
