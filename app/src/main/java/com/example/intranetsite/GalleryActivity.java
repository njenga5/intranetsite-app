package com.example.intranetsite;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class GalleryActivity extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        intent = getIntent();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.profile) {
            if (intent != null) {
                Intent intent1 = new Intent(GalleryActivity.this, ProfileActivity.class);
                intent1.putExtra("email", intent.getStringExtra("email"));
                intent1.putExtra("name", intent.getStringExtra("name"));
                intent1.putExtra("phoneNumber", intent.getStringExtra("phoneNumber"));
                intent1.putExtra("password", intent.getStringExtra("password"));
                startActivity(intent1);
            }
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }

}