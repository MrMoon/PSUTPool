package com.psut.pool.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.psut.pool.Models.Notifications;
import com.psut.pool.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationsAdapter extends ArrayAdapter<Notifications> {

    //Global Variables and Objects:
    private Context context;
    private List<Notifications> notificationsList = new ArrayList<>();

    public NotificationsAdapter(@NonNull Context context, @SuppressLint("SupportAnnotationUsage") @LayoutRes ArrayList<Notifications> list) {
        super(context, 0, list);
        this.context = context;
        this.notificationsList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.layout_listview, parent, false);

            Notifications notifications = notificationsList.get(position);

            ImageView imageView = listItem.findViewById(R.id.imgListView);
            imageView.setImageResource(notifications.getImgDrawable());

            TextView textView = listItem.findViewById(R.id.txtListView);
            textView.setText(notifications.getTxt());
        }
        return listItem;
    }
}
