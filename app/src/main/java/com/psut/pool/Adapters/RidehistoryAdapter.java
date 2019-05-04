package com.psut.pool.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.psut.pool.Models.RidehistoryModel;
import com.psut.pool.R;

import java.util.ArrayList;

public class RidehistoryAdapter extends RecyclerView.Adapter<RidehistoryAdapter.ViewHolder> {
    Context context;
    private ArrayList<RidehistoryModel> ridehistoryModelArrayList;

    public RidehistoryAdapter(Context context, ArrayList<RidehistoryModel> ridehistoryModelArrayList) {
        this.context = context;
        this.ridehistoryModelArrayList = ridehistoryModelArrayList;
    }

    @NonNull
    @Override
    public RidehistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ride_history_icab, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RidehistoryAdapter.ViewHolder holder, int position) {
        holder.i1.setImageResource(ridehistoryModelArrayList.get(position).getI1());
        holder.i2.setImageResource(ridehistoryModelArrayList.get(position).getI2());
        holder.i3.setImageResource(ridehistoryModelArrayList.get(position).getI3());
        holder.txtmall.setText(ridehistoryModelArrayList.get(position).getTxtmall());
        holder.txtmall.setMovementMethod(new ScrollingMovementMethod());

        holder.txthome.setText(ridehistoryModelArrayList.get(position).getTxthome());
        holder.txthome.setMovementMethod(new ScrollingMovementMethod());

        holder.txtdate.setText(ridehistoryModelArrayList.get(position).getTxtdate());
        holder.txtdate.setMovementMethod(new ScrollingMovementMethod());

        holder.txtprice.setText(ridehistoryModelArrayList.get(position).getTxtprice());
        holder.txtprice.setMovementMethod(new ScrollingMovementMethod());

        holder.txtDriverID.setText(ridehistoryModelArrayList.get(position).getTxtDriverID());
        holder.txtDriverID.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return ridehistoryModelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView i1, i2, i3;
        TextView txtmall, txthome, txtdate, txtprice, txtDriverID;

        public ViewHolder(View itemView) {
            super(itemView);
            i1 = itemView.findViewById(R.id.i1);
            i2 = itemView.findViewById(R.id.i2);
            i3 = itemView.findViewById(R.id.i3);
            txtmall = itemView.findViewById(R.id.txtmall);
            txthome = itemView.findViewById(R.id.txthome);
            txtdate = itemView.findViewById(R.id.txtdate);
            txtprice = itemView.findViewById(R.id.txtprice);
            txtDriverID = itemView.findViewById(R.id.txtDriverIDHistory);
        }
    }
}