package com.aarna.www.registerclientenquiry;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CallLog;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.aarna.www.registerclientenquiry.BackgroundWorker;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class registerClient extends AppCompatActivity {
    public static EditText etName, etPhone, etEventDate, etComments, etConfirmBy, etOfferPrice, etAskingPrice;
    public static Spinner locSpinner, statusSpinner;
    private int mYear, mMonth, mDay;
    public static String sClient;
    public static boolean bTimeout, bDirectBooking;
    public String sUserid, sRole;//, sclDt;
    public Date dclDt;
    private Button buttonBHBookings;
    private ProgressDialog pd;
    public final static String USERID_EXTRA = "com.aarna.www.registerclientenquiry.MySQL._USERID";
    public final static String USERROLE_EXTRA = "com.aarna.www.registerclientenquiry.MySQL._USERROLE";
    //public final static String USERCLDT_EXTRA = "com.aarna.www.registerclientenquiry.MySQL._USERCLDT";
    private Timer timer;
    private String current = "";
    private String ddmmyyyyhha = "DDMMYYYYHH#";
    private String sLoc, sStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);
        if (bTimeout) {
            Toast.makeText(registerClient.this, "Timeout.  Please relogin.", Toast.LENGTH_LONG).show();
            return;
        }
        bTimeout = false;
        sClient = getIntent().getStringExtra(listRegistrations.ID_EXTRA);
        sUserid = LoginActivity.sUserid;//getIntent().getStringExtra(USERID_EXTRA);
        //String uid = LoginActivity.sUserid;
        sRole = LoginActivity.sRole;//getIntent().getStringExtra(USERROLE_EXTRA);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            dclDt = sdf.parse(LoginActivity.sclDt);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //LogCallHst();

        buttonBHBookings = (Button) findViewById(R.id.btnBHBookings);
        if (sRole.equals("BH") || sRole.equals("Admin")) {
            buttonBHBookings.setVisibility(View.VISIBLE);
        } else {
            buttonBHBookings.setVisibility(View.INVISIBLE);
        }
        bDirectBooking = false;

        etName = (EditText) findViewById(R.id.etName);
        String sPhName = getIntent().getStringExtra(listCalls.ID_PHNMEXTRA);
        if (sPhName != null){
            etName.setText(sPhName);
        }
        //etName.setText(sclDt);
        etPhone = (EditText) findViewById(R.id.etPhone);
        String sPhNo = getIntent().getStringExtra(listCalls.ID_PHEXTRA);
        if (sPhNo != null){
            etPhone.setText(sPhNo);
        }

        etEventDate = (EditText) findViewById(R.id.etEvtDt);
        etComments = (EditText) findViewById(R.id.etComments);
        etConfirmBy = (EditText) findViewById(R.id.etConfirmBy);
        etOfferPrice = (EditText) findViewById(R.id.etOfferPrice);
        etAskingPrice = (EditText) findViewById(R.id.etAskPrice);
        if (sClient != null) {
            pd = new ProgressDialog(this);
            pd.setMessage("loading");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            BackgroundWorker backgroundWorker = new BackgroundWorker(this);
            backgroundWorker.execute("getRegDetails", sClient);

        }

        locSpinner = (Spinner) findViewById(R.id.spLoc);
        ArrayAdapter<String> locSpnAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.locSpn));
        locSpnAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        locSpinner.setAdapter(locSpnAdapter);
        locSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sLoc = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        //Setup Status Spinner
        ArrayAdapter<CharSequence> adapterStatus;
        statusSpinner = (Spinner) findViewById(R.id.spStatus);
        adapterStatus = ArrayAdapter.createFromResource(this, R.array.Status, android.R.layout.simple_spinner_item);
        adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapterStatus);
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sStatus = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        final Calendar myCalendar = Calendar.getInstance();
        etEventDate.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String dtEnt = s.toString();
                    if (dtEnt.indexOf('-') > 0) {
                        String dtDay = dtEnt.substring(0, dtEnt.indexOf("-"));
                        String dtMon = dtEnt.substring(dtEnt.indexOf("-") + 1, dtEnt.lastIndexOf("-"));
                    }
                    String clean = s.toString().replaceAll("[^\\d.ap]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.ap]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 9; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 11) {
                        clean = clean + ddmmyyyyhha.substring(clean.length());
                    } else {
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));
                        int hr = Integer.parseInt(clean.substring(8, 10));
                        String ampm = clean.substring(10, 11);

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        myCalendar.set(Calendar.MONTH, mon - 1);
                        year = (year < 1900) ? 1900 : (year > 2100) ? 2100 : year;
                        myCalendar.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012
                        hr = hr < 1 ? 1 : hr > 12 ? 12 : hr;
                        ampm = ((ampm.equals("a")) || (ampm.equals("p"))) ? ampm : "a";

                        day = (day > myCalendar.getActualMaximum(Calendar.DATE)) ? myCalendar.getActualMaximum(Calendar.DATE) : day;
                        clean = String.format("%02d%02d%02d%02d%s", day, mon, year, hr, ampm);
                    }

                    clean = String.format("%s/%s/%s_%s%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8),
                            clean.substring(8, 10),
                            clean.substring(10, 11));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    etEventDate.setText(current);
                    etEventDate.setSelection(sel < current.length() ? sel : current.length());
                }

            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                //Toast.makeText(registerClient.this, "after text changed.", Toast.LENGTH_LONG).show();
            }

        });
        final Button pickDate = (Button) findViewById(R.id.btnPickEvtDt);
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                // myCalendar.add(Calendar.DATE, 0);
                String myFormat = "yyyy-MM-dd"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                etEventDate.setText(sdf.format(myCalendar.getTime()));
            }
        };
        pickDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog dpd = new DatePickerDialog(registerClient.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                // Display Selected date in textbox

                                if (year < mYear)
                                    view.updateDate(mYear, mMonth, mDay);

                                if (monthOfYear < mMonth && year == mYear)
                                    view.updateDate(mYear, mMonth, mDay);

                                if (dayOfMonth < mDay && year == mYear && monthOfYear == mMonth)
                                    view.updateDate(mYear, mMonth, mDay);

                                etEventDate.setText(String.format("%02d", dayOfMonth) + "-"
                                        + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", year));

                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis());
                dpd.show();

            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);// hide keyboard for better visibility.
