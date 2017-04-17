package com.paweelk.cpvisandroid.service;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ImageView;

import com.paweelk.cpvisandroid.R;

import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * Created by Pawel Kopec <paweelkopec@gmail.com> on 13.04.17.
 */

public class GetImageTask extends AsyncTask<ImageView, Void, Bitmap> {

    private ApiService mApiService;
    private ImageView mImageView;
    private Exception mException;
    private Context mContext;

    public GetImageTask(Context mContext, ApiService mApiService) {
        this.mContext = mContext;
        this.mApiService = mApiService;
    }

    @Override
    protected Bitmap doInBackground(ImageView... imageViews) {
        this.mImageView = imageViews[0];
        Bitmap bitmap = null;
        try {
            bitmap = mApiService.getBitmapStream();
        } catch (Exception e) {
            this.mException = e;
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (bitmap != null) {
            mImageView.setImageBitmap(bitmap);
        } else if (this.mException != null) {
            Log.e(getClass().toString(), "Exception in get image task " , mException);
            String msg = mException.getMessage();
            if (mException.getClass() == FileNotFoundException.class) {
                msg = mContext.getString(R.string.error_file_not_found) + " " + msg;
            }
            new AlertDialog.Builder(mContext).setTitle(R.string.title_alert_server)
                    .setMessage(msg)
                    .setIcon(android.R.drawable.ic_dialog_alert).setPositiveButton(R.string.alert_ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //Nothing to do
                }
            }).show();
        }
    }
}
