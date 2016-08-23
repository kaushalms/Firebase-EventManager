package com.example.kaushalmandayam.eventmanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.example.kaushalmandayam.eventmanager.PlacesAutoCompleteAdapter;
import com.example.kaushalmandayam.eventmanager.RecyclerItemClickListener;
import com.example.kaushalmandayam.eventmanager.Constants;


/**
 * Created by Kaushal.Mandayam on 8/14/2016.
 */
public class SearchActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, View.OnClickListener{
        protected GoogleApiClient mGoogleApiClient;

        private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(
                new LatLng(-0, 0), new LatLng(0, 0));

        private EditText mAutocompleteView;
        private RecyclerView mRecyclerView;
        private LinearLayoutManager mLinearLayoutManager;
        private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
        ImageView delete;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            buildGoogleApiClient();
            setContentView(R.layout.activity_locationsearch);
            mAutocompleteView = (EditText)findViewById(R.id.locationEditText);

            delete=(ImageView)findViewById(R.id.crossImage);

            mAutoCompleteAdapter =  new PlacesAutoCompleteAdapter(this, R.layout.activity_searchrow,
                    mGoogleApiClient, BOUNDS_INDIA, null);

            mRecyclerView=(RecyclerView)findViewById(R.id.RecyclerView);
            mLinearLayoutManager=new LinearLayoutManager(this);
            mRecyclerView.setLayoutManager(mLinearLayoutManager);
            mRecyclerView.setAdapter(mAutoCompleteAdapter);
            delete.setOnClickListener(this);
            mAutocompleteView.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start, int before,
                                          int count) {
                    if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                        mAutoCompleteAdapter.getFilter().filter(s.toString());
                    }else if(!mGoogleApiClient.isConnected()){
                        Toast.makeText(getApplicationContext(), Constants.API_NOT_CONNECTED,Toast.LENGTH_SHORT).show();
                        Log.e(Constants.PlacesTag,Constants.API_NOT_CONNECTED);
                    }

                }

                public void beforeTextChanged(CharSequence s, int start, int count,
                                              int after) {

                }

                public void afterTextChanged(Editable s) {

                }
            });
            mRecyclerView.addOnItemTouchListener(
                    new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mAutoCompleteAdapter.getItem(position);
                            final String placeId = String.valueOf(item.placeId);
                            Log.i("TAG", "Autocomplete item selected: " + item.description);
                        /*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         */

                            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                    .getPlaceById(mGoogleApiClient, placeId);
                            placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                                @Override
                                public void onResult(PlaceBuffer places) {
                                    if(places.getCount()==1){
                                        //Do the things here on Click.....
                                        Toast.makeText(getApplicationContext(),String.valueOf(places.get(0).getName()),Toast.LENGTH_SHORT).show();

                                        Intent i = new Intent(SearchActivity.this, EventActivity.class);
                                        String placeName = String.valueOf(places.get(0).getName());
                                        i.putExtra("placeName", placeName);
                                        startActivity(i);
                                    }else {
                                        Toast.makeText(getApplicationContext(),Constants.SOMETHING_WENT_WRONG,Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            Log.i("TAG", "Clicked: " + item.description);
                            Log.i("TAG", "Called getPlaceById to get Place details for " + item.placeId);
                        }
                    })
            );
        }



        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            return true;
        }



        protected synchronized void buildGoogleApiClient() {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .build();
        }

        @Override
        public void onConnected(Bundle bundle) {
            Log.v("Google API Callback", "Connection Done");
        }

        @Override
        public void onConnectionSuspended(int i) {
            Log.v("Google API Callback", "Connection Suspended");
            Log.v("Code", String.valueOf(i));
        }

        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {
            Log.v("Google API Callback","Connection Failed");
            Log.v("Error Code", String.valueOf(connectionResult.getErrorCode()));
            Toast.makeText(this, Constants.API_NOT_CONNECTED,Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onClick(View v) {
            if(v==delete){
                mAutocompleteView.setText("");
            }

        }

        @Override
        public void onResume() {
            super.onResume();
            if (!mGoogleApiClient.isConnected() && !mGoogleApiClient.isConnecting()){
                Log.v("Google API","Connecting");
                mGoogleApiClient.connect();
            }
        }

        @Override
        public void onPause() {
            super.onPause();
            if(mGoogleApiClient.isConnected()){
                Log.v("Google API","Dis-Connecting");
                mGoogleApiClient.disconnect();
            }
        }

        @Override
        public void onBackPressed() {
            super.onBackPressed();
        }
}
