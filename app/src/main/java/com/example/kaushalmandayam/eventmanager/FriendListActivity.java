package com.example.kaushalmandayam.eventmanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Kaushal.Mandayam on 8/22/2016.
 */
public class FriendListActivity extends AppCompatActivity{

    CallbackManager callbackManager;
    AccessToken accessToken;
    Profile userProfile;
    GraphRequest pagingRequest;
    AccessTokenTracker accessTokenTracker;

    private String TAG = getClass().getSimpleName();



    FriendsAdapter friendsAdapter;
    List<Friend> friendList;

    @Bind(R.id.textViewName)
    TextView textViewName;

    @Bind(R.id.buttonLoadMore)
    Button buttonLoadMore;

    @Bind(R.id.listViewFriends)
    ListView listViewFriends;

    @Bind(R.id.imageViewProfilePic)
    ImageView imageViewProfilePic;

    @Bind(R.id.linearLayoutProfile)
    LinearLayout linearLayoutProfile;

    ConnectionDetector cd;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());

        setContentView(R.layout.activity_friendlist);
        callbackManager = CallbackManager.Factory.create();
        ButterKnife.bind(this);


        friendList = new ArrayList<>();
        friendsAdapter = new FriendsAdapter(this, R.layout.row_friends, friendList);
        listViewFriends.setAdapter(friendsAdapter);

        loadAccessToken();
        loadUserProfile();
        loadFriends();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (accessTokenTracker != null) {
            accessTokenTracker.stopTracking();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void loadUserProfile() {
        userProfile = Profile.getCurrentProfile();

        if (userProfile == null) {
            Log.d(TAG, "userProfile is null");
        } else {
            String name = userProfile.getName();
            Uri profileUri = userProfile.getProfilePictureUri(100, 100);

            Log.d(TAG, "name: " + name);
            Log.d(TAG, "profile uri: " + profileUri.getPath());

            textViewName.setText(name);

            Picasso.with(this)
                    .load(profileUri)
                    .into(imageViewProfilePic);

//            changeVisibility(View.VISIBLE);

        }

    }

    /*private void changeVisibility(int visibility){

        linearLayoutProfile.setVisibility(visibility);
        imageViewProfilePic.setVisibility(visibility);

    }*/


    private void loadAccessToken() {

        accessToken = AccessToken.getCurrentAccessToken();

        if (accessToken == null) {
            Log.d(TAG, "access token is null");
        } else {
            Log.d(TAG, "access token is not null");
            Set<String> permissions = accessToken.getPermissions();
            Log.d(TAG, "permissions: " + Arrays.toString(permissions.toArray()));
        }


    }

    private void loadFriends() {

        if (cd == null) {
            cd = new ConnectionDetector(this);
        }

        if (!cd.isConnectedToInternet()) {
            Utility.showSimpleAlertDialog(this, "Warning", "Please check your internet connection");
            return;
        }


        if (accessToken == null) {
            Log.d(TAG, "access token is null");
            return;
        }

        friendList.clear();
        friendsAdapter.notifyDataSetChanged();

        Bundle params = new Bundle();
        params.putString("fields", "name,id,email,picture.type(large)");

        new GraphRequest(
                AccessToken.getCurrentAccessToken(),
                "/me/friends",
                params,
                HttpMethod.GET,
                friendResultCallback

        ).executeAsync();


    }

    GraphRequest.Callback friendResultCallback = new GraphRequest.Callback() {
        public void onCompleted(GraphResponse response) {
                       /* handle the result */
            Log.d(TAG, "taggabe_friends: " + response.toString());
            JSONObject jsonObject = response.getJSONObject();
            try {
                JSONArray jsonArray = jsonObject.getJSONArray("data");
                JSONObject jsonObjectPaging = jsonObject.getJSONObject("paging");
                Log.d(TAG, "paging: " + jsonObjectPaging);
                Log.d(TAG, "friend list size: " + jsonArray.length());

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObjectEach = jsonArray.getJSONObject(i);
                    String name = jsonObjectEach.getString("name");
                    JSONObject jsonObjectPicture = jsonObjectEach.getJSONObject("picture");
                    JSONObject pictureData = jsonObjectPicture.getJSONObject("data");
                    String pictureUrl = pictureData.getString("url");

                    Friend friend = new Friend(name, pictureUrl);
                    friendList.add(friend);

                    Log.d(TAG, "name: " + name);
                }

                int totalItem = listViewFriends.getCount();

                friendsAdapter.notifyDataSetChanged();
                listViewFriends.smoothScrollToPosition(totalItem + 3);

                pagingRequest = response.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


}
