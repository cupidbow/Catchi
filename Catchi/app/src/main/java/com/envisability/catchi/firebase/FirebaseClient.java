package com.envisability.catchi.firebase;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.envisability.catchi.utils.ImageFileUtil;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.Map;

/**
 * Created by Тарас on 06.06.2017.
 */
public class FirebaseClient {
    FirebaseDatabase database;
    FirebaseStorage storage;
    private static FirebaseClient _client;

    private FirebaseClient()
    {
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public static FirebaseClient getInstance()
    {
        if(_client==null)
            _client = new FirebaseClient();
        return _client;
    }

    void getUserByUID(final String uuid, final OnDataReceivedCallback callback)
    {
        DatabaseReference ref = database.getReference("Users").child(uuid);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null) {
                    callback.onDataReceived(null, new Exception("No user with this uuid"));
                    return;
                }
                Log.d("save user",dataSnapshot.toString());

                String email = null,
                        username = null,
                        info = null,
                        avatar = null;
                if(dataSnapshot.hasChild("email"))
                    email = (String) dataSnapshot.child("email").getValue();
                if(dataSnapshot.hasChild("username"))
                    username = (String) dataSnapshot.child("username").getValue();
                if(dataSnapshot.hasChild("info"))
                    info = (String) dataSnapshot.child("info").getValue();
                if(dataSnapshot.hasChild("avatarUrl"))
                    avatar = (String) dataSnapshot.child("avatarUrl").getValue();
                FirebaseUser user = new FirebaseUser(uuid, email, username, info, avatar, null,null,null );
                callback.onDataReceived(user,null);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
               callback.onDataReceived(null, databaseError.toException());
            }
        });
    }

    void saveUser(String userId, Map<String, Object> dataToSave)
    {
        DatabaseReference ref = database.getReference("Users").child(userId);
        ref.updateChildren(dataToSave, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

            }
        });
    }

    public void uploadImage(Context context,String uuid, Bitmap bitmap, final OnDataReceivedCallback callback)
    {
        File file = ImageFileUtil.saveCompressed(context, bitmap, "imageProfile.jpg", 200 * 1024);
        StorageReference storageReference = storage.getReference("profileImages");
        UploadTask task = storageReference.child(uuid).putFile(Uri.fromFile(file));
        task.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                callback.onDataReceived(task.getResult().getDownloadUrl().toString(),task.getException());
            }
        });
    }



    public interface OnDataReceivedCallback
    {
        void onDataReceived(Object data, @Nullable Exception exception);
    }




}
