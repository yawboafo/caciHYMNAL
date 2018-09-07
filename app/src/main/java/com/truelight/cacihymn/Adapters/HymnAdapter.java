package com.truelight.cacihymn.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
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


public class HymnAdapter extends RecyclerView.Adapter<HymnAdapter.MyViewHolder> implements Filterable {
    private Context context;
    private List<Hymn> productList;
    private List<Hymn> productListFiltered;
    private int mini;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price,time,id;
        public CircleImageView thumbnail;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.hymnTitle);
            id = view.findViewById(R.id.hymnid);
            thumbnail= view.findViewById(R.id.imageView);
        }
    }


    public HymnAdapter(Context context, List<Hymn> productList) {
        this.context = context;
        this.productList = productList;
        this.productListFiltered = productList;
    }



    public HymnAdapter(Context context, List<Hymn> productList,int mini) {
        this.context = context;
        this.productList = productList;
        this.mini = mini;
        this.productListFiltered = productList;

    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;

        itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.hymn_item, parent, false);
        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {


        final SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");

        final Hymn product = productListFiltered.get(position);
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
                    List<Hymn> filteredList = new ArrayList<>();
                    for (Hymn row : productList) {

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
                productListFiltered = (ArrayList<Hymn>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }
}

