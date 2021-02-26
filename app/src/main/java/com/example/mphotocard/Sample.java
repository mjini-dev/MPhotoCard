package com.example.mphotocard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class Sample extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        Button buttonList = findViewById(R.id.btn_go_list);
        buttonList.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(getApplicationContext(), MyCardList.class);
                startActivity(intent4);
            }
        });


        //Button btn_img = findViewById(R.id.imageView2);
        findViewById(R.id.temp_1).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(Sample.this, "1 템플릿 을 선택했습니다.", Toast.LENGTH_SHORT).show();

                Bitmap sendBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sample);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();

                //Intent intent = new Intent(Sample.this, MakingCard.class);
                Intent intent = new Intent(getApplicationContext(), MakingCard.class);

                intent.putExtra("key","오늘하루도 행복하세요.");

                //바이트어레이 부가데이터로 저장
                intent.putExtra("image",byteArray);

                //액티비티 이동
                startActivity(intent);


//                //이미지파일을 비트맵변수(sendBitmap)에 담음
//                Bitmap sendBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.sample);
//                //비트맵 변수에 담은 이미지파일을 바이트어레이로 변경
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                sendBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
//                byte[] byteArray = stream.toByteArray();
//
//                //Intent intent = new Intent(Sample.this, MakingCard.class);
//                Intent intent = new Intent(getApplicationContext(), MakingCard.class);
//
//                intent.putExtra("key","오늘하루도 행복하세요.");
//
//                //바이트어레이 부가데이터로 저장
//                intent.putExtra("image",byteArray);
//
//                //액티비티 이동
//                startActivity(intent);

                finish();
            }
        });



        findViewById(R.id.temp_2).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Toast.makeText(Sample.this, "2 템플릿 을 선택했습니다.", Toast.LENGTH_SHORT).show();

                //이미지파일을 비트맵변수(sendBitmap)에 담음
                Bitmap sendBitmap2 = BitmapFactory.decodeResource(getResources(),R.drawable.dog);
                //비트맵 변수에 담은 이미지파일을 바이트어레이로 변경
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                sendBitmap2.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray2 = stream. toByteArray();

                Intent intent = new Intent(getApplicationContext(), MakingCard.class);
                intent.putExtra("key","아르릉");


                //바이트어레이 부가데이터로 저장
                intent.putExtra("image",byteArray2);

                startActivity(intent);

                finish();

            }
        });


    }





}
