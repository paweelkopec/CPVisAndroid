package com.paweelk.cpvisandroid;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


/**
 * Created by Pawel Kopec <paweelkopec@gmail.com> on 19.03.17.
 */

public class ServerAddActivity extends ServerAction {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_add);
        intent = getIntent();

    }





}
