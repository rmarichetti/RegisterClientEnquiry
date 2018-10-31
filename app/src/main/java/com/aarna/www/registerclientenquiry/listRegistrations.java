package com.aarna.www.registerclientenquiry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;

import com.aarna.www.registerclientenquiry.MySQL.Downloader;



public class listRegistrations extends AppCompatActivity {
    final static String urlAddress="http://210.18.139.72/bookings/getRegistrations.php";
    public final static String ID_EXTRA = "com.aarna.www.registerclientenquiry.MySQL._ID";

    private EditText etFilterName, etFilterHall;
    private Spinner statusSpinner;
    private String sStatus;
    public static GridView gv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_registrations);
        sStatus = "";
        gv= (GridView) findViewById(R.id.gridRegs);
        etFilterName = (EditText) findViewById(R.id.etFiltName);
        etFilterHall = (EditText) findViewById(R.id.etFiltHall);

        //Setup Status Spinner
        ArrayAdapter<CharSequence> adapterStatus;
        statusSpinner = (Spinner) findViewById(R.id.spFiltStatus);
        adapterStatus = ArrayAdapter.createFromResource(this,R.array.Status,android.R.layout.simple_spinner_item);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapterStatus);
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sStatus = parent.getItemAtPosition(position).toString();            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        //new Downloader(listRegistrations.this,urlAddress,gv,"listRegistrations").execute();

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute("listRegistrations", "", "", "");
        /*
        Downloader downloader = new Downloader(listRegistrations.this,urlAddress,"listRegistrations");
        downloader.setGVandParams(gv,"","",sStatus);
        downloader.execute();*/
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );// hide keyboard for better visibility.

    }
    public void onFilter(View view){
        String sFilterName = etFilterName.getText().toString();
        String sFilterHall = etFilterHall.getText().toString();

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute("listRegistrations", sFilterHall,sFilterName, sStatus);
/*        Downloader downloader = new Downloader(listRegistrations.this,urlAddress,"listRegistrations");
        downloader.setGVandParams(gv,sFilterHall,sFilterName, sStatus);
        downloader.execute();*/
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN );// hide keyboard for better visibility.
    }
}
