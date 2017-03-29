package com.paweelk.cpvisandroid;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paweelk.cpvisandroid.model.Server;
import com.paweelk.cpvisandroid.repositories.ServerRepository;

import java.util.ArrayList;

/**
 * Created by Pawel Kopec <paweelkopec@gmail.com> on 19.03.17.
 */
public class ServerAdapter  extends RecyclerView.Adapter<ServerAdapter.ViewHolder> {

    private static final String DEBUG_TAG =  "ServerAdapter";

    private Context context;
    private ArrayList<Server> serversList;
    private ServerRepository serverRepository;

    public ServerAdapter(Context context, ArrayList<Server> serversList, ServerRepository serverRepository) {
        this.context = context;
        this.serversList = serversList;
        this.serverRepository = serverRepository;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater li = LayoutInflater.from(parent.getContext());
        View v = li.inflate(R.layout.server_view_holder, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Log.i(DEBUG_TAG," onBindViewHolder "+position);
        String name = serversList.get(position).getName();
        holder.initialName.setText( name );
    }

    @Override
    public int getItemCount() {
        if(this.serversList.isEmpty()){
            return 0;
        }
        return this.serversList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView initialName;

        public ViewHolder(View itemView) {
            super(itemView);
            initialName = (TextView) itemView.findViewById(R.id.initialName);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ServerActivity) context).onItemClicked(getAdapterPosition());
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener(){

                @Override
                public boolean onLongClick(View v) {
                    ((ServerActivity) context).onItemClicked(-1);
                    return true;
                }
            });
        }
    }

    public void add(Server server){
        new CreateServerTask().execute(server);
    }

    public void edit(Server server){
        new UpdateServerTask().execute(server);
    }

    private class CreateServerTask extends AsyncTask<Server, Void, Server> {

        @Override
        protected Server doInBackground(Server... servers) {
            serverRepository.create(servers[0]);
            serversList.add(servers[0]);
            return servers[0];
        }

        @Override
        protected void onPostExecute(Server server) {
            super.onPostExecute(server);
            ((ServerActivity) context).doSmoothScroll(getItemCount() - 1);
            notifyItemInserted(getItemCount());
            Log.d(DEBUG_TAG, "Dodano nowy obiekt serwer do listy:  " + server.toString());
        }

    }

    private class UpdateServerTask extends AsyncTask<Server, Void, Server> {

        @Override
        protected Server doInBackground(Server... servers) {
            serverRepository.update(servers[0]);
            serversList.set(servers[0].getListPosition(), servers[0]);
            return servers[0];
        }

        @Override
        protected void onPostExecute(Server server) {
            super.onPostExecute(server);
            ((ServerActivity) context).doSmoothScroll(getItemCount() - 1);
            notifyItemChanged(server.getListPosition());
            Log.d(DEBUG_TAG, "Edytowano obiekt serwer:  " + server.toString());
        }

    }


}
