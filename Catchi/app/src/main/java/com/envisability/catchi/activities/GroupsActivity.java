package com.envisability.catchi.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.envisability.catchi.R;
import com.envisability.catchi.adapters.GroupsAndInvitesAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Тарас on 05.06.2017.
 */
public class GroupsActivity extends AppCompatActivity {

    @BindView(R.id.tabWidget)
    TabLayout tabLayout;
    @BindView(R.id.pager)
    ViewPager pager;

    public static void start(Activity activity)
    {
        activity.startActivity(new Intent(activity,GroupsActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups);
        ButterKnife.bind(this);

        pager.setAdapter(new GroupsAndInvitesAdapter(this,getSupportFragmentManager()));
        tabLayout.setupWithViewPager(pager);
    }

    @OnClick(R.id.ivBack)
    public void onBack()
    {
        finish();
    }
}