//        String calld = getCallDetails(this);
//        Toast.makeText(registerClient.this, "Calls:" + calld, Toast.LENGTH_LONG).show();
    }
    public void OnCallHst(View view) {
        startActivity(new Intent(this, listCalls.class));

    }
    private void LogCallHst() {
        /*String calld ;//= getCallDetails( this);
        Date yesterdayDt = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(yesterdayDt);
        cal.add(Calendar.DATE, -1);
        yesterdayDt = cal.getTime();*/


        //StringBuffer stringBuffer = new StringBuffer();
        if (ActivityCompat.checkSelfPermission(registerClient.this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(registerClient.this, "Not Allowed", Toast.LENGTH_LONG).show();
        }
        else {
            Cursor cursor = registerClient.this.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC");
            int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
            //int name = cursor.getColumnIndex(CallLog.Calls.CACHED_NAME);
            int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
            int date = cursor.getColumnIndex(CallLog.Calls.DATE);
            int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
            while (cursor.moveToNext()) {
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
                        dir = "OUTGOING";
                        break;
                    case CallLog.Calls.INCOMING_TYPE:
                        dir = "INCOMING";
                        break;

                    case CallLog.Calls.MISSED_TYPE:
                        dir = "MISSED";
                        break;
                }
                if (callDayTime.after(dclDt)) {
                    String sType = "callLog";
                    BackgroundWorker backgroundWorker = new BackgroundWorker(registerClient.this);
                    backgroundWorker.execute(sType, dateString, phNumber, phName, dir, callDuration);
                    //stringBuffer.append("\nPhone Number:--- " + phNumber + " \nCall Type:--- "
                     //       + dir + " \nCall Date:--- " + callDayTime
                     //       + " \nCall duration in sec :--- " + callDuration);
                    //stringBuffer.append("\n----------------------------------");
                }
                else{
                    break;
                }
            }
            //calld = stringBuffer.toString();
            cursor.close();
        }
        //Toast.makeText(registerClient.this, calld, Toast.LENGTH_LONG).show();
        String sType = "callLogUpdateDt";
        BackgroundWorker backgroundWorker = new BackgroundWorker(registerClient.this);
        backgroundWorker.execute(sType);

    }
    public void onDelete2(View view) {
        Intent intent = new Intent(this, Alarm.class);
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 5000, AlarmManager.INTERVAL_FIFTEEN_MINUTES, pendingIntent);

    }

