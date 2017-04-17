package com.paweelk.cpvisandroid;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.paweelk.cpvisandroid.model.Server;
import com.paweelk.cpvisandroid.repositories.ServerRepository;
import com.paweelk.cpvisandroid.service.ApiService;
import com.paweelk.cpvisandroid.service.GetImageTask;

import java.util.ArrayList;

/**
 * Created by Pawel Kopec <paweelkopec@gmail.com> on 05.04.17.
 */

class MainPagerAdapter extends PagerAdapter {

    private final ArrayList<Server> serversList;
    Context context;
    LayoutInflater mLayoutInflater;
    private static final String DEBUG_TAG = "Pager Adapter";
    private int currentPosition = -1;

    public MainPagerAdapter(Context context, ArrayList<Server> serversList, ServerRepository serverRepository) {
        this.context = context;
        this.serversList = serversList;
        mLayoutInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return serversList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Log.i(DEBUG_TAG, "Title " + String.valueOf(position));
        return this.serversList.get(position).getName();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);
        ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
        //new GetImageTask(imageView).execute(serversList.get(position));
        new GetImageTask(context, new ApiService(serversList.get(position))).execute(imageView);
        currentPosition = position;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.i(DEBUG_TAG, "Kliknięto w obrazek, pozycja elementu: " + String.valueOf(currentPosition));
                Intent intent = new Intent(context, VisualizationActivity.class);
                Log.i(DEBUG_TAG, "ServerId: " + String.valueOf(serversList.get(currentPosition).getId()));
                intent.putExtra(VisualizationActivity.EXTRA_SERVER_ID, serversList.get(currentPosition).getId());
                intent.putExtra(VisualizationActivity.EXTRA_SERVER_NAME, serversList.get(currentPosition).getName());
                intent.putExtra(VisualizationActivity.EXTRA_SERVER_URL, serversList.get(currentPosition).getUrl());
                intent.putExtra(VisualizationActivity.EXTRA_SERVER_USERNAME, serversList.get(currentPosition).getUsername());
                intent.putExtra(VisualizationActivity.EXTRA_SERVER_PASSWORD, serversList.get(currentPosition).getPassword());

                context.startActivity(intent);
            }
        });
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}