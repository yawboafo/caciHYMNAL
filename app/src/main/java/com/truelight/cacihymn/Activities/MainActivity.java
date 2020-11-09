package com.truelight.cacihymn.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.truelight.cacihymn.Adapters.HymnAdapter;
import com.truelight.cacihymn.AppDatabase;
import com.truelight.cacihymn.Helper.ClickListener;
import com.truelight.cacihymn.Helper.MyDividerItemDecoration;
import com.truelight.cacihymn.Helper.RecyclerTouchListener;
import com.truelight.cacihymn.Http.APIRequest;
import com.truelight.cacihymn.Http.Hymresponse;
import com.truelight.cacihymn.Models.Hymn;
import com.truelight.cacihymn.Models.MemoryData;
import com.truelight.cacihymn.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private APIRequest apiRequest;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private List<Hymn> itemsList;
    private HymnAdapter mAdapter;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swipeRefreshLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        apiRequest = new APIRequest();
        setUpAdapter();

        new getAllProductAsync().execute();
        //fetchHymns();
    }


    private void setUpAdapter(){






        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        Log.i("TAG", "onRefresh called from SwipeRefreshLayout");

                        // This method performs the actual data-refresh operation.
                        // The method calls setRefreshing(false) when it's finished.
                        fetchHymnSilent();
                    }
                }
        );
        recyclerView = findViewById(R.id.recyclerView);
        itemsList = new ArrayList<>();
        mAdapter = new HymnAdapter(MainActivity.this, itemsList);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new MyDividerItemDecoration(this, LinearLayoutManager.VERTICAL, 16));


        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(MainActivity.this, recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                searchView.clearFocus();
                String value,transType;
                TextView id=(TextView)view.findViewById(R.id.hymnid);


                if (!id.getText().toString().isEmpty())
                    new getProductAsync(id.getText().toString()).execute();
             //   startActivity(new Intent(MainActivity.this, DetailsActivity.class));

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        /**searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                mAdapter.getFilter().filter(s);
                return false;
            }
        });
        **/


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


      //  searchView.setBackgroundResource(R.drawable.bg_white_rounded);


    }
    @Override
    protected void onResume() {
        super.onResume();
        //searchView.setIconified(false);
       // searchView.requestFocus();

        //InputMethodManager imm = (InputMethodManager)this.getSystemService(Service.INPUT_METHOD_SERVICE);

      //  imm.hideSoftInputFromWindow(searchView.getWindowToken(), 0);


        hideKeyboard(MainActivity.this);


    }

    public static void hideKeyboard(Activity activity) {
        View view = activity.findViewById(R.id.searchView);
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        hideKeyboard(MainActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater findMenuItems = getMenuInflater();
        findMenuItems.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.settings:
                startActivity(new Intent(MainActivity.this,SettingActivity.class));
                return true;

            case R.id.favorites :

                startActivity(new Intent(MainActivity.this,FavoritesActivity.class));
                return  true;

            case  R.id.refresh:

                Toast.makeText(MainActivity.this
                ,"Refreshing hymns",Toast.LENGTH_SHORT).show();
                swipeRefreshLayout.setRefreshing(true);
                fetchHymnSilent();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }



    void simpleMessageDailog(String title,String message){
        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
        alertDialog.setTitle(title);
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        fetchHymnSilent();
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


    private void fetchHymns() {


        progressDialog = ProgressDialog.show(this, "Loading hymns","Please Wait...", true);
        progressDialog.show();


        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, APIRequest.getAllhymns,null,
                new Response.Listener<JSONObject>()
                {





                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        progressDialog.dismiss();
                        if (response == null) {

                            simpleMessageDailog("Try Again","Poor internet connection, Please try again.");

                            Toast.makeText(MainActivity.this, "Poor internet connection, Please try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        Log.d("HYMMS", "response " + response.toString());




                        Hymresponse hymresponse = new Gson().fromJson(response.toString(), new TypeToken<Hymresponse>() {
                        }.getType());

                        itemsList.clear();
                        itemsList.addAll(hymresponse.getData());
                        new InsertHymnsAsync(hymresponse.getData()).execute();
                        Log.d("TAG", "itemsList.count: " + itemsList.size()  +" "+ hymresponse.getData().get(0).getBody());

                        mAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.dismiss();
                        simpleMessageDailog("Try Again","Poor internet connection, Please try again.");
                        // error in getting json
                        Log.e("HYMMS", "Error: " + error.getMessage());
                      //  Toast.makeText(MainActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );








        apiRequest.addToRequestQueue(request);
    }
    private void fetchHymnSilent() {




        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, APIRequest.getAllhymns,null,
                new Response.Listener<JSONObject>()
                {





                    @Override
                    public void onResponse(JSONObject response) {
                        // display response
                        swipeRefreshLayout.setRefreshing(false);
                        if (response == null) {
                            Toast.makeText(MainActivity.this, "Couldn't fetch the store items! Please try again.", Toast.LENGTH_LONG).show();
                            return;
                        }

                        Log.d("HYMMS", "response " + response.toString());




                        Hymresponse hymresponse = new Gson().fromJson(response.toString(), new TypeToken<Hymresponse>() {
                        }.getType());

                        itemsList.clear();
                        itemsList.addAll(hymresponse.getData());
                        new InsertHymnsAsync(hymresponse.getData()).execute();
                        Log.d("TAG", "itemsList.count: " + itemsList.size()  +" "+ hymresponse.getData().get(0).getBody());

                        mAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        swipeRefreshLayout.setRefreshing(false);

                        // error in getting json
                        Log.e("HYMMS", "Error: " + error.getMessage());
                        Toast.makeText(MainActivity.this, "Error: Failed to fetch hymmns " , Toast.LENGTH_SHORT).show();
                    }
                }
        );








        apiRequest.addToRequestQueue(request);
    }

    class getAllProductAsync extends AsyncTask<Void, Void, Void>
    {

        List<Hymn> products;

        AppDatabase db = AppDatabase.getAppDatabase(MainActivity.this);

        String TAG = getClass().getSimpleName();

        protected void onPreExecute (){
            super.onPreExecute();

        }

        protected Void doInBackground(Void... unused) {

            products = db.getHymnDao().getAllHymns();
            MemoryData.setActiveHymnList(products);
            return null;
        }

        protected void onProgressUpdate(Integer...a){

        }

        protected void onPostExecute(Void result) {

            if (products.size() <= 0){

                fetchHymns();
            }else{

                itemsList.clear();
                itemsList.addAll(products);
                mAdapter.notifyDataSetChanged();
            }




        }
    }
    class InsertHymnsAsync extends AsyncTask<Void, Void, Void>
    {

        List<Hymn> hymnsList;

        InsertHymnsAsync(List<Hymn> hymnsList){

            this.hymnsList = hymnsList;

        }



        protected void onPreExecute (){
            super.onPreExecute();

        }

        protected Void doInBackground(Void... unused) {

            insertHymns(hymnsList);
            return null;
        }

        protected void onProgressUpdate(Integer...a){

        }

        protected void onPostExecute(Void result) {


        }
    }

    class getProductAsync extends AsyncTask<Void, Void, Void>
    {




        Hymn product;

        AppDatabase db = AppDatabase.getAppDatabase(MainActivity.this);

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
                startActivity(new Intent(MainActivity.this, DetailsActivity.class));
            else
                Toast.makeText(MainActivity.this,"Hymn not found ",Toast.LENGTH_LONG).show();


        }
    }

    private void insertHymns(List<Hymn> products){

        AppDatabase db = AppDatabase.getAppDatabase(MainActivity.this);

        for (Hymn product : products){




            Hymn product1 = db.getHymnDao().findHymnByIDNow(product.getId());

            if (product1 == null)
                db.getHymnDao().insert(product);
            else
                db.getHymnDao().update(product);
        }



    }


}
