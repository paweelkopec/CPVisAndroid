package com.paweelk.cpvisandroid.service;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ImageView;

import com.paweelk.cpvisandroid.R;


/**
 * Created by Pawel Kopec <paweelkopec@gmail.com> on 13.04.17.
 */

public class ImageTouchedTask extends AsyncTask<int[], Void, Void> {

    private ApiService mApiService;
    private ImageView mImageView;
    private Exception mException;
    private Context mContext;

    public ImageTouchedTask(Context mContext, ApiService mApiService) {
        this.mContext = mContext;
        this.mApiService = mApiService;
    }

    @Override
    protected Void doInBackground(int[]... params) {
        int[] param = params[0];
        try {
            Log.i(getClass().toString(), "Touched x: " + param[0] + " y:" + param[1]);
            mApiService.touched(param[0], param[1]);
        } catch (Exception e) {
            this.mException = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        if (this.mException == null) {
            super.onPreExecute();
            return;
        }
        mException.printStackTrace();
        Log.e(getClass().toString(), mException.getClass().toString() + " " + mException.getMessage());
        String msg = mException.getMessage();
        new AlertDialog.Builder(mContext).setTitle(R.string.title_alert_server)
                .setMessage(mException.getMessage())
                .setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton(R.string.alert_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                //Nothing to do
            }
        }).show();
        super.onPreExecute();
    }

}
