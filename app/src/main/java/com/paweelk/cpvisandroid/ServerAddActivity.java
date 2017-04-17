package com.paweelk.cpvisandroid;

import android.os.Bundle;


/**
 * Created by Pawel Kopec <paweelkopec@gmail.com> on 19.03.17.
 */

public class ServerAddActivity extends ServerAction {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_server_add);
        super.onCreate(savedInstanceState);
        intent = getIntent();

    }





}
