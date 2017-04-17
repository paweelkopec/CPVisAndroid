package com.paweelk.cpvisandroid;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.paweelk.cpvisandroid.model.Server;
import com.paweelk.cpvisandroid.repositories.ServerRepository;

import java.util.ArrayList;

/**
 * Created by Pawel Kopec <paweelkopec@gmail.com> on 15.03.17.
 */
public class MainActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "Main Activity";
    protected ServerRepository serverRepository = new ServerRepository(this);
    protected ArrayList<Server> serversList;
    private MainPagerAdapter adapter;
    private ViewPager viewPager;
    private static Boolean mShowDialog = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.i(DEBUG_TAG, " running");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        new GetServerListTask().execute();

    }

    @Override
    protected void onResume() {
        super.onResume();
        serverRepository.open();
        new GetServerListTask().execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_servers) {
            startActivity( new Intent(this, ServerActivity.class));
        }
        else if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(DEBUG_TAG, "request code ma wartość " + requestCode);
        super.onActivityResult(requestCode, resultCode, data);
        serverRepository.open();
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == 0) {
            Server server = new Server();
            server.setName(data.getStringExtra(ServerActivity.EXTRA_SERVER_NAME))
                    .setUrl(data.getStringExtra(ServerActivity.EXTRA_SERVER_URL))
                    .setUsername(data.getStringExtra(ServerActivity.EXTRA_SERVER_USERNAME))
                    .setPassword(data.getStringExtra(ServerActivity.EXTRA_SERVER_PASSWORD));
            new CreateServerTask().execute(server);
        }
    }

    private class GetServerListTask extends AsyncTask<Void, Void, ArrayList<Server>> {
        @Override
        protected ArrayList<Server> doInBackground(Void... params) {
            serversList = serverRepository.findAllOrderedByName();
            return serversList;
        }

        @Override
        protected void onPostExecute(ArrayList<Server> servers) {
            super.onPostExecute(servers);
            adapter = new MainPagerAdapter(MainActivity.this, serversList, serverRepository);
            viewPager = (ViewPager) findViewById(R.id.pager);
            viewPager.setAdapter(adapter);
            //List servers is empty
            if(serversList.size()==0 && mShowDialog){
                mShowDialog = false;
                new AlertDialog.Builder(MainActivity.this).setTitle(R.string.title_alert_configuration)
                        .setMessage(R.string.msg_alert_server_list_empyt)
                        .setIcon(android.R.drawable.ic_dialog_info).setPositiveButton(R.string.alert_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(MainActivity.this, ServerAddActivity.class), 0);

                    }
                }).show();
            }
        }
    }
    private class CreateServerTask extends AsyncTask<Server, Void, Server> {

        @Override
        protected Server doInBackground(Server... servers) {
            serverRepository.create(servers[0]);
            return servers[0];
        }

        @Override
        protected void onPostExecute(Server server) {
            super.onPostExecute(server);
            adapter.notifyDataSetChanged();
        }

    }
}
