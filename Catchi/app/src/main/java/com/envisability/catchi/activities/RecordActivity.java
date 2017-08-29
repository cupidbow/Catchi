package com.envisability.catchi.activities;

import android.app.Activity;
import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.envisability.catchi.R;
import com.envisability.catchi.views.CameraPreview;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Тарас on 05.06.2017.
 */
public class RecordActivity extends Activity{

    @BindView(R.id.rlCameraPreview)
    FrameLayout rlCameraPreview;

    private int lastCamera = 1;
    Camera mCamera;
    CameraPreview mPreview;

    public static void start(Activity activity)
    {
        activity.startActivity(new Intent(activity, RecordActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);
        ButterKnife.bind(this);
        startCamera();
        if(mCamera!=null)
            mCamera.startPreview();
    }

    @OnClick(R.id.ivRecord)
    public void onRecord()
    {

    }

    @OnClick(R.id.ivBack)
    public void onBack()
    {
        finish();
    }

    @OnClick(R.id.ivReverse)
    public void onReverse()
    {
        if(mCamera!=null)
            releaseCamera();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        startCamera();
                    }
                });
            }
        },500);
    }

    @OnClick(R.id.rlInvites)
    public void onInvites()
    {
        GroupsActivity.start(this);
    }

    @OnClick(R.id.ivAddPeople)
    public void onAddPeople()
    {
        InviteActivity.start(this);
    }

    @OnClick(R.id.ivGallery)
    public void onGallery()
    {
        GalleryActivity.start(this);
    }

    @OnClick(R.id.ivEdit)
    public void onEdit()
    {

    }

    public Camera getCameraInstance(){
        Camera c = null;
        try {
            if(!whereStopped)
                lastCamera = lastCamera==0? 1 : 0;
            whereStopped = false;
            c = Camera.open(lastCamera); // attempt to get a Camera instance
            //      c.startFaceDetection();

        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
            e.printStackTrace();
        }


        return c; // returns null if camera is unavailable
    }

    private void startCamera()
    {
        // preview = (FrameLayout) getView().findViewById(R.id.camera_preview);
        mCamera = getCameraInstance();
        //  mCamera.setDisplayOrientation();
        if (mCamera == null) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    onResume();
                }
            },200);
            return;
        }

        Camera.Parameters parameters = mCamera.getParameters();
        for(Camera.Size size:parameters.getSupportedPreviewSizes())
        {
            Log.d("supported size", size.width +" x "+ size.height);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Log.d("preview size", rlCameraPreview.getWidth() + " x "+ rlCameraPreview.getHeight()+ " : " + (rlCameraPreview.getWidth()*1.0f/rlCameraPreview.getHeight()));
                }
            },100);
            if(size.width/16==size.height/9 && size.width>800) {
                parameters.setPreviewSize(size.width, size.height);
                Log.d("size choosed", size.width +" x "+ size.height);
                break;
            }
        }
        for(Camera.Size size:parameters.getSupportedPictureSizes())
        {
            Log.d("supported size picture", size.width +" x "+ size.height);
            if(size.width/16==size.height/9 && size.width>800) {
                parameters.setPictureSize(size.width, size.height);
                Log.d("pic size choosed", size.width +" x "+ size.height);
                break;
            }
        }

        for(String mode: parameters.getSupportedFocusModes())
        {
            if(mode.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        if(lastCamera==1)
            parameters.setRotation(270);
        else
            parameters.setRotation(90);
        mCamera.setParameters(parameters);
        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);

        rlCameraPreview.addView(mPreview);

        mCamera.startPreview();
        mPreview.requestLayout();

    }

    boolean whereStopped = false;
    @Override
    public void onPause() {
        super.onPause();
        whereStopped = true;
        releaseCamera();              // release the camera immediately on pause event
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if(whereStopped)
            startCamera();

    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mPreview.getHolder().removeCallback(mPreview);
            mCamera.release();
            rlCameraPreview.removeView(mPreview);


            // release the camera for other applications
            mCamera = null;
        }
    }

}
