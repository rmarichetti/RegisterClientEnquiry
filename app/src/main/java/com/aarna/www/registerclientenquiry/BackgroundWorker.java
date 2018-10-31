package com.aarna.www.registerclientenquiry;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
//import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by thirumalai on 11.12.17.
 */

public class BackgroundWorker extends AsyncTask<String,Void,String> {
    GridView gv;
    ArrayList<String> regList = new ArrayList<>();
    Intent intGVData;
    Context context;
    String sResult;
    AlertDialog alertDialog;
    String sType;
    BackgroundWorker (Context ctx){
        context = ctx;
    }

    @Override
    protected void onCancelled(String s) {
        super.onCancelled(s);
    }

    @Override
    protected String doInBackground(String... params){
        sType = params[0];
        String sURL="";
        String post_data="";
        try {
            if(sType.equals("login")){
                sURL = "http://210.18.139.72/bookings/loginAndroid.php";
                String sLogin = params[1];
                String sPassword = params[2];
                String sdb = params[3];
                post_data = URLEncoder.encode("username","UTF-8")+"="+URLEncoder.encode(sLogin,"UTF-8")+"&"
                        +URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(sPassword,"UTF-8")+"&"
                        +URLEncoder.encode("db","UTF-8")+"="+URLEncoder.encode(sdb,"UTF-8");
            }
            else if(sType.equals("registerDelete")){
                sURL = "http://210.18.139.72/bookings/regDeleteClient.php";
                String sClientID = params[1];
                post_data = URLEncoder.encode("db","UTF-8")+"="+URLEncoder.encode(LoginActivity.sdb,"UTF-8")+"&"
                        +URLEncoder.encode("ClientID", "UTF-8") + "=" + URLEncoder.encode(sClientID, "UTF-8");
            }
            else if(sType.equals("book")){
                sURL = "http://210.18.139.72/bookings/convertIntoBooking.php";
                String sClientID = params[1];
                post_data = URLEncoder.encode("ClientID", "UTF-8") + "=" + URLEncoder.encode(sClientID, "UTF-8");
            }
            else if(sType.equals("listRegistrations")){
                sURL = "http://210.18.139.72/bookings/getRegistrations.php";
                post_data = URLEncoder.encode("db","UTF-8")+"="+URLEncoder.encode(LoginActivity.sdb,"UTF-8")+"&"
                        +URLEncoder.encode("Hall","UTF-8")+"="+URLEncoder.encode(params[1],"UTF-8")+"&"
                        +URLEncoder.encode("Name","UTF-8")+"="+URLEncoder.encode(params[2],"UTF-8")+"&"
                        +URLEncoder.encode("Status","UTF-8")+"="+URLEncoder.encode(params[3],"UTF-8");
            }
            else if(sType.equals("listBeachBookings")){
                sURL = "http://210.18.139.72/bookings/getBHEvents.php";
                post_data = URLEncoder.encode("Hall","UTF-8")+"="+URLEncoder.encode(params[1],"UTF-8")+"&"
                        +URLEncoder.encode("Name","UTF-8")+"="+URLEncoder.encode(params[2],"UTF-8");
            }
            else if(sType.equals("listPayments")){
                sURL = "http://210.18.139.72/bookings/getBHPayments.php";
                post_data = URLEncoder.encode("EventID","UTF-8")+"="+URLEncoder.encode(params[1],"UTF-8");
            }
            else if(sType.equals("register")){
                sURL = "http://210.18.139.72/bookings/regNewClient.php";
                String sName = params[1];
                String sPhone = params[2];
                String sEventDate = params[3];
                String sComments = params[4];
                String sConfirmBy = params[5];
                String sLoc = params[6];
                String sClientID = params[7];
                String lOfferPrice = params[8];
                String lAskingPrice = params[9];
                String sStatus = params[10];
                if (sClientID== null)
                    sClientID = "";
                post_data = URLEncoder.encode("db","UTF-8")+"="+URLEncoder.encode(LoginActivity.sdb,"UTF-8")+"&"
                        +URLEncoder.encode("Name","UTF-8")+"="+URLEncoder.encode(sName,"UTF-8")+"&"
                        +URLEncoder.encode("Phone","UTF-8")+"="+URLEncoder.encode(sPhone,"UTF-8")+"&"
                        +URLEncoder.encode("EventDate","UTF-8")+"="+URLEncoder.encode(sEventDate,"UTF-8")+"&"
                        +URLEncoder.encode("Comments","UTF-8")+"="+URLEncoder.encode(sComments,"UTF-8")+"&"
                        +URLEncoder.encode("ConfirmBy","UTF-8")+"="+URLEncoder.encode(sConfirmBy,"UTF-8")+"&"
                        +URLEncoder.encode("Loc","UTF-8")+"="+URLEncoder.encode(sLoc,"UTF-8")+"&"
                        +URLEncoder.encode("ClientID","UTF-8")+"="+URLEncoder.encode(sClientID,"UTF-8")+"&"
                        +URLEncoder.encode("OfferPrice","UTF-8")+"="+URLEncoder.encode(lOfferPrice,"UTF-8")+"&"
                        +URLEncoder.encode("AskingPrice","UTF-8")+"="+URLEncoder.encode(lAskingPrice,"UTF-8")+"&"
                        +URLEncoder.encode("Status","UTF-8")+"="+URLEncoder.encode(sStatus,"UTF-8")+"&"
                        +URLEncoder.encode("UserID","UTF-8")+"="+URLEncoder.encode(LoginActivity.sUserid,"UTF-8");
            }
            else if(sType.equals("callLog")){
                sURL = "http://210.18.139.72/bookings/saveCallLogs.php";
                String callDate = params[1];
                String phNumber = params[2];
                String phName = params[3];
                String dir = params[4];
                String callDuration = params[5];
                post_data = URLEncoder.encode("db","UTF-8")+"="+URLEncoder.encode(LoginActivity.sdb,"UTF-8")+"&"
                        +URLEncoder.encode("callDate","UTF-8")+"="+URLEncoder.encode(callDate,"UTF-8")+"&"
                        +URLEncoder.encode("phNumber","UTF-8")+"="+URLEncoder.encode(phNumber,"UTF-8")+"&"
                        +URLEncoder.encode("phName","UTF-8")+"="+URLEncoder.encode(phName,"UTF-8")+"&"
                        +URLEncoder.encode("callType","UTF-8")+"="+URLEncoder.encode(dir,"UTF-8")+"&"
                        +URLEncoder.encode("Dur","UTF-8")+"="+URLEncoder.encode(callDuration,"UTF-8")+"&"
                        +URLEncoder.encode("UserID","UTF-8")+"="+URLEncoder.encode(LoginActivity.sUserid,"UTF-8");
            }
            else if(sType.equals("getRegDetails")) {
                sURL ="http://210.18.139.72/bookings/getRegistrationDetails.php";
                String sClientID = params[1];
                post_data = URLEncoder.encode("db","UTF-8")+"="+URLEncoder.encode(LoginActivity.sdb,"UTF-8")+"&"
                        +URLEncoder.encode("ClientID", "UTF-8") + "=" + URLEncoder.encode(sClientID, "UTF-8");
            }
            else if(sType.equals("callLogUpdateDt")) {
                sURL ="http://210.18.139.72/bookings/callLogUpdateDt.php";
                post_data = URLEncoder.encode("db","UTF-8")+"="+URLEncoder.encode(LoginActivity.sdb,"UTF-8")+"&"
                        +URLEncoder.encode("UserID","UTF-8")+"="+URLEncoder.encode(LoginActivity.sUserid,"UTF-8");            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            URL url = new URL(sURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream= httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
            sResult="";
            String sLine="";
            while ((sLine = bufferedReader.readLine())!=null){
                sResult += sLine;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return sResult;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPreExecute() {
        alertDialog  = new AlertDialog.Builder(context).create();
        alertDialog.setTitle("LoginStatus");
    }


    @Override
    protected void onPostExecute(String sResult) {
        if (sType == "getRegDetails"){
            if(sResult == null)
            {
                //showClientRegn.td.setText("Unable to fetch record");
            }
            else if(sResult.contains("Fetch Success")){
                parseResultAndFillRegisterPage();
            }
        }
        else if (sType.contains("list")){
            parseResultAndListGVData();
        }
        else {
            if(sResult == null)
            {
                //registerClient.etName.setText("testing");
                // do what you want to do
            }
            else if(sResult.contains("Registration Delete Success"))
            {
                Toast.makeText(context, sResult, Toast.LENGTH_LONG).show();
                registerClient.clearScreen();
            } else if(sResult.contains("Registration Success"))
            {
                Toast.makeText(context, sResult, Toast.LENGTH_LONG).show();
                //registerClient.sClient = sResult.substring(sResult.indexOf(":") + 1);
            }
            else if(sResult.contains("Login Success")) // msg you get from success like "Login Success"
            {
                alertDialog.setMessage(sResult);
                alertDialog.show();

                /*context.startActivity(new Intent(context, registerClient.class));
                registerClient.sUserid = sResult.substring(sResult.indexOf(":") + 1,sResult.indexOf("Role") );
                */
                Intent intRegClient = new Intent(context, registerClient.class);
                LoginActivity.sUserid = sResult.substring(sResult.indexOf(":") + 1,sResult.indexOf("Role") );

                registerClient.bTimeout = false;
                intRegClient.putExtra(registerClient.USERID_EXTRA,LoginActivity.sUserid);
                LoginActivity.sRole = sResult.substring(sResult.indexOf("Role")+5,sResult.indexOf("cl"));
                intRegClient.putExtra(registerClient.USERROLE_EXTRA,LoginActivity.sRole);
                LoginActivity.sclDt = sResult.substring(sResult.indexOf("cl")+3,sResult.indexOf("IP"));

                //intRegClient.putExtra(registerClient.USERCLDT_EXTRA,LoginActivity.sclDt);
                context.startActivity(intRegClient);
            }
            else if(sResult.contains("Login Failure")) // msg you get from success like "Login Failure"
            {
                alertDialog.setMessage(sResult);
                alertDialog.show();
            }
            else if(sResult.contains("calls Logged"))
            {
                //alertDialog.setMessage(sResult);
                //alertDialog.show();
            }
            else{
                alertDialog.setMessage(sResult);
                alertDialog.show();
            }

        }

    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    public BackgroundWorker() {
        super();
    }

    private boolean parseResultAndFillRegisterPage(){
        try {
            JSONArray JA = new JSONArray(sResult);
            for (int row=0; row < JA.length()-1;row++){
                JSONObject jsonObject = (JSONObject)JA.get(row);
                registerClient.etName.setText((String)jsonObject.get("Name"));
                registerClient.etPhone.setText((String)jsonObject.get("Phone"));
                registerClient.etEventDate.setText((String)jsonObject.get("EventDate"));
                registerClient.etConfirmBy.setText((String)jsonObject.get("ConfirmBy"));
                registerClient.etOfferPrice.setText((String)jsonObject.get("OfferPrice"));
                registerClient.etAskingPrice.setText((String)jsonObject.get("AskingPrice"));
                String sComments = "\n" + ((String)jsonObject.get("upd")) +":" + (String)jsonObject.get("Comments");
                registerClient.etComments.setText(sComments);
                String sLoc = (String)((String) jsonObject.get("hallLocation")).toUpperCase();
                sLoc.replaceAll(" ","");
                int iLoc = (sLoc.compareTo("PARTY HALL")==0)? 0:(sLoc.compareTo("MAHAL")==0)? 1:(sLoc.compareTo("BEACH HOUSE")==0)? 2:3;
                registerClient.locSpinner.setSelection(iLoc);
                String sStatus = (String) jsonObject.get("Status");
                int iStatus = (sStatus.compareTo("Interested")==0)? 1:(sStatus.compareTo("AlmostReady")==0)? 2:(sStatus.compareTo("Booked")==0)? 3:
                        (sStatus.compareTo("DoubleBooking")==0)? 4:(sStatus.compareTo("Price Bargain")==0)? 5:(sStatus.compareTo("NotInterested")==0)? 6:0;
                registerClient.statusSpinner.setSelection(iStatus);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
    private boolean parseResultAndListGVData(){
        try {
            JSONArray ja=new JSONArray(sResult);
            JSONObject jo=null;
            regList.clear();
            for(int i=0;i<ja.length();i++)
            {
                jo=ja.getJSONObject(i);
                String name="";
                if (sType.equals("listRegistrations") ) {
                    name=jo.getString("hallLocation");
                    name = name + "." + jo.getString("EventDate");
                    name = name + "." + jo.getString("Name");
                    name = name + ":" + jo.getString("Id");
                }
                else if (sType.equals("listBeachBookings")){
                    name=jo.getString("EventDt");
                    name = name + "." + jo.getString("Name");
                    name = name + ":" + jo.getString("Id");
                }
                else if (sType.equals("listPayments")){
                    name=jo.getString("UpdateTS");
                    name = name.substring(1,17);
                    name = name + ".Rs" + jo.getString("Amount");
                    name = name + "." + jo.getString("paymentMode");
                }
                regList.add(name);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(context, "Unable to parse:"+e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
        //final Intent intGVData;
        if (sType.equals("listRegistrations") ) {
            intGVData = new Intent(context, registerClient.class);
            gv = listRegistrations.gv;
        }
        else if (sType.equals("listBeachBookings")) {
            intGVData = new Intent(context, ListPayments.class);
            gv = listBeachBookings.gv;
        }
        else if (sType.equals("listPayments")) {
            intGVData = new Intent(context, ListPayments.class);
            gv = ListPayments.gv;
        }

        ArrayAdapter adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1,regList);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Toast.makeText(c, regList.get(position), Toast.LENGTH_SHORT).show();

                //Intent intRegClient = new Intent(c,showClientRegn.class);
                String dispVal = regList.get(position) ;
                intGVData.putExtra(listRegistrations.ID_EXTRA,dispVal.substring(dispVal.indexOf(":") + 1));
                context.startActivity(intGVData);


            }
        });


        return true;
    }
}
