package com.example.mphotocard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Sample_0 extends AppCompatActivity {
    public static final int REQUEST_CODE_LIST = 103;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sample_0);

        Button buttonList = findViewById(R.id.btn_go_list);
        buttonList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyCardList.class);
                startActivityForResult(intent,REQUEST_CODE_LIST  );
            }
        });






    }
}
