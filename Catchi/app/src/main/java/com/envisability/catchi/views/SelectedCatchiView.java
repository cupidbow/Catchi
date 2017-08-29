package com.envisability.catchi.views;

import android.animation.Animator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.envisability.catchi.R;
import com.envisability.catchi.models.Catchi;

import java.util.ArrayList;

/**
 * Created by Тарас on 08.06.2017.
 */
public class SelectedCatchiView extends FrameLayout {
    ArrayList<Catchi> catchis;
    ArrayList<View> views;
    private boolean blocked;
    int currentIndex = 0;

    CatchiClickListener listener;

    public void setListener(CatchiClickListener listener) {
        this.listener = listener;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public boolean canAdd()
    {
        return views.size()<4;
    }

    float addX = 0 ,dx;

    public SelectedCatchiView(Context context)
    {
        super(context);
        views = new ArrayList<>();
        catchis = new ArrayList<>();

        dx = getResources().getDimension(R.dimen.selected_catchi) * 0.6f;


    }

    public SelectedCatchiView(Context context, AttributeSet attributeSet)
    {
        super(context,attributeSet);
        catchis = new ArrayList<>();
        addX = getWidth()*0.5f;
        dx = getResources().getDimension(R.dimen.selected_catchi) * 0.6f;
        views = new ArrayList<>();
    }

    public void addCatchi(final Catchi catchi, Drawable drawable)
    {
        if(addX == 0)
            addX = getWidth()*0.5f - dx*5/6;
        if(catchis.size()<4)
        {
            blocked = true;
            View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_selected_catchi, null, false);
            ((ImageView)itemView.findViewById(R.id.ivAvatar)).setImageDrawable(drawable);
            itemView.setScaleX(0.1f);
            itemView.setScaleY(0.1f);
            itemView.setX(addX);
            itemView.setY(getHeight()/2 - dx*5/6);
            addView(itemView,new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            itemView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener!=null)
                        listener.onClick(catchi);
                }
            });
            itemView.animate().scaleX(1).scaleY(1).setDuration(300).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    blocked = false;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            }).start();
            addX += dx;
            for(View v: views)
            {
                v.animate().xBy(-dx).setDuration(300).start();
            }
            views.add(itemView);
            catchis.add(catchi);

        }
    }

    public void removeCatchi(Catchi catchi)
    {
        if(catchis.size()>0)
        {
            blocked = true;
            final int toDelete = catchis.indexOf(catchi);
            Animator.AnimatorListener listener = new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    addX-=dx;
                    catchis.remove(toDelete);
                    views.remove(toDelete);
                    blocked = false;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            };
            for(int i = 0; i< catchis.size();i++)
            {
                if(i<toDelete)
                {
                    views.get(i).animate().xBy(dx).setDuration(300).start();
                }
                if(i==toDelete)
                {
                    views.get(i).animate().scaleX(0).scaleY(0).setListener(listener).setDuration(300).start();
                }
                if(i>toDelete)
                {
                    views.get(i).animate().xBy(-dx).setDuration(300).start();
                }
            }

        }
    }

    public interface CatchiClickListener
    {
        void onClick(Catchi catchi);
    }
}
