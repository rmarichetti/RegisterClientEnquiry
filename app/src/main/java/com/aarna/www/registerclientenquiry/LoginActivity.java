package com.aarna.www.registerclientenquiry;


import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.EditText;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class LoginActivity extends AppCompatActivity {
    EditText etLogin, etPwd, etdb;
    public static String sUserid, sRole, sdb, sclDt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etLogin = (EditText)findViewById(R.id.etLogin);
        etPwd = (EditText)findViewById(R.id.etPwd);
        etdb = (EditText)findViewById(R.id.etDB);
    }
    public void OnLogin(View view){
        String sUsername = etLogin.getText().toString();
        String sPassword = etPwd.getText().toString();
        sdb = etdb.getText().toString();
        String sType = "login";
        //Intent callIntent = new Intent(Intent.ACTION_CALL);
        //callIntent.setData(Uri.parse("tel:04444230330"));

        //startActivity(callIntent);

        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(sType, sUsername, md5(sPassword), sdb);
        //backgroundWorker.execute(sType, "Rajesh", md5("r2"));

    }
    private static final String md5(final String password) {
        try {

            MessageDigest digest = java.security.MessageDigest
                    .getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
//import org.apache.http.conn.util.InetAddressUtils;

