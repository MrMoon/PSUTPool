package com.psut.pool.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.psut.pool.Models.Offers;
import com.psut.pool.R;

import java.util.ArrayList;

public class OffersAdapter extends ArrayAdapter<Offers> {

    //Global Variables and Objects:
    private Context context;
    private ArrayList<Offers> offers = new ArrayList<>();

    public OffersAdapter(Context context, int resource, Context context1, ArrayList<Offers> offers) {
        super(context, 0, resource);
        this.context = context1;
        this.offers = offers;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_listview, parent, false);

            Offers offer = offers.get(position);

            ImageView imageView = view.findViewById(R.id.imgListView);
            imageView.setImageResource(offer.getImageView());

            TextView textView = view.findViewById(R.id.txtListView);
            textView.setText(offer.getTitle());
        }
        return view;
    }
}
