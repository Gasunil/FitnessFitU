package com.example.fitnessfitu;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.frgment.FragmentAnalysis;
import com.example.frgment.FragmentInfo;
import com.example.frgment.FragmentRealtime;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.makeramen.roundedimageview.RoundedImageView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;


public class RealtimeActivity<picValue> extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentManager fm;
    private FragmentTransaction ft;

    private FragmentRealtime fragmentRealtime;
    private FragmentAnalysis fragmentAnalysis;
    private FragmentInfo fragmentInfo;

    TextView textNavi;
    Button btnConfirm;

    Button mBtnConnect;
    BluetoothAdapter mBluetoothAdapter;
    Set<BluetoothDevice> mPairedDevices;
    List<String> mListPairedDevices;

    Handler mBluetoothHandler;
    ConnectedBluetoothThread mThreadConnectedBluetooth;
    BluetoothDevice mBluetoothDevice;
    BluetoothSocket mBluetoothSocket;

    final static int BT_REQUEST_ENABLE = 1;
    final static int BT_MESSAGE_READ = 2;
    final static int BT_CONNECTING_STATUS = 3;
    final static UUID BT_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");


    // 누적 시간 check
    public static int timeCounter = 0;

    //로그인 정보 - 변수
    String userId;
    String userPassword;

    //bottom_sheet 목표설정 data
    public static String OneRm;
    public static String getOneRm(){ return OneRm;}
    TextView txProgress;

    //목표설정 플래그
    public static int targetFlag = 0;

    public static int getTargetFlag(){
        return targetFlag;
    }

    //목표설정에 따른 부위 1~4 1.팔, 2.다리, 3.가슴, 4.복근
    public static String goalsetting;
    public static String getgoalsetting(){
        return goalsetting;
    }

    int colorvalue;
    // 목표설정
    public static int Goal;

    // 1rm 무게
    EditText txOneRm;
//    public static String txWeightValue;


    public static float getGoal(){
        float GoalValue = ((float) Goal /100)*getPicValue();
        Log.v("로그표시","로그"+Goal);
        return GoalValue;
    }

    Date today = new Date();
    //SimpleDateFormat sdformat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss");
    SimpleDateFormat sdformat = new SimpleDateFormat("HH:mm:ss");
    String todayString = sdformat.format(today);


    public float spvalue;
    public static float picValue = (float) 0.0;
    public static int batterygauge;

    public float getSpvalue(){
        return spvalue;
    }
    public static float getPicValue(){
        return picValue;
    }
    public static int getBatterygauge(){return batterygauge;}

    int confirmflag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_realtime);

        TextView textNavi = findViewById(R.id.textNavi);

        Button buttonShow = findViewById(R.id.btnTarget);
        buttonShow.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public void onClick(View v) {

                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(RealtimeActivity.this, R.style.BottomSheetDialogTheme);

                View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.layout_bottom_sheet,
                        (LinearLayout)findViewById(R.id.bottomSheetContainer));

//                edTx = bottomSheetDialog.findViewById(R.id.txWeight);
//                String OneRMWeight = edTx.

                txProgress = bottomSheetView.findViewById(R.id.txProgress);
                btnConfirm = bottomSheetView.findViewById(R.id.btnConfirm);
                txOneRm = bottomSheetView.findViewById(R.id.txOneRm);

//                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

                txOneRm.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        OneRm = txOneRm.getText().toString();
                        if(OneRm.length() > 0){
                            //btnConfirm.setEnabled(true);
                            confirmflag = 1;

                        }else{
                            //btnConfirm.setEnabled(false);
                            confirmflag = 0;
                        }
                    }

                });

                bottomSheetView.findViewById(R.id.btnConfirm).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        OneRm = txOneRm.getText().toString();
                        if(OneRm.length() == 0){
                            Toast.makeText(RealtimeActivity.this, "1RM을 입력하세요", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(RealtimeActivity.this, "확인", Toast.LENGTH_SHORT).show();
                            targetFlag = 1;
                            bottomSheetDialog.hide();
                        }

                    }
                });

                bottomSheetView.findViewById(R.id.btnCancle).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(RealtimeActivity.this, "취소", Toast.LENGTH_SHORT).show();
                        bottomSheetDialog.hide();
                    }
                });

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();

//                txOneRm = findViewById(R.id.txOneRm);
//                if(!txOneRm.getText().toString().equals("")){
//                    txWeightValue = txOneRm.getText().toString();
//                }


                SeekBar seekBar = bottomSheetView.findViewById(R.id.seek_bar);
                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        txProgress.setText(progress+"%");
                        Goal = progress;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
