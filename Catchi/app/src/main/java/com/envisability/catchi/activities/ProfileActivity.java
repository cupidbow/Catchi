package com.envisability.catchi.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.envisability.catchi.utils.CircleTransform;
import com.envisability.catchi.utils.ImageFileUtil;
import com.envisability.catchi.R;
import com.envisability.catchi.dialog_fragments.MainDialogFragment;
import com.envisability.catchi.firebase.FirebaseClient;
import com.envisability.catchi.firebase.FirebaseUser;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.ivProfile)
    ImageView ivProfile;
    @BindView(R.id.etUserName)
    EditText etUsername;
    @BindView(R.id.etAbout)
    EditText etAbout;
    @BindView(R.id.etMail)
    EditText etEmail;
    @BindView(R.id.ivAvatar)
    ImageView ivAvatar;
    @BindView(R.id.ivUserName)
    ImageView ivUsername;
    @BindView(R.id.ivMail)
    ImageView ivMail;
    @BindView(R.id.ivAbout)
    ImageView ivAbout;
    @BindView(R.id.sepUsername)
    View sepUsername;
    @BindView(R.id.sepEmail)
    View sepEmail;
    @BindView(R.id.sepAbout)
    View sepAbout;

    Bitmap bmp;

    Drawable dWhiteLine, dCyanLine;

    String imagePath;

    private static final int REQUEST_CAMERA = 1;
    private static final int REQUEST_GALLERY = 2;
    com.envisability.catchi.firebase.FirebaseUser currentUser;

    public static void start(Activity activity)
    {
        activity.startActivity(new Intent(activity,ProfileActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);


        dWhiteLine = getResources().getDrawable(R.drawable.white_line_1).mutate();
        dCyanLine = dWhiteLine.mutate();
        dCyanLine.setColorFilter(getResources().getColor(R.color.cyan_100), PorterDuff.Mode.SRC_ATOP);

        ivProfile.setImageResource(R.drawable.profile_icon_selected);
        FirebaseUser.getCurrentUser(new FirebaseUser.UserCallback() {
            @Override
            public void onUserReceived(FirebaseUser user, Exception exception) {
                if(user!=null&& exception==null)
                {
                    currentUser = user;
                    if(user.getEmail()!=null)
                        etEmail.setText(user.getEmail());
                    if(user.getUsername()!=null)
                        etUsername.setText(user.getUsername());
                    if(user.getInfo()!=null)
                        etAbout.setText(user.getInfo());
                    if(user.getAvatarURL()!=null)
                    {

                        Glide.with(getApplicationContext())
                                .load(user.getAvatarURL())
                                .transform(new CircleTransform(getApplicationContext()))
                                .into(ivAvatar);
                    }

                }
            }
        });

        etAbout.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b)
                {
                    sepAbout.setBackground(dCyanLine);
                    ivAbout.setVisibility(View.INVISIBLE);

                }
                else
                {
                    sepAbout.setBackgroundResource(R.drawable.white_line_1);
                    ivAbout.setVisibility(View.VISIBLE);
                }
            }
        });

        etUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                {
                    sepUsername.setBackground(dCyanLine);
                    ivUsername.setVisibility(View.INVISIBLE);
                }
                else
                {
                    sepUsername.setBackgroundResource(R.drawable.white_line_1);
                    ivUsername.setVisibility(View.VISIBLE);
                }
            }
        });

        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b)
                {
                    sepEmail.setBackground(dCyanLine);
                    ivMail.setVisibility(View.INVISIBLE);
                }
                else
                {
                    sepEmail.setBackgroundResource(R.drawable.white_line_1);
                    ivMail.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @OnClick(R.id.ivTakePicture)
    public void onTakePicture()
    {
        MainDialogFragment.build()
                .setFlags(MainDialogFragment.FLAG_CANCELABLE | MainDialogFragment.FLAG_WITH_OPTIONS)
                .setOptions("from gallery", "from camera")
                .setDialogOptionListener(new MainDialogFragment.OnDialogOptionListener() {
                    @Override
                    public void onOption1() {
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("image/*");
                        startActivityForResult(
                                Intent.createChooser(intent, "Select image..."),
                                REQUEST_GALLERY);
                    }

                    @Override
                    public void onOption2() {
                        takePicture(REQUEST_CAMERA);
                    }
                })
                .show(this);
    }

    @OnClick(R.id.ivAddPeople)
    public void onAddPeople()
    {

    }

    @OnClick(R.id.ivCamera)
    public void onCamera()
    {

    }

    @OnClick(R.id.rlInbox)
    public void onInbox()
    {

    }

    @OnClick(R.id.ivHome)
    public void onHome()
    {
        HomeActivity.start(this);
    }


    @OnClick(R.id.ivOptions)
    public void onOptions()
    {
        HomeActivity.start(this);
    }

    @OnClick(R.id.ivSearch)
    public void onSearch()
    {
        HomeActivity.start(this);
    }

    public void takePicture(int request){
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra("android.intent.extras.CAMERA_FACING", 1);
        intent.putExtra("android.intent.extras.LENS_FACING_FRONT", 1);
        intent.putExtra("android.intent.extra.USE_FRONT_CAMERA", true);
        if (intent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File image = null;
            try {
                image = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File..handle
            }
            // Continue only if the File was successfully created
            if (image != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(image));
                startActivityForResult(intent, request);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_GALLERY)
            {
                Bitmap image = null;
                Uri selectedImage = data.getData();
                String mTmpGalleryPicturePath = ImageFileUtil.getPath(this, selectedImage);
                if (mTmpGalleryPicturePath == null)
                    mTmpGalleryPicturePath = selectedImage.getPath();
                imagePath = mTmpGalleryPicturePath;

            }
            else if (requestCode == REQUEST_CAMERA)
            {
                File image = null;

                try {
                    image = new File(new URI(imagePath));
                    imagePath = image.getAbsolutePath();

                } catch (URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
            if(imagePath!=null)
                Glide.with(this)
                    .load(imagePath)
                    .transform(new CircleTransform(this))
                    .into(ivAvatar);
        }


    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File img = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        imagePath = "file:" + img.getAbsolutePath();
        return img;
    }


    public void updateUser()
    {
        if(isValidEmail(etEmail.getText().toString())) {
            final ProgressDialog pd = new ProgressDialog(this);
            pd.setCancelable(false);
            pd.show();
            currentUser.setEmail(etEmail.getText().toString());
            currentUser.setInfo(etAbout.getText().toString());
            currentUser.setUsername(etUsername.getText().toString());

            if (imagePath != null) {
                Bitmap bitmap = ImageFileUtil.compress(BitmapFactory.decodeFile(imagePath, new BitmapFactory.Options()));
                FirebaseClient.getInstance().uploadImage(this,currentUser.getUuid(), bitmap, new FirebaseClient.OnDataReceivedCallback() {
                    @Override
                    public void onDataReceived(Object data, @Nullable Exception exception) {
                        if (exception != null)
                            exception.printStackTrace();
                        else if (data != null)
                            currentUser.setAvatarURL((String) data);

                        currentUser.saveUser();
                        pd.dismiss();
                        ProfileActivity.super.finish();
                    }
                });
            } else {
                currentUser.saveUser();
                pd.dismiss();
                super.finish();
            }
        }
        else
        {
            MainDialogFragment.build().setFlags(MainDialogFragment.FLAG_TWO_BTNS).setBody(getString(R.string.email_format)).setButtons("Discard","OK").setDialogClickListener(new MainDialogFragment.OnDialogClickListener() {
                @Override
                public void onActionClick() {

                }

                @Override
                public void onCancelClick() {
                    ProfileActivity.super.finish();
                }
            }).show(this);
        }
    }

    public void finish()
    {
        updateUser();
    }

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }




}
