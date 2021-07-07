package com.example.frgment;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.fitnessfitu.LoginActivity;
import com.example.fitnessfitu.R;
import com.example.fitnessfitu.RealtimeActivity;
import com.example.fitnessfitu.WeekExerciseData;
import com.example.httpconnect.PutData;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.graphics.Color;

public class FragmentAnalysis extends Fragment {

    private View view;
    float armCount = 4f;
    float legCount = 2f;
    float chestCount = 2f;
    float abdominalCount = 3f;
    float periodSum = 4f;
    float periodAvg = 5f;

    Button txOneRM;
    Button txWeight;
    Button txTime;

    TextView txTotal;
    TextView txAvg;

    SeekBar skSum;
    SeekBar skAvg;

    //시간
    Date today = new Date();
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA);
    String todayString = dateFormat.format(today);
    Calendar currentDate = Calendar.getInstance();
    Calendar cal = Calendar.getInstance();
    String weekString;
    String monString;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @org.jetbrains.annotations.NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_analysis, container, false);

        TextView sumCount = view.findViewById(R.id.txtSumCount);
        TextView avgCount = view.findViewById(R.id.txtAvgCount);
        TextView sumKg = view.findViewById(R.id.txtSumkg);
        TextView avgKg = view.findViewById(R.id.txtAvgkg);
        TextView sumMin = view.findViewById(R.id.txtSumtime);
        TextView avgMin = view.findViewById(R.id.txtAvgtime);

        PieChart pieChart = view.findViewById(R.id.pieChart);

        ArrayList<PieEntry> exercise = new ArrayList<>();
        exercise.add(new PieEntry(armCount,"팔"));
        exercise.add(new PieEntry(armCount,"다리"));
        exercise.add(new PieEntry(armCount,"가슴"));
        exercise.add(new PieEntry(armCount,"복근"));

        int[] colorset = {Color.argb(100, 160, 223, 180), Color.argb(100, 12, 68, 122), Color.argb(100, 149, 192, 191), Color.argb(100, 62,111,154)};


        PieDataSet pieDataSet = new PieDataSet(exercise, "");
        pieDataSet.setColors(ColorTemplate.PASTEL_COLORS);
        pieDataSet.setValueTextColor(Color.WHITE);
        pieDataSet.setValueTextSize(15f);

        PieData pieData = new PieData(pieDataSet);

        pieChart.setData(pieData);
        pieChart.setCenterTextColor(Color.BLACK);
        pieChart.getDescription().setEnabled(false);
        pieChart.setCenterText("운동부위");
        pieChart.animateY(1500, Easing.EasingOption.EaseInOutCubic);
        pieChart.animate();


        //시간
        cal.setTime(today);
        cal.add(cal.DATE,-7);
        weekString =dateFormat.format(cal.getTime());
        cal.setTime(today);
        cal.add(cal.DATE,-30);
        monString = dateFormat.format(cal.getTime());

        //주간 버튼
        MaterialButton weeklyExercise = view.findViewById(R.id.weeklyExercise);
        weeklyExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String[] field = new String[3];
                        field[0] = "id";
                        field[1] = "fday";
                        field[2] = "lday";

                        String[] data = new String[3];
                        data[0] = LoginActivity.getid();
                        data[1] = weekString;
                        data[2] = todayString;
                        Log.v("Fragment Analysis :", " ---------------todayString : " + todayString);
                        Log.v("Fragment Analysis :", " ---------------weekString : " + weekString);

                        PutData putData = new PutData("http://175.205.234.222:81/SectorSearch.php", "POST", field, data);
                        //PutData putData = new PutData("http://192.168.0.16:81/signup.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                Log.v("phoneID data :", " ---------------result : " + result);
                                String[] results = result.split("@");
                                String[][] resultpase = new String[results.length][];

                                for (int i = 0; i < results.length; i++){
                                    resultpase[i] = results[i].split(",");//데이터를 ,로 하여 넣음
                                }


                                int[] weekarmsum = {0,0,0};
                                int[] weeklegsum= {0,0,0};
                                int[] weekchestsum= {0,0,0};
                                int[] weekabdominalsum= {0,0,0};

                                Log.v("phoneID data :", " ---------------pase.length : " + resultpase.length);
                                for (int i = 0; i < resultpase.length-1; i++){
                                    Log.v("phoneID data :", " ---------------resultpase.length : " + i);
                                    //if (itresult[0].equals(resultpase[i][0])){
                                    Log.v("phoneID data :", " ---------------resultpase[i][1] :  " + resultpase[i][1]);
                                    switch(resultpase[i][1]){
                                        case "1" :
                                            for(int j = 0; j < 3; j++ ){
                                                if (resultpase[i][j+2].equals("1")){
                                                    weekarmsum[j]++;
                                                    Log.v("phoneID data :", " ---------------result : " + weekarmsum[0]);
                                                }
                                            }
                                            break;

                                        case "2" :
                                            for(int a = 0; a < 3; a++ ){
                                                if (resultpase[i][a+2].equals("1")){
                                                    weeklegsum[a]++;
                                                }
                                            }
                                            break;

                                        case "3" :
                                            for(int b = 0; b < 3; b++ ){
                                                if (resultpase[i][b+2].equals("1")){
                                                    weekchestsum[b]++;
                                                }
                                            }
                                            break;
                                        case "4" :
                                            for(int c = 0; c < 3; c++ ){
                                                if (resultpase[i][c+2].equals("1")){
                                                    weekabdominalsum[c]++;
                                                }
                                            }
                                            break;
                                        default:
                                            break;
                                    }

//                                    }else{
//                                        itresult = resultpase[i];
//                                        continue;
//                                    }

                                }

                                //검색하기 위한 코드
