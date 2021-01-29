package com.example.intranetsite;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.google.android.material.button.MaterialButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    public static final String TAG = ProfileActivity.class.getSimpleName();
    private TextView name, email, phone, txtEditEmail, txtPassword;
    private ImageView profilePic;
    private MaterialButton logoutBtn, btnEdit, btnCommit, btnCancel;
    private EditText edtEditName, edtEditPhone, edtEditPassword;
    Map<String, String> updateParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initViews();
        Intent intent = getIntent();
        if (intent != null){
            name.setText(String.format("Name: %s", intent.getStringExtra("name")));
            email.setText(String.format("Email: %s", intent.getStringExtra("email")));
            phone.setText(String.format("Phone: %s", intent.getStringExtra("phoneNumber")));
            txtPassword.setText(intent.getStringExtra("password"));
        }
        logoutBtn.setOnClickListener(v -> {
            Intent intent1 = new Intent(ProfileActivity.this, MainActivity.class);
            intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent1);
        });

        btnEdit.setOnClickListener(v -> {
            // Hide the text views
            name.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            phone.setVisibility(View.GONE);
            btnEdit.setVisibility(View.GONE);

            // Show the edit fields
            edtEditName.setVisibility(View.VISIBLE);
            txtEditEmail.setVisibility(View.VISIBLE);
            edtEditPhone.setVisibility(View.VISIBLE);
            edtEditPassword.setVisibility(View.VISIBLE);
            btnCommit.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.VISIBLE);

            if (intent != null){
                edtEditName.setText(name.getText().toString().split(":")[1].trim());
                txtEditEmail.setText(email.getText().toString());
                edtEditPhone.setText(phone.getText().toString().split(":")[1]);
                edtEditPassword.setText(txtPassword.getText().toString());
            }
        });

        btnCommit.setOnClickListener(v -> {
            if (edtEditName.getText().toString().isEmpty() ||
                edtEditPassword.getText().toString().isEmpty() ||
                edtEditPhone.getText().toString().isEmpty()){
                Toast.makeText(this, "Fill out all fields", Toast.LENGTH_SHORT).show();
            }else{
                updateParams = new HashMap<>();
                updateParams.put("full_name", edtEditName.getText().toString().trim());
                updateParams.put("phone_number", edtEditPhone.getText().toString().trim());
                updateParams.put("password", edtEditPassword.getText().toString().trim());
                updateParams.put("email", txtEditEmail.getText().toString().split(":")[1].trim());
                new ApiRequestsHandler(this).updateDetailsRequest(updateParams, "users", new VolleyResponseListener() {
                    @Override
                    public void onResponse(Object response) {
                        JSONObject data = (JSONObject) response;
                        try {
                        name.setText(String.format("Name: %s", data.getString("full_name")));
                        email.setText(String.format("Email: %s", data.getString("email")));
                        phone.setText(String.format("Phone: %s", data.getString("phone_number")));
                        txtPassword.setText(data.getString("password"));

                        hideEditFields();
                        Toast.makeText(ProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                        }catch (JSONException e){
                            Log.e(TAG, "onResponse: JsonException Occurred");
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) {
                        if (error.networkResponse != null) {
                            Log.e(TAG, "onError: " + error.networkResponse.statusCode);
                        }else{
                            Log.e(TAG, "onError: " + error.getMessage());
                        }
                    }
                });
            }
        });

        btnCancel.setOnClickListener(v -> hideEditFields());
    }

    private void hideEditFields(){
        name.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        phone.setVisibility(View.VISIBLE);
        btnEdit.setVisibility(View.VISIBLE);

        edtEditName.setVisibility(View.GONE);
        txtEditEmail.setVisibility(View.GONE);
        edtEditPhone.setVisibility(View.GONE);
        edtEditPassword.setVisibility(View.GONE);
        btnCommit.setVisibility(View.GONE);
        btnCancel.setVisibility(View.GONE);
    }
    private void initViews(){
        name = findViewById(R.id.txtName);
        email = findViewById(R.id.txtEmail);
        profilePic = findViewById(R.id.profilePic);
        phone = findViewById(R.id.txtPhone);
        txtPassword = findViewById(R.id.txtPassword);
        edtEditName = findViewById(R.id.edtEditName);
        txtEditEmail = findViewById(R.id.txtEditEmail);
        edtEditPhone = findViewById(R.id.edtEditPhone);
        edtEditPassword = findViewById(R.id.edtEditPassword);
        logoutBtn = findViewById(R.id.btnLogout);
        btnEdit = findViewById(R.id.btnEdit);
        btnCommit = findViewById(R.id.btnCommit);
        btnCancel = findViewById(R.id.btnCancel);
    }
}