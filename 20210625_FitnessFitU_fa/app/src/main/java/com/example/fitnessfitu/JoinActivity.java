package com.example.fitnessfitu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.httpconnect.PutData;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class JoinActivity extends AppCompatActivity {

    TextInputEditText textInputEditTextName, textInputEditTextEmail, textInputEditTextBirth, textInputEditTextMobile,
            textInputEditTextId, textInputEditTextPassword;

    // radioGroup
    RadioGroup radioGroupSex;
    String radioBtnSex;

    Button buttonSignUp;
    TextView textViewLogin;
    ProgressBar progressBar;
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");

    Date selectedDate;

    private String birth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        textInputEditTextName=findViewById(R.id.userName);
        textInputEditTextEmail=findViewById(R.id.userEmail);
        textInputEditTextBirth=findViewById(R.id.userBirthday);
        textInputEditTextMobile=findViewById(R.id.userMobile);
        textInputEditTextId=findViewById(R.id.userID);
        textInputEditTextPassword=findViewById(R.id.userPW);
        radioGroupSex=findViewById(R.id.userSex);
        buttonSignUp=findViewById(R.id.btnRegister);
        textViewLogin=findViewById(R.id.loginText);
//        progressBar=findViewById(R.id.progress);

        textViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        radioGroupSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.male){
                    radioBtnSex = "남";
                }else if(checkedId == R.id.female){
                    radioBtnSex = "여";
                }
            }
        });

        textInputEditTextBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog();
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name;
                String email;
                String mobile;
                String id;
                String password;
                String sex;

                name=String.valueOf(textInputEditTextName.getText());
                email=String.valueOf(textInputEditTextEmail.getText());

                birth=String.valueOf(textInputEditTextBirth.getText());
                birth = birth.replace(" ", "");
                birth = birth.replace("년", "");
                birth = birth.replace("월", "");
                birth = birth.replace("일", "");

                mobile=String.valueOf(textInputEditTextMobile.getText());
                id=String.valueOf(textInputEditTextId.getText());
                password=String.valueOf(textInputEditTextPassword.getText());
                sex=radioBtnSex;

                if(!name.equals("")&&!email.equals("")&&!birth.equals("")&&!mobile.equals("")&&!id.equals("")&&!password.equals("")) {
                    //Start ProgressBar first(Set visibility VISIBLE)
//                    progressBar.setVisibility(View.VISIBLE);
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            //Starting Write and Read data with URL
                            //Creating array for parameters
                            String[] field = new String[7];
                            field[0] = "name";
                            field[1] = "email";
                            field[2] = "birth";
                            field[3] = "mobile";
                            field[4] = "id";
                            field[5] = "password";
                            field[6] = "sex";
                            //Creating array for data
                            String[] data = new String[7];
                            data[0] = name;
                            data[1] = email;
                            data[2] = birth;
                            data[3] = mobile;
                            data[4] = id;
                            data[5] = password;
                            data[6] = sex;

                            PutData putData = new PutData("http://175.205.234.222:81/signup.php", "POST", field, data);
                            if (putData.startPut()) {
                                if (putData.onComplete()) {
//                                    progressBar.setVisibility(View.GONE);
                                    String result = putData.getResult();
                                    if(result.equals("signup")) {
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                        Intent intent=new Intent(getApplicationContext(), RealtimeActivity.class);
                                        startActivity(intent);
                                        finish();

                                    }else{
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    }
                                    //End ProgressBar (Set visibility to GONE)
                                }
                            }
                            //End Write and Read data with URL
                        }
                    });
                }
            }
        });
    }

    private void showDateDialog() {
        String birthDateStr = textInputEditTextBirth.getText().toString();

        Calendar calendar = Calendar.getInstance();
        Date birthDate = new Date();
        try {
            birthDate = dateFormat.parse(birthDateStr);
        } catch(Exception ex) {
            ex.printStackTrace();
        }

        calendar.setTime(birthDate);

        int birthYear = calendar.get(Calendar.YEAR);
        int birthMonth = calendar.get(Calendar.MONTH);
        int birthDay = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this,  birthDateSetListener,  birthYear, birthMonth, birthDay);
        dialog.show();
    }

    private DatePickerDialog.OnDateSetListener birthDateSetListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar selectedCalendar = Calendar.getInstance();
            selectedCalendar.set(Calendar.YEAR, year);
            selectedCalendar.set(Calendar.MONTH, monthOfYear);
            selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            Date birthDate = selectedCalendar.getTime();
            setSelectedDate(birthDate);
        }
    };

    private void setSelectedDate(Date birthDate) {
        selectedDate = birthDate;

        String selectedDateStr = dateFormat.format(birthDate);
        textInputEditTextBirth.setText(selectedDateStr);
    }
}
