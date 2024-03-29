package com.example.task;

import static com.example.task.Session.SaveSharedPreference.getClientId;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.task.UserServiceInterface.ApiClass;
import com.example.task.DocumentDataFiles.RequestDocumentCountrywise;
import com.example.task.DocumentDataFiles.ResponseDocumentCountrywise;
import com.example.task.DocumentsStartFiles.ListDocument;
import com.example.task.DocumentsStartFiles.RequestDocumentStartSend;
import com.example.task.DocumentsStartFiles.ResponseDocumentStartSend;
import com.example.task.adapters.DocumentsgetListAdapter;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartDoumentAddScreen extends AppCompatActivity implements DocumentsgetListAdapter.CallbackInterface {
    private static final int MY_REQUEST = 1001;
    String countrycode;
    RecyclerView recyclerViewdocument;
    LinearLayoutManager linearLayoutManager;
    DocumentsgetListAdapter rideStepsListAdapter;
    Activity activity;
    Context context;
    String front_base_code, back_base_code;
    byte[] byteArray;
    String encodedImage;
    String filename, taskresult;
    int positon_array;
    boolean path = false;
    ConstraintLayout rootContainer;
     ProgressBar progressBar;
     List<String[]> arraydata;
     ListView simplelistview;
     Button next_btn;

     List<ListDocument> listDocuments;
    ProgressBar simpleProgressBar;
     String data_name,data_type,data_document_type,data_unique_code,data_expiry_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_doument_add_screen);
        activity = this;
        context = this;
        Intent intent = getIntent();
        countrycode = intent.getStringExtra("countrycode");
        simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        arraydata = new ArrayList<>();
        listDocuments= new ArrayList<>();
