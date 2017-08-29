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
public class GroupInviteAdapter extends RecyclerView.Adapter<GroupInviteAdapter.GroupInviteViewHolder> {

    Context context;
    ArrayList<Group> groups;

    public GroupInviteAdapter(Context context, ArrayList<Group> groups)
    {
        this.context = context;
        this.groups = groups;
    }

    public static class GroupInviteViewHolder extends RecyclerView.ViewHolder
    {
        ImageView ivAvatar;
        TextView tvTitle;
        ImageView ivClose;
        RelativeTimeTextView tvTime;
        TextView tvLastMessage;
        View parent;
        public GroupInviteViewHolder(View view)
        {
            super(view);
            parent = view;
            ivAvatar = (ImageView) view.findViewById(R.id.ivAvatar);
            ivClose = (ImageView) view.findViewById(R.id.ivClose);
            tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            tvTime = (RelativeTimeTextView) view.findViewById(R.id.tvTime);
            tvLastMessage = (TextView) view.findViewById(R.id.tvLastMessage);
        }
    }

    @Override
    public GroupInviteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group_invite, parent, false);

        return new GroupInviteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GroupInviteViewHolder holder, int position) {
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