//                        SeekbarTarget = txProgress.getText().toString();

                    }
                });

                //이미지 눌렀을 때 그 근량에 적합한 최대치 설정 유도
                RoundedImageView arm = bottomSheetView.findViewById(R.id.arm);
                RoundedImageView leg = bottomSheetView.findViewById(R.id.leg);
                RoundedImageView chest = bottomSheetView.findViewById(R.id.chest);
                RoundedImageView abdominal = bottomSheetView.findViewById(R.id.abdominal);

                arm.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (colorvalue == 1){
                            leg.setBackgroundColor(Color.WHITE);
                            chest.setBackgroundColor(Color.WHITE);
                            abdominal.setBackgroundColor(Color.WHITE);
                        }
                        arm.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        colorvalue = 1;
                        goalsetting = "1";
                        picValue = (float) 100.0;
                        Log.v("picValue는---------","밸류거ㅏ아아아아아"+picValue);
                        return false;
                    }
                });

                leg.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (colorvalue == 1){
                            arm.setBackgroundColor(Color.WHITE);
                            chest.setBackgroundColor(Color.WHITE);
                            abdominal.setBackgroundColor(Color.WHITE);
                        }
                        leg.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        colorvalue = 1;
                        goalsetting = "2";
                        picValue = (float) 100.0;
                        Log.v("picValue는---------","밸류거ㅏ아아아아아"+picValue);
                        return false;
                    }
                });

                chest.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (colorvalue == 1){
                            leg.setBackgroundColor(Color.WHITE);
                            arm.setBackgroundColor(Color.WHITE);
                            abdominal.setBackgroundColor(Color.WHITE);
                        }
                        chest.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        colorvalue = 1;
                        goalsetting = "3";
                        picValue = (float) 100.0;
                        Log.v("picValue는---------","밸류거ㅏ아아아아아"+picValue);
                        return false;
                    }
                });

                abdominal.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (colorvalue == 1){
                            leg.setBackgroundColor(Color.WHITE);
                            chest.setBackgroundColor(Color.WHITE);
                            arm.setBackgroundColor(Color.WHITE);
                        }
                        abdominal.setBackgroundColor(Color.parseColor("#dcdcdc"));
                        colorvalue = 1;
                        goalsetting = "4";
                        picValue = (float) 100.0;
                        Log.v("picValue는---------","Value : "+picValue);
                        return false;
                    }
                });
            }
        });

        bottomNavigationView = findViewById(R.id.bottomNavi);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch(item.getItemId()) {
                    case R.id.realtime:
                        setFragment(0);
                        textNavi.setText("실시간 측정");
                        break;
                    case R.id.analysis:
                        setFragment(1);
                        textNavi.setText("분석");
                        break;
                    case R.id.info:
                        setFragment(2);
                        textNavi.setText("영상");
                        break;
                }
                return true;
            }
        });

        fragmentRealtime = new FragmentRealtime();
        fragmentAnalysis = new FragmentAnalysis();
        fragmentInfo = new FragmentInfo();
        setFragment(0);


        mBtnConnect = (Button)findViewById(R.id.btnConnect);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        //블루투스 프로그램 시작
        mBtnConnect.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                listPairedDevices();
            }
        });

        mBluetoothHandler = new Handler(Looper.getMainLooper()){
            public void handleMessage(android.os.Message msg){
                String readMessage = null;
                String readBody;

                String[] readBodys;
                String[] Dotpase;
                String[] readMes;
                String[] readMeshelper;

                Log.v("msg:","msg : "+msg);
                if(msg.what == BT_MESSAGE_READ) {
                    readMessage = new String((byte[]) msg.obj, StandardCharsets.UTF_8);

                    //Log.v("RealtimeActivity:","readMessage : "+readMessage);
                    readMessage = readMessage.replace("\r", "");
                    readMessage = readMessage.replace("\n", "");
                    Log.v("RealtimeActivity:","Replace readMessage---- : "+readMessage);
                    readBodys = readMessage.split("@");
                    readMes = readBodys[0].split(",");

                    try {
                        batterygauge = Integer.parseInt(readMes[0]);
                    }catch (Exception e){
                        Log.v("RealtimeActivity:","배터리 오류 발생!");
                    }

                    try {
                        readBody = readMes[1].replace("-","");
                        spvalue = Float.parseFloat(readBody);
                        Log.v("RealtimeActivity:","readBody : "+readBody);
                    }catch (Exception e){
                        try{
                            readBody = readMes[1].replace("-","");
                            Dotpase = readBody.split(".");
                            readBody = Dotpase[0];
                            Log.v("RealtimeActivity:","Exception e : "+readBody);
                            spvalue = Float.parseFloat(readBody);
                        }catch (Exception y){
                            readMeshelper =  readBodys[1].split(",");
                            try{
                                readBody = readMeshelper[1].replace("-","");
                                Log.v("RealtimeActivity:","Exception y : "+readBody);
                                spvalue = Float.parseFloat(readBody);
                            }catch (Exception x){
                                try{
                                    readBody = readMeshelper[0].replace("-","");
                                    Log.v("RealtimeActivity:","Exception x : "+readBody);
                                    spvalue = Float.parseFloat(readBody);
                                }catch(Exception s){
                                    Log.e("RealtimeActivity:","Exception s : 이건 어쩔수없더라... ");
                                }
                            }
                        }
                    }
                    //readBodyhelper = readMeshelper[1];
                }
            }
        };

        //frgment 누적 시간
        TimerTask tt = new TimerTask(){
            @Override
            public void run() {
            timeCounter++;
            Log.v("로그표시","시간"+timeCounter);
            }
        };

        Timer timer = new Timer();
        timer.schedule(tt, 0, 60000);

        //로그인 id/pw 전달받음
        Intent userInfoIntent = getIntent();
        userId = userInfoIntent.getStringExtra("id");
        userPassword = userInfoIntent.getStringExtra("password");
        Toast.makeText(getApplicationContext(),"로그인 되었습니다", Toast.LENGTH_SHORT).show();

    }
    public static int nvalue;
    public static int getnvalue(){ return  nvalue;}

    private void setFragment(int n){
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();

        switch(n){
            case 0:
                ft.replace(R.id.main_frame, fragmentRealtime);
                nvalue = n;
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.main_frame, fragmentAnalysis);
                nvalue = n;
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.main_frame, fragmentInfo);
                nvalue = n;
                ft.commit();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BT_REQUEST_ENABLE) {
                if (resultCode == RESULT_OK) { // 블루투스 활성화를 확인을 클릭하였다면
                    Toast.makeText(getApplicationContext(), "블루투스 활성화", Toast.LENGTH_SHORT).show();
                } else if (resultCode == RESULT_CANCELED) { // 블루투스 활성화를 취소를 클릭하였다면
                    Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_LONG).show();
                }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    void listPairedDevices() {
        if (mBluetoothAdapter.isEnabled()) {
            mPairedDevices = mBluetoothAdapter.getBondedDevices();

            if (mPairedDevices.size() > 0) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("장치 선택");

                mListPairedDevices = new ArrayList<String>();
                for (BluetoothDevice device : mPairedDevices) {
                    mListPairedDevices.add(device.getName());
                    //mListPairedDevices.add(device.getName() + "\n" + device.getAddress());
                }
                final CharSequence[] items = mListPairedDevices.toArray(new CharSequence[mListPairedDevices.size()]);
                mListPairedDevices.toArray(new CharSequence[mListPairedDevices.size()]);

                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        connectSelectedDevice(items[item].toString());
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            } else {
                Toast.makeText(getApplicationContext(), "페어링된 장치가 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
        else {
            Toast.makeText(getApplicationContext(), "블루투스가 비활성화 되어 있습니다.", Toast.LENGTH_SHORT).show();
        }
    }
    void connectSelectedDevice(String selectedDeviceName) {
        for(BluetoothDevice tempDevice : mPairedDevices) {
            if (selectedDeviceName.equals(tempDevice.getName())) {
                mBluetoothDevice = tempDevice;
                break;
            }
        }
        try {
            mBluetoothSocket = mBluetoothDevice.createRfcommSocketToServiceRecord(BT_UUID);
            mBluetoothSocket.connect();
            mThreadConnectedBluetooth = new ConnectedBluetoothThread(mBluetoothSocket);
            mThreadConnectedBluetooth.start();
            mBluetoothHandler.obtainMessage(BT_CONNECTING_STATUS, 1, -1).sendToTarget();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "블루투스 연결 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }
    }

    private class ConnectedBluetoothThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedBluetoothThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "소켓 연결 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }
        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;

            while (true) {
                try {
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        SystemClock.sleep(100);
                        bytes = mmInStream.available();
                        bytes = mmInStream.read(buffer, 0, bytes);
                        mBluetoothHandler.obtainMessage(BT_MESSAGE_READ, bytes, -1, buffer).sendToTarget();
                    }
                } catch (IOException e) {
                    break;
                }
            }
        }
    }
}