//        dataDocumentget(getCountryCode(context));
        dataDocumentget();
        recyclerViewdocument = findViewById(R.id.recycler_view_documents);
        rootContainer = findViewById(R.id.rootContainer);
        next_btn = findViewById(R.id.next_btn);

        // Create progressBar dynamically...

        linearLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerViewdocument.setLayoutManager(linearLayoutManager);

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (listDocuments.size()<=0)
//                {
//                    Toast.makeText(StartDoumentAddScreen.this, "Fill the data", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    next_btn.setEnabled(false);
////                    rootContainer.setEnabled(false);
//                dataDocumentsent(listDocuments);
//                    simpleProgressBar.setVisibility(View.VISIBLE);
//                }
////                dataDocumentsent(arraydata);

                Intent intent = new Intent(StartDoumentAddScreen.this, PaymentMethodScreenFirst.class);
                startActivity(intent);
            }
        });

    }

    public void dataDocumentget() {
        RequestDocumentCountrywise requestSignUp = new RequestDocumentCountrywise();
        requestSignUp.setNumber("+92");


        Call<ResponseDocumentCountrywise> signUpResponseCall = ApiClass.getUserServiceAPI().userDriverGetDocuments(requestSignUp);
        signUpResponseCall.enqueue(new Callback<ResponseDocumentCountrywise>() {
            @Override
            public void onResponse(Call<ResponseDocumentCountrywise> call, Response<ResponseDocumentCountrywise> response) {
                if (response.isSuccessful()) {
                    if (response.body().message.equals("Success")) {
                        rideStepsListAdapter = new DocumentsgetListAdapter(activity, context, response.body().data);
                        recyclerViewdocument.setAdapter(rideStepsListAdapter);
                    }
                } else {
                }
            }

            @Override
            public void onFailure(Call<ResponseDocumentCountrywise> call, Throwable t) {
                Toast.makeText(StartDoumentAddScreen.this, "Please change your internet connection and try again", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "Error " + t);
            }
        });
    }

    public void dataDocumentsent(List<ListDocument> arrydata) {
        RequestDocumentStartSend requestSignUp = new RequestDocumentStartSend();
        requestSignUp.setListDocuments(arrydata);


        Call<ResponseDocumentStartSend> signUpResponseCall = ApiClass.getUserServiceAPI().userDriverAllDocuments(requestSignUp);
        signUpResponseCall.enqueue(new Callback<ResponseDocumentStartSend>() {
            @Override
            public void onResponse(Call<ResponseDocumentStartSend> call, Response<ResponseDocumentStartSend> response) {
                if (response.isSuccessful()) {
                    if (response.body().message.equals("Success")) {
                        next_btn.setEnabled(true);
                        simpleProgressBar.setVisibility(View.GONE);
                        Intent intent = new Intent(StartDoumentAddScreen.this, PaymentMethodScreenFirst.class);
                        startActivity(intent);
                    }
                    else{
                        next_btn.setEnabled(true);
                        simpleProgressBar.setVisibility(View.GONE);
                        Toast.makeText(StartDoumentAddScreen.this, ""+response.body().message, Toast.LENGTH_SHORT).show();
                    }
                } else {

                }
            }

            @Override
            public void onFailure(Call<ResponseDocumentStartSend> call, Throwable t) {
//                Toast.makeText(SignUp.this, "Throwable " + t, Toast.LENGTH_SHORT).show();
                Log.d("TAG", "Error " + t);
                Toast.makeText(StartDoumentAddScreen.this, "Please change your internet connection and try again", Toast.LENGTH_SHORT).show();
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {


            Bitmap originBitmap = null;
            Uri selectedImage = data.getData();
            filename = selectedImage.getPath();
            path = true;
//            Toast.makeText(activity, selectedImage.toString(),
//                    Toast.LENGTH_LONG).show();
//            txtmsg.setText(selectedImage.toString());
            InputStream imageStream;
            try {
                imageStream = getContentResolver().openInputStream(
                        selectedImage);
                originBitmap = BitmapFactory.decodeStream(imageStream);

            } catch (FileNotFoundException e) {

//                txtmsg.setText(e.getMessage().toString());
            }
            if (originBitmap != null) {


                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                originBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byteArray = stream.toByteArray();
                encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
//                if (button_value.equals("0")) {

                front_base_code = encodedImage;
                Log.d("ImageName", "Image : " + front_base_code);
//                    this.id_card_front_img.setImageBitmap(originBitmap);

//                }
//                else{
//                    back_base_code=encodedImage;
//                    this.id_card_back_img.setImageBitmap(originBitmap);
                String[] ary= new String[]{data_name, data_type, data_document_type, data_unique_code, data_expiry_code, front_base_code};
//                arraydata.add(positon_array, new String[]{data_name, data_type, data_document_type, data_unique_code, data_expiry_code, front_base_code});
                arraydata.add(positon_array,ary);
                String driverId = getClientId(context);

                Log.d("Row",positon_array+" :"+driverId+"\n"+data_name+"\n"+data_type+"\n"+data_document_type+"\n"+data_unique_code+"\n"+data_expiry_code);
                ListDocument listDocument = new ListDocument(driverId,data_name, data_type, data_document_type, data_unique_code, data_expiry_code, front_base_code);
                listDocuments.add(positon_array,listDocument);
//                for (String[] arry:arraydata
//                     ) {
//                    Log.d("ArrayData", "Array : " +arry[0] );
//                }
//                ArrayAdapter adapter = new ArrayAdapter<String[]>(this,
//                        android.R.layout.simple_list_item_1,
//                       arraydata);
//                simplelistview.setAdapter(adapter);
//                Log.d("ArrayData", "Array : " + );
//                arraydata.add(data_name,data_type,data_document_type,data_unique_code,data_expiry_code,front_base_code);
//                }
//                Toast.makeText(StartDoumentAddScreen.this, "Conversion Done",
//                        Toast.LENGTH_SHORT).show();
//                progressBar.setVisibility(View.GONE);
//                int visibility = (progressBar.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
//
//                progressBar.setVisibility(visibility);
            }
        } else {
//            txtmsg.setText("There's an error if this code doesn't work, thats all I know");

        }
    }

    public void ChooseImage() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)
                && !Environment.getExternalStorageState().equals(
                Environment.MEDIA_CHECKING)) {
//            if (rootContainer != null) {
//                rootContainer.addView(progressBar);
//            }
//            progressBar.setVisibility(View.VISIBLE);
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
//            while(path==false){
//                timeseconds++;
//            }
//            return filename;

        } else {
            Toast.makeText(activity,
                    "No activity found to perform this task",
                    Toast.LENGTH_SHORT).show();
//            return null;

        }
    }


    @Override
    public void onHandleSelection(int position,  String name,String type,String document_type,String unique_code,String expiry_date) {

//        Toast.makeText(this, "Selected item in list " + position + " with text " + text, Toast.LENGTH_SHORT).show();
        ChooseImage();
        positon_array=position;
        data_name=name;
        data_type=type;
        data_document_type=document_type;
        data_unique_code=unique_code;
        data_expiry_code=expiry_date;

    }
}



