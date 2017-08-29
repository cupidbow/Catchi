package com.envisability.catchi.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.envisability.catchi.R;
import com.envisability.catchi.dialog_fragments.MainDialogFragment;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements OnCompleteListener<AuthResult> {
    private static final String TAG = "LoginActivity";

    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    private FirebaseAuth mAuth;

    public static final int RC_SIGN_IN = 9001;

    public static CallbackManager callbackmanager;

    private GoogleApiClient mGoogleApiClient;

    public static void start(Activity activity)
    {
        activity.startActivity(new Intent(activity, LoginActivity.class));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();


    }

    @OnClick(R.id.ivClear)
    public void onClear()
    {
        etEmail.setText("");
        etPassword.setText("");
    }

    @OnClick(R.id.tvFacebook)
    public void onFacebook()
    {
        callbackmanager = CallbackManager.Factory.create();
        LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList("email","user_friends","public_profile","user_birthday"));

        LoginManager.getInstance().registerCallback(callbackmanager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                        Log.d("Facebook", "On cancel");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        Log.d("Facebook", error.toString());
                    }
                });
    }

    @OnClick(R.id.tvGoogle)
    public void onGoogle()
    {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Log.d(TAG, "Google connection failed");
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @OnClick(R.id.tvEnter)
    public void onManualLogin()
    {
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();
        if(email.length() == 0)
        {
            MainDialogFragment.build().setBody(getString(R.string.email_blank)).setButton(getString(R.string.ok)).show(this);
            return;
        }
        if(password.length() == 0)
        {
            MainDialogFragment.build().setBody(getString(R.string.password_blank)).setButton(getString(R.string.ok)).show(this);
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, this);
    }



    public void onSuccessLogin()
    {
        finish();
        HomeActivity.start(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            }
            else
            {
                MainDialogFragment.build().setBody(getString(R.string.auth_failed)).setButton(getString(R.string.ok)).show(LoginActivity.this);
            }
        }
        else
            callbackmanager.onActivityResult(requestCode, resultCode, data);
    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, this);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, this);
    }

    @Override
    public void onComplete(@NonNull Task<AuthResult> task) {
        if (task.isSuccessful()) {
            Log.d(TAG, "signInWithEmail:success");
            onSuccessLogin();
        }
        else
        {
            if(task.getException().getMessage().equalsIgnoreCase("The email address is badly formatted."))
            {
                MainDialogFragment.build().setBody(getString(R.string.email_format)).setButton(getString(R.string.ok)).show(LoginActivity.this);
            }
            else if(task.getException().getMessage().equalsIgnoreCase("There is no user record corresponding to this identifier. The user may have been deleted."))
            {
                MainDialogFragment.build().setBody(getString(R.string.email_not_registered)).setButton(getString(R.string.ok)).show(LoginActivity.this);
            }
            else if(task.getException().getMessage().equalsIgnoreCase("The password is invalid or the user does not have a password."))
            {
                MainDialogFragment.build().setBody(getString(R.string.incorrect_password)).setButton(getString(R.string.ok)).show(LoginActivity.this);
            }
            else
            {
                MainDialogFragment.build().setBody(getString(R.string.auth_failed)).setButton(getString(R.string.ok)).show(LoginActivity.this);
            }
            Log.w(TAG,  task.getException().getMessage());
        }
    }


}
