package com.truelight.cacihymn.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.truelight.cacihymn.AppDatabase;
import com.truelight.cacihymn.Models.FavoriteHymn;
import com.truelight.cacihymn.Models.Hymn;
import com.truelight.cacihymn.Models.MemoryData;
import com.truelight.cacihymn.R;
import com.uncopt.android.widget.text.justify.JustifiedTextView;

import java.util.List;

public class DetailsActivity extends AppCompatActivity {

    private JustifiedTextView justifiedTextView;
    private RelativeLayout mainscreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);


        setTitle(MemoryData.getActiveHymn().getTitle()+"");

        mainscreen = findViewById(R.id.mainscreen);
        justifiedTextView = findViewById(R.id.section_label);
        String body = MemoryData.getActiveHymn().getBody();

        int fontsize = 16;


        if (MemoryData.getFontSize(DetailsActivity.this) != null ){

            fontsize = MemoryData.getFontSize(DetailsActivity.this);

            justifiedTextView.setTextSize(Float.parseFloat(fontsize +""));


            Log.d("This class name ", "font size" + fontsize);
        }

        justifiedTextView.setText(body);

        Log.d("Check value  ",MemoryData.getIsNightMode(DetailsActivity.this)+"");

        changeTheme(MemoryData.getIsNightMode(DetailsActivity.this));




        Log.d("Check value ",MemoryData.getIsNightMode(DetailsActivity.this)+"");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    void saveHymn(){
       // AppDatabase db = AppDatabase.getAppDatabase(DetailsActivity.this);

        FavoriteHymn favoriteHymn = new FavoriteHymn();
        favoriteHymn.setBody(MemoryData.getActiveHymn().getBody());
        favoriteHymn.setTitle(MemoryData.getActiveHymn().getTitle());
        favoriteHymn.setId(MemoryData.getActiveHymn().getId());

        new InsertHymnsAsync(favoriteHymn).execute();
    }

    class InsertHymnsAsync extends AsyncTask<Void, Void, Void>
    {

        FavoriteHymn hymnsList;

        InsertHymnsAsync(FavoriteHymn hymnsList){

            this.hymnsList = hymnsList;

        }



        protected void onPreExecute (){
            super.onPreExecute();

        }

        protected Void doInBackground(Void... unused) {

            AppDatabase db = AppDatabase.getAppDatabase(DetailsActivity.this);


            FavoriteHymn product1 = db.getFavoriteHymnDao().findFavoriteHymnByIDNow(this.hymnsList.getId());

            if (product1 == null)
                db.getFavoriteHymnDao().insert(this.hymnsList);
            else
                db.getFavoriteHymnDao().update(this.hymnsList);


           // db.getFavoriteHymnDao().update( this.hymnsList);
            return null;
        }

        protected void onProgressUpdate(Integer...a){

        }

        protected void onPostExecute(Void result) {


        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:

                onBackPressed();

                //this.finish();
                //Toast.makeText(getApplicationContext(),"Back button clicked", Toast.LENGTH_SHORT).show();

                 return  true;

                //break;

            case  R.id.nitemode:

                if (MemoryData.isIsNighMode()){

                    MemoryData.setIsNighMode(false);
                    changeTheme(MemoryData.isIsNighMode());
                }else{
                    MemoryData.setIsNighMode(true);
                    changeTheme(MemoryData.isIsNighMode());
                }




                break;


            case  R.id.bookmark:



                saveHymn();
                Toast.makeText(DetailsActivity.this,"Dwom saved to favorites",Toast.LENGTH_SHORT).show();



                break;
        }
        return super.onOptionsItemSelected(item);

    }



    @SuppressLint("LongLogTag")
    private void changeTheme(Boolean value){

        Log.d("Value is night mode ",value+"");
        Log.d("Value is night mode  raw",MemoryData.getIsNightMode(DetailsActivity.this)+"");

        if(value){

            mainscreen.setBackgroundColor( getResources().getColor(android.R.color.black) );
            justifiedTextView.setBackgroundColor( getResources().getColor(android.R.color.black)  );
            justifiedTextView.setTextColor( getResources().getColor(android.R.color.white));
        }else{
            mainscreen.setBackgroundColor( getResources().getColor(android.R.color.white) );
            justifiedTextView.setBackgroundColor( getResources().getColor(android.R.color.white));
            justifiedTextView.setTextColor( getResources().getColor(android.R.color.black));

        }

    }

}
