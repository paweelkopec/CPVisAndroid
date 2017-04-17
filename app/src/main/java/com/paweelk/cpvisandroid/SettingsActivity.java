package com.paweelk.cpvisandroid;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.paweelk.cpvisandroid.model.Settings;
import com.paweelk.cpvisandroid.repositories.SettingsRepository;

public class SettingsActivity extends AppCompatActivity {

    private SettingsRepository mSettingsRepository = new SettingsRepository(this);
    private Settings mSettings;
    private SeekBar mSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mSeekBar = (SeekBar) findViewById(R.id.seekBar);
        mSeekBar.setProgress(1000);
        final TextView seekBarValue = (TextView) findViewById(R.id.displayIntervalValue);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                seekBarValue.setText(String.valueOf(progress));
                mSettings.setIntervalMillisecond(progress);
                new UpdateSettingsTask().execute(mSettings);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Nothing to do
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Nothing to do
            }
        });
        //Settings loading from DB
        new GetSettingsTask().execute();
    }

    private class GetSettingsTask extends AsyncTask<Void, Void, Settings> {

        @Override
        protected Settings doInBackground(Void... params) {
            mSettingsRepository.open();
            return mSettingsRepository.findAll();
        }

        @Override
        protected void onPostExecute(Settings settings) {
            super.onPostExecute(settings);
            mSettings = settings;
            mSeekBar.setProgress(mSettings.getIntervalMillisecond());
        }
    }

    private class UpdateSettingsTask extends AsyncTask<Settings, Void, Void> {

        @Override
        protected Void doInBackground(Settings... settingses) {
            mSettingsRepository.open();
            mSettingsRepository.update(settingses[0]);
            return null;
        }

    }
}
