package com.paweelk.cpvisandroid;

import android.os.Bundle;

/**
 * Created by Pawel Kopec <paweelkopec@gmail.com> on 26.03.17.
 */
public class ServerEditActivity extends ServerAction {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_server_add);
        intent = getIntent();
        super.onCreate(savedInstanceState);
        //Name
        editName.setText(intent.getStringExtra(ServerActivity.EXTRA_SERVER_NAME));
        //URL
        editUrl.setText(intent.getStringExtra(ServerActivity.EXTRA_SERVER_URL));
        //Username
        editUsername.setText(intent.getStringExtra(ServerActivity.EXTRA_SERVER_USERNAME));
        //Password
        editPassword.setText(intent.getStringExtra(ServerActivity.EXTRA_SERVER_PASSWORD));
    }

}
