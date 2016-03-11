package com.abhinav.academydemoapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.abhinav.academydemoapp.Model.AcademyItem;

/**
 * Created by abhinavsharma on 10-03-2016.
 */
public class AcademyDBHelper extends SQLiteOpenHelper {

    private static AcademyDBHelper mAcademyDBHelper = null;
    private static SQLiteDatabase academyDatabase;
    private static final String TAG = "AcademyDBHelper";
    private static final String QUERY_CREATE_TABLE = "CREATE TABLE " + TableEntries.TABLE_NAME +
            "("
            + TableEntries._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + TableEntries.COL_ACADEMY_NAME + " TEXT, "
            + TableEntries.COL_ACADEMY_USER + " TEXT, "
            + TableEntries.COL_ACADEMY_WEBSITE + " TEXT, "
            + TableEntries.COL_ACADEMY_EMAIL + " TEXT, "
            + TableEntries.COL_ACADEMY_MOBILE + " TEXT, "
            + TableEntries.COL_RECORD_ADDED_ON + " TEXT, "
            + TableEntries.COL_IMAGE_PATH + " TEXT, "
            + TableEntries.COL_ACADEMY_LOCATION + " TEXT, "
            + TableEntries.COL_ACADEMY_DESCRIPTION + " TEXT"
            + " )";
    private static final String DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TableEntries.TABLE_NAME;
    private static final String GET_ALL_ITEMS = "SELECT * FROM " + TableEntries.TABLE_NAME;

    public static final class TableEntries implements BaseColumns {

        public static final String DATABASE_NAME = "ACADEMYDATABASE";
        public static final String TABLE_NAME = "ACADEMY";
        public static final String COL_ACADEMY_NAME = "ACADEMY_NAME";
        public static final String COL_ACADEMY_USER = "ACADEMY_USER";
        public static final String COL_ACADEMY_WEBSITE = "ACADEMY_WEBSITE";
        public static final String COL_ACADEMY_LOCATION = "ACADEMY_LOCATION";
        public static final String COL_ACADEMY_EMAIL = "ACADEMY_EMAIL";
        public static final String COL_ACADEMY_MOBILE = "ACADEMY_MOBILE";
        public static final String COL_ACADEMY_DESCRIPTION = "ACADEMY_DESCRIPTION";
        public static final String COL_RECORD_ADDED_ON = "RECORD_ADDED_ON";
        public static final String COL_IMAGE_PATH = "IMAGE_PATH";
        public static final int DB_VERSION = 1;
    }

    public static AcademyDBHelper getInstance(Context context) {
        if (mAcademyDBHelper == null) {
            mAcademyDBHelper = new AcademyDBHelper(context, TableEntries.DATABASE_NAME, null, TableEntries.DB_VERSION);
            Log.d(TAG, "DatabaseHelper object created");
        }
        return mAcademyDBHelper;
    }

    private AcademyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        Log.d(TAG, "Database created");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(QUERY_CREATE_TABLE);
        Log.d(TAG, "Table created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //code to upgrade database
    }

    public static Cursor getAllItems(AcademyDBHelper mAcademyDBHelper) {
        academyDatabase = mAcademyDBHelper.getReadableDatabase();
        Cursor cursor = academyDatabase.rawQuery(GET_ALL_ITEMS, null);
        Log.d(TAG, "Items list returned");
        return cursor;
    }

    public static long addItem(AcademyDBHelper mAcademyDBHelper, AcademyItem item) {
        academyDatabase = mAcademyDBHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(TableEntries.COL_ACADEMY_NAME,item.getAcademyName());
        cv.put(TableEntries.COL_ACADEMY_USER,item.getUser());
        cv.put(TableEntries.COL_ACADEMY_EMAIL,item.getEmail());
        cv.put(TableEntries.COL_ACADEMY_MOBILE,item.getMobile());
        cv.put(TableEntries.COL_ACADEMY_LOCATION,item.getLocation());
        cv.put(TableEntries.COL_ACADEMY_WEBSITE,item.getWebsite());
        cv.put(TableEntries.COL_ACADEMY_DESCRIPTION,item.getDescription());
        cv.put(TableEntries.COL_IMAGE_PATH,item.getImagePath());
        cv.put(TableEntries.COL_RECORD_ADDED_ON,item.getAddedOn().toString());

        long newRowId = academyDatabase.insert(TableEntries.TABLE_NAME, null, cv);
        Log.d(TAG, "Item added successfully");
        return newRowId;
    }

    public static int deleteItem(int id){
        int rowsAffected = -1;
        if(id!=-1){
            academyDatabase = mAcademyDBHelper.getWritableDatabase();
            String sID = String.valueOf(id);
            rowsAffected = academyDatabase.delete(TableEntries.TABLE_NAME, TableEntries._ID + " = ?", new String[]{sID});
            Log.d(TAG, "Record Deleted from AccountDetails at ID: "
                    + id
                    + "Number of rows affected: "
                    + rowsAffected);
        }
        return rowsAffected;
    }
}
