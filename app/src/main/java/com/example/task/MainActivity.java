package com.example.task;

import static com.example.task.Session.SaveSharedPreference.setCity;
import static com.example.task.Session.SaveSharedPreference.setClientId;
import static com.example.task.Session.SaveSharedPreference.setEmail;
import static com.example.task.Session.SaveSharedPreference.setFirstName;
import static com.example.task.Session.SaveSharedPreference.setImageUrl;
import static com.example.task.Session.SaveSharedPreference.setLastName;
import static com.example.task.Session.SaveSharedPreference.setMobileNumber;
import static com.example.task.Session.SaveSharedPreference.setStatus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task.UserServiceInterface.ApiClass;
import com.example.task.FilesLogin.RequestLogin;
import com.example.task.FilesLogin.ResponseLogin;
import com.example.task.LoginValues.RequestLoginValues;
import com.example.task.LoginValues.ResponseLoginValues;
import com.wang.avi.AVLoadingIndicatorView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    TextView login_txt;
    EditText email, password;
    Context context;
    String countrycode;
    RelativeLayout parentLayout;
    private AVLoadingIndicatorView avi;
    Button signIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);

        parentLayout = findViewById(R.id.parentLayout);
        signIn = findViewById(R.id.signIn);


        Intent intent= getIntent();
        countrycode= intent.getStringExtra("countrycode");
        login_txt = findViewById(R.id.login_txt);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        login_txt.setText(getText(R.string.login_text));


        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
                String semail, spassword;
                semail = email.getText().toString();
                spassword = password.getText().toString();
                if (semail.isEmpty()) {
                    email.setError("Required");
                    signIn.setEnabled(true);
                    stopAnim();
                } else if (spassword.isEmpty()) {
                    password.setError("Required");
                    signIn.setEnabled(true);
                    stopAnim();
                } else {
                    login(semail, spassword);
                    startAnim();
                    signIn.setEnabled(false);
                }
            }
        });

    }



    public void login(String email, String password) {
        RequestLogin requestLogin = new RequestLogin();
        requestLogin.setEmail(email);
        requestLogin.setPassword(password);

//        signUpRequest.setI_code(invite_code);


        Call<ResponseLogin> signUpResponseCall = ApiClass.getUserServiceAPI().userDriverLogin(requestLogin);
        signUpResponseCall.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                if (response.isSuccessful()) {
                    stopAnim();
                    signIn.setEnabled(true);
                    Toast.makeText(MainActivity.this, "" + response.body().message, Toast.LENGTH_SHORT).show();
                    if (response.body().message.equals("Login Successfully")) {
                        signIn.setEnabled(true);
                        String idClient = response.body().data.id;
                        setClientId(context, idClient);
                        if (response.body().data.f_name != null) {

                            stopAnim();
                            String firstName = response.body().data.f_name;
                            String lastname = response.body().data.l_name;
                            String city = response.body().data.city;
                            String email = response.body().data.email;
                            String number = response.body().data.mobile_number;
                            String image;
                            if (response.body().data.image ==null)
                            {
                                 image="0";
                            }
                            else {
                                 image = response.body().data.image;
                            }

                            setFirstName(context, firstName);
                            setLastName(context, lastname);
                            setMobileNumber(context, number);
                            setEmail(context, email);
                            setCity(context, city);
                            setStatus(context, "0");
                            setImageUrl(context,image);


                            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
                            Date currentLocalTime = cal.getTime();
                            DateFormat date = new SimpleDateFormat("HH:mm a");
// you can get seconds by adding  "...:ss" to it
                            date.setTimeZone(TimeZone.getTimeZone("GMT+1:00"));

                            String localTime = date.format(currentLocalTime);


                            loginvalues(idClient, number, localTime);

//                        setClientId(context,idClient,firstName,lastname,email,number,password,city);
//                        Toast.makeText(MainActivity.this, ""+response.body().data.id, Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, SetupGPSLocationActivity.class);
                            startActivity(intent);
                            finish();

                        } else {
                            Intent intent = new Intent(MainActivity.this, PersonalInformationScreen.class);
                            intent.putExtra("countrycode", countrycode);
                            startActivity(intent);
                            finish();
                        }
                    }

                } else {
//                    Toast.makeText(MainActivity.this, "Not Successful", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                signIn.setEnabled(true);
//                Toast.makeText(MainActivity.this, "Throwable " + t, Toast.LENGTH_SHORT).show();
                Log.d("TAG", "Error " + t);
                Toast.makeText(MainActivity.this, "Please change your internet connection and try again", Toast.LENGTH_SHORT).show();
                stopAnim();
            }
        });
    }


    public void loginvalues(String driverId, String mobile, String logged_in) {
        RequestLoginValues requestLoginValues = new RequestLoginValues();
        requestLoginValues.setDriver_id(driverId);
        requestLoginValues.setMobile(mobile);
        requestLoginValues.setLogged_in(logged_in);

//        signUpRequest.setI_code(invite_code);


        Call<ResponseLoginValues> responseLoginValuesCall = ApiClass.getUserServiceAPI().userDriverLoginStatus(requestLoginValues);
        responseLoginValuesCall.enqueue(new Callback<ResponseLoginValues>() {
            @Override
            public void onResponse(Call<ResponseLoginValues> call, Response<ResponseLoginValues> response) {
                if (response.isSuccessful()) {


                } else {

                }
            }

            @Override
            public void onFailure(Call<ResponseLoginValues> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "Throwable " + t, Toast.LENGTH_SHORT).show();
                Log.d("TAG", "Error " + t);
                Toast.makeText(MainActivity.this, "Please change your internet connection and try again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void forget_password(View view) {
        startActivity(new Intent(MainActivity.this, ForgetPassword.class));
    }

    public void back_button(View view) {
        onBackPressed();
    }

    void startAnim(){
        avi.show();
        // or avi.smoothToShow();
    }

    void stopAnim(){
        avi.hide();
        // or avi.smoothToHide();
    }
}