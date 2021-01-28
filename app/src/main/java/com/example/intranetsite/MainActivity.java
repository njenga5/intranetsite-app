package com.example.intranetsite;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private int counter = 0;
    private TextView txtInvalidEmail, txtInvalidPassword;
    private EditText edtEmail, edtPass;
    private MaterialButton btnLogin, btnSignUp;
    private RelativeLayout RelParent;
    Map<String, String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        btnLogin.setOnClickListener(v -> {
                    if (edtEmail.getText().toString().isEmpty() || !(edtEmail.getText().toString().matches("([\\w.%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4})"))) {
                        txtInvalidEmail.setVisibility(View.VISIBLE);
                    } else if (edtPass.getText().toString().isEmpty()) {
                        txtInvalidPassword.setVisibility(View.VISIBLE);
                    }else{
                        params = new HashMap<>();
                        params.put("email", edtEmail.getText().toString());
                        params.put("password", edtPass.getText().toString());
                        if (params != null) {
                            makeServiceCall();
                        }
                    }
                });

        btnSignUp.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
            startActivity(intent);
        });
    }

    private void makeServiceCall(){
        new ApiRequestsHandler(MainActivity.this).makeJsonObjectGetRequest(params, new VolleyResponseListener() {
            @Override
            public void onResponse(Object response) {
                JSONObject data = (JSONObject) response;
                try {
                    Intent intent = new Intent(MainActivity.this, GalleryActivity.class);
                    intent.putExtra("email", data.getString("email"));
                    intent.putExtra("name", data.getString("full_name"));
                    intent.putExtra("phoneNumber", data.getString("phone_number"));
                    intent.putExtra("password", data.getString("password"));
                    startActivity(intent);

                }catch (JSONException e){
                    Log.e(TAG, "onResponse: JSONException occurred.");
                    e.printStackTrace();
                }
                edtEmail.setText("");
                edtPass.setText("");
                txtInvalidEmail.setVisibility(View.GONE);
                txtInvalidPassword.setVisibility(View.GONE);
                params = null;
            }

            @Override
            public void onError(VolleyError error) {
                if (error.networkResponse != null){
                    if (error.networkResponse.statusCode >= 500){
                        Toast.makeText(MainActivity.this, "An error occurred.", Toast.LENGTH_SHORT).show();
                    }else if (error.networkResponse.statusCode >= 400) {
                        Toast.makeText(MainActivity.this, "Wrong username or password.", Toast.LENGTH_SHORT).show();
                        txtInvalidEmail.setVisibility(View.VISIBLE);
                        txtInvalidPassword.setVisibility(View.VISIBLE);
                    }
                }else {
                    Snackbar.make(RelParent, "Network Error.", Snackbar.LENGTH_INDEFINITE)
                            .setAction("Retry", v1 -> makeServiceCall())
                            .setActionTextColor(Color.YELLOW)
                            .show();
                    if (error.getMessage() != null) {
                        Log.e(TAG, "onErrorResponse: " + error.getMessage());
                    }
                }
            }
        });
    }

    private void initViews(){
        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);
        txtInvalidEmail = findViewById(R.id.txtInvalidEmail);
        txtInvalidPassword = findViewById(R.id.txtInvalidPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        RelParent = findViewById(R.id.RelParent);

    }

    @Override
    public void onBackPressed() {
        if (counter == 0){
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
            counter ++;
        }else {
        super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        counter = 0;
        super.onResume();
    }
}