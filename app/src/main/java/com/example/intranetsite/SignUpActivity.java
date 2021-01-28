package com.example.intranetsite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = SignUpActivity.class.getSimpleName();
    private EditText fullName, email, phoneNumber, password, confPass;
    private MaterialButton registerBtn;
    private CheckBox checkBox;
    Map<String, String> postData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initViews();
        postData = new HashMap<>();

        registerBtn.setOnClickListener(v -> {
            if (checkBox.isChecked()){
                if (fullName.getText().toString().isEmpty() || email.getText().toString().isEmpty() ||
                    phoneNumber.getText().toString().isEmpty() || password.getText().toString().isEmpty() ||
                    confPass.getText().toString().isEmpty()){
                    Toast.makeText(this, "Fill out all fields", Toast.LENGTH_SHORT).show();
                } else if (!password.getText().toString().equals(confPass.getText().toString())){
                    Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                    password.setHighlightColor(Color.RED);
                    confPass.setHighlightColor(Color.RED);
                } else {
                    postData.put("full_name", fullName.getText().toString());
                    postData.put("email", email.getText().toString());
                    postData.put("phone_number", phoneNumber.getText().toString());
                    postData.put("password", password.getText().toString());

                    new ApiRequestsHandler(this).makeJsonObjectPostRequest(postData, new VolleyResponseListener() {
                        @Override
                        public void onResponse(Object response) {
                            Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                            startActivity(intent);

                        }

                        @Override
                        public void onError(VolleyError error) {
                            if (error.networkResponse != null){
                                if (error.networkResponse.statusCode == 403){
                                    Toast.makeText(SignUpActivity.this, "Forbidden", Toast.LENGTH_SHORT).show();
                                }else if (error.networkResponse.statusCode == 400){
                                    Toast.makeText(SignUpActivity.this, "An error occurred", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Log.e(TAG, "onError: " + error.getMessage());
                            }
                        }
                    });
                }
            }else{
                Toast.makeText(this, "Agree to the Terms and Conditions", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        fullName = findViewById(R.id.edtFName);
        email = findViewById(R.id.edtSEmail);
        phoneNumber = findViewById(R.id.edtPNumber);
        password = findViewById(R.id.edtPassword);
        confPass = findViewById(R.id.edtCPassword);
        registerBtn = findViewById(R.id.btnRegister);
        checkBox = findViewById(R.id.checkCond);
    }
}