//                                for (int i = 0; i < resultpase.length; i++){
//                                    for (int j = 0; j < resultpase[i].length; j++){
//                                        Log.v("phoneID data :", " ---------------resultpase   "+i+" , "+j+":" + resultpase[i][j]);
//                                    }
//                                }
                                Log.v("FragmentAnalysis :", " ---------------weekarmsum[0] : " + weekarmsum[0]);
                                Log.v("FragmentAnalysis :", " ---------------weeklegsum[0] : " + weeklegsum[0]);
                                Log.v("FragmentAnalysis :", " ---------------weekchestsum[0] : " + weekchestsum[0]);
                                Log.v("FragmentAnalysis :", " ---------------weekabdominalsum[0] : " + weekabdominalsum[0]);
                                armCount = (float) weekarmsum[0];
                                legCount = (float) weeklegsum[0];
                                chestCount = (float) weekchestsum[0];
                                abdominalCount = (float) weekabdominalsum[0];
                                Log.v("FragmentAnalysis :", " ---------------armCount : " + armCount);
                                Log.v("FragmentAnalysis :", " ---------------legCount : " + legCount);
                                Log.v("FragmentAnalysis :", " ---------------chestCount : " + chestCount);
                                Log.v("FragmentAnalysis :", " ---------------abdominalCount : " + abdominalCount);
                                exercise.set(0,new PieEntry(armCount,"팔"));
                                exercise.set(1,new PieEntry(legCount,"다리"));
                                exercise.set(2,new PieEntry(chestCount,"가슴"));
                                exercise.set(3,new PieEntry(abdominalCount,"복근"));
                                pieChart.animateY(1500, Easing.EasingOption.EaseInOutCubic);
                                pieChart.animate();

                                pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                                    @Override
                                    public void onValueSelected(Entry e, Highlight h) {
                                        if (exercise.get(0) == e){
                                            sumCount.setText(String.valueOf(weekarmsum[0]));
                                            avgCount.setText(String.valueOf(weekarmsum[0]/7));
                                            sumKg.setText(String.valueOf(weekarmsum[1]));
                                            avgKg.setText(String.valueOf(weekarmsum[1]/7));
                                            sumMin.setText(String.valueOf(weekarmsum[2]));
                                            avgMin.setText(String.valueOf(weekarmsum[2]/7));
                                        }else if(exercise.get(1) == e){
                                            sumCount.setText(String.valueOf(weeklegsum[0]));
                                            avgCount.setText(String.valueOf(weeklegsum[0]/7));
                                            sumKg.setText(String.valueOf(weeklegsum[1]));
                                            avgKg.setText(String.valueOf(weeklegsum[1]/7));
                                            sumMin.setText(String.valueOf(weeklegsum[2]));
                                            avgMin.setText(String.valueOf(weeklegsum[2]/7));
                                        }else if(exercise.get(2) == e){
                                            sumCount.setText(String.valueOf(weekchestsum[0]));
                                            avgCount.setText(String.valueOf(weekchestsum[0]/7));
                                            sumKg.setText(String.valueOf(weekchestsum[1]));
                                            avgKg.setText(String.valueOf(weekchestsum[1]/7));
                                            sumMin.setText(String.valueOf(weekchestsum[2]));
                                            avgMin.setText(String.valueOf(weekchestsum[2]/7));
                                        }else if(exercise.get(3) == e){
                                            sumCount.setText(String.valueOf(weekabdominalsum[0]));
                                            avgCount.setText(String.valueOf(weekabdominalsum[0]/7));
                                            sumKg.setText(String.valueOf(weekabdominalsum[1]));
                                            avgKg.setText(String.valueOf(weekabdominalsum[1]/7));
                                            sumMin.setText(String.valueOf(weekabdominalsum[2]));
                                            avgMin.setText(String.valueOf(weekabdominalsum[2]/7));
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected() {

                                    }
                                });


                            }
                        }
                    }
                });
            }
        });

        MaterialButton monthlyExercise = view.findViewById(R.id.monthlyExercise);
        monthlyExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler(Looper.getMainLooper());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        String[] field = new String[3];
                        field[0] = "id";
                        field[1] = "fday";
                        field[2] = "lday";

                        String[] data = new String[3];
                        data[0] = LoginActivity.getid();
                        data[1] = monString;
                        data[2] = todayString;
                        Log.v("Fragment Analysis :", " ---------------todayString : " + todayString);
                        Log.v("Fragment Analysis :", " ---------------weekString : " + weekString);

                        PutData putData = new PutData("http://175.205.234.222:81/SectorSearch.php", "POST", field, data);
                        //PutData putData = new PutData("http://192.168.0.16:81/signup.php", "POST", field, data);
                        if (putData.startPut()) {
                            if (putData.onComplete()) {
                                String result = putData.getResult();
                                Log.v("phoneID data :", " ---------------result : " + result);
                                String[] results = result.split("@");
                                String[][] resultpase = new String[results.length][];

                                for (int i = 0; i < results.length; i++) {
                                    resultpase[i] = results[i].split(",");//데이터를 ,로 하여 넣음
                                }


                                int[] weekarmsum = {0, 0, 0};
                                int[] weeklegsum = {0, 0, 0};
                                int[] weekchestsum = {0, 0, 0};
                                int[] weekabdominalsum = {0, 0, 0};
                                Log.v("phoneID data :", " ---------------resultpase.length : " + resultpase.length);
                                for (int i = 0; i < resultpase.length - 1; i++) {
                                    Log.v("phoneID data :", " ---------------resultpase.length : " + i);
                                    //if (itresult[0].equals(resultpase[i][0])){
                                    switch (resultpase[i][1]) {
                                        case "1":
                                            for (int j = 0; j < 3; j++) {
                                                if (resultpase[i][j + 2].equals("1")) {
                                                    weekarmsum[j]++;
                                                    Log.v("phoneID data :", " ---------------result : " + weekarmsum[0]);
                                                }
                                            }
                                            break;

                                        case "2":
                                            for (int a = 0; a < 3; a++) {
                                                if (resultpase[i][a + 2].equals("1")) {
                                                    weeklegsum[a]++;
                                                }
                                            }
                                            break;

                                        case "3":
                                            for (int b = 0; b < 3; b++) {
                                                if (resultpase[i][b + 2].equals("1")) {
                                                    weekchestsum[b]++;
                                                }
                                            }
                                            break;
                                        case "4":
                                            for (int c = 0; c < 3; c++) {
                                                if (resultpase[i][c + 2].equals("1")) {
                                                    weekabdominalsum[c]++;
                                                }
                                            }
                                            break;
                                        default:
                                            break;
                                    }

//                                    }else{
//                                        itresult = resultpase[i];
//                                        continue;
//                                    }

                                }

                                //검색하기 위한 코드
//                                for (int i = 0; i < resultpase.length; i++){
//                                    for (int j = 0; j < resultpase[i].length; j++){
//                                        Log.v("phoneID data :", " ---------------resultpase   "+i+" , "+j+":" + resultpase[i][j]);
//                                    }
//                                }
                                Log.v("FragmentAnalysis :", " ---------------weekarmsum[0] : " + weekarmsum[0]);
                                Log.v("FragmentAnalysis :", " ---------------weeklegsum[0] : " + weeklegsum[0]);
                                Log.v("FragmentAnalysis :", " ---------------weekchestsum[0] : " + weekchestsum[0]);
                                Log.v("FragmentAnalysis :", " ---------------weekabdominalsum[0] : " + weekabdominalsum[0]);
                                armCount = (float) weekarmsum[0];
                                legCount = (float) weeklegsum[0];
                                chestCount = (float) weekchestsum[0];
                                abdominalCount = (float) weekabdominalsum[0];
                                Log.v("FragmentAnalysis :", " ---------------armCount : " + armCount);
                                Log.v("FragmentAnalysis :", " ---------------legCount : " + legCount);
                                Log.v("FragmentAnalysis :", " ---------------chestCount : " + chestCount);
                                Log.v("FragmentAnalysis :", " ---------------abdominalCount : " + abdominalCount);
                                exercise.set(0, new PieEntry(armCount, "팔"));
                                exercise.set(1, new PieEntry(legCount, "다리"));
                                exercise.set(2, new PieEntry(chestCount, "가슴"));
                                exercise.set(3, new PieEntry(abdominalCount, "복근"));
                                pieChart.animateY(1500, Easing.EasingOption.EaseInOutCubic);
                                pieChart.animate();

                                pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                                    @Override
                                    public void onValueSelected(Entry e, Highlight h) {
                                        if (exercise.get(0) == e) {
                                            sumCount.setText(String.valueOf(weekarmsum[0]));
                                            avgCount.setText(String.valueOf(weekarmsum[0] / 7));
                                            sumKg.setText(String.valueOf(weekarmsum[1]));
                                            avgKg.setText(String.valueOf(weekarmsum[1] / 7));
                                            sumMin.setText(String.valueOf(weekarmsum[2]));
                                            avgMin.setText(String.valueOf(weekarmsum[2] / 7));
                                        } else if (exercise.get(1) == e) {
                                            sumCount.setText(String.valueOf(weeklegsum[0]));
                                            avgCount.setText(String.valueOf(weeklegsum[0] / 7));
                                            sumKg.setText(String.valueOf(weeklegsum[1]));
                                            avgKg.setText(String.valueOf(weeklegsum[1] / 7));
                                            sumMin.setText(String.valueOf(weeklegsum[2]));
                                            avgMin.setText(String.valueOf(weeklegsum[2] / 7));
                                        } else if (exercise.get(2) == e) {
                                            sumCount.setText(String.valueOf(weekchestsum[0]));
                                            avgCount.setText(String.valueOf(weekchestsum[0] / 7));
                                            sumKg.setText(String.valueOf(weekchestsum[1]));
                                            avgKg.setText(String.valueOf(weekchestsum[1] / 7));
                                            sumMin.setText(String.valueOf(weekchestsum[2]));
                                            avgMin.setText(String.valueOf(weekchestsum[2] / 7));
                                        } else if (exercise.get(3) == e) {
                                            sumCount.setText(String.valueOf(weekabdominalsum[0]));
                                            avgCount.setText(String.valueOf(weekabdominalsum[0] / 7));
                                            sumKg.setText(String.valueOf(weekabdominalsum[1]));
                                            avgKg.setText(String.valueOf(weekabdominalsum[1] / 7));
                                            sumMin.setText(String.valueOf(weekabdominalsum[2]));
                                            avgMin.setText(String.valueOf(weekabdominalsum[2] / 7));
                                        }
                                    }

                                    @Override
                                    public void onNothingSelected() {

                                    }
                                });


                            }
                        }
                    }
                });
            }
        });

        return view;
    }
}

