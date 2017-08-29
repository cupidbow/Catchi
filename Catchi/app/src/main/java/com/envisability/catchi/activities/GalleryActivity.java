package com.envisability.catchi.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.envisability.catchi.AdapterClickListener;
import com.envisability.catchi.R;
import com.envisability.catchi.adapters.DirectoryAdapter;
import com.envisability.catchi.adapters.FileAdapter;
import com.envisability.catchi.utils.ImageFileUtil;
import com.envisability.catchi.models.GalleryModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Тарас on 07.06.2017.
 */
public class GalleryActivity extends Activity {
    @BindView(R.id.rwDirectories)
    RecyclerView rwDirectories;
    @BindView(R.id.rwFiles)
    RecyclerView rwFiles;

    DirectoryAdapter directoryAdapter;
    FileAdapter fileAdapter;
    GalleryModel gallery;


    public static void start(Activity activity)
    {
        activity.startActivity(new Intent(activity, GalleryActivity.class));
    }


    @OnClick(R.id.ivBack)
    public void onBack()
    {
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);


        new GalleryLoader(this).execute();
        rwDirectories.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));




        rwFiles.setLayoutManager(new GridLayoutManager(this,4));


    }

    AdapterClickListener fileClickListener = new AdapterClickListener() {
        @Override
        public void onClick(int i, String data) {
            if(gallery!=null)
            {

                if(gallery.getSelectedIndex(data)!=-1)
                    gallery.unselectFile(data);
                else
                    gallery.selectFile(data);

                fileAdapter.notifyItemRangeChanged(0, fileAdapter.getItemCount(),gallery);
            }
        }
    };

    public void onGalleryLoaded(GalleryModel galleryModel)
    {
        this.gallery = galleryModel;
        gallery.selectedDirectory = gallery.getDirectories().get(0).getName();

        directoryAdapter = new DirectoryAdapter(this, gallery.getDirectories(),gallery);
        rwDirectories.setAdapter(directoryAdapter);

        directoryAdapter.setClickListener(new AdapterClickListener() {
            @Override
            public void onClick(int i, String data) {
                fileAdapter = new FileAdapter(getApplicationContext(), gallery.getDirectories().get(i).getFiles(), gallery);
                rwFiles.setLayoutManager(new GridLayoutManager(getApplicationContext(),4));
                rwFiles.setAdapter(fileAdapter);
                gallery.selectedDirectory = data;
                directoryAdapter.notifyItemRangeChanged(0, directoryAdapter.getItemCount());

                fileAdapter.setClickListener(fileClickListener);
            }
        });

        fileAdapter = new FileAdapter(this, gallery.getDirectories().get(0).getFiles(), gallery);
        rwFiles.setAdapter(fileAdapter);
        fileAdapter.setClickListener(fileClickListener);
    }

    public static class GalleryLoader extends AsyncTask<Void,Void,GalleryModel>
    {
        GalleryActivity galleryActivity;
        ProgressDialog progressDialog;
        public GalleryLoader(GalleryActivity galleryActivity)
        {
            this.galleryActivity = galleryActivity;
            progressDialog = new ProgressDialog(galleryActivity);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected GalleryModel doInBackground(Void... voids) {
            return ImageFileUtil.getAllImages(galleryActivity);
        }

        @Override
        protected void onPostExecute(GalleryModel galleryModel) {
            super.onPostExecute(galleryModel);
            galleryActivity.onGalleryLoaded(galleryModel);
            progressDialog.dismiss();

        }
    }
}
