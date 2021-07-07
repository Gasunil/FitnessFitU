package com.example.frgment;

import android.content.Context;
import android.graphics.Color;
import android.media.Image;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.health.TimerStat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fitnessfitu.LoginActivity;
import com.example.fitnessfitu.R;
import com.example.fitnessfitu.RealtimeActivity;
import com.example.httpconnect.PutData;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentRealtime extends Fragment {

    public static LineChart chart;
    private Thread thread;
    private ImageButton btnPlay;
    private ImageButton btnStop;

    TextView text5;

    //time
    public static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    Date today = new Date();
    String totimeString = timeFormat.format(today);
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
    String todayString = dateFormat.format(today);

    // 누적시간 count
    private int counter = 0;
    private String TimeStartString;
    static int sendTime = 0;

    public static void setsendTime(){
        sendTime++;
    }

    private long TimeStart;
    private long TimeEnd;
    private int TimeSub;

    TextView txTime;

    // 근전도 값 저장
    public static float spvalue;

    // 목표달성 변수 count set
    public static int goalTemp;
    TextView txGoalTemp;

    //목표퍼센트 변수
    TextView txTarget1;
    TextView txTarget2;
    TextView txTarget3;

    //무게 누적 + 1RM
//    private  String txOneRm;
    private int txWeightSet = 0;
    TextView txWeight;
    int sendWeight = 0;
    String weightValue;
    TextView txKg;


    float spvalue2 = (float) 0.0;
    int valueflag = 0;

    int valueflag1 = 0;
    int valueflag2 = 0;
    int valueflag3 = 0;

    //Count
    int sendCount = 0;
    String valueCount;
    String valueCount1;
    String valueCount2;
    String valueCount3;

    public int valueCounter = 0;
    public int valueCounter1 = 0;
    public int valueCounter2 = 0;
    public int valueCounter3 = 0;

    //battery
    ProgressBar batteryProgress;
    private int battery;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_realtime, container, false);

        text5 = view.findViewById(R.id.text5);

        Thread thread = new Thread();

        chart = view.findViewById(R.id.chart);

        //누적 시간 set
        txTime = view.findViewById(R.id.text7);
        txWeight = view.findViewById(R.id.text6);

        //battery set
        batteryProgress = view.findViewById(R.id.battery);

        //목표퍼센트 뷰 60퍼 80퍼 100퍼
        txTarget1 = view.findViewById(R.id.text1);
        txTarget2 = view.findViewById(R.id.text2);
        txTarget3 = view.findViewById(R.id.text3);

        // 목표달성 textview --> count set
