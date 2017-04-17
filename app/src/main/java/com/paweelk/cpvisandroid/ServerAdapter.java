package com.paweelk.cpvisandroid;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.paweelk.cpvisandroid.model.Server;
import com.paweelk.cpvisandroid.repositories.ServerRepository;

import java.util.ArrayList;

/**
 * Created by Pawel Kopec <paweelkopec@gmail.com> on 19.04.17.
 */

public class ServerAdapter extends BaseAdapter {

    private Context mContext;
    private static LayoutInflater inflater = null;
    private ArrayList<Server> mServersList;
    private ServerRepository mServerRepository;
    private static final String DEBUG_TAG = "Server Adapter";

    public ServerAdapter(Context context, ArrayList<Server> serversList, ServerRepository serverRepository) {
        // TODO Auto-generated constructor stub
        mContext =context;
        mServersList = serversList;
        mServerRepository = serverRepository;

        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mServersList.size();
    }

    @Override
    public Object getItem(int position) {
        return mServersList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mServersList.get(position).getId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View rowView = inflater.inflate(R.layout.activity_server_row, null);
        TextView textView = (TextView) rowView.findViewById(R.id.textServerName);
        LinearLayout layout = (LinearLayout) rowView.findViewById(R.id.serverLayoutRow);
        if(((ServerActivity) mContext).getPositionSelected() == position){
            layout.setBackgroundColor(mContext.getResources().getColor(R.color.serverOn) );
        }
        textView .setText(mServersList.get(position).getName());

        //OnClick
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((ServerActivity) mContext).getPositionSelected() == position ){
                    ((ServerActivity) mContext).onItemClicked(-1);
                }else{
                    ((ServerActivity) mContext).onItemClicked(position);
                }
                notifyDataSetChanged();
            }
        };
        rowView.setOnClickListener(listener );
        textView.setOnClickListener(listener );
        return rowView;
    }
    public void add(Server server) {
        new CreateServerTask().execute(server);
    }

    public void edit(Server server) {
        new UpdateServerTask().execute(server);
    }

    private class CreateServerTask extends AsyncTask<Server, Void, Server> {

        @Override
        protected Server doInBackground(Server... servers) {
            mServerRepository.create(servers[0]);
            mServersList.add(servers[0]);
            return servers[0];
        }

        @Override
        protected void onPostExecute(Server server) {
            super.onPostExecute(server);
            ((ServerActivity) mContext).doSmoothScroll(getCount() - 1);
            notifyDataSetChanged();
            Log.d(DEBUG_TAG, "Dodano nowy obiekt serwer do listy:  " + server.toString());
        }

    }

    private class UpdateServerTask extends AsyncTask<Server, Void, Server> {

        @Override
        protected Server doInBackground(Server... servers) {
            mServerRepository.update(servers[0]);
            mServersList.set(servers[0].getListPosition(), servers[0]);
            return servers[0];
        }

        @Override
        protected void onPostExecute(Server server) {
            super.onPostExecute(server);
            ((ServerActivity) mContext).doSmoothScroll(getCount() - 1);
            notifyDataSetChanged();
            Log.d(DEBUG_TAG, "Edytowano obiekt serwer:  " + server.toString());
        }

    }
}