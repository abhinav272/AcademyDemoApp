package com.abhinav.academydemoapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.abhinav.academydemoapp.Model.AcademyItem;
import com.abhinav.academydemoapp.Services.AcademyDBService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by abhinavsharma on 10-03-2016.
 */
public class AddListingItem extends AppCompatActivity {

    private EditText academyName, name, website, email, mobile, description;
    private TextView location;
    private Toolbar toolbar;
    private LocationManager mlocationManager;
    private AcademyItem academyItem = new AcademyItem();
    private static final String TAG = "AddListingItem";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 2277;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_listing);
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.listing_save_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (validations()) {
            academyItem.setAcademyName(academyName.getText().toString());
            academyItem.setUser(name.getText().toString());
            academyItem.setWebsite(website.getText().toString());
            academyItem.setEmail(email.getText().toString());
            academyItem.setMobile(mobile.getText().toString());
            academyItem.setLocation(location.getText().toString());
            academyItem.setDescription(description.getText().toString());
            academyItem.setAddedOn(new Date());
//            saveImageFile();
            Intent dbIntent = new Intent(AddListingItem.this, AcademyDBService.class);
            dbIntent.putExtra("query_type", AcademyDBService.QUERY_TYPE_ADD);
            dbIntent.putExtra("item", academyItem);
            startService(dbIntent);
            Toast.makeText(AddListingItem.this, "Academy Added", Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean validations() {
        if (academyName.getText().toString().isEmpty()) {
            Snackbar.make(academyName, "Academy Name must not be empty", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if (name.getText().toString().isEmpty()) {
            Snackbar.make(academyName, "Name must not be empty", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if (website.getText().toString().isEmpty()) {
            Snackbar.make(academyName, "Website must not be empty", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if (email.getText().toString().isEmpty()) {
            Snackbar.make(academyName, "Email must not be empty", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if (mobile.getText().toString().isEmpty()) {
            Snackbar.make(academyName, "Mobile Number must not be empty", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if (description.getText().toString().isEmpty()) {
            Snackbar.make(academyName, "Description must not be empty", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        if (location.getText().toString().isEmpty()) {
            Snackbar.make(academyName, "Please select the location", Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void initialize() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Academy");
//        image = (ImageView) findViewById(R.id.image);
        academyName = (EditText) findViewById(R.id.academy_name);
        name = (EditText) findViewById(R.id.name);
        website = (EditText) findViewById(R.id.link);
        email = (EditText) findViewById(R.id.email);
        mobile = (EditText) findViewById(R.id.mobile);
        description = (EditText) findViewById(R.id.description);
        location = (TextView) findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
                    if (ContextCompat.checkSelfPermission(AddListingItem.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(AddListingItem.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                    } else {
                        carryWithLocationDialog();
                    }
                } else{
                    carryWithLocationDialog();
                }
            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showLocationChooser();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showLocationChooser() {
        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .setFilter(typeFilter)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            GooglePlayServicesUtil
                    .getErrorDialog(e.getConnectionStatusCode(), this, 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            // Handle the exception
        }
    }

    private void carryWithLocationDialog(){
        if (checkPlayServices()) {
            mlocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (!mlocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                showGPSDisabledAlertToUser();

            } else {
                showLocationChooser();
            }
        } else
            Toast.makeText(AddListingItem.this, "Location feature not available on device", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                Log.i(TAG, "Place: " + place.getName()+"~"+place.getAddress());
                location.setText(place.getAddress());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }


    }


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(this, "This device does not support google play services.", Toast.LENGTH_LONG).show();
            }
            return false;
        }
        return true;
    }

    public void showGPSDisabledAlertToUser() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Location Services Not Active");
        builder.setMessage("Please enable Location Services and GPS");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        });

        Dialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();

    }



}
