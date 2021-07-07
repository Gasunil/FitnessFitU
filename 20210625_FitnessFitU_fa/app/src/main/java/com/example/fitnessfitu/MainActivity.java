package com.example.fitnessfitu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.httpconnect.PutData;

public class MainActivity extends AppCompatActivity {

    Button btnLogin;
    Button btnRegister;
    //자동 로그인

    String userid;
    String username;

    TextView txLoginJump;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.btnMainLogin);
        btnRegister = findViewById(R.id.btnRegister);

        TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String phoneID = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String[] field = new String[1];
                        field[0] = "phoneid";
                        //Creating array for data
                        String[] data = new String[1];
                        data[0] = phoneID;
                        Log.v("phoneID data :", " ---------------phoneID : " + phoneID);
                        PutData putData = new PutData("http://175.205.234.222:81/login_recordserch.php", "POST", field, data);
                        //PutData putData = new PutData("http://192.168.0.16:81/signup.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                Log.v("phoneID data :", " ---------------result : " + result);
                                if (result.equals("데이터없음")) {
                                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                } else {
                                    String[] results = result.split(",");
                                    userid = results[0];
                                    username = results[1];

                                    LoginActivity.setautoid(userid);
                                    LoginActivity.setautoname(username);
                                    Log.v("userid data :", " ---------------userid : " + userid);
                                    Log.v("username data :", " ---------------username : " + username);
                                    Intent intent = new Intent(MainActivity.this, RealtimeActivity.class);
                                    startActivity(intent);

                                }

                                Log.v("phoneID data :", " ---------------phoneID : " + phoneID);
                            }
                        }
                    }
                });
            }
        });

        txLoginJump = findViewById(R.id.txLoginJump);
        txLoginJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
