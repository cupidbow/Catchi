package com.envisability.catchi.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.envisability.catchi.AdapterClickListener;
import com.envisability.catchi.CatchiAdapterClickListener;
import com.envisability.catchi.R;
import com.envisability.catchi.adapters.CatchiAdapter;
import com.envisability.catchi.models.Catchi;
import com.envisability.catchi.views.SelectedCatchiView;

import java.util.ArrayList;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Тарас on 07.06.2017.
 */
public class InviteActivity extends Activity {
    @BindView(R.id.tvCatchi)
    TextView tvCatchi;
    @BindView(R.id.etCatchi)
    EditText etCatchi;
    @BindView(R.id.etSearch)
    EditText etSearch;
    @BindView(R.id.vEditTextLine)
    View vEditTextLine;
    @BindView(R.id.vSearchLine)
    View vSearchLine;
    @BindView(R.id.rwOnline)
    RecyclerView rwOnline;
    @BindView(R.id.scCatchies)
    SelectedCatchiView scCatchis;
    @BindView(R.id.rwOffline)
    RecyclerView rwOffline;

    CatchiAdapter onlineAdapter, offlineAdapter;


    ArrayList<Catchi> onlineCatchis , offlineCatchis;

    public static void start(Activity activity)
    {
        activity.startActivity(new Intent(activity, InviteActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite);
        ButterKnife.bind(this);

        etCatchi.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(etCatchi.getText().length()==0)
                {
                    if(b)
                        tvCatchi.animate().yBy(-tvCatchi.getHeight()*1.2f).setDuration(300);
                    else
                        tvCatchi.animate().yBy(tvCatchi.getHeight()*1.2f).setDuration(300);
                }

                if(b)
                    vEditTextLine.setBackgroundResource(R.drawable.search_line_active);
                else
                    vEditTextLine.setBackgroundResource(R.drawable.input_title_line);
            }
        });

        etSearch.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                    vSearchLine.setBackgroundResource(R.drawable.search_line_active);
                else
                    vSearchLine.setBackgroundResource(R.drawable.input_title_line);
            }
        });

        //For template
        String[] names = {"cameron", "taras", "david", "dima", "alex", "max", "hitler","mcdonald","trump"};
        String[] urls = {"http://static4.businessinsider.com/image/56c640526e97c625048b822a-480/donald-trump.jpg",
                "https://s-media-cache-ak0.pinimg.com/originals/00/37/03/0037037f1590875493f413c1fdbd52b1.jpg",
                "http://createyourownphotoblog.com/wp-content/uploads/2014/11/IMG_1977-Version-3.jpg",
                "http://webneel.com/sites/default/files/images/project/best-portrait-photography-regina-pagles%20(10).jpg",
                "http://cdn.newsapi.com.au/image/v1/7939d546a507f18a2f342562301709c5",
                "https://s-media-cache-ak0.pinimg.com/originals/af/2d/29/af2d2934f437c0618e3937e2be832b71.jpg",
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ4cqoZBES8-6bZ2BIhcv26FwLuV_Q440CwLo3OwJX453oXZsPgRg",
                "http://img.whitezine.com/Martin-Schoeller-Jay-Z-portrait.jpeg"};

        onlineCatchis = new ArrayList<>();
        Random random = new Random();
        for(int i=0;i<15;i++)
        {
            onlineCatchis.add(new Catchi(names[random.nextInt(names.length)],urls[random.nextInt(urls.length)],true));
        }

        offlineCatchis = new ArrayList<>();
        for(int i=0;i<35;i++)
        {
            offlineCatchis.add(new Catchi(names[random.nextInt(names.length)],urls[random.nextInt(urls.length)],false));
        }

        offlineAdapter = new CatchiAdapter(this, offlineCatchis,listener);
        onlineAdapter = new CatchiAdapter(this, onlineCatchis,listener);
        rwOffline.setLayoutManager(new GridLayoutManager(this,4));
        rwOnline.setLayoutManager(new GridLayoutManager(this,4));
        rwOffline.setAdapter(offlineAdapter);
        rwOnline.setAdapter(onlineAdapter);

        scCatchis.setListener(new SelectedCatchiView.CatchiClickListener() {
            @Override
            public void onClick(Catchi catchi) {
                if(scCatchis.isBlocked())
                    return;
                if(catchi.isSelected())
                {
                    scCatchis.removeCatchi(catchi);
                    catchi.unselect();
                    if(catchi.isOnline())
                        onlineAdapter.notifyItemChanged(onlineCatchis.indexOf(catchi));
                    else
                        offlineAdapter.notifyItemChanged(offlineCatchis.indexOf(catchi));
                }

            }
        });

    }

    @OnClick(R.id.ivBack)
    public void onBack()
    {
        finish();
    }

    CatchiAdapterClickListener listener = new CatchiAdapterClickListener() {
        @Override
        public void onClick(int i, Catchi catchi, Drawable drawable) {
            if(scCatchis.isBlocked())
                return;
            if(catchi.isSelected())
            {
                scCatchis.removeCatchi(catchi);
                catchi.unselect();
                if(catchi.isOnline())
                    onlineAdapter.notifyItemChanged(i);
                else
                    offlineAdapter.notifyItemChanged(i);
            }
            else
            {
                if(scCatchis.canAdd()) {
                    scCatchis.addCatchi(catchi, drawable);
                    catchi.select();
                    if (catchi.isOnline())
                        onlineAdapter.notifyItemChanged(i);
                    else
                        offlineAdapter.notifyItemChanged(i);
                }
            }

        }
    };
}
