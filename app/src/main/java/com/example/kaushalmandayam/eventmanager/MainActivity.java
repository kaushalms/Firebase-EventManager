package com.example.kaushalmandayam.eventmanager;

import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Kaushal.Mandayam on 8/2/2016.
 */
public class MainActivity extends AppCompatActivity {

    static final int CUSTOM_DIALOG = 1;
    private static final String TAG = "MainActivity ";
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.app_bar_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null || AccessToken.getCurrentAccessToken() == null) {
            startActivity(new Intent(this, LoginActivity.class));
            Toast.makeText(MainActivity.this, user.toString(), Toast.LENGTH_SHORT).show();
        } else {
            Log.d(TAG, user.getDisplayName());
            Log.d(TAG, user.getEmail());
            Log.d(TAG, user.getPhotoUrl().toString());

        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        if (fab != null) {
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(MainActivity.this, EventActivity.class));
                }
            });
        }
    }

    /*@Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            FirebaseUser user= null;
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs
            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }*/
    private void goLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logout(View view) {
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }
}