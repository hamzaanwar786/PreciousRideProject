package com.example.task;

import static com.example.task.Session.SaveSharedPreference.setCity;
import static com.example.task.Session.SaveSharedPreference.setClientId;
import static com.example.task.Session.SaveSharedPreference.setEmail;
import static com.example.task.Session.SaveSharedPreference.setFirstName;
import static com.example.task.Session.SaveSharedPreference.setLastName;
import static com.example.task.Session.SaveSharedPreference.setMobileNumber;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.task.API.ApiClass;
import com.example.task.FilesLogin.RequestLogin;
import com.example.task.FilesLogin.ResponseLogin;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    TextView login_txt;
    EditText email,password;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context =this;

        login_txt= findViewById(R.id.login_txt);
        email= findViewById(R.id.email);
        password= findViewById(R.id.password);

        login_txt.setText(getText(R.string.login_text));





    }

    public void sign_in(View view) {
//        Intent intent = new Intent(MainActivity.this,HomeOffline.class);
//        startActivity(intent);

        String semail,spassword;
        semail = email.getText().toString();
        spassword = password.getText().toString();

        login(semail,spassword);
    }

//    public void clear(View view) {
//        number.setText("");
//    }


    public void login(String email,String password) {
        RequestLogin requestLogin = new RequestLogin();
        requestLogin.setEmail(email);
        requestLogin.setPassword(password);

//        signUpRequest.setI_code(invite_code);


        Call<ResponseLogin> signUpResponseCall = ApiClass.getUserServiceLogin().userLogin(requestLogin);
        signUpResponseCall.enqueue(new Callback<ResponseLogin>() {
            @Override
            public void onResponse(Call<ResponseLogin> call, Response<ResponseLogin> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, ""+response.body().message, Toast.LENGTH_SHORT).show();
                    if (response.body().message.equals("Login Successfully"))
                    {

                        String idClient = response.body().data.id;
                        String firstName = response.body().data.f_name;
                        String lastname = response.body().data.l_name;
                        String city = response.body().data.city;
                        String email = response.body().data.email;
                        String number = response.body().data.mobile_number;

                        setClientId(context,idClient);
                        setFirstName(context,firstName);
                        setLastName(context,lastname);
                        setMobileNumber(context,number);
                        setEmail(context,email);
                        setCity(context,city);

//                        setClientId(context,idClient,firstName,lastname,email,number,password,city);
//                        Toast.makeText(MainActivity.this, ""+response.body().data.id, Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, HomeOffline.class);
                        startActivity(intent);
                        finish();
                    }

                } else {
                    Toast.makeText(MainActivity.this, "Register Not Successfull", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseLogin> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Throwable " + t, Toast.LENGTH_SHORT).show();
                Log.d("TAG", "Error " + t);
            }
        });
    }
}