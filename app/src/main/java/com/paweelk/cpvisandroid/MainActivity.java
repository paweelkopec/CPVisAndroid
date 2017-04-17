package com.paweelk.cpvisandroid;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
    public ServerRepository serverRepository = new ServerRepository(this);
    private ArrayList<Server> serversList;
    MainPagerAdapter adapter;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_servers) {
            Intent intent = new Intent(this, ServerActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
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
            adapter = new MainPagerAdapter(MainActivity.this, serversList, serverRepository);
            viewPager = (ViewPager) findViewById(R.id.pager);
            viewPager.setAdapter(adapter);
        }
    }
}
