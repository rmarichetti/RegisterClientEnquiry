package com.aarna.www.registerclientenquiry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;

import com.aarna.www.registerclientenquiry.MySQL.Downloader;

public class listBeachBookings extends AppCompatActivity {
    final static String urlAddress="http://210.18.139.72/bookings/getBHEvents.php";

    private EditText etFilterName;
    public static GridView gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_beach_bookings);
        gv= (GridView) findViewById(R.id.gridBHBookings);
        etFilterName = (EditText) findViewById(R.id.etBHFiltName);

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute("listBeachBookings", "", "");

        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );// hide keyboard for better visibility.

    }
    public void onFilter(View view){
        String sFilterName = etFilterName.getText().toString();
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute("listBeachBookings", "BeachHouse", sFilterName);
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );// hide keyboard for better visibility.
    }

}
