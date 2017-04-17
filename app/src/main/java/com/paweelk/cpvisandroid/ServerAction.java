package com.paweelk.cpvisandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.paweelk.cpvisandroid.model.Server;
import com.paweelk.cpvisandroid.service.ApiService;

import java.io.FileNotFoundException;

/**
 * Created by Pawel Kopec <paweelkopec@gmail.com> on 26.03.17.
 */
public abstract class ServerAction extends AppCompatActivity {

    protected Intent intent;
    protected EditText editName;
    protected EditText editUrl;
    protected EditText editUsername;
    protected EditText editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        editName = (EditText) findViewById(R.id.editTextName);
        editUrl = (EditText) findViewById(R.id.editTextUrl);
        editUsername = (EditText) findViewById(R.id.editTextUsername);
        editPassword = (EditText) findViewById(R.id.editTextPassword);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_server_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_on_save) {
            this.onSave();
        } else if (id == R.id.action_on_test_connection) {
            this.onTestConnection();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onTestConnection() {
        //Load Server
        Server server = new Server();
        server.setName(editName.getText().toString())
                .setUrl(editUrl.getText().toString())
                .setUsername(editUsername.getText().toString())
                .setPassword(editPassword.getText().toString());

        //Api loading and make test connection task
        new TestConnectionTask(new ApiService(server)).execute();

    }

    protected void onSave() {
        //Name
        String name = editName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Enter server name", Toast.LENGTH_SHORT).show();
            return;
        }
        //URL
        String url = editUrl.getText().toString().trim();
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(getApplicationContext(), "Enter server URL", Toast.LENGTH_SHORT).show();
            return;
        }
        //Username
        String username = editUsername.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getApplicationContext(), "Enter username", Toast.LENGTH_SHORT).show();
            return;
        }
        //Password
        String password = editPassword.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), "Enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        //Put Extra
        intent.putExtra(ServerActivity.EXTRA_SERVER_NAME, String.valueOf(name))
                .putExtra(ServerActivity.EXTRA_SERVER_URL, String.valueOf(url))
                .putExtra(ServerActivity.EXTRA_SERVER_USERNAME, String.valueOf(username))
                .putExtra(ServerActivity.EXTRA_SERVER_PASSWORD, String.valueOf(password));
        //Set Result & finish
        setResult(RESULT_OK, intent);
        supportFinishAfterTransition();
    }

    private class TestConnectionTask extends AsyncTask {

        private ApiService mApiService;
        private Exception mException;
        public TestConnectionTask(ApiService mApiService) {
            this.mApiService = mApiService;
        }

        @Override
        protected Object doInBackground(Object[] objects) {
            try {
                mApiService.getBitmapStream();
                mApiService.touched(1, 1);
            } catch (Exception e) {
                this.mException = e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            if (mException != null) {
                mException.printStackTrace();
                Log.e(getClass().toString(), "Exception occurred in test connection ", mException);
                String msg = mException.getMessage();
                if (mException.getClass() == FileNotFoundException.class) {
                    msg = getBaseContext().getString(R.string.error_file_not_found) + " " + msg;
                }
                Toast.makeText(getBaseContext(), msg,
                        Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getBaseContext(), R.string.test_connection_success,
                        Toast.LENGTH_LONG).show();
            }
        }
    }

}
