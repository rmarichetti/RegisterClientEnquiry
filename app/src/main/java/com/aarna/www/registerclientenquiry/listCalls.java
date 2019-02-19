package com.aarna.www.registerclientenquiry;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class listCalls extends AppCompatActivity {
    final static String urlAddress="http://210.18.139.72/bookings/getRegistrations.php";
    public final static String ID_PHEXTRA = "com.aarna.www.registerclientenquiry.MySQL._IDPH";
    public final static String ID_PHNMEXTRA = "com.aarna.www.registerclientenquiry.MySQL._IDPHNM";

    private String sStatus;
    public static GridView gv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_calls);
        sStatus = "";
        gv= (GridView) findViewById(R.id.gridCalls);

        if (ActivityCompat.checkSelfPermission(listCalls.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(listCalls.this, "Not Allowed", Toast.LENGTH_LONG).show();
        }
        else {
            final ArrayList<String> callList = new ArrayList<>();
            int noOfPhoneCalls =300;
            Cursor cursor = listCalls.this.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");
            int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
            //int name = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
            int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
            int date = cursor.getColumnIndex(CallLog.Calls.DATE);
            int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
            while (cursor.moveToNext() ) {
                noOfPhoneCalls--;
                if (noOfPhoneCalls < 1) break;
                String phNumber = cursor.getString(number);
                //String phName = cursor.getString(name);
                String phName = cursor.getString(cursor.getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
                if (phName == null) phName  = "";
                String callType = cursor.getString(type);
                String callDate = cursor.getString(date);
                //String callDate = cur.getString(cur.getColumnIndex(android.provider.CallLog.Calls.DATE));
                SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
                String dateString = formatter.format(new Date(Long.parseLong(callDate)));
                Date callDayTime = new Date(Long.valueOf(callDate));
                String callDuration = cursor.getString(duration);
                String dir = "Others";
                int dircode = Integer.parseInt(callType);
                switch (dircode) {
                    case CallLog.Calls.OUTGOING_TYPE:
                        dir = "O";
                        break;
                    case CallLog.Calls.INCOMING_TYPE:
                        dir = "I";
                        break;

                    case CallLog.Calls.MISSED_TYPE:
                        dir = "M";
                        break;
                }

                callList.add(phNumber + "|" + phName + "|" + dir + "|" + dateString + "|" + callDuration);
            }
            final Intent intGVData;
            intGVData = new Intent(listCalls.this, registerClient.class);
            ArrayAdapter adapter = new ArrayAdapter(listCalls.this, android.R.layout.simple_list_item_1,callList);
            gv.setAdapter(adapter);
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //Toast.makeText(c, regList.get(position), Toast.LENGTH_SHORT).show();

                    //Intent intRegClient = new Intent(c,showClientRegn.class);
                    String dispVal = callList.get(position) ;
                    intGVData.putExtra(listCalls.ID_PHEXTRA,dispVal.substring(0,dispVal.indexOf("|")));
                    int stName = dispVal.indexOf("|") + 1;
                    intGVData.putExtra(listCalls.ID_PHNMEXTRA,dispVal.substring( stName,dispVal.indexOf("|", stName) ));
                    startActivity(intGVData);


                }
            });
            //calld = stringBuffer.toString();
            cursor.close();
        }

    }

}
