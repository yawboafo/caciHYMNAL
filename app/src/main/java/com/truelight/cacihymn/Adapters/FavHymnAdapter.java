package com.truelight.cacihymn.Adapters;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.truelight.cacihymn.AppDatabase;
import com.truelight.cacihymn.Models.FavoriteHymn;
import com.truelight.cacihymn.Models.Hymn;
import com.truelight.cacihymn.Models.MemoryData;
import com.truelight.cacihymn.R;
import android.widget.Filter;
import android.widget.Filterable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;


public class FavHymnAdapter extends RecyclerView.Adapter<FavHymnAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<FavoriteHymn> productList;
    private List<FavoriteHymn> productListFiltered;
    private int mini;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price,time,id;
        public CircleImageView thumbnail;
        public ImageButton imageButton;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.hymnTitle);
            id = view.findViewById(R.id.hymnid);
            thumbnail= view.findViewById(R.id.imageView);
            imageButton = view.findViewById(R.id.deleteButton);
        }
    }


    public FavHymnAdapter(Context context, List<FavoriteHymn> productList) {
        this.context = context;
        this.productList = productList;
        this.productListFiltered = productList;
    }




    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fav_hymn_item, parent, false);
        return new MyViewHolder(itemView);
    }


    public void removeFavorites(int position){

        final FavoriteHymn product = productListFiltered.get(position);

        Log.d("PrepareDelete ", "favorite found : "+product.getTitle());

        new DeleFavHymnsAsync(product).execute();
        productListFiltered.remove(position);
        MemoryData.setActiveFavHymnList(productListFiltered);
    }




    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

        final FavoriteHymn product = productListFiltered.get(position);
        holder.name.setText(""+product.getTitle());
        holder.id.setText(""+product.getId());

        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
// generate random color
        int color1 = generator.getRandomColor();
// generate color based on a key (same key returns the same color), useful for list/grid views
        //int color2 = generator.getColor(product.getId());

        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .width(60)  // width in px
                .height(60) // height in px
                .fontSize(18)
                .endConfig()
                .buildRect(product.getTitle().substring(10) +"", color1);

        holder.thumbnail.setImageDrawable(drawable);

        //CACI DWOM 2


    }

    @Override
    public int getItemCount() {
        return productListFiltered.size();
    }



    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    productListFiltered = productList;
                } else {
                    List<FavoriteHymn> filteredList = new ArrayList<>();
                    for (FavoriteHymn row : productList) {

                        if (row.getTitle().toLowerCase().contains(charString.toLowerCase()) || row.getBody().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    productListFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = productListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                productListFiltered = (ArrayList<FavoriteHymn>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    class DeleFavHymnsAsync extends AsyncTask<Void, Void, Void>
    {

        FavoriteHymn hymnsList;

        DeleFavHymnsAsync(FavoriteHymn hymnsList){

            this.hymnsList = hymnsList;

        }



        protected void onPreExecute (){
            super.onPreExecute();

        }

        protected Void doInBackground(Void... unused) {

            AppDatabase db = AppDatabase.getAppDatabase(context);


            FavoriteHymn product1 = db.getFavoriteHymnDao().findFavoriteHymnByIDNow(this.hymnsList.getId());

            Log.d("Delete Layer", "Delete title in do background : " +product1.getTitle());

            if (product1 != null)
              db.getFavoriteHymnDao().delete(product1);




            // db.getFavoriteHymnDao().update( this.hymnsList);
            return null;
        }

        protected void onProgressUpdate(Integer...a){

        }

        protected void onPostExecute(Void result) {


        }
    }
}

