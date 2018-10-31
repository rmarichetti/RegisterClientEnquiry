package com.aarna.www.registerclientenquiry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;

import com.aarna.www.registerclientenquiry.MySQL.Downloader;

public class ListPayments extends AppCompatActivity {
    final static String urlAddress="http://210.18.139.72/bookings/getPayments.php";

    public static GridView gv;
    public static String sEventID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_payments);
        gv= (GridView) findViewById(R.id.gridPayments);
        sEventID = getIntent().getStringExtra(listRegistrations.ID_EXTRA);

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute("listPayments",sEventID);

        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );// hide keyboard for better visibility.
    }
}
