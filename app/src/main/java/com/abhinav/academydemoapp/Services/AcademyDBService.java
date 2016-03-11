package com.abhinav.academydemoapp.Services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.abhinav.academydemoapp.Database.AcademyDBHelper;
import com.abhinav.academydemoapp.Model.AcademyItem;

/**
 * Created by abhinavsharma on 10-03-2016.
 */
public class AcademyDBService extends IntentService {

    public AcademyItem item;
    private static final String TAG = "AcademyDBService";
    public static final String QUERY_TYPE_ADD = "Add";
    public static final String QUERY_TYPE_DELETE = "Delete";
    public static final String QUERY_TYPE_UPDATE = "Update";

    public AcademyDBService() {
        super("AcademyDBService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        switch(intent.getStringExtra("query_type")){
            case QUERY_TYPE_ADD:
                item = intent.getParcelableExtra("item");
                long rowId = AcademyDBHelper.addItem(AcademyDBHelper.getInstance(getBaseContext()),item);
                Log.d(TAG,QUERY_TYPE_ADD+": Result Added at -- "+rowId);
                break;
            case QUERY_TYPE_DELETE:
                break;
            case QUERY_TYPE_UPDATE:
                break;
        }
    }
}
