package com.envisability.catchi.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.envisability.catchi.R;
import com.envisability.catchi.dialog_fragments.MainDialogFragment;
import com.envisability.catchi.dialog_fragments.OptionsDialogFragment;
import com.envisability.catchi.utils.CircleTransform;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeActivity extends AppCompatActivity {

    @BindView(R.id.ivHome)
    ImageView ivHome;

    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.ivAvatar1)
    ImageView ivAvatar1;
    @BindView(R.id.ivAvatar2)
    ImageView ivAvatar2;
    @BindView(R.id.ivAvatar3)
    ImageView ivAvatar3;

    float avatarAnimationDistance;

    public static void start(Activity activity)
    {
        activity.startActivity(new Intent(activity,HomeActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);

        ivHome.setImageResource(R.drawable.home_icon_selected);
        avatarAnimationDistance = getResources().getDimension(R.dimen.avatar_distance);

        Glide.with(this).load("http://static4.businessinsider.com/image/56c640526e97c625048b822a-480/donald-trump.jpg").transform(new CircleTransform(this)).into(ivAvatar);
        Glide.with(this).load("https://s-media-cache-ak0.pinimg.com/originals/00/37/03/0037037f1590875493f413c1fdbd52b1.jpg").transform(new CircleTransform(this)).into(ivAvatar1);
        Glide.with(this).load("http://createyourownphotoblog.com/wp-content/uploads/2014/11/IMG_1977-Version-3.jpg").transform(new CircleTransform(this)).into(ivAvatar2);
        Glide.with(this).load("http://img.whitezine.com/Martin-Schoeller-Jay-Z-portrait.jpeg").transform(new CircleTransform(this)).into(ivAvatar3);
    }

    @OnClick(R.id.ivAddPeople)
    public void onAddPeople()
    {
        GroupsActivity.start(this);
    }

    @OnClick(R.id.ivCamera)
    public void onCamera()
    {
        RecordActivity.start(this);
    }

    @OnClick(R.id.rlInbox)
    public void onInbox()
    {

    }

    @OnClick(R.id.ivMenu)
    public void onMenu()
    {
        MainDialogFragment.build().setFlags(MainDialogFragment.FLAG_CANCELABLE | MainDialogFragment.FLAG_WITH_OPTIONS)
                .setOptions(getString(R.string.report),getString(R.string.block))
                .setDialogOptionListener(new MainDialogFragment.OnDialogOptionListener() {
                    @Override
                    public void onOption1() {
                        final String[] options = getResources().getStringArray(R.array.report_reason);
                        OptionsDialogFragment.build().setOptions(options).setListener(new OptionsDialogFragment.OnOptionDialogClick() {
                            @Override
                            public void onOption(OptionsDialogFragment dialogFragment, int i, String data) {
                                if(i == options.length - 1)
                                {
                                    ReportActivity.start(HomeActivity.this);
                                }
                                dialogFragment.dismiss();
                            }
                        }).show(HomeActivity.this);
                    }

                    @Override
                    public void onOption2() {

                    }
                }).show(this);
    }

    @OnClick(R.id.ivProfile)
    public void onProfile()
    {
        ProfileActivity.start(this);
    }

    @OnClick({R.id.ivShare,R.id.tvShare})
    public void onShare()
    {

    }

    @OnClick({R.id.ivComment, R.id.tvComment})
    public void onComment()
    {

    }

    @OnClick({R.id.ivLike, R.id.tvLike})
    public void onLike()
    {

    }




    boolean collapsed = true;

    @OnClick(R.id.ivAvatar)
    public void animateAvatars()
    {
        if(collapsed)
        {
            ivAvatar1.animate().y(ivAvatar.getY()+avatarAnimationDistance).setDuration(500).start();
            ivAvatar2.animate().y(ivAvatar.getY()+2*avatarAnimationDistance).setDuration(500).start();
            ivAvatar3.animate().y(ivAvatar.getY()+3*avatarAnimationDistance).setDuration(500).start();
        }
        else
        {
            ivAvatar1.animate().y(ivAvatar.getY()).setDuration(500).start();
            ivAvatar2.animate().y(ivAvatar.getY()).setDuration(500).start();
            ivAvatar3.animate().y(ivAvatar.getY()).setDuration(500).start();
        }
        collapsed = !collapsed;
    }
}
