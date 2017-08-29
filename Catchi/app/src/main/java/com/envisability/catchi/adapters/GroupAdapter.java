package com.envisability.catchi.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.envisability.catchi.R;
import com.envisability.catchi.models.Group;
import com.envisability.catchi.utils.CircleTransform;
import com.github.curioustechizen.ago.RelativeTimeTextView;

import java.util.ArrayList;

/**
 * Created by Тарас on 08.06.2017.
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder> {

    Context context;
    ArrayList<Group> groups;

    public GroupAdapter(Context context, ArrayList<Group> groups)
    {
        this.context = context;
        this.groups = groups;
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder
    {
        ImageView ivAvatar;
        TextView tvTitle;
        ImageView ivMoreOptions;
        RelativeTimeTextView tvTime;
        TextView tvLastMessage;
        View parent;
        public GroupViewHolder(View view)
        {
            super(view);
            parent = view;
            ivAvatar = (ImageView) view.findViewById(R.id.ivAvatar);
            ivMoreOptions = (ImageView) view.findViewById(R.id.ivMoreOptions);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvTime = (RelativeTimeTextView) view.findViewById(R.id.tvTime);
            tvLastMessage = (TextView) view.findViewById(R.id.tvLastMessage);
        }
    }

    @Override
    public GroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);

        return new GroupViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GroupViewHolder holder, int position) {
        holder.tvTitle.setText(groups.get(position).getName());
        holder.tvLastMessage.setText(groups.get(position).getLastMessage());
        holder.tvTime.setReferenceTime(groups.get(position).getTime());
        Glide.with(context).load(groups.get(position).getImageUrl()).transform(new CircleTransform(context)).thumbnail(0.1f).into(holder.ivAvatar);
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }
}
