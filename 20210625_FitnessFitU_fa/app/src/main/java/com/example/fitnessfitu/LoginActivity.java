package com.example.fitnessfitu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.httpconnect.PutData;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText textInputEditTextId, textInputEditTextPassword;

    Button buttonLogin;
    TextView textViewSignUp;
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    Date today = new Date();
    String totimeString = timeFormat.format(today);
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    String todayString = dateFormat.format(today);

    static String id;
    String password;
    static String userName;

    public static String getname(){
        return userName;
    }
    public static String getid(){
        return id;
    }
    public static String setautoname(String autoname){
        userName = autoname;
        return userName;
    }
    public static String setautoid(String autoid){
        id = autoid;
        return id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textInputEditTextId=(TextInputEditText)findViewById(R.id.userID);
        textInputEditTextPassword=(TextInputEditText)findViewById(R.id.userPW);
        buttonLogin=(Button)findViewById(R.id.btnLogin);
        textViewSignUp=(TextView)findViewById(R.id.signUpText);
        //자동 로그인에 필요한 부분 (로그인 기록 관리)
        TelephonyManager telephony =  (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String phoneID = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        textViewSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), JoinActivity.class);
                startActivity(intent);
                finish();
            }
        });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name, email, mobile;
                id=String.valueOf(textInputEditTextId.getText());
                password=String.valueOf(textInputEditTextPassword.getText());

                if(!id.equals("")&&!password.equals("")){
//                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[2];
                            field[0] = "userid";
                            field[1] = "password";
                            //Creating array for data
                            String[] data = new String[2];
                            data[0] = id;
                            data[1] = password;

                            PutData putData = new PutData("http://175.205.234.222:81/login.php", "POST", field, data);
                            //PutData putData = new PutData("http://192.168.0.16:81/signup.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
                                    String result = putData.getResult();
                                    Log.v("result data :"," ----------------result : "+ result);
                                    String[] results = result.split(",");
                                    userName = results[0];
                                    if (result.equals("로그인 실패")){
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    }else{
                                        String loginresult = results[1];
                                        Log.v("phoneID data :"," ---------------phoneID : "+ phoneID);
                                        if(loginresult.equals("로그인 성공")) {
                                            String[] field2 = new String[5];
                                            field2[0] = "date";
                                            field2[1] = "time";
                                            field2[2] = "id";
                                            field2[3] = "name";
                                            field2[4] = "phoneid";
                                            //Creating array for data
                                            String[] data2 = new String[5];
                                            data2[0] = todayString;
                                            data2[1] = totimeString;
                                            data2[2] = id;
                                            data2[3] = userName;
                                            data2[4] = phoneID;
                                            PutData putData2 = new PutData("http://175.205.234.222:81/login_recordsave.php", "POST", field2, data2);
                                            //PutData putData2 = new PutData("http://192.168.0.16:81/signup.php", "POST", field, data);
                                            if(putData2.startPut()){
                                                if(putData2.onComplete()){
                                                    String result2 = putData2.getResult();
                                                    Log.v("일단 저장 된거같은데?"," -----------------------------------------------"+result2);
                                                }
                                            }

                                            Toast.makeText(getApplicationContext(), "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                                            Log.v("phoneID data :"," ---------------phoneID :11111111111111111111111111111111111111111111111 ");
                                            Intent intent=new Intent(getApplicationContext(), RealtimeActivity.class);
                                            intent.putExtra("Id",data[0]);
                                            intent.putExtra("password", data[1]);
                                            startActivity(intent);
                                            finish();
                                        }else{
                                            Toast.makeText(getApplicationContext(), "로그인 되었습니다.", Toast.LENGTH_SHORT).show();
                                            Log.v("phoneID data :"," ---------------phoneID :2222222222222222222222222222222222222222222222 ");
                                        }
                                        //End ProgressBar (Set visibility to GONE)
                                    }
                                }
                            }
                        }
                    });
                }
            }
        });
    }
}