//    private static String getCallDetails(Context context) {
//        return stringBuffer.toString();
    //}
    public void onDelete(View view){
        if (bTimeout){
            Toast.makeText(registerClient.this, "Timeout.  Please relogin.", Toast.LENGTH_LONG).show();
            return;
        }

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        String sType = "registerDelete";
                        String sName = etName.getText().toString();
                        String sPhone = etPhone.getText().toString();
                        String sEventDate = etEventDate.getText().toString();
                        String sComments = etComments.getText().toString();
                        String sConfirmBy = etConfirmBy.getText().toString();
                        //String sLoc = etLoc.getText().toString();
                        if (bTimeout){
                            Toast.makeText(registerClient.this, "Timeout.  Please relogin.", Toast.LENGTH_LONG).show();
                            return;
                        }


                        BackgroundWorker backgroundWorker = new BackgroundWorker(registerClient.this);
                        backgroundWorker.execute(sType, sClient);

                        String sWhatsupinfo="Deleting Client.  Not responding Anymore::\n";
                        sWhatsupinfo += "Name:" + sName + "  EventDate:" + sEventDate + " Phone:" + sPhone + " Comments:" + sComments + " ConfirmBy:" + sConfirmBy + " Loc:" + sLoc;
                        sendWhatsAppMsg(sWhatsupinfo);
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Deleting Client.  Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();

    }
    public void OnWhatsApp(View view){
        //https://api.whatsapp.com/send?phone=15551234567
        //+ etPhone.getText().toString();
        String sMsg = "testing";

        PackageManager packageManager = getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);

        try {
            String url = "https://api.whatsapp.com/send?phone=" +etPhone.getText().toString() + "&text=" + URLEncoder.encode(sMsg, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void OnBook(View view){
        if (bTimeout){
            Toast.makeText(registerClient.this, "Timeout.  Please relogin.", Toast.LENGTH_LONG).show();
            return;
        }
        if (sClient ==""){
            Toast.makeText(registerClient.this, "Please save the client info and reload from List.", Toast.LENGTH_LONG).show();
            return;
        }
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked

                        String sType = "book";
                        String sName = etName.getText().toString();
                        String sEventDate = etEventDate.getText().toString();

                        BackgroundWorker backgroundWorker = new BackgroundWorker(registerClient.this);
                        backgroundWorker.execute(sType, sClient);

                        String sWhatsupinfo="";
                        if (sClient != "") sWhatsupinfo += "BOOKED::\n";
                        sWhatsupinfo += "Name:" + sName + "  EventDate:" + sEventDate + " Loc:" + sLoc;
                        sendWhatsAppMsg(sWhatsupinfo);
                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Convert this into a Booking?  Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();


    }
    public void OnSave(View view) {
        if (bTimeout){
            Toast.makeText(registerClient.this, "Timeout.  Please relogin.", Toast.LENGTH_LONG).show();
            return;
        }
        String sName = etName.getText().toString();
        String sPhone = etPhone.getText().toString();
        String sEventDate = etEventDate.getText().toString();
        String sComments = etComments.getText().toString();
        String sConfirmBy = etConfirmBy.getText().toString();
        String lOfferPrice = etOfferPrice.getText().toString();
        String lAskingPrice = etAskingPrice.getText().toString();
        //String sLoc = etLoc.getText().toString();
        //locSpinner.get
        String sType = "register";


        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(sType, sName, sPhone, sEventDate, sComments, sConfirmBy, sLoc, sClient, lOfferPrice, lAskingPrice, sStatus);

        String sWhatsupinfo="";
        if (sClient != null) {
            sWhatsupinfo += "Update::\n";
        }
        //if (sClient != "")
        sWhatsupinfo += "Name:" + sName + "  EventDate:" + sEventDate + " Phone:" + sPhone + " Comments:" + sComments + " ConfirmBy:" + sConfirmBy + " Loc:" + sLoc;
        sWhatsupinfo += "Offer Price:" + lOfferPrice + "Asking Price:"+lAskingPrice;
                //openConversationWithWhatsapp("Marichetty");
        sendWhatsAppMsg(sWhatsupinfo);

        //Uri uri = Uri.parse("smsto:" + "9500010099");

    }

    void sendWhatsAppMsg(String sMsg){
        Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
        whatsappIntent.setType("text/plain");
        whatsappIntent.setPackage("com.whatsapp");
        whatsappIntent.putExtra(Intent.EXTRA_TEXT, sMsg);
        try {
            startActivity(whatsappIntent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Whatsapp have not been installed.", Toast.LENGTH_LONG).show();
        }

    }
    /*
    private void openConversationWithWhatsapp(String e164PhoneNumber){
        String whatsappId = e164PhoneNumber+"@s.whatsapp.net";
        Uri uri = Uri.parse("smsto:" + whatsappId);
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.setPackage("com.whatsapp");

        intent.putExtra(Intent.EXTRA_TEXT, "this is a test");
        intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
        intent.putExtra(Intent.EXTRA_TITLE, "title");
        intent.putExtra(Intent.EXTRA_EMAIL, "email");
        intent.putExtra("sms_body", "The text goes here");
        intent.putExtra("text","asd");
        intent.putExtra("body","body");
        intent.putExtra("subject","subjhect");

        startActivity(intent);
    }
    void oldsendWhatsAppMsg(String sMsg){

        PackageManager packageManager = getPackageManager();
        Intent i = new Intent(Intent.ACTION_VIEW);

        try {
            String url = "https://api.whatsapp.com/send?phone=+919500009779&text=" + URLEncoder.encode(sMsg, "UTF-8");
            i.setPackage("com.whatsapp");
            i.setData(Uri.parse(url));
            if (i.resolveActivity(packageManager) != null) {
                startActivity(i);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    private boolean whatsappInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }
*/
    public void onRegList(View view) {
        if (bTimeout){
            Toast.makeText(registerClient.this, "Timeout.  Please relogin.", Toast.LENGTH_LONG).show();
            return;
        }
        startActivity(new Intent(this, listRegistrations.class));
    }

    public void OnListBHBookings(View view){
        if (bTimeout){
            Toast.makeText(registerClient.this, "Timeout.  Please relogin.", Toast.LENGTH_LONG).show();
            return;
        }
        startActivity(new Intent(this, listBeachBookings.class));
    }
    public void onNew(View view){
        if (bTimeout){
            Toast.makeText(registerClient.this, "Timeout.  Please relogin.", Toast.LENGTH_LONG).show();
            return;
        }

        sClient = "";
        etName.setText("");
        etPhone.setText("");
        etEventDate.setText("");
        etComments.setText("");
        etConfirmBy.setText("");
        //etLoc.setText("");
    }
    public static void clearScreen(){
        sClient = "";
        etName.setText("");
        etPhone.setText("");
        etEventDate.setText("");
        etComments.setText("");
        etConfirmBy.setText("");
        //etLoc.setText("");
    }

    public void OnCall(View view) {
        //Now call the contact number stored in Phone
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        String ph = "tel:" + etPhone.getText().toString();
        callIntent.setData(Uri.parse(ph));
        if (ActivityCompat.checkSelfPermission(registerClient.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        startActivity(callIntent);
            /*Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:0377778888"));

        if (ActivityCompat.checkSelfPermission(registerClient.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);*/
    }



    @Override
    protected void onPause() {
        super.onPause();

        timer = new Timer();
        DisableButtons disableButtons = new DisableButtons();
        timer.schedule(disableButtons,  1800000); //auto logout in 10 minutes
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    private class DisableButtons extends TimerTask {
        @Override
        public void run() {
            timer.cancel();
            registerClient.bTimeout = true;
            finish();
        }
    }
}
