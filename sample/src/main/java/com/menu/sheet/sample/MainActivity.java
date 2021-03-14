package com.menu.sheet.sample;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.menu.sheet.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.menu_button).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, MenuActivity.class)));
    }
}
