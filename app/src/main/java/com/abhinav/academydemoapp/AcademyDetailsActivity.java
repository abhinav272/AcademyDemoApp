package com.abhinav.academydemoapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.abhinav.academydemoapp.Model.AcademyItem;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

/**
 * Created by abhinavsharma on 10-03-2016.
 */
public class AcademyDetailsActivity extends AppCompatActivity {

    private static final String TAG = "AcademyDetailsActivity";
    private TextView user, website, email, mobile, location, description;
    private Toolbar toolbar;
    private AcademyItem item;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.academy_details_activity);
        initialize();
    }

    private void initialize() {
        item = getIntent().getParcelableExtra("item");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        user = (TextView) findViewById(R.id.user);
        website = (TextView) findViewById(R.id.link);
        mobile = (TextView) findViewById(R.id.mobile);
        location = (TextView) findViewById(R.id.location);
        email = (TextView) findViewById(R.id.email);
        description = (TextView) findViewById(R.id.description);

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new LatLngfromAdress(AcademyDetailsActivity.this).execute();
            }
        });

        setupUI(item);
    }

    private void setupUI(AcademyItem item) {
        getSupportActionBar().setTitle(item.getAcademyName());
        user.setText(item.getUser());
        website.setText(item.getWebsite());
        mobile.setText(item.getMobile());
        location.setText(item.getLocation());
        email.setText(item.getEmail());
        description.setText(item.getDescription());
    }

    private LatLng getLatLongfromAddress(String address) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addressList = null;
        LatLng latLng;

        try {
            addressList = geocoder.getFromLocationName(address, 5);
            if (addressList.size() == 0) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(AcademyDetailsActivity.this, "No address found...", Toast.LENGTH_SHORT).show();
                    }
                });

                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Address location = addressList.get(0);
        latLng = new LatLng(location.getLatitude(), location.getLongitude());

        return latLng;


    }

    class LatLngfromAdress extends AsyncTask {

        Context mContext = null;
        LatLng latLng;

        public LatLngfromAdress(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(mContext, "Please Wait", "We are Loading Map");
        }

        @Override
        protected Object doInBackground(Object[] params) {
            latLng = getLatLongfromAddress(item.getLocation());

            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            progressDialog.hide();
            progressDialog.dismiss();
            if (latLng != null) {
                Intent intent = new Intent(AcademyDetailsActivity.this, MapsActivity.class);
                intent.putExtra("latlong", latLng);
                intent.putExtra("title",item.getAcademyName());
                Log.d(TAG, "Map activity initialized");
                startActivity(intent);
            }

        }
    }
}
