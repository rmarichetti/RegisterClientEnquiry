package com.aarna.www.registerclientenquiry.MySQL;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;

import com.aarna.www.registerclientenquiry.listRegistrations;
import com.aarna.www.registerclientenquiry.registerClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DataParser extends AsyncTask<Void, Void, Integer> {
    Context c;
    GridView gv;
    String jsonData;
    String callingPgm;
    ProgressDialog pd;
    ArrayList<String> regList = new ArrayList<>();
//    ArrayList<String> regPhoneNoList = new ArrayList<>();

    public DataParser(Context c, GridView gv, String jsonData, String callingPgm) {
        this.c = c;
        this.gv = gv;
        this.jsonData = jsonData;
        this.callingPgm = callingPgm;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd = new ProgressDialog(c);
        pd.setTitle("Parse");
        pd.setMessage("Parsing..Please Wait");
        pd.show();
    }

    @Override
    protected Integer doInBackground(Void... params) {
        return this.parseData();
    }

    @Override

    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        pd.dismiss();
        if (result == 0) {
            Toast.makeText(c, "Unable to parse", Toast.LENGTH_SHORT).show();
        } else {

            if (callingPgm.equals("listRegistrations") || callingPgm.equals("listBeachBookings")){

                ArrayAdapter adapter = new ArrayAdapter(c, android.R.layout.simple_list_item_1,regList);
                gv.setAdapter(adapter);
                gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        //Toast.makeText(c, regList.get(position), Toast.LENGTH_SHORT).show();

                        //Intent intRegClient = new Intent(c,showClientRegn.class);
                        Intent intRegClient = new Intent(c, registerClient.class);
                        String dispVal = regList.get(position) ;
                        intRegClient.putExtra(listRegistrations.ID_EXTRA,dispVal.substring(dispVal.indexOf(":") + 1));
                        c.startActivity(intRegClient);


                    }
                });
            }
            else{

                //etName = (EditText)c.getClass().getName().findViewById(etName);
                //etName
                //registerClient r = new registerClient();
                //r.setValues();
            }
        }
    }
    private int parseData()
    {
        try {
            JSONArray ja=new JSONArray(jsonData);
            JSONObject jo=null;
            regList.clear();
            for(int i=0;i<ja.length();i++)
            {
                jo=ja.getJSONObject(i);
                String name="";
                if (callingPgm.equals("listRegistrations") ) {
                    name=jo.getString("hallLocation");
                    name = name + "." + jo.getString("EventDate");
                    name = name + "." + jo.getString("Name");
                    name = name + ":" + jo.getString("Id");
                }
                else if (callingPgm.equals("listBeachBookings")){
                    name=jo.getString("EventDt");
                    name = name + "." + jo.getString("Name");
                    name = name + ":" + jo.getString("Id");
                }
                regList.add(name);
            }
            return 1;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }
}