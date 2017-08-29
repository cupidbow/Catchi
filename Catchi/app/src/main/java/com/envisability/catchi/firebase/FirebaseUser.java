package com.envisability.catchi.firebase;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.firebase.auth.*;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Тарас on 06.06.2017.
 */
public class FirebaseUser {
    private String username;
    private String info;
    private String email;
    private String uuid;
    private String avatarURL;
    private ArrayList<String> videos, followings, followers;
    private static FirebaseUser _currentUser;

    public FirebaseUser(String uuid, String email, String username, String info, String avatarURL, ArrayList<String> videos,ArrayList<String> followers,ArrayList<String> followings )
    {
        this.uuid = uuid;
        this.email = email;
        this.username = username;
        this.info = info;
        this.avatarURL = avatarURL;
        this.videos = videos;
        this.followers = followers;
        this.followings = followings;
    }

    public FirebaseUser(com.google.firebase.auth.FirebaseUser user)
    {
        username = user.getDisplayName();
        if(username!=null)
            username = username.toLowerCase().replace(" ","_");
        email = user.getEmail();
        if(user.getPhotoUrl()!=null)
            avatarURL = user.getPhotoUrl().toString();
        videos = null;
        followings = null;
        followers = null;
        uuid = user.getUid();
    }

    public static void getCurrentUser(final UserCallback callback)
    {
        if(_currentUser!=null)
            callback.onUserReceived(_currentUser, null);
        FirebaseClient.getInstance().getUserByUID(FirebaseAuth.getInstance().getCurrentUser().getUid(), new FirebaseClient.OnDataReceivedCallback() {
            @Override
            public void onDataReceived(Object data, Exception exception) {

                if(exception==null)
                {
                    Log.d("save user","received 1");
                    _currentUser = (FirebaseUser)data;
                    callback.onUserReceived(_currentUser,null);
                }
                else if(!(exception instanceof DatabaseException))
                {
                    Log.d("save user","received 2");
                    _currentUser = new FirebaseUser(FirebaseAuth.getInstance().getCurrentUser());
                    callback.onUserReceived(_currentUser,null);
                    _currentUser.saveUser();
                }
                else
                {
                    Log.d("save user","received 3", exception);
                    callback.onUserReceived(null,exception);
                }

            }
        });
    }

    public void saveUser()
    {
        Log.d("save user","try to save");
        HashMap<String, Object> data = new HashMap<>();
        data.put("email",email);
        data.put("info",info);
        data.put("username",username);
        data.put("avatarUrl",avatarURL);
        FirebaseClient.getInstance().saveUser(uuid,data);

    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getInfo() {
        return info;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public void setAvatarURL(String avatarURL) {
        this.avatarURL = avatarURL;
    }

    public String getUuid() {
        return uuid;
    }

    public interface UserCallback
    {
        public void onUserReceived(FirebaseUser user, Exception exception);
    }
}
