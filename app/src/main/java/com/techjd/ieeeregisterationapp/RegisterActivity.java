package com.techjd.ieeeregisterationapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private DatabaseReference reference;
    String[] colleges_name = {"ADIT", "GCET", "BVM" , "MBIT" };
    private Spinner spinner;
    private Button register;
    private RadioGroup radioGroup1;
    private RadioGroup radioGroup2;
    private RadioGroup radioGroup3;
    private EditText name,email;
    String regname,regemail;
    String collegename ;
    private ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().hide();
        name = findViewById(R.id.editText);
        email = findViewById(R.id.editText2);
        radioGroup1 = findViewById(R.id.radiogroup1);
        radioGroup2 = findViewById(R.id.radiogroup2);
        radioGroup3 = findViewById(R.id.radiogroup3);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter colleges = new ArrayAdapter(RegisterActivity.this, android.R.layout.simple_spinner_item,  colleges_name);
        colleges.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(colleges);

        register = findViewById(R.id.button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd= new ProgressDialog(RegisterActivity.this);
                pd.setMessage("Please wait...");
                pd.show();
                pd.setCancelable(false);
                pd.setCanceledOnTouchOutside(false);
                saveInfo();
            }
        });

    }

    private void saveInfo() {
        int price = 350;
        collegename =   spinner.getSelectedItem().toString();
        regname = name.getText().toString();
        regemail = email.getText().toString();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        int selectedId = radioGroup1.getCheckedRadioButtonId();
        final RadioButton radioButton = (RadioButton) findViewById(selectedId);
        int selectedId1 = radioGroup2.getCheckedRadioButtonId();
        final RadioButton radioButton1 = (RadioButton) findViewById(selectedId1);
        int selectedId2 = radioGroup3.getCheckedRadioButtonId();
        final RadioButton radioButton2 = (RadioButton) findViewById(selectedId2);
        if(radioButton.getText() == null) {
            return;
        }
        if(radioButton1.getText() == null) {
            return;
        }
        if(radioButton2.getText() == null) {
            return;
        }
        reference = FirebaseDatabase.getInstance().getReference().child("RegisteredPsersons");
        HashMap<String, Object> userdetails = new HashMap();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getUid();

        String regId = reference.push().getKey();
        userdetails.put("mCollege", collegename);
        userdetails.put("mName", regname);
        userdetails.put("nEmail",regemail);
        userdetails.put("registereddate",date);
        userdetails.put("registeredby",userId);
        userdetails.put("mAmount",price);
        userdetails.put("mPaymentType",radioButton.getText().toString());
        userdetails.put("mRegistrationType",radioButton1.getText().toString());
        userdetails.put("mMemberShipType",radioButton2.getText().toString());

        reference.child(regId).setValue(userdetails);
        pd.dismiss();
        finish();

        Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        final String pos = colleges_name[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
