package com.paweelk.cpvisandroid;

import android.os.Bundle;
import android.widget.EditText;
/**
 * Created by Pawel Kopec <paweelkopec@gmail.com> on 26.03.17.
 */
public class ServerEditActivity extends ServerAction{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_add);
        intent = getIntent();
        //Name
        EditText editName = (EditText) findViewById(R.id.editTextName);
        editName.setText(intent.getStringExtra(ServerActivity.EXTRA_SERVER_NAME));
        //URL
        EditText editUrl = (EditText) findViewById(R.id.editTextUrl);
        editUrl.setText(intent.getStringExtra(ServerActivity.EXTRA_SERVER_URL));
        //Username
        EditText editUsername = (EditText) findViewById(R.id.editTextUsername);
        editUsername.setText(intent.getStringExtra(ServerActivity.EXTRA_SERVER_USERNAME));
        //Password
        EditText editPassword = (EditText) findViewById(R.id.editTextPassword);
        editPassword.setText(intent.getStringExtra(ServerActivity.EXTRA_SERVER_PASSWORD));
    }

}