//        txGoal = view.findViewById(R.id.text4);
//        txGoal.setText(String.valueOf(RealtimeActivity.getGoal()));
        txGoalTemp = view.findViewById(R.id.text4);
        txKg = view.findViewById(R.id.txKg);

        // click --> char start
        btnPlay = view.findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(RealtimeActivity.getTargetFlag() == 1){
                    btnPlay.setVisibility(View.GONE);
                    btnStop.setVisibility(View.VISIBLE);

                    chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                    chart.getAxisRight().setEnabled(false);
                    chart.getLegend().setTextColor(Color.WHITE);
                    chart.invalidate();
                    chart.setDescription(null);
                    chart.setAutoScaleMinMaxEnabled(true);
                    chart.setDragEnabled(false);
                    chart.setScaleEnabled(false);

                    LineData data = new LineData();
                    chart.setData(data);
                    XAxis xAxis = chart.getXAxis();
                    xAxis.setDrawLabels(false);

                    //X축 값 갯수
                    xAxis.setLabelCount(10, true);

                    //Y축 설정
                    YAxis yAxis = chart.getAxisLeft();
                    yAxis.setAxisMaximum(RealtimeActivity.getPicValue());
                    yAxis.setAxisMinimum(0f);

                    //범주 설정
                    Legend l = chart.getLegend();
                    l.setEnabled(false);

                    //목표라인
                    LimitLine ll1 = new LimitLine(RealtimeActivity.getGoal(), "Target");
                    ll1.setLineWidth(2f);
                    ll1.enableDashedLine(10f, 10f, 0f);
                    ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
                    ll1.setTextSize(10f);

                    yAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
                    yAxis.addLimitLine(ll1);

    //                long TimeStart = System.currentTimeMillis();
    //                TimeStartString = String.valueOf(TimeStart/6000);//

                    goalTemp = RealtimeActivity.Goal;
                    txGoalTemp.setText(String.valueOf(goalTemp + "%"));
                    Log.e("-------------txKg", "--------------txGoalTemp : " + String.valueOf(goalTemp + "%"));
                    Log.e("-------------txKg", "--------------kg : " + RealtimeActivity.getOneRm());
                    String oneRm = RealtimeActivity.getOneRm();
                    txKg.setText(oneRm+"kg");


                    feedMultiple();
                }

            }
        });

        // click --> char stop
        btnStop = view.findViewById(R.id.btnStop);
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnPlay.setVisibility(View.VISIBLE);
                btnStop.setVisibility(View.GONE);

                chart.clear();
            }
        });

        return view;
    }

    private void addEntry() {
        LineData data = chart.getData();
//      spvalue = RealtimeActivity.getSpvalue();
        spvalue = ((RealtimeActivity) getActivity()).getSpvalue();

        float diff=spvalue-spvalue2;
        float GoalLevel = RealtimeActivity.getGoal();

//        Log.e("valueflag","-------valueflag  : "+ valueflag);
//        Log.e("valueflag","-------이거보다 GoalLevel  : "+ GoalLevel);
//        Log.e("valueflag","-------이게 커야함spvalue  : "+ spvalue);
//        Log.e("valueflag","-------0보다 작을 것 diff  : "+ diff);
//        Log.e("valueflag","-------참고용  spvalue2  : "+ spvalue2);
//        if(valueflag == 0){
//            Log.e("valueflag","-------valueflag  : 성공 ");
//        }
//        if(spvalue > GoalLevel){
//            Log.e("valueflag","-------GoalLevel  : 성공 ");
//        }
//        if(diff < 0){
//            Log.e("valueflag","-------diff  : 성공 ");
//        }

        //목표달성Count
        if ((diff < 0) && (spvalue > GoalLevel) && (valueflag == 0) ){
            valueflag = 1;
//            Log.e("valueflag","-------valueflag  : "+ valueflag);
            valueCounter++;
            sendCount++;
        }else if (spvalue < GoalLevel){
            valueflag = 0;
        }
        valueCount = String.valueOf(valueCounter);
        text5.setText(valueCount);

        //근지구력
        if ((diff < 0) && (spvalue > RealtimeActivity.getPicValue()*0.6) && (valueflag1 == 0) &&(spvalue < RealtimeActivity.getPicValue()*0.8)){
            valueflag1 = 1;
            valueCounter1++;
        }else if (spvalue < RealtimeActivity.getPicValue()*0.6){
            valueflag1 = 0;
        }
        valueCount1 = String.valueOf(valueCounter1);
        txTarget1.setText(valueCount1);

        //근비대
        if ((diff < 0) && (spvalue > RealtimeActivity.getPicValue()*0.8) && (valueflag2 == 0) && (spvalue < RealtimeActivity.getPicValue())){
            valueflag2 = 1;
            valueCounter2++;
        }else if (spvalue < RealtimeActivity.getPicValue()*0.8){
            valueflag2 = 0;
        }
        valueCount2 = String.valueOf(valueCounter2);
        txTarget2.setText(valueCount2);

        //최대근력
        if ((spvalue >= RealtimeActivity.getPicValue()) && (valueflag3 == 0)){
            valueflag3 = 1;
            valueCounter3++;
            //무게 counting

            txWeightSet = Integer.valueOf(RealtimeActivity.getOneRm())*valueCounter3;
            sendWeight++;

        }else if (spvalue < RealtimeActivity.getPicValue()){
            valueflag3 = 0;
        }
        valueCount3 = String.valueOf(valueCounter3);
        txTarget3.setText(valueCount3);
        weightValue = String.valueOf(txWeightSet);
        txWeight.setText(weightValue);


        //timer set
        if (counter == 0){
            RealtimeActivity.timeCounter = 0;
            counter = 1;
        }
        TimeStartString = String.valueOf(RealtimeActivity.timeCounter);
        txTime.setText(TimeStartString);

        //battery set
        battery = RealtimeActivity.batterygauge;
        batteryProgress.setProgress(battery);//무게 counting

        if (90 <= battery) {
            batteryProgress.setProgress(90);
        } else if (65 <= battery && battery < 90) {
            batteryProgress.setProgress(70);
        } else if (40 <= battery && battery < 65) {
            batteryProgress.setProgress(50);
        } else if (15 <= battery && battery < 40) {
            batteryProgress.setProgress(30);
        } else {
            batteryProgress.setProgress(10);
        }


        //시간 전달
        today = new Date();
        totimeString = timeFormat.format(today);

//        //1RM counting
//        txOneRm = RealtimeActivity.txWeightValue;

        if (data != null) {
            ILineDataSet set = data.getDataSetByIndex(0);

            if (set == null) {
                set = createSet();
                data.addDataSet(set);
            }

            Log.v("FagmentRealtime:","spvalue : "+spvalue);
            data.addEntry(new Entry(set.getEntryCount(), spvalue), 0);
            data.notifyDataChanged();
            Log.v("FagmentRealtime : ","아래값부터 저장 ------------------------- ");
            if (spvalue != 0){
                String valueString = String.valueOf(spvalue);
                Log.v("DB 저장값 =======:","send spvalue : "+valueString);
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String[] field = new String[9];
                        field[0] = "date";
                        field[1] = "time";
                        field[2] = "id";
                        field[3] = "name";
                        field[4] = "value";
                        field[5] = "count";
                        field[6] = "kg";
                        field[7] = "min";
                        field[8] = "body";


                        String[] data = new String[9];
                        data[0] = todayString;
                        data[1] = totimeString;
                        data[2] = LoginActivity.getid();
                        data[3] = LoginActivity.getname();
                        data[4] = valueString;
                        data[5] = String.valueOf(sendCount);
                        data[6] = String.valueOf(sendWeight);
                        data[7] = String.valueOf(sendTime);
                        data[8] = RealtimeActivity.getgoalsetting();

                        PutData putData = new PutData("http://175.205.234.222:81/muscleDBsave.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                Log.v("result:","result : "+result);
                                sendCount = 0;
                                sendWeight = 0;
                                sendTime = 0;
                            }
                        }
                    }
                });
            }

            spvalue2 = spvalue;
            spvalue = 0;
            chart.notifyDataSetChanged();
            chart.setVisibleXRangeMaximum(10);
            chart.moveViewToX(data.getEntryCount());
        }
    }

    private LineDataSet createSet() {

        LineDataSet set = new LineDataSet(null, "Dynamic Data");
        set.setFillAlpha(110);
        set.setFillColor(Color.parseColor("#d7e7fa"));
        set.setColor(Color.parseColor("#0B80C9"));
        set.setCircleColor(Color.parseColor("#FFA1B4DC"));
        set.setCircleColorHole(Color.BLUE);
        set.setValueTextColor(Color.WHITE);
        set.setDrawValues(false);
        set.setLineWidth(2);
        set.setCircleRadius(6);
        set.setDrawCircleHole(false);
        set.setDrawCircles(false);
        set.setValueTextSize(9f);
        set.setDrawFilled(true);
        set.setMode(LineDataSet.Mode.CUBIC_BEZIER);

        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setHighLightColor(Color.rgb(244, 117, 117));

        return set;
    }

    private void feedMultiple() {
        if (thread != null)
            thread.interrupt();

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                addEntry();
            }
        };

            thread = new Thread(new Runnable() {

            @Override
            public void run() {
                while (RealtimeActivity.getnvalue() == 0) {
                    try {
                        requireActivity().runOnUiThread(runnable);
                        Thread.sleep(150);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }
}

