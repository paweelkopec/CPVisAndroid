package com.paweelk.cpvisandroid;


import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.paweelk.cpvisandroid.model.Server;
import com.paweelk.cpvisandroid.repositories.ServerRepository;
import java.util.ArrayList;
/**
 * Created by Pawel Kopec <paweelkopec@gmail.com> on 26.03.17.
 */
public class ServerActivity extends AppCompatActivity {

    public ServerRepository serverRepository = new ServerRepository(this);
    private ArrayList<Server>  serversList;
    private ServerAdapter adapter;
    public RecyclerView recyclerView;
    private static final String DEBUG_TAG ="Server Activity";;
    public static final String EXTRA_SERVER_NAME ="serverName";
    public static final String EXTRA_SERVER_URL ="serverUrl";
    public static final String EXTRA_SERVER_USERNAME ="serverUsername";
    public static final String EXTRA_SERVER_PASSWORD ="serverPassword";
    private int positionSelected=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity act = ServerActivity.this;
                Intent transitionIntent = new Intent(act, ServerAddActivity.class);
                act.startActivityForResult(transitionIntent, adapter.getItemCount());
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        new GetServerListTask().execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_servers, menu);
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu)
    {
        Boolean visible = this.positionSelected >= 0;
        menu.findItem(R.id.action_delete).setVisible(visible);
        menu.findItem(R.id.action_edit).setVisible(visible);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_delete) {
            Server server = serversList.get(this.positionSelected);
            server.setListPosition(this.positionSelected);
            new DeleteServerTask().execute(server);
            this.positionSelected =-1;
            invalidateOptionsMenu();
        } else if(id == R.id.action_edit){
            Server server = serversList.get(this.positionSelected);
            server.setListPosition(this.positionSelected);
            Activity act = ServerActivity.this;
            Intent intent = this.fillAsExtra( new Intent( act, ServerEditActivity.class), server);
            act.startActivityForResult(intent, this.positionSelected);
        }
        Log.i(DEBUG_TAG,"Kliknięto w menu, pozycja elementu "+ String.valueOf(this.positionSelected));
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        serverRepository.open();
    }

    @Override
    protected void onPause() {
        super.onPause();
        serverRepository.close();
    }

    private Server fillFromExtra(Intent data){
        Server server = new Server();
        server.setName(data.getStringExtra(ServerActivity.EXTRA_SERVER_NAME))
                .setUrl(data.getStringExtra(ServerActivity.EXTRA_SERVER_URL))
                .setUsername(data.getStringExtra(ServerActivity.EXTRA_SERVER_USERNAME))
                .setPassword(data.getStringExtra(ServerActivity.EXTRA_SERVER_PASSWORD));
        return server;
    }

    private Intent fillAsExtra(Intent intent, Server server){
        intent.putExtra(ServerActivity.EXTRA_SERVER_NAME, server.getName());
        intent.putExtra(ServerActivity.EXTRA_SERVER_URL, server.getUrl());
        intent.putExtra(ServerActivity.EXTRA_SERVER_USERNAME, server.getUsername());
        intent.putExtra(ServerActivity.EXTRA_SERVER_PASSWORD, server.getPassword());
        return intent;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        serverRepository.open();
        Log.d(DEBUG_TAG, "request code ma wartość " + requestCode);
        Log.d(DEBUG_TAG, "Liczba itemów w obiekcie adapter ma wartość  " + adapter.getItemCount());
        if(resultCode != RESULT_OK){
            return;
        }
        //Add new
        if (requestCode == adapter.getItemCount() ) {
            Server server = fillFromExtra(data);
            Log.d(DEBUG_TAG, "Dodanie nowego obiektu server");
            adapter.add(server);
        } else { //Edit
            Server server = fillFromExtra(data);
            server.setListPosition(requestCode);
            adapter.edit(server);
            Log.d(DEBUG_TAG, "Edycja obiektu server" + server .toString());
            this.positionSelected =-1;
            invalidateOptionsMenu();
        }
    }

    public void onItemClicked(int position){
        this.positionSelected = position;
        invalidateOptionsMenu();
        Log.d(DEBUG_TAG, "Kliknięto pozycja elementu; "+String.valueOf(position));
    }

    public void doSmoothScroll(int position) {
        recyclerView.smoothScrollToPosition(position);
    }

    public class GetServerListTask extends AsyncTask<Void, Void, ArrayList<Server>> {
        @Override
        protected ArrayList<Server> doInBackground(Void... params) {
            serverRepository.open();
            serversList = serverRepository.findAllOrderedByName();
            return serversList;
        }

        @Override
        protected void onPostExecute(ArrayList<Server> servers) {
            super.onPostExecute(servers);
            adapter = new ServerAdapter(ServerActivity.this, serversList, serverRepository);
            recyclerView.setAdapter(adapter);
        }
    }

    private class  DeleteServerTask extends AsyncTask<Server, Void, Server>{

        @Override
        protected Server doInBackground(Server... servers) {
            Log.d(DEBUG_TAG, "Server to remove:  " +servers[0].getId());
            serverRepository.delete(servers[0].getId());
            serversList.remove(servers[0].getListPosition());
            return servers[0];
        }

        @Override
        protected void onPostExecute(Server server) {
            super.onPostExecute(server);
            adapter.notifyItemRemoved(server.getListPosition());
        }
    }
}
