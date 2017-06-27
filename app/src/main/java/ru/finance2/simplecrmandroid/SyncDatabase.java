package ru.finance2.simplecrmandroid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;


import ru.finance2.simplecrmandroid.helpers.SyncHelper;

public class SyncDatabase extends Activity {
    MainDbHelper dbHelper;
    Context context;
    TextView log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync_database);

        context = getApplicationContext();

        log = (TextView) findViewById(R.id.log);
        dbHelper = MainDbHelper.getInstance(this);
        (new MyAsyncUpdateDatabase()).execute();
    }

    public void log(String msg) {
        this.log.setText( this.log.getText().toString() + "\n" + msg );
    }



    public class MyAsyncUpdateDatabase extends AsyncTask<Void, String, Void> {

        @Override
        protected void onProgressUpdate(String... progress) {
            log(progress[0]);
        }


        @Override
        protected Void doInBackground(Void... params) {
            new SyncHelper().doFullSync(new SyncHelper.IPublish() {
                @Override
                public void publish(String progress) {
                    publishProgress(progress);
                    Log.d("SYNC", progress);
                }
            });
            return null;
        }

    }
}