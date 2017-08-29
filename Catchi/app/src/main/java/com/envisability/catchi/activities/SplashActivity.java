package com.envisability.catchi.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.gifdecoder.GifDecoder;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.envisability.catchi.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Тарас on 07.06.2017.
 */
public class SplashActivity  extends Activity{
    public static final String TAG = "SplashActivity";


    @BindView(R.id.animated)
    ImageView animated;

    int duration = 0;
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        GlideDrawableImageViewTarget target = new GlideDrawableImageViewTarget(animated);

        Glide.with(this).load(new File("//android_asset/logo.gif")).listener(new RequestListener<File, GlideDrawable>() {

            @Override// Load error
            public boolean onException(Exception arg0, File arg1,
                                       Target<GlideDrawable> arg2, boolean arg3) {
                return false;
            }

            @Override// Loading completed
            public boolean onResourceReady(GlideDrawable resource,
                                           File model, final Target<GlideDrawable> target,
                                           boolean isFromMemoryCache, boolean isFirstResource) {
                //  Calculate animation time
                GifDrawable drawable = (GifDrawable) resource;
                GifDecoder decoder = drawable.getDecoder();
                for (int i = 0; i < drawable.getFrameCount(); i++) {
                    duration += decoder.getDelay(i);
                }
                // Send delay message ， Notice the animation is over
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        target.onStop();
                        animationFinished();
                    }
                },duration);
                return false;
            }
        }).into(target);
    }

    public void animationFinished()
    {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if(currentUser != null) {
            onSuccessLogin();
            com.envisability.catchi.firebase.FirebaseUser.getCurrentUser(new com.envisability.catchi.firebase.FirebaseUser.UserCallback() {
                @Override
                public void onUserReceived(com.envisability.catchi.firebase.FirebaseUser user, Exception exception) {
                    if(exception!=null)
                        Log.d(TAG,"error in get current user", exception);
                }
            });
        }
        else
        {
            onLoggedOut();
        }
    }

    public void onSuccessLogin()
    {
        finish();
        HomeActivity.start(this);
    }

    public void onLoggedOut()
    {
        finish();
        LoginActivity.start(this);
    }

}
