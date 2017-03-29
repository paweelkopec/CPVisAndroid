package com.paweelk.cpvisandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Pawel Kopec <paweelkopec@gmail.com> on 26.03.17.
 */
public abstract class ServerAction extends AppCompatActivity {

    protected Intent intent;

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
        }else if(id == R.id.action_on_test_connection){
            this.onTestConnection();
        }
        return super.onOptionsItemSelected(item);
    }

    protected void onTestConnection() {
        final EditText name =  (EditText) findViewById(R.id.editTextName);
        Toast.makeText(getBaseContext(), "Test connection message: " + name.getText().toString(),
                Toast.LENGTH_LONG).show();
    }

    protected void onSave(){
        //Name
        EditText editName = (EditText) findViewById(R.id.editTextName);
        String  name = editName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Enter server name", Toast.LENGTH_SHORT).show();
            return;
        }
        //URL
        EditText editUrl = (EditText) findViewById(R.id.editTextUrl);
        String  url = editUrl.getText().toString().trim();
        if (TextUtils.isEmpty(url)) {
            Toast.makeText(getApplicationContext(), "Enter server URL", Toast.LENGTH_SHORT).show();
            return;
        }
        //Username
        EditText editUsername = (EditText) findViewById(R.id.editTextUsername);
        String  username = editUsername.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            Toast.makeText(getApplicationContext(), "Enter username", Toast.LENGTH_SHORT).show();
            return;
        }
        //Password
        EditText editPassword = (EditText) findViewById(R.id.editTextPassword);
        String  password = editPassword.getText().toString().trim();
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
}
