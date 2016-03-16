package com.abhinav.academydemoapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.abhinav.academydemoapp.Adapter.CustomRecyclerViewAdapter;
import com.abhinav.academydemoapp.Database.AcademyDBHelper;
import com.abhinav.academydemoapp.Model.AcademyItem;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

/**
 * Created by abhinavsharma on 09-03-2016.
 */
public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton fab;
    private RecyclerView lview;
    private Cursor allItems;
    private ArrayList<AcademyItem> academyItems;
    private AcademyItem item;
    private CustomRecyclerViewAdapter customRecyclerViewAdapter;
    private Uri photoUrl;
    private ImageView userImage;
    private Toolbar toolbar;
    private ArrayList<AcademyItem> offsetList = new ArrayList<AcademyItem>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        final MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_activity_menu, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                offsetList.clear();
                for(int i=0; i<academyItems.size(); i++){
                    if(academyItems.get(i).getAcademyName().toLowerCase().contains(newText.toLowerCase())
                            ||academyItems.get(i).getLocation().toLowerCase().contains(newText.toLowerCase())){
                        offsetList.add(academyItems.get(i));
                    }
                }
                refreshAdapter(offsetList);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.sort:
                Collections.sort(academyItems, new Comparator<AcademyItem>() {
                    @Override
                    public int compare(AcademyItem lhs, AcademyItem rhs) {
                        return lhs.getAcademyName().compareTo(rhs.getAcademyName());
                    }
                });
                refreshAdapter(academyItems);
                break;
            case R.id.search:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshAdapter(ArrayList<AcademyItem> offsetList){
        customRecyclerViewAdapter = new CustomRecyclerViewAdapter(HomeActivity.this,offsetList);
        lview.setAdapter(customRecyclerViewAdapter);
        lview.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
        customRecyclerViewAdapter.SetOnItemClickListener(new CustomRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(HomeActivity.this,AcademyDetailsActivity.class);
                intent.putExtra("item",academyItems.get(position));
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        allItems = AcademyDBHelper.getAllItems(AcademyDBHelper.getInstance(this));
        academyItems = buildListItems(allItems);
        customRecyclerViewAdapter = new CustomRecyclerViewAdapter(this,academyItems);
        lview.setAdapter(customRecyclerViewAdapter);
        lview.setLayoutManager(new LinearLayoutManager(this));
        customRecyclerViewAdapter.SetOnItemClickListener(new CustomRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(HomeActivity.this,AcademyDetailsActivity.class);
                intent.putExtra("item",academyItems.get(position));
                startActivity(intent);
            }
        });
        super.onStart();
    }

    private void initialize(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        photoUrl = getIntent().getParcelableExtra("photoUrl");
        userImage = (ImageView) findViewById(R.id.user_image);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        lview = (RecyclerView) findViewById(R.id.lview);
        if(photoUrl!=null){
            Picasso.with(this).load(photoUrl).fit().centerCrop().into(userImage);
        }
        fab.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                startActivity(new Intent(HomeActivity.this,AddListingItem.class));
                break;
        }
    }

    private ArrayList<AcademyItem> buildListItems(Cursor allItems){
        academyItems =  new ArrayList<>();
        while(allItems.moveToNext()){
            item = new AcademyItem();
            item.setAcademyName(allItems.getString(allItems.getColumnIndex(AcademyDBHelper.TableEntries.COL_ACADEMY_NAME)));
            item.setUser(allItems.getString(allItems.getColumnIndex(AcademyDBHelper.TableEntries.COL_ACADEMY_USER)));
            item.setMobile(allItems.getString(allItems.getColumnIndex(AcademyDBHelper.TableEntries.COL_ACADEMY_MOBILE)));
            item.setEmail(allItems.getString(allItems.getColumnIndex(AcademyDBHelper.TableEntries.COL_ACADEMY_EMAIL)));
            item.setLocation(allItems.getString(allItems.getColumnIndex(AcademyDBHelper.TableEntries.COL_ACADEMY_LOCATION)));
            item.setWebsite(allItems.getString(allItems.getColumnIndex(AcademyDBHelper.TableEntries.COL_ACADEMY_WEBSITE)));
            item.setDescription(allItems.getString(allItems.getColumnIndex(AcademyDBHelper.TableEntries.COL_ACADEMY_DESCRIPTION)));
            SimpleDateFormat df = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
            try {
                Date dd = df.parse(allItems.getString(allItems.getColumnIndex(AcademyDBHelper.TableEntries.COL_RECORD_ADDED_ON)));
                item.setAddedOn(dd);
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            Date date = new Date(allItems.getString(allItems.getColumnIndex(AcademyDBHelper.TableEntries.COL_RECORD_ADDED_ON)));
//            item.setAddedOn(date);
            academyItems.add(item);
        }
        return academyItems;
    }


}
