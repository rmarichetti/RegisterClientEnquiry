package com.aarna.www.registerclientenquiry.MySQL;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.GridView;
import android.widget.Toast;
import java.io.BufferedInputStream;
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


public class Downloader extends AsyncTask<Void,Void,String> {
    Context c;
    String urlAddress;
    GridView gv;
    ProgressDialog pd;
    String callingPgm, pHall, pName, sBooked, pEventID;
    public Downloader(Context c, String urlAddress, String callingPgm) {
        this.c = c;
        this.urlAddress = urlAddress;
        //this.gv = gv;
        this.callingPgm = callingPgm;
    }
    public void setGVandParams(GridView gv, String pHall, String pName, String sBooked){
        this.gv = gv;
        this.pHall = pHall;
        this.pName = pName;
        this.sBooked = sBooked;
        /*
        String params = "";
        if (pHall.length()> 0) params += "?Hall=" + pHall + "&";
        if (params.length()<=0) params += "?";
        if (pName.length()> 0) params += "Name=" + pName + "&";;
        if (params.length()<=0) params += "?";
        if (sBooked.length()> 0) params += "Status=" + sBooked + "&";;
        urlAddress += params;*/
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd=new ProgressDialog(c);
        pd.setTitle("Fetch");
        pd.setMessage("Fetching data....Please wait");
        pd.show();
    }
    @Override
    protected String doInBackground(Void... params) {
        return this.downloadData();
    }
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pd.dismiss();
        if(s==null)
        {
            Toast.makeText(c,"Unsuccessful,Null returned",Toast.LENGTH_SHORT).show();
        }else {
            //CALL DATA PARSER TO PARSE
            DataParser parser=new DataParser(c,gv,s,callingPgm);
            parser.execute();
        }
    }

    public Downloader() {
        super();
    }

    private String downloadData(){
        String sResult;
        try {
            URL url = new URL(urlAddress);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream= httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
            String post_data="";
            //if (callingPgm.equals("listPayments")){
                post_data = URLEncoder.encode("EventID","UTF-8")+"="+URLEncoder.encode(pHall,"UTF-8")+"&"
                        +URLEncoder.encode("Name","UTF-8")+"="+URLEncoder.encode(pName,"UTF-8")+"&"
                        +URLEncoder.encode("Status","UTF-8")+"="+URLEncoder.encode(sBooked,"UTF-8");
            //}
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
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }
    private String downloadData2()
    {
        HttpURLConnection con=Connector.connect(urlAddress);
        if(con==null)
        {
            return null;
        }
        InputStream is=null;
        try {
            is=new BufferedInputStream(con.getInputStream());
            BufferedReader br=new BufferedReader(new InputStreamReader(is));
            String line=null;
            StringBuffer response=new StringBuffer();
            if(br != null)
            {
                while ((line=br.readLine()) != null)
                {
                    response.append(line+"\n");
                }
                br.close();
            }else{
                return null;
            }
            return response.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(is != null)
            {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}