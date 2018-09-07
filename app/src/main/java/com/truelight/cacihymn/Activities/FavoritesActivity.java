package com.truelight.cacihymn.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.truelight.cacihymn.Adapters.FavHymnAdapter;
import com.truelight.cacihymn.Adapters.HymnAdapter;
import com.truelight.cacihymn.AppDatabase;
import com.truelight.cacihymn.Helper.ClickListener;
import com.truelight.cacihymn.Helper.MyDividerItemDecoration;
import com.truelight.cacihymn.Helper.RecyclerTouchListener;
import com.truelight.cacihymn.Helper.SwipeControllerActions;
import com.truelight.cacihymn.Models.FavoriteHymn;
import com.truelight.cacihymn.Models.Hymn;
import com.truelight.cacihymn.Models.MemoryData;
import com.truelight.cacihymn.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FavoritesActivity extends AppCompatActivity {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private List<FavoriteHymn> itemsList;
    private FavHymnAdapter mAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    SwipeController swipeController = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);



        this.setTitle("My Favorite");
        setUpAdapter();

        new getAllProductAsync().execute();
    }


    private void setUpAdapter(){





        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setEnabled(false);

        recyclerView = findViewById(R.id.recyclerView);
        itemsList = new ArrayList<>();
        mAdapter = new FavHymnAdapter(FavoritesActivity.this, itemsList);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));


        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(FavoritesActivity.this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {


                ImageButton imageButton = (ImageButton) view.findViewById(R.id.deleteButton);

                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        new AlertDialog.Builder(FavoritesActivity.this)
                                .setTitle("Confirm Action")
                                .setMessage("Do you really want to remove DWOM from favorites ?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int whichButton) {


                                        mAdapter.removeFavorites(position);



                                        new getAllProductAsync().execute();


                                        Toast.makeText(FavoritesActivity.this, "Deleted", Toast.LENGTH_SHORT).show();



                                    }})


                                .setNegativeButton(android.R.string.no, null).show();

                    }
                });



                searchView.clearFocus();
                String value,transType;



                final TextView id=(TextView)view.findViewById(R.id.hymnid);



                TextView  titleView = (TextView)view.findViewById(R.id.hymnTitle);
                titleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!id.getText().toString().isEmpty())
                            new getProductAsync(id.getText().toString()).execute();

                    }
                });


                CircleImageView imageView = (CircleImageView)view.findViewById(R.id.imageView);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (!id.getText().toString().isEmpty())
                            new getProductAsync(id.getText().toString()).execute();
                    }
                });




               //  startActivity(new Intent(FavoritesActivity.this, DetailsActivity.class));

            }

            @Override
            public void onLongClick(View view, int position) {





            }
        }));





        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView)findViewById(R.id.searchView);

        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                mAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                mAdapter.getFilter().filter(query);
                return false;
            }
        });
        //return true;



    }

    class getProductAsync extends AsyncTask<Void, Void, Void>
    {




        Hymn product;

        AppDatabase db = AppDatabase.getAppDatabase(FavoritesActivity.this);

        String TAG = getClass().getSimpleName();

        String productID ;

        getProductAsync(String productID){

            this.productID = productID;

        }

        protected void onPreExecute (){
            super.onPreExecute();

        }

        protected Void doInBackground(Void... unused) {
            product = db.getHymnDao().findHymnByIDNow(Integer.valueOf(productID));
            MemoryData.setActiveHymn(product);
            return null;
        }

        protected void onProgressUpdate(Integer...a){

        }

        protected void onPostExecute(Void result) {

            // MemoryData.activeProduct = product;
            if(MemoryData.getActiveHymn() != null)
                startActivity(new Intent(FavoritesActivity.this, DetailsActivity.class));
            else
                Toast.makeText(FavoritesActivity.this,"Hymn not found ",Toast.LENGTH_LONG).show();


        }
    }


    class getAllProductAsync extends AsyncTask<Void, Void, Void>
    {

        List<FavoriteHymn> products;

        AppDatabase db = AppDatabase.getAppDatabase(FavoritesActivity.this);

        String TAG = getClass().getSimpleName();

        protected void onPreExecute (){
            super.onPreExecute();

        }

        protected Void doInBackground(Void... unused) {

            products = db.getFavoriteHymnDao().getAllFavoriteHymns();
            MemoryData.setActiveFavHymnList(products);
            return null;
        }

        protected void onProgressUpdate(Integer...a){

        }

        protected void onPostExecute(Void result) {

            if (products.size() <= 0){

               // fetchHymns();
            }else{

                itemsList.clear();
                itemsList.addAll(products);
                mAdapter.notifyDataSetChanged();
            }




        }
    }



}
